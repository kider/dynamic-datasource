package com.example.dynamic.mybatis.pulgin.dialet;

import org.springframework.util.StringUtils;

/**
 * mysql数据库方言
 */
public class MySQLDialect extends Dialect {

    @Override
    public boolean supportsLimitOffset() {
        return true;
    }

    @Override
    public boolean supportsLimit() {
        return true;
    }

    @Override
    public String getLimitString(String sql, int offset, String offsetPlaceholder, int limit, String limitPlaceholder, String sortOrder, String sortField) {
        if (!StringUtils.isEmpty(sortOrder) && !StringUtils.isEmpty(sortField)) {
            sql += " order by " + sortField + " " + sortOrder;
        }
        if (offset > 0) {
            return sql + " limit " + offsetPlaceholder + "," + limitPlaceholder;
        } else {
            return sql + " limit " + limitPlaceholder;
        }
    }
}
