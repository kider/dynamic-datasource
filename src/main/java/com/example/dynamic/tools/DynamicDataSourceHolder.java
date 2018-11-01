package com.example.dynamic.tools;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * dataSourceType线程变量
 */
public class DynamicDataSourceHolder {

    private static final Logger logger = LogManager.getLogger(DynamicDataSourceHolder.class);

    /**
     * DataSource上下文，每个线程对应相应的数据源key
     */
    public static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

    public static void setDataSourceType(String dataSourceType) {
        contextHolder.set(dataSourceType);
        logger.info("DynamicDataSourceHolder 当前线程Thread:" + Thread.currentThread().getName() + " 当前的数据源 set key is " + dataSourceType);

    }

    public static String getDataSourceType() {
        String dataSourceType = contextHolder.get();
        logger.info("DynamicDataSourceHolder 当前线程Thread:" + Thread.currentThread().getName() + " 当前的数据源 get key is " + dataSourceType);
        return dataSourceType;
    }

    public static void clearDataSourceType() {
        contextHolder.remove();
    }
}
