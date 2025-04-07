package com.example.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = {"com.example.mapper", "com.example.SSS.mapper"})
public class MyBatisConfig {
    // 这个类用于配置MyBatis，确保所有Mapper接口都被正确扫描到
} 