package com.example.dynamic.service;

import com.example.dynamic.domain.business.Order;
import com.example.dynamic.domain.system.Record;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("systemService")
public class SystemServiceImpl {

    @Autowired
    @Qualifier("systemSqlSessionTemplate")
    private SqlSessionTemplate systemSqlSessionTemplate;

    public void saveRecord(Order order) {
        Record record = new Record();
        record.setType("order");
        record.setMsg("购买" + order.getProduct() + "一个,价格：" + order.getPrice());
        systemSqlSessionTemplate.insert("insert", record);
        //抛出错误
        if ("香蕉".equals(order.getProduct())) {
            throw new RuntimeException("对不起，特价香蕉已经卖完了");
        }
    }

}
