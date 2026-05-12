package com.ygn.springboot.service;

import com.ygn.springboot.domain.FileChunkInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ygn.springboot.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
* @author 10467
* @description 针对表【file_chunk_info(文件分片信息表)】的数据库操作Service
* @createDate 2026-04-22 17:56:17
*/
public interface FileChunkInfoService extends IService<FileChunkInfo> {
    List<FileChunkInfo> getUploadedChunks(String fileKey);
    FileChunkInfo getChunkInfo(String fileKey, Integer chunkNumber);
    boolean saveOrUpdateChunk(FileChunkInfo chunkInfo);
    boolean deleteByFileKey(String fileKey);
    int getUploadedChunkCount(String fileKey);
    void initChunkRecords(String fileKey, int totalChunks);
}
