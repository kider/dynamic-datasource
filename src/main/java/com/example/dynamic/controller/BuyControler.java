package com.example.dynamic.controller;


import com.example.dynamic.model.business.Order;
import com.example.dynamic.model.system.Record;
import com.example.dynamic.mybatis.pulgin.Pager;
import com.example.dynamic.service.BuyServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/buy")
public class BuyControler {

    private static final Logger logger = LogManager.getLogger(BuyControler.class);

    @Autowired
    private BuyServiceImpl buyService;


    @RequestMapping(value = "")
    public ModelAndView index(Record record, Pager<Record> pager) {
        ModelAndView result = new ModelAndView("index");
        Map<String, Double> goodsList = new HashMap<>(4);
        goodsList.put("苹果", 0.5d);
        goodsList.put("香蕉", 0.6d);
        goodsList.put("橘子", 0.4d);
        goodsList.put("芒果", 0.7d);
        result.addObject("goodsList", goodsList);
        //购买记录
        Map<String, Object> params = new HashMap<>();
        params.put("msg", record.getMsg());
        pager.setParams(params);
        pager.setSortOrder("desc");
        pager.setSortField("buy_time");
        buyService.getRecordList(pager);
        result.addObject("pageInfo", pager);
        result.addObject("record", record);
        return result;

    }

    @RequestMapping(value = "/pay")
    public ModelAndView pay(Order order, ModelMap map) {
        ModelAndView result = new ModelAndView("result");
        try {
            buyService.buy(order);
            result.addObject("result", "恭喜您，抢购成功!");
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.addObject("result", e.getMessage());
        }
        return result;
    }


}
