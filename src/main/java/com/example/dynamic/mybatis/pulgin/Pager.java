package com.example.dynamic.mybatis.pulgin;

import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分页
 *
 * @param <E>
 */
public class Pager<E> {

    public static int DEFAULT_PAGESIZE = 10;

    /**
     * 当前页
     */
    private int pageNo = 1;
    /**
     * 每页条数 默认10
     */
    private int pageSize = DEFAULT_PAGESIZE;
    /**
     * 第几条开始
     */
    private int startRow = 1;
    /**
     * 第几条结束
     */
    private int endRow;
    /**
     * 总条数
     */
    private int total;
    /**
     * 总页数
     */
    private int totalPage;
    /**
     * 前一页
     */
    private int prePage;
    /**
     * 后一页
     */
    private int nextPage;
    /**
     * 导航页数
     */
    private int[] navigatepageNums;
    /**
     * 数据集合
     */
    private List<E> list;
    /**
     * 数据库类型。默认mysql
     */
    private String dbType;
    /**
     * 查询条件
     */
    private Map<String, Object> params = new HashMap<String, Object>();
    /**
     * 排序方式
     */
    private String sortOrder;
    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 是否有前一页
     */
    private boolean hasPreviousPage;

    /**
     * 是否有后一页
     */
    private boolean hasNextPage;

    public Pager() {
    }

    public Pager(String pageNo, String pageSize) {
        this.pageNo = !StringUtils.isEmpty(pageNo) ? Integer
                .parseInt(pageNo) : 0;
        this.pageSize = !StringUtils.isEmpty(pageSize) ? Integer
                .parseInt(pageSize) : 20;
    }

    public void setTotal(int total) {
        this.total = total;
        this.totalPage = (total % this.pageSize == 0 ? total / this.pageSize
                : total / this.pageSize + 1);
        this.startRow = (this.pageNo - 1) * this.pageSize;
        this.endRow = this.startRow + this.pageSize;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getEndRow() {
        return endRow;
    }

    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }

    public int getTotal() {
        return total;
    }

    public List<E> getList() {
        return list;
    }

    public void setList(List<E> list) {
        this.list = list;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getPrePage() {
        return getPageNo() - 1;
    }


    public int getNextPage() {
        return getPageNo() + 1;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public boolean isHasPreviousPage() {
        if (getPageNo() > 1) {
            return true;
        }
        return false;
    }


    public boolean isHasNextPage() {
        if (getPageNo() < getTotalPage()) {
            return true;
        }
        return false;
    }


    public int[] getNavigatepageNums() {

        navigatepageNums = new int[getTotalPage()];

        int start = 0;

        for (; start < getTotalPage(); start++) {

            navigatepageNums[start] = start + 1;
        }

        return navigatepageNums;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"pageNo\":")
                .append(pageNo);
        sb.append(",\"pageSize\":")
                .append(pageSize);
        sb.append(",\"startRow\":")
                .append(startRow);
        sb.append(",\"endRow\":")
                .append(endRow);
        sb.append(",\"total\":")
                .append(total);
        sb.append(",\"totalPage\":")
                .append(totalPage);
        sb.append(",\"prePage\":")
                .append(prePage);
        sb.append(",\"nextPage\":")
                .append(nextPage);
        sb.append(",\"navigatepageNums\":")
                .append(Arrays.toString(navigatepageNums));
        sb.append(",\"list\":")
                .append(list);
        sb.append(",\"dbType\":\"")
                .append(dbType).append('\"');
        sb.append(",\"params\":")
                .append(params);
        sb.append(",\"sortOrder\":\"")
                .append(sortOrder).append('\"');
        sb.append(",\"sortField\":\"")
                .append(sortField).append('\"');
        sb.append(",\"hasPreviousPage\":")
                .append(hasPreviousPage);
        sb.append(",\"hasNextPage\":")
                .append(hasNextPage);
        sb.append('}');
        return sb.toString();
    }
}