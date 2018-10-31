package com.example.dynamic.mybatis.pulgin.dialet;

import org.springframework.util.StringUtils;

/**
 * oracle数据库方言
 */
public class OracleDialect extends Dialect {

    @Override
    public boolean supportsLimit() {
        return true;
    }

    @Override
    public boolean supportsLimitOffset() {
        return true;
    }

    @Override
    public String getLimitString(String sql, int offset, String offsetPlaceholder, int limit, String limitPlaceholder, String sortOrder, String sortField) {
        sql = sql.trim();
        StringBuffer pagingSelect = new StringBuffer(sql.length() + 100);
        if (offset > 0) {
            pagingSelect.append("select * from ( select row_.*, rownum rownum_ from ( ");
        } else {
            pagingSelect.append("select * from ( ");
        }
        pagingSelect.append(sql);
        if (offset > 0) {
            // int end = offset+limit;
            String endString = offsetPlaceholder + "+" + limitPlaceholder;
            if (!StringUtils.isEmpty(sortOrder) && !StringUtils.isEmpty(sortField)) {
                pagingSelect.append(" order by " + sortField + sortOrder);
            }
            pagingSelect.append(" ) row_ ) where rownum_ <= " + endString + " and rownum_ > " + offsetPlaceholder);
        } else {
            if (!StringUtils.isEmpty(sortOrder) && !StringUtils.isEmpty(sortField)) {
                pagingSelect.append(" order by " + sortField + " " + sortOrder);
            }
            pagingSelect.append(" ) where rownum <= " + limitPlaceholder);
        }
        return pagingSelect.toString();
    }

}
