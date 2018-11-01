package com.example.dynamic.mybatis.extend;

import org.mybatis.spring.SqlSessionFactoryBean;

/**
 * 扩展SqlSessionFactoryBean
 */
public class DynamicSqlSessionFactoryBean extends SqlSessionFactoryBean {

    /**
     * dataSource BeanName
     */
    private String dataSourceBeanName;

    public String getDataSourceBeanName() {
        return dataSourceBeanName;
    }

    public void setDataSourceBeanName(String dataSourceBeanName) {
        this.dataSourceBeanName = dataSourceBeanName;
    }
}
