package com.ygn.springboot.utils;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.ygn.springboot.dto.UploadProgress;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
public class FileUtils {
    
    private static final Map<String, Object> FILE_LOCK_MAP = new ConcurrentHashMap<>();
    
    // 缓存上传进度
    private static final Cache<String, UploadProgress> UPLOAD_PROGRESS_CACHE = Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofMinutes(30))
            .maximumSize(1000)
            .build();
    
    /**
     * 计算文件MD5
     */
    public static String calculateFileHash(File file, long chunkSize) throws IOException {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r")) {
            MessageDigest md5Digest = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[(int) chunkSize];
            long fileSize = file.length();
            long bytesRead = 0;
            
            while (bytesRead < fileSize) {
                int bytes = randomAccessFile.read(buffer);
                if (bytes > 0) {
                    md5Digest.update(buffer, 0, bytes);
                    bytesRead += bytes;
                }
            }
            
            byte[] digest = md5Digest.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte b : digest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new IOException("计算文件哈希失败", e);
        }
    }
    
    /**
     * 计算分片哈希
     */
    public static String calculateChunkHash(MultipartFile file) throws IOException {
        return DigestUtils.md5Hex(file.getBytes());
    }
    
    /**
     * 获取文件后缀
     */
    public static String getFileExtension(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return "";
        }
        int dotIndex = fileName.lastIndexOf(".");
        return dotIndex > 0 ? fileName.substring(dotIndex) : "";
    }
    
    /**
     * 生成文件存储路径
     */
    public static String generateObjectName(String fileHash, String fileName) {
        String extension = getFileExtension(fileName);
        String datePath = new java.text.SimpleDateFormat("yyyy/MM/dd").format(new Date());
        return String.format("%s/%s%s", datePath, fileHash, extension);
    }
    
    /**
     * 生成临时分片对象名
     */
    public static String generateChunkObjectName(String fileHash, Integer chunkNumber) {
        return String.format("chunks/%s/%d.tmp", fileHash, chunkNumber);
    }
    
    /**
     * 获取文件锁
     */
    public static synchronized Object getFileLock(String fileKey) {
        return FILE_LOCK_MAP.computeIfAbsent(fileKey, k -> new Object());
    }
    
    /**
     * 更新上传进度
     */
    public static void updateProgress(String fileKey, long uploadedSize, long totalSize, 
                                      int uploadedChunks, int totalChunks) {
        double progress = totalSize > 0 ? (double) uploadedSize / totalSize * 100 : 0;
        UploadProgress uploadProgress = new UploadProgress();
        uploadProgress.setFileKey(fileKey);
        uploadProgress.setUploadedSize(uploadedSize);
        uploadProgress.setTotalSize(totalSize);
        uploadProgress.setProgress(progress);
        uploadProgress.setUploadedChunkCount(uploadedChunks);
        uploadProgress.setTotalChunkCount(totalChunks);
        
        UPLOAD_PROGRESS_CACHE.put(fileKey, uploadProgress);
    }
    
    /**
     * 获取上传进度
     */
    public static UploadProgress getProgress(String fileKey) {
        return UPLOAD_PROGRESS_CACHE.getIfPresent(fileKey);
    }
    
    /**
     * 清理进度缓存
     */
    public static void clearProgress(String fileKey) {
        UPLOAD_PROGRESS_CACHE.invalidate(fileKey);
    }
}