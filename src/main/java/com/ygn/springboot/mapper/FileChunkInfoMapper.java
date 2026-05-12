package com.ygn.springboot.mapper;

import com.ygn.springboot.domain.FileChunkInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author 10467
* @description 针对表【file_chunk_info(文件分片信息表)】的数据库操作Mapper
* @createDate 2026-04-22 17:56:17
* @Entity com.ygn.springboot.domain.FileChunkInfo
*/
public interface FileChunkInfoMapper extends BaseMapper<FileChunkInfo> {
    @Select("SELECT * FROM file_chunk_info WHERE file_key = #{fileKey} AND upload_status = 1 ORDER BY chunk_number")
    List<FileChunkInfo> selectUploadedChunks(@Param("fileKey") String fileKey);

    @Select("SELECT * FROM file_chunk_info WHERE file_key = #{fileKey} AND chunk_number = #{chunkNumber}")
    FileChunkInfo selectByFileKeyAndChunkNumber(
            @Param("fileKey") String fileKey,
            @Param("chunkNumber") Integer chunkNumber
    );

    @Select("SELECT COUNT(*) FROM file_chunk_info WHERE file_key = #{fileKey} AND upload_status = 1")
    int countUploadedChunks(@Param("fileKey") String fileKey);
}




