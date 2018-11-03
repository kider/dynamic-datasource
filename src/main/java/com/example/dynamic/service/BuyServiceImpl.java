package com.example.dynamic.service;

import com.example.dynamic.mapper.business.OrderMapper;
import com.example.dynamic.mapper.system.SystemMapper;
import com.example.dynamic.model.business.Order;
import com.example.dynamic.model.system.Record;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.util.Date;
import java.util.List;

@Service("buyService")
public class BuyServiceImpl {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private SystemMapper systemMapper;

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void buy(Order order) {
        orderMapper.insert(order);
        Record record = new Record();
        record.setType("order");
        record.setMsg("购买" + order.getProduct() + "一个,价格：" + order.getPrice() + "元！");
        record.setBuyTime(new Date());
        systemMapper.insert(record);
    }


    public List<Record> getRecordList(Record record) {
        if (record.getPage() != null && record.getRows() != null) {
            PageHelper.startPage(record.getPage(), record.getRows());
        }
        Weekend<Record> weekend = Weekend.of(Record.class);
        WeekendCriteria<Record, Object> criteria = weekend.weekendCriteria();
        if (!StringUtils.isEmpty(record.getMsg())) {
            criteria.andLike(Record::getMsg, "%" + record.getMsg() + "%");
        }
        weekend.orderBy("buyTime").desc();
        return systemMapper.selectByExample(weekend);
    }

}
