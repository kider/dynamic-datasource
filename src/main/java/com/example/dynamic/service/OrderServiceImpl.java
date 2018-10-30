package com.example.dynamic.service;


import com.example.dynamic.domain.business.Order;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("orderService")
public class OrderServiceImpl {

    private static final Logger logger = LogManager.getLogger(OrderServiceImpl.class);

    @Autowired
    @Qualifier("businessSqlSessionTemplate")
    private SqlSessionTemplate businessSqlSessionTemplate;

    public void saveOrder(Order order) {
        businessSqlSessionTemplate.insert("insert", order);
        //抛出错误
        if ("芒果".equals(order.getProduct())) {
            throw new RuntimeException("对不起，特价芒果已经卖完了");
        }
    }


}
