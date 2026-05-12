package com.ygn.springboot.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

/**
 * 文件分片信息表
 * @TableName file_chunk_info
 */
@TableName(value ="file_chunk_info")
@Data
public class FileChunkInfo {
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
     * 分片序号
     */
    @TableField(value = "chunk_number")
    private Integer chunkNumber;

    /**
     * 分片哈希值
     */
    @TableField(value = "chunk_hash")
    private String chunkHash;

    /**
     * 分片大小
     */
    @TableField(value = "chunk_size")
    private Long chunkSize;

    /**
     * 上传状态(0:未上传,1:已上传)
     */
    @TableField(value = "upload_status")
    private Integer uploadStatus;

    /**
     * 上传时间
     */
    @TableField(value = "upload_time")
    private LocalDateTime uploadTime;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private LocalDateTime updateTime;

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
        FileChunkInfo other = (FileChunkInfo) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getFileKey() == null ? other.getFileKey() == null : this.getFileKey().equals(other.getFileKey()))
            && (this.getChunkNumber() == null ? other.getChunkNumber() == null : this.getChunkNumber().equals(other.getChunkNumber()))
            && (this.getChunkHash() == null ? other.getChunkHash() == null : this.getChunkHash().equals(other.getChunkHash()))
            && (this.getChunkSize() == null ? other.getChunkSize() == null : this.getChunkSize().equals(other.getChunkSize()))
            && (this.getUploadStatus() == null ? other.getUploadStatus() == null : this.getUploadStatus().equals(other.getUploadStatus()))
            && (this.getUploadTime() == null ? other.getUploadTime() == null : this.getUploadTime().equals(other.getUploadTime()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getFileKey() == null) ? 0 : getFileKey().hashCode());
        result = prime * result + ((getChunkNumber() == null) ? 0 : getChunkNumber().hashCode());
        result = prime * result + ((getChunkHash() == null) ? 0 : getChunkHash().hashCode());
        result = prime * result + ((getChunkSize() == null) ? 0 : getChunkSize().hashCode());
        result = prime * result + ((getUploadStatus() == null) ? 0 : getUploadStatus().hashCode());
        result = prime * result + ((getUploadTime() == null) ? 0 : getUploadTime().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
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
        sb.append(", chunkNumber=").append(chunkNumber);
        sb.append(", chunkHash=").append(chunkHash);
        sb.append(", chunkSize=").append(chunkSize);
        sb.append(", uploadStatus=").append(uploadStatus);
        sb.append(", uploadTime=").append(uploadTime);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append("]");
        return sb.toString();
    }
}