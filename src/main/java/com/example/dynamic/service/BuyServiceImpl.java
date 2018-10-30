package com.example.dynamic.service;

import com.example.dynamic.domain.business.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("buyService")
public class BuyServiceImpl {

    @Autowired
    private OrderServiceImpl orderService;

    @Autowired
    private SystemServiceImpl systemService;

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void buy(Order order) {
        orderService.saveOrder(order);
        systemService.saveRecord(order);
    }

}
