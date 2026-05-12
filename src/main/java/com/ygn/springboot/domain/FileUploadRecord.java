package com.ygn.springboot.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * 文件上传记录表
 * @TableName file_upload_record
 */
@TableName(value ="file_upload_record")
@Data
public class FileUploadRecord {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 文件唯一标识
     */
    @TableField(value = "file_key")
    private String fileKey;

    /**
     * 原始文件名
     */
    @TableField(value = "file_name")
    private String fileName;

    /**
     * 文件大小(字节)
     */
    @TableField(value = "file_size")
    private Long fileSize;

    /**
     * 文件MD5哈希值
     */
    @TableField(value = "file_hash")
    private String fileHash;

    /**
     * 文件后缀名
     */
    @TableField(value = "file_suffix")
    private String fileSuffix;

    /**
     * 存储桶名称
     */
    @TableField(value = "bucket_name")
    private String bucketName;

    /**
     * 对象存储路径
     */
    @TableField(value = "object_name")
    private String objectName;

    /**
     * 上传状态(0:上传中,1:成功,2:失败,3:已合并)
     */
    @TableField(value = "upload_status")
    private Integer uploadStatus;

    /**
     * 已上传大小
     */
    @TableField(value = "uploaded_size")
    private Long uploadedSize;

    /**
     * 总分片数
     */
    @TableField(value = "total_chunks")
    private Integer totalChunks;

    /**
     * 已上传分片列表(逗号分隔)
     */
    @TableField(value = "uploaded_chunks")
    private String uploadedChunks;

    /**
     * 上传开始时间
     */
    @TableField(value = "upload_start_time")
    private LocalDateTime uploadStartTime;

    /**
     * 上传结束时间
     */
    @TableField(value = "upload_end_time")
    private LocalDateTime uploadEndTime;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

//    /**
//     * 更新时间
//     */
//    @TableField(value = "upLocalDateTime_time")
//    private LocalDateTime upLocalDateTimeTime;

    /**
     * 逻辑删除
     */
    @TableField(value = "is_deleted")
    private Integer isDeleted;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        FileUploadRecord other = (FileUploadRecord) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getFileKey() == null ? other.getFileKey() == null : this.getFileKey().equals(other.getFileKey()))
            && (this.getFileName() == null ? other.getFileName() == null : this.getFileName().equals(other.getFileName()))
            && (this.getFileSize() == null ? other.getFileSize() == null : this.getFileSize().equals(other.getFileSize()))
            && (this.getFileHash() == null ? other.getFileHash() == null : this.getFileHash().equals(other.getFileHash()))
            && (this.getFileSuffix() == null ? other.getFileSuffix() == null : this.getFileSuffix().equals(other.getFileSuffix()))
            && (this.getBucketName() == null ? other.getBucketName() == null : this.getBucketName().equals(other.getBucketName()))
            && (this.getObjectName() == null ? other.getObjectName() == null : this.getObjectName().equals(other.getObjectName()))
            && (this.getUploadStatus() == null ? other.getUploadStatus() == null : this.getUploadStatus().equals(other.getUploadStatus()))
            && (this.getUploadedSize() == null ? other.getUploadedSize() == null : this.getUploadedSize().equals(other.getUploadedSize()))
            && (this.getTotalChunks() == null ? other.getTotalChunks() == null : this.getTotalChunks().equals(other.getTotalChunks()))
            && (this.getUploadedChunks() == null ? other.getUploadedChunks() == null : this.getUploadedChunks().equals(other.getUploadedChunks()))
            && (this.getUploadStartTime() == null ? other.getUploadStartTime() == null : this.getUploadStartTime().equals(other.getUploadStartTime()))
            && (this.getUploadEndTime() == null ? other.getUploadEndTime() == null : this.getUploadEndTime().equals(other.getUploadEndTime()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getIsDeleted() == null ? other.getIsDeleted() == null : this.getIsDeleted().equals(other.getIsDeleted()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getFileKey() == null) ? 0 : getFileKey().hashCode());
        result = prime * result + ((getFileName() == null) ? 0 : getFileName().hashCode());
        result = prime * result + ((getFileSize() == null) ? 0 : getFileSize().hashCode());
        result = prime * result + ((getFileHash() == null) ? 0 : getFileHash().hashCode());
        result = prime * result + ((getFileSuffix() == null) ? 0 : getFileSuffix().hashCode());
        result = prime * result + ((getBucketName() == null) ? 0 : getBucketName().hashCode());
        result = prime * result + ((getObjectName() == null) ? 0 : getObjectName().hashCode());
        result = prime * result + ((getUploadStatus() == null) ? 0 : getUploadStatus().hashCode());
        result = prime * result + ((getUploadedSize() == null) ? 0 : getUploadedSize().hashCode());
        result = prime * result + ((getTotalChunks() == null) ? 0 : getTotalChunks().hashCode());
        result = prime * result + ((getUploadedChunks() == null) ? 0 : getUploadedChunks().hashCode());
        result = prime * result + ((getUploadStartTime() == null) ? 0 : getUploadStartTime().hashCode());
        result = prime * result + ((getUploadEndTime() == null) ? 0 : getUploadEndTime().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getIsDeleted() == null) ? 0 : getIsDeleted().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", fileKey=").append(fileKey);
        sb.append(", fileName=").append(fileName);
        sb.append(", fileSize=").append(fileSize);
        sb.append(", fileHash=").append(fileHash);
        sb.append(", fileSuffix=").append(fileSuffix);
        sb.append(", bucketName=").append(bucketName);
        sb.append(", objectName=").append(objectName);
        sb.append(", uploadStatus=").append(uploadStatus);
        sb.append(", uploadedSize=").append(uploadedSize);
        sb.append(", totalChunks=").append(totalChunks);
        sb.append(", uploadedChunks=").append(uploadedChunks);
        sb.append(", uploadStartTime=").append(uploadStartTime);
        sb.append(", uploadEndTime=").append(uploadEndTime);
        sb.append(", createTime=").append(createTime);
        sb.append(", isDeleted=").append(isDeleted);
        sb.append("]");
        return sb.toString();
    }
}