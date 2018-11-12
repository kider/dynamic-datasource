package com.example.dynamic.model.business;

import java.math.BigDecimal;

public class Order {

    private Long id;

    private String product;

    private BigDecimal price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
