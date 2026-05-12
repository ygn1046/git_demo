package com.ygn.springboot;

import com.ygn.springboot.bean.Dog;
import com.ygn.springboot.bean.User;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.ygn.springboot.mapper")
@Slf4j
public class Application {

    public static void main(String[] args) {

        var ioc= SpringApplication.run(Application.class, args);
//        String[] beanDefinitionNames = ioc.getBeanDefinitionNames();
//        for (String name:beanDefinitionNames){
//            System.out.println(name);
//        }
//        Object userBean1 = ioc.getBean("userBean");
//        Object userBean2 = ioc.getBean("userBean");
//        System.out.println(Integer.toHexString(System.identityHashCode(userBean1)));
//        System.out.println(Integer.toHexString(System.identityHashCode(userBean2)));

       for (String user:ioc.getBeanNamesForType(User.class)){
           System.out.println("user:"+user);
       }
        for (String dog:ioc.getBeanNamesForType(Dog.class)){
            System.out.println("dog:"+dog);
        }
        User user = ioc.getBean(User.class);
        System.out.println(user);
    }

}
