package com.example.dynamic.mapper.system;

import com.example.dynamic.annotation.DynamicDataSource;
import com.example.dynamic.model.system.Record;
import com.example.dynamic.mybatis.pulgin.Pager;

import java.util.List;

@DynamicDataSource(dataSourceType = DynamicDataSource.SYSTEM_DS)
public interface SystemMapper {

    void saveRecord(Record record) throws Exception;

    List<Record> selectRecordPaginationList(Pager<Record> pager);

}
