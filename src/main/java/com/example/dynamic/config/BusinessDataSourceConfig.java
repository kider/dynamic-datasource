package com.example.dynamic.config;

import com.example.dynamic.tools.PropertiesTools;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import tk.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * BusinessDataSource数据源配置
 *
 * @author kider
 */
@Configuration
@MapperScan(basePackages = BusinessDataSourceConfig.PACKAGE, sqlSessionFactoryRef = "businessSqlSessionFactory")
public class BusinessDataSourceConfig {

    protected static final String PACKAGE = "com.example.dynamic.mapper.business";

    protected static final String MAPPER_LOCATION = "classpath:mybatis/mapper/business/*.xml";

    private static final Logger logger = LogManager.getLogger(BusinessDataSourceConfig.class);

    @Primary
    @Bean(name = "businessDataSource")
    public DataSource businessDataSource(Environment env) {
        AtomikosDataSourceBean ds = new AtomikosDataSourceBean();
        Properties prop = PropertiesTools.build(env, "spring.datasource.druid.businessDB.");
        ds.setXaDataSourceClassName("com.alibaba.druid.pool.xa.DruidXADataSource");
        ds.setUniqueResourceName(prop.getProperty("name"));
        ds.setPoolSize(5);
        ds.setXaProperties(prop);
        return ds;
    }


    @Primary
    @Bean(name = "businessSqlSessionFactory")
    public SqlSessionFactory businessSqlSessionFactory(@Qualifier("businessDataSource") DataSource businessDataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(businessDataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(BusinessDataSourceConfig.MAPPER_LOCATION));
        return sessionFactory.getObject();
    }

}