package com.example.dynamic.config;


import com.example.dynamic.mybatis.pulgin.PagePlugin;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
public class DynamicSqlSessionFactoryBeanConfig {

    private static final Logger logger = LogManager.getLogger(DataSourceConfig.class);

    @Bean(name = "systemSqlSessionFactoryBean")
    public DynamicSqlSessionFactoryBean systemSqlSessionFactoryBean(Environment env, @Qualifier("systemDataSource") DataSource ds, @Qualifier("pagePlugin") PagePlugin pagePlugin) throws Exception {
        DynamicSqlSessionFactoryBean dynamicSqlSessionFactoryBean = new DynamicSqlSessionFactoryBean();
        String mapperLocations = env.getProperty("mybatis.systemDB.mapperLocations");
        Resource[] resources = new PathMatchingResourcePatternResolver()
                .getResources(mapperLocations);
        dynamicSqlSessionFactoryBean.setMapperLocations(resources);
        dynamicSqlSessionFactoryBean.setDataSource(ds);
        dynamicSqlSessionFactoryBean.setDataSourceBeanName("systemDataSource");
        dynamicSqlSessionFactoryBean.setPlugins(new Interceptor[]{pagePlugin});
        return dynamicSqlSessionFactoryBean;
    }

    @Bean(name = "businessSqlSessionFactoryBean")
    public DynamicSqlSessionFactoryBean businessSqlSessionFactoryBean(Environment env, @Qualifier("businessDataSource") DataSource ds, @Qualifier("pagePlugin") PagePlugin pagePlugin) throws Exception {
        DynamicSqlSessionFactoryBean dynamicSqlSessionFactoryBean = new DynamicSqlSessionFactoryBean();
        String mapperLocations = env.getProperty("mybatis.businessDB.mapperLocations");
        Resource[] resources = new PathMatchingResourcePatternResolver()
                .getResources(mapperLocations);
        dynamicSqlSessionFactoryBean.setMapperLocations(resources);
        dynamicSqlSessionFactoryBean.setDataSource(ds);
        dynamicSqlSessionFactoryBean.setDataSourceBeanName("businessDataSource");
        dynamicSqlSessionFactoryBean.setPlugins(new Interceptor[]{pagePlugin});
        return dynamicSqlSessionFactoryBean;
    }

}
