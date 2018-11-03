package com.example.dynamic.model.business;

import com.example.dynamic.model.BaseEntity;

import javax.persistence.Table;
import java.math.BigDecimal;

@Table(name = "t_order")
public class Order extends BaseEntity {

    private String product;

    private BigDecimal price;

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
                .append(getId());
        sb.append(",\"product\":\"")
                .append(product).append('\"');
        sb.append(",\"price\":")
                .append(price);
        sb.append('}');
        return sb.toString();
    }
}
