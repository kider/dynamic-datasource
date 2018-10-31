package com.example.dynamic.config;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SqlSessionTemplateConfig {

    private static final Logger logger = LogManager.getLogger(SqlSessionTemplateConfig.class);


    @Bean("systemSqlSessionTemplate")
    public SqlSessionTemplate systemSqlSessionTemplate(@Qualifier("systemSqlSessionFactoryBean") SqlSessionFactoryBean sqlSessionFactoryBean) throws Exception {

        return new SqlSessionTemplate(sqlSessionFactoryBean.getObject());

    }

    @Bean("businessSqlSessionTemplate")
    public SqlSessionTemplate businessSqlSessionTemplate(@Qualifier("businessSqlSessionFactoryBean") SqlSessionFactoryBean sqlSessionFactoryBean) throws Exception {

        return new SqlSessionTemplate(sqlSessionFactoryBean.getObject());

    }

}
