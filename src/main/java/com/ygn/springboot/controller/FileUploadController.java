package com.ygn.springboot.controller;


import com.ygn.springboot.dto.*;
import com.ygn.springboot.service.FileUploadRecordService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



@Slf4j
@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
@Validated
public class FileUploadController {
    
    private final FileUploadRecordService fileUploadService;

    @PostMapping("/check")
    public ResponseEntity<FileCheckResult> checkFile(@Valid @RequestBody FileCheckDTO checkDTO) {
        FileCheckResult result = fileUploadService.checkFile(checkDTO);
        return ResponseEntity.ok(result);
    }
    
    @PostMapping("/chunk")
    public ResponseEntity<String> uploadChunk(
            @Valid @ModelAttribute ChunkUploadDTO uploadDTO,
            @RequestParam("file") MultipartFile file) {
        try {
            fileUploadService.uploadChunk(uploadDTO, file);
            return ResponseEntity.ok("分片上传成功");
        } catch (Exception e) {
            log.error("分片上传失败", e);
            return ResponseEntity.badRequest().body("分片上传失败: " + e.getMessage());
        }
    }
    
    @PostMapping("/merge")
    public ResponseEntity<String> mergeFile(@Valid @RequestBody FileMergeDTO mergeDTO) {
        try {
            String fileUrl = fileUploadService.mergeFile(mergeDTO);
            return ResponseEntity.ok(fileUrl);
        } catch (Exception e) {
            log.error("文件合并失败", e);
            return ResponseEntity.badRequest().body("文件合并失败: " + e.getMessage());
        }
    }
    
    @PostMapping("/cancel/{fileKey}")
    public ResponseEntity<String> cancelUpload(
            @PathVariable @NotBlank(message = "文件标识不能为空") String fileKey) {
        fileUploadService.cancelUpload(fileKey);
        return ResponseEntity.ok("上传已取消");
    }
    
    @GetMapping("/progress/{fileKey}")
    public ResponseEntity<UploadProgress> getUploadProgress(
            @PathVariable @NotBlank(message = "文件标识不能为空") String fileKey) {
        UploadProgress progress = fileUploadService.getUploadProgress(fileKey);
        return ResponseEntity.ok(progress);
    }
}