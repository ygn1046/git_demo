package com.ygn.springboot.dto;

import lombok.Data;

@Data
public class UploadProgress {
    private String fileKey;
    private Long uploadedSize;
    private Long totalSize;
    private Double progress;
    private Integer uploadedChunkCount;
    private Integer totalChunkCount;
}