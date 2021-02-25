package com.polinakulyk.cashregister2.db.entity;

import com.polinakulyk.cashregister2.db.DbHelper;
import com.polinakulyk.cashregister2.db.dto.ProductAmountUnit;
import java.math.BigDecimal;
import java.util.StringJoiner;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.polinakulyk.cashregister2.db.DbHelper.calcCostByPriceAndAmount;

public class ReceiptItem {

    private String id;

    @NotNull(message = "Receipt cannot be null")
    private Receipt receipt;

    @NotNull(message = "Product cannot be null")
    private Product product;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @DecimalMin(value = "0.001", message = "Amount must be greater than 0")
    @DecimalMax(value = "999.999", message = "Amount must be less than 1000")
    private BigDecimal amount;

    @NotNull(message = "Amount unit cannot be null")
    private ProductAmountUnit amountUnit;

    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @DecimalMax(value = "99999.99", message = "Price must be less than 100k")
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
