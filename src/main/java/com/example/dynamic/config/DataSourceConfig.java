package com.example.dynamic.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class DataSourceConfig {

    private static final Logger logger = LogManager.getLogger(DataSourceConfig.class);

    @Bean(name = "systemDataSource")
    public DataSource systemDataSource(Environment env) {
        AtomikosDataSourceBean ds = new AtomikosDataSourceBean();
        Properties prop = build(env, "spring.datasource.druid.systemDB.");
        ds.setXaDataSourceClassName("com.alibaba.druid.pool.xa.DruidXADataSource");
        ds.setUniqueResourceName(prop.getProperty("name"));
        ds.setPoolSize(5);
        ds.setXaProperties(prop);
        return ds;
    }

    @Bean(name = "businessDataSource")
    public DataSource businessDataSource(Environment env) {
        AtomikosDataSourceBean ds = new AtomikosDataSourceBean();
        Properties prop = build(env, "spring.datasource.druid.businessDB.");
        ds.setXaDataSourceClassName("com.alibaba.druid.pool.xa.DruidXADataSource");
        ds.setUniqueResourceName(prop.getProperty("name"));
        ds.setPoolSize(5);
        ds.setXaProperties(prop);
        return ds;
    }

    @Bean("sysJdbcTemplate")
    public JdbcTemplate sysJdbcTemplate(@Qualifier("systemDataSource") DataSource ds) {
        return new JdbcTemplate(ds);
    }

    @Bean("busJdbcTemplate")
    public JdbcTemplate busJdbcTemplate(@Qualifier("businessDataSource") DataSource ds) {
        return new JdbcTemplate(ds);
    }


    /**
     * 读取DataSource配置
     *
     * @param env
     * @param prefix
     * @return Properties
     * @see com.alibaba.druid.pool.DruidDataSource
     */
    private Properties build(Environment env, String prefix) {
        Properties prop = new Properties();
        prop.put("url", env.getProperty(prefix + "url"));
        prop.put("name", env.getProperty(prefix + "name"));
        prop.put("username", env.getProperty(prefix + "username"));
        prop.put("password", env.getProperty(prefix + "password"));
        prop.put("driverClassName", env.getProperty(prefix + "driver-class-name", ""));
        prop.put("initialSize", env.getProperty(prefix + "initial-size", Integer.class));
        prop.put("maxActive", env.getProperty(prefix + "max-active", Integer.class));
        prop.put("minIdle", env.getProperty(prefix + "min-idle", Integer.class));
        prop.put("maxWait", env.getProperty(prefix + "max-wait", Integer.class));
        prop.put("poolPreparedStatements", env.getProperty(prefix + "pool-prepared-statements", Boolean.class));
        prop.put("maxPoolPreparedStatementPerConnectionSize",
                env.getProperty(prefix + "max-pool-prepared-statement-per-connection-size", Integer.class));
        prop.put("validationQuery", env.getProperty(prefix + "validation-query"));
        prop.put("validationQueryTimeout", env.getProperty(prefix + "validation-query-timeout", Integer.class));
        prop.put("testOnBorrow", env.getProperty(prefix + "test-on-borrow", Boolean.class));
        prop.put("testOnReturn", env.getProperty(prefix + "test-on-return", Boolean.class));
        prop.put("testWhileIdle", env.getProperty(prefix + "test-while-idle", Boolean.class));
        prop.put("timeBetweenEvictionRunsMillis",
                env.getProperty(prefix + "time-between-eviction-runs-millis", Integer.class));
        prop.put("minEvictableIdleTimeMillis", env.getProperty(prefix + "min-evictable-idle-time-millis", Integer.class));
        prop.put("filters", env.getProperty(prefix + "filters"));
        prop.put("connectProperties", setConnectionProperties(env.getProperty(prefix + "connection-properties")));
        prop.put("useGlobalDataSourceStat", env.getProperty(prefix + "use-global-data-source-stat"));
        logger.info("dataSource Properties :{}", prop.toString());
        return prop;
    }


    /**
     * 把connectionProperties配置转化为 Properties
     *
     * @param connectionProperties
     * @return Properties
     */

    private Properties setConnectionProperties(String connectionProperties) {
        String[] entries = connectionProperties.split(";");
        Properties properties = new Properties();
        for (int i = 0; i < entries.length; i++) {
            String entry = entries[i];
            if (entry.length() > 0) {
                int index = entry.indexOf('=');
                if (index > 0) {
                    String name = entry.substring(0, index);
                    String value = entry.substring(index + 1);
                    properties.setProperty(name, value);
                } else {
                    // no value is empty string which is how java.util.Properties works
                    properties.setProperty(entry, "");
                }
            }
        }
        return properties;
    }

}