package com.ygn.springboot.service;

import io.minio.MinioClient;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class MinioService {

    @Resource
    private MinioClient minioClient;

    public void testMinioClient(){
        System.out.println(minioClient);
    }
}
