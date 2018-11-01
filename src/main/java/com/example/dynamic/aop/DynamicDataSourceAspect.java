package com.example.dynamic.aop;

import com.example.dynamic.annotation.DynamicDataSource;
import com.example.dynamic.tools.DynamicDataSourceHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class DynamicDataSourceAspect {

    private static final Logger logger = LogManager.getLogger(DynamicDataSourceAspect.class);

    @Pointcut("execution(* com.example.dynamic.mapper..*.*(..))")
    public void switchDataSource() {
    }


    @Before("switchDataSource()")
    public void doBefore(JoinPoint joinPoint) {

        try {
            Signature signature = joinPoint.getSignature();
            MethodSignature methodSignature = (MethodSignature) signature;
            Method method = methodSignature.getMethod();
            boolean methodAnnotation = method.isAnnotationPresent(DynamicDataSource.class);

            DynamicDataSource dynamicDataSource = null;

            if (methodAnnotation) {
                dynamicDataSource = method.getAnnotation(DynamicDataSource.class);
            } else {
                Class[] clazz = joinPoint.getTarget().getClass().getInterfaces();
                dynamicDataSource = (DynamicDataSource) clazz[0].getAnnotation(DynamicDataSource.class);
            }
            if (dynamicDataSource != null) {
                DynamicDataSourceHolder.setDataSourceType(dynamicDataSource.dataSourceType());
                logger.info("mybatis接口: " + (method.getDeclaringClass() + "." + method.getName()) + " 设置数据源 key is " + dynamicDataSource.dataSourceType());
            }
        } catch (Exception e) {
            logger.error("切换数据源异常：" + e.getMessage(), e);
        }
    }
}
