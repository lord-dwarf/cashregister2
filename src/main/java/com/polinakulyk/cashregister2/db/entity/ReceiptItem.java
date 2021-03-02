package com.polinakulyk.cashregister2.db.entity;

import com.polinakulyk.cashregister2.db.dto.ProductAmountUnit;

import java.math.BigDecimal;
import java.util.StringJoiner;

import static com.polinakulyk.cashregister2.db.DbHelper.calcCostByPriceAndAmount;

public class ReceiptItem {
    private String id;
    private Receipt receipt;
    private Product product;
    private String name;
    private BigDecimal amount;
    private ProductAmountUnit amountUnit;
    private BigDecimal price;

    public String getId() {
        return id;
    }

    public ReceiptItem setId(String id) {
        this.id = id;
        return this;
    }

    public Receipt getReceipt() {
        return receipt;
    }

    public ReceiptItem setReceipt(Receipt receipt) {
        this.receipt = receipt;
        return this;
    }

    public Product getProduct() {
        return product;
    }

    public ReceiptItem setProduct(Product product) {
        this.product = product;
        return this;
    }

    public String getName() {
        return name;
    }

    public ReceiptItem setName(String productName) {
        this.name = productName;
        return this;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public ReceiptItem setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public ProductAmountUnit getAmountUnit() {
        return amountUnit;
    }

    public ReceiptItem setAmountUnit(ProductAmountUnit amountUnit) {
        this.amountUnit = amountUnit;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public ReceiptItem setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public BigDecimal getCost() {
        return price != null && amount != null ? calcCostByPriceAndAmount(price, amount) : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReceiptItem that = (ReceiptItem) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ReceiptItem.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("receipt.id='" + (receipt != null ? receipt.getId() : null) + "'")
                .add("product.id='" + (product != null ? product.getId() : null) + "'")
                .add("name='" + name + "'")
                .add("amount=" + amount)
                .add("amountUnit='" + amountUnit + "'")
                .add("price=" + price)
                .toString();
    }
}
