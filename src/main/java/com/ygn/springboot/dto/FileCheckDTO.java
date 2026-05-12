package com.ygn.springboot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FileCheckDTO {
    @NotBlank(message = "文件哈希不能为空")
    private String fileHash;
    
    @NotBlank(message = "文件名称不能为空")
    private String fileName;
    
    @NotNull(message = "文件大小不能为空")
    private Long fileSize;
    
    @NotNull(message = "总分片数不能为空")
    private Integer totalChunks;
    
    @NotNull(message = "分片大小不能为空")
    private Long chunkSize;
}