package com.example.dynamic.service;

import com.example.dynamic.dao.OrderDao;
import com.example.dynamic.dao.SystemDao;
import com.example.dynamic.domain.business.Order;
import com.example.dynamic.domain.system.Record;
import com.example.dynamic.mybatis.pulgin.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service("buyService")
public class BuyServiceImpl {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private SystemDao systemDao;

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void buy(Order order) {
        orderDao.saveOrder(order);
        //抛出错误
        if ("香蕉".equals(order.getProduct())) {
            throw new RuntimeException("对不起，特价香蕉已经卖完了");
        }
        Record record = new Record();
        record.setType("order");
        record.setMsg("购买" + order.getProduct() + "一个,价格：" + order.getPrice());
        record.setBuyTime(new Date());
        systemDao.saveRecord(record);
        //抛出错误
        if ("芒果".equals(order.getProduct())) {
            throw new RuntimeException("对不起，特价芒果已经卖完了");
        }
    }


    public Pager<Record> getRecordList(Pager<Record> pager) {
        pager.setList(systemDao.selectRecordPaginationList(pager));
        return pager;
    }

}
