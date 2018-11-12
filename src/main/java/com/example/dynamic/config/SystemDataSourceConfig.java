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
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import tk.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * SystemDataSource数据源配置
 *
 * @author kider
 */
@Configuration
@MapperScan(basePackages = SystemDataSourceConfig.PACKAGE, sqlSessionFactoryRef = "systemSqlSessionFactory")
public class SystemDataSourceConfig {

    protected static final String PACKAGE = "com.example.dynamic.mapper.system";

    protected static final String MAPPER_LOCATION = "classpath:mybatis/mapper/system/*.xml";

    private static final Logger logger = LogManager.getLogger(SystemDataSourceConfig.class);

    @Bean(name = "systemDataSource")
    public DataSource systemDataSource(Environment env) {
        AtomikosDataSourceBean ds = new AtomikosDataSourceBean();
        Properties prop = PropertiesTools.build(env, "spring.datasource.druid.systemDB.");
        ds.setXaDataSourceClassName("com.alibaba.druid.pool.xa.DruidXADataSource");
        ds.setUniqueResourceName(prop.getProperty("name"));
        ds.setPoolSize(5);
        ds.setXaProperties(prop);
        return ds;
    }


    @Bean(name = "systemSqlSessionFactory")
    public SqlSessionFactory systemSqlSessionFactory(@Qualifier("systemDataSource") DataSource systemDataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(systemDataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(SystemDataSourceConfig.MAPPER_LOCATION));
        return sessionFactory.getObject();
    }

}