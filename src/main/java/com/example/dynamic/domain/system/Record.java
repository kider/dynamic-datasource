package com.example.dynamic.domain.system;

import java.io.Serializable;

public class Record implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;

    private String type;

    private String msg;

    public void setId(long id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setMsg(String msg) {
        this.msg = msg;
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
        sb.append('}');
        return sb.toString();
    }
}
