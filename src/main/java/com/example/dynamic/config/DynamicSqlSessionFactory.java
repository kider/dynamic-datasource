/**
 * Gome.com.cn Inc.
 * Copyright (c) 2016-2018 All Rights Reserved.
 */
package com.example.dynamic.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DynamicSqlSessionFactory implements ApplicationContextAware {


    private static final Logger logger = LogManager.getLogger(DynamicSqlSessionFactory.class);

    /**
     * 处理类集合
     */
    private static Map<String, SqlSessionFactory> sqlSessionFactoryMap;


    /**
     * 加载实现类
     *
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {

        Map<String, DynamicSqlSessionFactoryBean> map = applicationContext.getBeansOfType(DynamicSqlSessionFactoryBean.class);

        sqlSessionFactoryMap = new HashMap<>();

        for (DynamicSqlSessionFactoryBean value : map.values()) {
            try {
                sqlSessionFactoryMap.put(value.getDataSourceBeanName(), value.getObject());
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }

    /**
     * 通过dataSourceName获取服务
     *
     * @param dataSourceName
     * @param <T>
     * @return
     */
    public static <T extends SqlSessionFactory> T getSqlSessionFactory(String dataSourceName) {
        return (T) sqlSessionFactoryMap.get(dataSourceName);
    }


}
