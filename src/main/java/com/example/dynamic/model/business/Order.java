package com.example.dynamic.model.business;

import java.math.BigDecimal;

public class Order {

    private static final long serialVersionUID = 1L;

    private long id;

    private String product;

    private BigDecimal price;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":")
                .append(id);
        sb.append(",\"product\":\"")
                .append(product).append('\"');
        sb.append(",\"price\":")
                .append(price);
        sb.append('}');
        return sb.toString();
    }
}
