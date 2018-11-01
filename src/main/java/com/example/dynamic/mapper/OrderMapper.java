package com.example.dynamic.mapper;

import com.example.dynamic.annotation.DynamicDataSource;
import com.example.dynamic.model.business.Order;

@DynamicDataSource(dataSourceType = DynamicDataSource.BUSINESS_DS)
public interface OrderMapper {

    public void saveOrder(Order order);

}
