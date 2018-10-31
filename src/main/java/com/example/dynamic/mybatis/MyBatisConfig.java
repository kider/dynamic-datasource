package com.example.dynamic.mybatis;

import com.example.dynamic.mybatis.pulgin.PagePlugin;
import com.example.dynamic.mybatis.pulgin.dialet.MySQLDialect;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyBatisConfig {

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionTemplateBeanName("dynamicSqlSessionTemplate");
        mapperScannerConfigurer.setBasePackage("com.example.dynamic.dao");
        return mapperScannerConfigurer;
    }


    @Bean("pagePlugin")
    public PagePlugin getPagePlugin() {
        //default
        return new PagePlugin(new MySQLDialect());
    }

}
