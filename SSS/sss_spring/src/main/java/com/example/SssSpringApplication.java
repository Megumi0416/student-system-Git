package com.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({"com.example.mapper", "com.example.SSS.mapper"})
public class SssSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(SssSpringApplication.class, args);
    }

} 