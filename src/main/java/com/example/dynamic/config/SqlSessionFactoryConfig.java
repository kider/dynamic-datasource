package com.example.dynamic.config;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
public class SqlSessionFactoryConfig {

    private static final Logger logger = LogManager.getLogger(DataSourceConfig.class);

    @Autowired
    @Bean(name = "systemSqlSessionFactory")
    public SqlSessionFactoryBean systemSqlSessionFactory(Environment env, @Qualifier("systemDataSource") DataSource ds) throws IOException {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        String mapperLocations = env.getProperty("mybatis.systemDB.mapperLocations");
        Resource[] resources = new PathMatchingResourcePatternResolver()
                .getResources(mapperLocations);
        sqlSessionFactoryBean.setMapperLocations(resources);
        sqlSessionFactoryBean.setDataSource(ds);
        return sqlSessionFactoryBean;
    }

    @Autowired
    @Bean(name = "businessSqlSessionFactory")
    public SqlSessionFactoryBean businessSqlSessionFactory(Environment env, @Qualifier("businessDataSource") DataSource ds) throws IOException {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        String mapperLocations = env.getProperty("mybatis.businessDB.mapperLocations");
        Resource[] resources = new PathMatchingResourcePatternResolver()
                .getResources(mapperLocations);
        sqlSessionFactoryBean.setMapperLocations(resources);
        sqlSessionFactoryBean.setDataSource(ds);
        return sqlSessionFactoryBean;
    }

}
