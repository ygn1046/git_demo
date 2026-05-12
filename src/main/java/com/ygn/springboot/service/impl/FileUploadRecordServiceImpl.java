package com.ygn.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ygn.springboot.config.MinioConfig;
import com.ygn.springboot.domain.FileChunkInfo;
import com.ygn.springboot.domain.FileUploadRecord;
import com.ygn.springboot.dto.*;
import com.ygn.springboot.service.FileChunkInfoService;
import com.ygn.springboot.service.FileUploadRecordService;
import com.ygn.springboot.mapper.FileUploadRecordMapper;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.DeleteObject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
* @author 10467
* @description 针对表【file_upload_record(文件上传记录表)】的数据库操作Service实现
* @createDate 2026-04-22 17:56:17
*/
@Service
@Slf4j
@AllArgsConstructor
public class FileUploadRecordServiceImpl extends ServiceImpl<FileUploadRecordMapper, FileUploadRecord>
    implements FileUploadRecordService{

    private final MinioClient minioClient;
    private final MinioConfig minioConfig;
    private final FileChunkInfoService fileChunkInfoService;

    @Override
    @Transactional
    public FileCheckResult checkFile(FileCheckDTO checkDTO) {
        FileCheckResult result = new FileCheckResult();
        String fileHash = checkDTO.getFileHash();
     

        try {
            // 1. 检查文件是否已完整上传
            //     * 上传状态(0:上传中,1:成功,2:失败,3:已合并)
            LambdaQueryWrapper<FileUploadRecord> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(FileUploadRecord::getFileHash, fileHash)
                    .eq(FileUploadRecord::getUploadStatus, 1)
                    .eq(FileUploadRecord::getIsDeleted, 0);

            FileUploadRecord existRecord = this.getOne(wrapper);
            if (existRecord != null) {
                result.setNeedUpload(false);
                result.setFileUrl(getFileUrl(existRecord.getBucketName(), existRecord.getObjectName()));
                return result;
            }

            // 2. 检查是否有未完成的记录
            wrapper.clear();
            wrapper.eq(FileUploadRecord::getFileHash, fileHash)
                    .eq(FileUploadRecord::getIsDeleted, 0)
                    .in(FileUploadRecord::getUploadStatus, Arrays.asList(0, 3));

            FileUploadRecord record = this.getOne(wrapper);
            String fileKey;

            if (record == null) {
                // 创建新记录
                fileKey = generateFileKey(fileHash, checkDTO.getFileName());
                record = new FileUploadRecord();
                BeanUtils.copyProperties(checkDTO, record);
                record.setFileKey(fileKey);
                record.setBucketName("test");
                record.setObjectName(generateObjectName(fileHash, checkDTO.getFileName()));
                record.setUploadStartTime(LocalDateTime.now());

                this.save(record);

                // 初始化分片记录
                fileChunkInfoService.initChunkRecords(fileKey, checkDTO.getTotalChunks());
            } else {
                fileKey = record.getFileKey();
            }

            result.setFileKey(fileKey);

            // 3. 获取已上传的分片
            List<FileChunkInfo> uploadedChunks = fileChunkInfoService.getUploadedChunks(fileKey);
            List<Integer> uploadedChunkNumbers = uploadedChunks.stream()
                    .map(FileChunkInfo::getChunkNumber)
                    .collect(Collectors.toList());

            result.setUploadedChunks(uploadedChunkNumbers);

        } catch (Exception e) {
            log.error("检查文件状态失败: {}", fileHash, e);
            throw new RuntimeException("检查文件状态失败", e);
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void uploadChunk(ChunkUploadDTO uploadDTO, MultipartFile file) throws IOException {
        String fileKey = getFileKeyByHash(uploadDTO.getFileHash());
        if (fileKey == null) {
            throw new RuntimeException("文件记录不存在");
        }

        // 检查分片是否已上传
        FileChunkInfo chunkInfo = fileChunkInfoService.getChunkInfo(fileKey, uploadDTO.getChunkNumber());
        if (chunkInfo != null && chunkInfo.getUploadStatus() == 1) {
            log.info("分片已上传: {}-{}", fileKey, uploadDTO.getChunkNumber());
            return;
        }

        try (InputStream inputStream = file.getInputStream()) {
            // 上传到MinIO临时存储桶
            String objectName = generateChunkObjectName(uploadDTO.getFileHash(), uploadDTO.getChunkNumber());

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket("test")
                            .object(objectName)
                            .stream(inputStream, file.getSize(), -1)
                            .build()
            );

            // 保存分片记录
            if (chunkInfo == null) {
                chunkInfo = new FileChunkInfo();
                chunkInfo.setFileKey(fileKey);
                chunkInfo.setChunkNumber(uploadDTO.getChunkNumber());
            }

            chunkInfo.setChunkHash(DigestUtils.md5DigestAsHex(file.getBytes()));
            chunkInfo.setChunkSize(file.getSize());
            chunkInfo.setUploadStatus(1);
            chunkInfo.setUploadTime(LocalDateTime.now());

            fileChunkInfoService.saveOrUpdateChunk(chunkInfo);

            // 更新上传进度
            updateUploadProgress(fileKey, file.getSize());

            log.info("分片上传成功: {}-{}", fileKey, uploadDTO.getChunkNumber());

        } catch (Exception e) {
            log.error("分片上传失败: {}-{}", fileKey, uploadDTO.getChunkNumber(), e);
            throw new RuntimeException("分片上传失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String mergeFile(FileMergeDTO mergeDTO) throws Exception {
        String fileKey = getFileKeyByHash(mergeDTO.getFileHash());
        FileUploadRecord record = getBaseMapper().selectByFileKey(fileKey);

        if (record == null) {
            throw new RuntimeException("文件记录不存在");
        }

        // 检查是否所有分片都已上传
        int uploadedCount = fileChunkInfoService.getUploadedChunkCount(fileKey);
        if (uploadedCount != record.getTotalChunks()) {
            throw new RuntimeException("分片上传不完整");
        }

        try {
            // 构建分片源列表
            List<ComposeSource> sources = new ArrayList<>();
            for (int i = 1; i <= record.getTotalChunks(); i++) {
                String chunkObjectName = generateChunkObjectName(mergeDTO.getFileHash(), i);
                ComposeSource source = ComposeSource.builder()
                        .bucket("test")
                        .object(chunkObjectName)
                        .build();
                sources.add(source);
            }

            // 合并文件
            minioClient.composeObject(
                    ComposeObjectArgs.builder()
                            .bucket("test")
                            .object(record.getObjectName())
                            .sources(sources)
                            .build()
            );

            // 清理临时分片
            List<DeleteObject> objects = new ArrayList<>();
            for (int i = 1; i <= record.getTotalChunks(); i++) {
                String chunkObjectName = generateChunkObjectName(mergeDTO.getFileHash(), i);
                objects.add(new DeleteObject(chunkObjectName));
            }

            minioClient.removeObjects(
                    RemoveObjectsArgs.builder()
                            .bucket("test")
                            .objects(objects)
                            .build()
            );

            // 更新文件记录
            record.setUploadStatus(1);
            record.setUploadedSize(record.getFileSize());
            record.setUploadEndTime(LocalDateTime.now());
            this.updateById(record);

            // 清理分片记录
            fileChunkInfoService.deleteByFileKey(fileKey);

            // 生成文件URL
            String fileUrl = getFileUrl(record.getBucketName(), record.getObjectName());

            log.info("文件合并成功: {}, URL: {}", fileKey, fileUrl);

            return fileUrl;

        } catch (Exception e) {
            log.error("文件合并失败: {}", fileKey, e);
            throw new RuntimeException("文件合并失败", e);
        }
    }

    @Override
    public void cancelUpload(String fileKey) {
        LambdaQueryWrapper<FileUploadRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FileUploadRecord::getFileKey, fileKey)
                .eq(FileUploadRecord::getIsDeleted, 0);

        FileUploadRecord record = this.getOne(wrapper);
        if (record != null) {
            record.setIsDeleted(1);
            this.updateById(record);
            log.info("上传已取消: {}", fileKey);
        }
    }

    @Override
    public UploadProgress getUploadProgress(String fileKey) {
        LambdaQueryWrapper<FileUploadRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FileUploadRecord::getFileKey, fileKey)
                .eq(FileUploadRecord::getIsDeleted, 0);

        FileUploadRecord record = this.getOne(wrapper);
        if (record == null) {
            return null;
        }

        int uploadedCount = fileChunkInfoService.getUploadedChunkCount(fileKey);
        double progress = record.getFileSize() > 0 ?
                (double) record.getUploadedSize() / record.getFileSize() * 100 : 0;

        UploadProgress uploadProgress = new UploadProgress();
        uploadProgress.setFileKey(fileKey);
        uploadProgress.setUploadedSize(record.getUploadedSize());
        uploadProgress.setTotalSize(record.getFileSize());
        uploadProgress.setProgress(progress);
        uploadProgress.setUploadedChunkCount(uploadedCount);
        uploadProgress.setTotalChunkCount(record.getTotalChunks());

        return uploadProgress;
    }

    // 私有辅助方法
    private String generateFileKey(String fileHash, String fileName) {
        return fileHash + "_" + System.currentTimeMillis() + "_" + fileName.hashCode();
    }

    private String generateObjectName(String fileHash, String fileName) {
        String extension = fileName.contains(".") ?
                fileName.substring(fileName.lastIndexOf(".")) : "";
        String date = LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        return String.format("%s/%s%s", date, fileHash, extension);
    }

    private String generateChunkObjectName(String fileHash, Integer chunkNumber) {
        return String.format("chunks/%s/%d.tmp", fileHash, chunkNumber);
    }

    private String getFileUrl(String bucketName, String objectName) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(objectName)
                            .expiry(7, TimeUnit.DAYS)
                            .build()
            );
        } catch (Exception e) {
            log.error("生成文件URL失败", e);
            return null;
        }
    }

    private String getFileKeyByHash(String fileHash) {
        FileUploadRecord record = getBaseMapper().selectByFileHash(fileHash);
        return record != null ? record.getFileKey() : null;
    }

    private void updateUploadProgress(String fileKey, Long chunkSize) {
        getBaseMapper().increaseUploadedSize(fileKey, chunkSize);
    }

}




