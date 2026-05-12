package com.ygn.springboot;

import com.ygn.springboot.service.MinioService;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class ApplicationTests {

    @Resource
    private MinioService minioService;

    @Resource
    private MinioClient minioClient;

    @Test
    void contextLoads() {
    }

    @Test
    public void testMinioClientService(){
        minioService.testMinioClient();
    }

    @Test
    public  void  testBucketExists(){
        try {
            boolean myFile = minioClient.bucketExists(BucketExistsArgs.builder().bucket("my-file").build());
            System.out.println("myFile是否存在："+myFile);
        }  catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public  void  testMakeBucket(){
        try {
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket("my-file").build())){
                minioClient.makeBucket(MakeBucketArgs.builder().bucket("my-file").build());
            }else{
                System.out.println("my-file文件夹已经存在");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public  void  testListBuckets(){
        try {
            List<Bucket> buckets = minioClient.listBuckets();
            buckets.forEach(bucket->{
                System.out.println("bucket名称："+bucket.name()+"creationDate:"+bucket.creationDate());
            });

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public  void  testRemoveBucket(){
        try {
           minioClient.removeBucket(RemoveBucketArgs.builder().bucket("my-file").build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //*****************************文件操作
    @Test
    public  void  testPutObject(){
        //"C:\Users\10467\Pictures\9ba9f657ly1hmrwq4i2zgj20u016xaoc.jpg"
        File file = new File("C:\\Users\\10467\\Pictures\\系统\\01.jpg");
        try {
            ObjectWriteResponse objectWriteResponse = minioClient.putObject(PutObjectArgs.builder()
                    .bucket("my-file")
                    .object("test1.jpg").stream(new FileInputStream(file), file.length(), -1).build());
            System.out.println(objectWriteResponse);
            ObjectWriteResponse objectWriteResponse1 = minioClient.uploadObject(UploadObjectArgs.builder().bucket("my-file").object("test2.jpg")
                    .filename("C:\\Users\\10467\\Pictures\\9ba9f657ly1hmrwq4i2zgj20u016xaoc.jpg").build());
            System.out.println(objectWriteResponse1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public  void  testStatObject(){
        //"C:\Users\10467\Pictures\9ba9f657ly1hmrwq4i2zgj20u016xaoc.jpg"
        File file = new File("C:\\Users\\10467\\Pictures\\系统\\01.jpg");
        try {
            StatObjectResponse statObjectResponse = minioClient.statObject(StatObjectArgs.builder().bucket("my-file").object("test1.jpg").build());
            System.out.println(statObjectResponse);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public  void  testGetPresignedObjectUrl() throws  Exception {
        String presignedObjectUrl = minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs
                .builder()
                .bucket("my-file")
                .object("test1.jpg")
                .expiry(3, TimeUnit.MINUTES)
                .method(Method.GET)
                .build());
        System.out.println(presignedObjectUrl);
    }

    @Test
    public  void  testObjectResponse() throws  Exception {
        GetObjectResponse objectResponse = minioClient.getObject(GetObjectArgs
                .builder()
                .bucket("my-file")
                .object("test1.jpg")
                .build());
       objectResponse.transferTo(new FileOutputStream(new File("D:\\test")));
    }

    @Test
    public  void  testListObjects() throws  Exception {
        Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs
                .builder()
                .bucket("my-file")
                .build());
        results.forEach(item-> System.out.println(item));
    }

}
