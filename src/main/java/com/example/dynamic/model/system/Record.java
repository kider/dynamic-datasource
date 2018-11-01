package com.example.dynamic.model.system;

import java.io.Serializable;
import java.util.Date;

public class Record implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;

    private String type;

    private String msg;

    private Date buyTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Date getBuyTime() {
        return buyTime;
    }

    public void setBuyTime(Date buyTime) {
        this.buyTime = buyTime;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":")
                .append(id);
        sb.append(",\"type\":\"")
                .append(type).append('\"');
        sb.append(",\"msg\":\"")
                .append(msg).append('\"');
        sb.append(",\"buyTime\":\"")
                .append(buyTime).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
