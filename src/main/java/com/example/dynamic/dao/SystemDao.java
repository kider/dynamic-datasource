package com.example.dynamic.dao;

import com.example.dynamic.annotation.DynamicDataSource;
import com.example.dynamic.domain.system.Record;
import com.example.dynamic.mybatis.pulgin.Pager;

import java.util.List;

@DynamicDataSource(dataSourceType = DynamicDataSource.SYSTEM_DS)
public interface SystemDao {

    /**
     * 保存
     *
     * @param record
     */
    public void saveRecord(Record record);

    /**
     * 分页查询
     *
     * @param pager
     * @return
     */
    public List<Record> selectRecordPaginationList(Pager<Record> pager);

}
