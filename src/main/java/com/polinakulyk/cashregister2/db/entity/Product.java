package com.polinakulyk.cashregister2.db.entity;

import com.polinakulyk.cashregister2.db.dto.ProductAmountUnit;

import java.math.BigDecimal;
import java.util.StringJoiner;

public class Product {
    private String id;
    private String code;
    private String category;
    private String name;
    private String details;
    private BigDecimal price;
    private ProductAmountUnit amountUnit;
    private BigDecimal amountAvailable;

    public String getId() {
        return id;
    }

    public Product setId(String id) {
        this.id = id;
        return this;
    }

    public String getCode() {
        return code;
    }

    public Product setCode(String code) {
        this.code = code;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public Product setCategory(String category) {
        this.category = category;
        return this;
    }

    public String getName() {
        return name;
    }

    public Product setName(String name) {
        this.name = name;
        return this;
    }

    public String getDetails() {
        return details;
    }

    public Product setDetails(String details) {
        this.details = details;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Product setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public ProductAmountUnit getAmountUnit() {
        return amountUnit;
    }

    public Product setAmountUnit(ProductAmountUnit unit) {
        this.amountUnit = unit;
        return this;
    }

    public BigDecimal getAmountAvailable() {
        return amountAvailable;
    }

    public Product setAmountAvailable(BigDecimal amountAvailable) {
        this.amountAvailable = amountAvailable;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        return id.equals(product.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return new StringJoiner(
                ", ", Product.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("code='" + code + "'")
                .add("category='" + category + "'")
                .add("name='" + name + "'")
                .add("details='" + details + "'")
                .add("price=" + price)
                .add("amountUnit=" + amountUnit)
                .add("amountAvailable=" + amountAvailable)
                .toString();
    }
}
