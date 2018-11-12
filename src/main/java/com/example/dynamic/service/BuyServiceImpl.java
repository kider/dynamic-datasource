package com.example.dynamic.service;

import com.example.dynamic.mapper.business.OrderMapper;
import com.example.dynamic.mapper.system.SystemMapper;
import com.example.dynamic.model.business.Order;
import com.example.dynamic.model.system.Record;
import com.example.dynamic.mybatis.pulgin.Pager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service("buyService")
public class BuyServiceImpl {

    private static final Logger logger = LogManager.getLogger(BuyServiceImpl.class);


    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private SystemMapper systemMapper;

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void buy(Order order) {
        try {
            orderMapper.saveOrder(order);
            Record record = new Record();
            record.setType("order");
            record.setMsg("购买" + order.getProduct() + "一个,价格：" + order.getPrice() + "元！");
            record.setBuyTime(new Date());
            systemMapper.saveRecord(record);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public void getRecordList(Pager<Record> pager) {
        pager.setList(systemMapper.selectRecordPaginationList(pager));
    }

}
