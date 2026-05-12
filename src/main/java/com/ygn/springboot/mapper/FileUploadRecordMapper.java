package com.ygn.springboot.mapper;

import com.ygn.springboot.domain.FileUploadRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
* @author 10467
* @description 针对表【file_upload_record(文件上传记录表)】的数据库操作Mapper
* @createDate 2026-04-22 17:56:17
* @Entity com.ygn.springboot.domain.FileUploadRecord
*/
public interface FileUploadRecordMapper extends BaseMapper<FileUploadRecord> {
    @Select("SELECT * FROM file_upload_record WHERE file_hash = #{fileHash} AND is_deleted = 0")
    FileUploadRecord selectByFileHash(@Param("fileHash") String fileHash);

    @Select("SELECT * FROM file_upload_record WHERE file_key = #{fileKey} AND is_deleted = 0")
    FileUploadRecord selectByFileKey(@Param("fileKey") String fileKey);

    @Update("UPDATE file_upload_record SET uploaded_size = uploaded_size + #{size} WHERE file_key = #{fileKey}")
    int increaseUploadedSize(@Param("fileKey") String fileKey, @Param("size") Long size);
}




