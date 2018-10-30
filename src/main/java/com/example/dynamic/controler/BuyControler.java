package com.example.dynamic.controler;


import com.example.dynamic.domain.business.Order;
import com.example.dynamic.service.BuyServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/buy")
public class BuyControler {

    private static final Logger logger = LogManager.getLogger(BuyControler.class);

    @Autowired
    private BuyServiceImpl buyService;


    @RequestMapping(value = "")
    public String index(ModelMap map) {
        Map<String, Double> goodsList = new HashMap<>();
        goodsList.put("苹果", 0.5d);
        goodsList.put("香蕉", 0.6d);
        goodsList.put("橘子", 0.4d);
        goodsList.put("芒果", 0.7d);
        map.put("goodsList", goodsList);
        return "index";

    }

    @RequestMapping(value = "/pay")
    public String pay(Order order, ModelMap map) {
        try {
            buyService.buy(order);
            map.put("result", "恭喜您，抢购成功!");
        } catch (Exception e) {
            logger.error(e.getMessage());
            map.put("result", e.getMessage());
        }
        return "result";
    }


}
