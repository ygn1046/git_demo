package com.ygn.springboot.configure;

import com.ygn.springboot.bean.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.util.FastByteArrayOutputStream;

@Configuration
@Import(FastByteArrayOutputStream.class)
public class MyConfigure {

//    @Scope("prototype")
//    @Bean
//    public User getUser(){
//        User user = new User();
//        user.setAge(20);
//        user.setName("张三");
//        return user;
//    }
}
