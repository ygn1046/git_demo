package com.ygn.springboot.configure;

import com.ygn.springboot.bean.Dog;
import com.ygn.springboot.bean.User;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.cbor.Jackson2CborDecoder;

@SpringBootConfiguration
@EnableConfigurationProperties(User.class)
public class MyConfigure2 {

    @Bean("dogBean")
    @ConditionalOnClass(Jackson2CborDecoder.class)
    public Dog getDog(){
        return  new Dog();
    }

//    @Bean("userBean")
//
//    public User getUser(){
//        User user = new User();
//        user.setAge(11);
//        user.setName("李四");
//        return  user;
//    }
}
