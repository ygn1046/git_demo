package com.ygn.springboot.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
@Data
public class ChunkUploadDTO {
    @NotBlank(message = "文件哈希不能为空")
    private String fileHash;
    
    @NotNull(message = "分片序号不能为空")
    private Integer chunkNumber;
    
    @NotNull(message = "总分片数不能为空")
    private Integer totalChunks;
    
    @NotNull(message = "分片大小不能为空")
    private Long chunkSize;
    
    private String chunkHash;
}