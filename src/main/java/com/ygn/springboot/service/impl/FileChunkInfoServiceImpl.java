package com.ygn.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ygn.springboot.domain.FileChunkInfo;
import com.ygn.springboot.dto.*;
import com.ygn.springboot.service.FileChunkInfoService;
import com.ygn.springboot.mapper.FileChunkInfoMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
* @author 10467
* @description 针对表【file_chunk_info(文件分片信息表)】的数据库操作Service实现
* @createDate 2026-04-22 17:56:17
*/
@Service
public class FileChunkInfoServiceImpl extends ServiceImpl<FileChunkInfoMapper, FileChunkInfo>
    implements FileChunkInfoService{


    @Override
    public List<FileChunkInfo> getUploadedChunks(String fileKey) {
        return getBaseMapper().selectUploadedChunks(fileKey);
    }

    @Override
    public FileChunkInfo getChunkInfo(String fileKey, Integer chunkNumber) {
        return getBaseMapper().selectByFileKeyAndChunkNumber(fileKey, chunkNumber);
    }

    @Override
    public boolean saveOrUpdateChunk(FileChunkInfo chunkInfo) {
        FileChunkInfo existing = getChunkInfo(chunkInfo.getFileKey(), chunkInfo.getChunkNumber());
        if (existing != null) {
            chunkInfo.setId(existing.getId());
            return this.updateById(chunkInfo);
        } else {
            return this.save(chunkInfo);
        }
    }

    @Override
    public boolean deleteByFileKey(String fileKey) {
        LambdaQueryWrapper<FileChunkInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FileChunkInfo::getFileKey, fileKey);
        return this.remove(wrapper);
    }

    @Override
    public int getUploadedChunkCount(String fileKey) {
        return getBaseMapper().countUploadedChunks(fileKey);
    }

    @Override
    @Transactional
    public void initChunkRecords(String fileKey, int totalChunks) {
        // 先删除旧记录
        deleteByFileKey(fileKey);

        // 创建新记录
        List<FileChunkInfo> chunkInfos = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (int i = 1; i <= totalChunks; i++) {
            FileChunkInfo chunkInfo = new FileChunkInfo();
            chunkInfo.setFileKey(fileKey);
            chunkInfo.setChunkNumber(i);
            chunkInfo.setUploadStatus(0);
            chunkInfo.setCreateTime(now);
            chunkInfo.setUpdateTime(now);
            chunkInfo.setChunkSize(5 * 1024 * 1024l);
            chunkInfos.add(chunkInfo);
        }

        this.saveBatch(chunkInfos);
    }
}




