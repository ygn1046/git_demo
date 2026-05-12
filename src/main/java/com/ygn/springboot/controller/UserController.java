package com.ygn.springboot.controller;

import com.ygn.springboot.domain.UserInfo;
import com.ygn.springboot.result.R;
import com.ygn.springboot.service.MinioService;
import com.ygn.springboot.service.UserInfoService;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import io.minio.UploadObjectArgs;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.util.List;

@RestController
@RequestMapping("/userController")
public class UserController {
   @Autowired
    UserInfoService userInfoService;

    @Resource
    private MinioClient minioClient;


   @GetMapping("/getUserList")
    public R getUserList(){
        List<UserInfo> list = userInfoService.list();
        return  R.ok(list);
    }

    @PostMapping("/upload/image")
    public R uploadImage(MultipartFile file){
        try {
            ObjectWriteResponse objectWriteResponse = minioClient.putObject(PutObjectArgs.builder()
                    .bucket("my-file")
                    .object(file.getOriginalFilename()).stream(file.getInputStream(), file.getSize(), -1).build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return  R.ok();
    }
    @PostMapping("/upload/contact")
    public R uploadContact(MultipartFile file){
        try {
            ObjectWriteResponse objectWriteResponse = minioClient.putObject(PutObjectArgs.builder()
                    .bucket("my-file")
                    .object(file.getOriginalFilename()).stream(file.getInputStream(), file.getSize(), -1).build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return  R.ok();
    }
}
