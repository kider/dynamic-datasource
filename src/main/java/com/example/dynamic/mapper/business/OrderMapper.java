package com.example.dynamic.mapper.business;

import com.example.dynamic.annotation.DynamicDataSource;
import com.example.dynamic.model.business.Order;

@DynamicDataSource(dataSourceType = DynamicDataSource.BUSINESS_DS)
public interface OrderMapper {

    void saveOrder(Order order) throws Exception;


}
