package com.example.dynamic.mybatis.pulgin;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pager<T> {
    private int pageNo = 1;
    private int pageSize = 10;
    private int startRow = 1;
    private int endRow;
    private int total;

    private int totalPage;
    private int pages;
    private List<T> list;
    /**
     * 数据库类型。默认mysql
     */
    private String dbType;
    /**
     * 查询条件
     */
    private Map<String, Object> params = new HashMap<String, Object>();
    private String sortOrder;
    private String sortField;


    public Pager() {
        super();
    }

    public Pager(String pageNo, String pageSize) {
        this.pageNo = !StringUtils.isEmpty(pageNo) ? Integer
                .parseInt(pageNo) : 0;
        this.pageSize = !StringUtils.isEmpty(pageSize) ? Integer
                .parseInt(pageSize) : 20;
    }

    public Pager(HttpServletRequest request) {
        String pageSize = request.getParameter("pageSize");
        String pageNo = request.getParameter("pageNo");
        String limit = request.getParameter("limit");
        if (limit != null) {
            String startRowStr = request.getParameter("offset");
            this.startRow = !StringUtils.isEmpty(startRowStr) ? Integer.parseInt(startRowStr) : 0;
            this.pageSize = (!StringUtils.isEmpty(limit) ? Integer.parseInt(limit) : 10);
            this.endRow = this.startRow + this.pageSize;
        } else {
            this.pageSize = (!StringUtils.isEmpty(pageSize) ? Integer.parseInt(pageSize) : 10);
            this.pageNo = (!StringUtils.isEmpty(pageNo) ? Integer.parseInt(pageNo) : 1);
            this.startRow = (this.pageNo <= 0 ? 0 : this.pageNo - 1) * this.getPageSize();
            this.endRow = this.startRow + this.pageSize;
        }
    }

    public void setTotal(int total) {
        this.total = total;
        this.pages = (total % this.pageSize == 0 ? total / this.pageSize : total / this.pageSize + 1);
        this.totalPage = this.pages;
        this.startRow = (this.pageNo - 1) * this.pageSize;
        this.endRow = this.startRow + this.pageSize;
    }


    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
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

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
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

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<T> getList() {
        return this.list;
    }

    public void setList(List<T> list) {
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


    @Override
    public String toString() {
        return "Pager [pageNo=" + pageNo + ", pageSize=" + pageSize + ", startRow=" + startRow + ", endRow=" + endRow + ", total=" + total + ", pages=" + pages + ", list=" + list + ", dbType=" + dbType + ", params=" + params + ", sortOrder=" + sortOrder + ", sortField=" + sortField + "]";
    }

}
