package com.example.dynamic.config;


import com.example.dynamic.annotation.DynamicDataSource;
import com.example.dynamic.mybatis.extend.DynamicSqlSessionTemplate;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DynamicSqlSessionTemplateConfig {

    @Bean("dynamicSqlSessionTemplate")
    public DynamicSqlSessionTemplate getDynamicSqlSessionTemplate() {

        //default
        SqlSessionFactory defaultSqlSessionFactory = DynamicSqlSessionFactory.getSqlSessionFactory(DynamicDataSource.BUSINESS_DS);

        return new DynamicSqlSessionTemplate(defaultSqlSessionFactory);

    }


}
