package com.example.dynamic.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface DynamicDataSource {

    public final static String BUSINESS_DS = "businessDataSource";


    public final static String SYSTEM_DS = "systemDataSource";


    String dataSourceType() default DynamicDataSource.BUSINESS_DS;
}
