package com.example.dynamic.dao;

import com.example.dynamic.annotation.DynamicDataSource;
import com.example.dynamic.domain.business.Order;

@DynamicDataSource(dataSourceType = DynamicDataSource.BUSINESS_DS)
public interface OrderDao {

    public void saveOrder(Order order);

}
