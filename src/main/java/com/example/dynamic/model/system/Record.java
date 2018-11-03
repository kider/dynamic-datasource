package com.example.dynamic.model.system;

import com.example.dynamic.model.BaseEntity;

import javax.persistence.Table;
import java.util.Date;

@Table(name = "t_record")
public class Record extends BaseEntity {

    private String type;

    private String msg;

    private Date buyTime;

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
                .append(getId());
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
