package com.ygn.springboot.service;

import com.ygn.springboot.domain.FileChunkInfo;
import com.ygn.springboot.domain.FileUploadRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ygn.springboot.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
* @author 10467
* @description 针对表【file_upload_record(文件上传记录表)】的数据库操作Service
* @createDate 2026-04-22 17:56:17
*/
public interface FileUploadRecordService extends IService<FileUploadRecord> {
    FileCheckResult checkFile(FileCheckDTO checkDTO);
    void uploadChunk(ChunkUploadDTO uploadDTO, MultipartFile file) throws IOException;
    String mergeFile(FileMergeDTO mergeDTO) throws Exception;
    void cancelUpload(String fileKey);
    UploadProgress getUploadProgress(String fileKey);
}
