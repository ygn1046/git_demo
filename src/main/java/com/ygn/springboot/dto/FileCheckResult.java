package com.ygn.springboot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class FileCheckResult {
    private Boolean needUpload = true;
    private List<Integer> uploadedChunks;
    private String fileUrl;
    private String fileKey;
}