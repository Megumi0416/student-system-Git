package com.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//指定 MyBatis Mapper 接口所在的包路径
@MapperScan("com.example.mapper")
public class SssSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(SssSpringApplication.class, args);
    }

}
