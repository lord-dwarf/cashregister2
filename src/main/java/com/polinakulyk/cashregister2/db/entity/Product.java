package com.polinakulyk.cashregister2.db.entity;

import com.polinakulyk.cashregister2.db.dto.ProductAmountUnit;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class Product {

    private String id;

    @NotBlank(message = "Code cannot be blank")
    private String code;

    @NotBlank(message = "Category cannot be blank")
    private String category;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    private String details;

    @NotNull(message = "Price cannot be null")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @DecimalMax(value = "99999.99", message = "Price must be less than 100k")
    private BigDecimal price;

    @NotNull(message = "Amount unit cannot be null")
    private ProductAmountUnit amountUnit;

    @NotNull(message = "Amount available cannot be null")
    @DecimalMin(value = "0.000", message = "Amount available must be positive")
    @DecimalMax(value = "999.999", message = "Amount available  must be less than 1000")
    private BigDecimal amountAvailable;

    @NotNull(message = "Receipt items cannot be null")
    private Set<ReceiptItem> receiptItems = new HashSet<>();

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

    public Set<ReceiptItem> getReceiptItems() {
        return receiptItems;
    }

    public Product setReceiptItems(Set<ReceiptItem> items) {
        this.receiptItems = items;
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
                .add("receiptItems=" + receiptItems)
                .toString();
    }
}
