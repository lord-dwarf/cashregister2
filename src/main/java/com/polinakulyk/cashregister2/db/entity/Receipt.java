package com.polinakulyk.cashregister2.db.entity;

import com.polinakulyk.cashregister2.db.dto.ReceiptStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import static com.polinakulyk.cashregister2.db.DbHelper.calcReceiptCode;

/**
 * Receipt for purchasing products, consists of {@link ReceiptItem}-s and also holds
 * information about {@link ReceiptStatus}.
 */
public class Receipt {
    private String id;
    private LocalDateTime createdTime;
    private LocalDateTime checkoutTime;
    private ReceiptStatus status;
    private BigDecimal sumTotal;
    private List<ReceiptItem> receiptItems = new ArrayList<>();
    private User user;

    public String getId() {
        return id;
    }

    public Receipt setId(String id) {
        this.id = id;
        return this;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public Receipt setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
        return this;
    }

    public LocalDateTime getCheckoutTime() {
        return checkoutTime;
    }

    public Receipt setCheckoutTime(LocalDateTime checkoutTime) {
        this.checkoutTime = checkoutTime;
        return this;
    }

    public ReceiptStatus getStatus() {
        return status;
    }

    public Receipt setStatus(ReceiptStatus status) {
        this.status = status;
        return this;
    }

    public BigDecimal getSumTotal() {
        return sumTotal;
    }

    public Receipt setSumTotal(BigDecimal total) {
        this.sumTotal = total;
        return this;
    }

    public List<ReceiptItem> getReceiptItems() {
        return receiptItems;
    }

    public Receipt setReceiptItems(List<ReceiptItem> items) {
        this.receiptItems = items;
        return this;
    }

    public User getUser() {
        return user;
    }

    public Receipt setUser(User user) {
        this.user = user;
        return this;
    }

    public String getCode() {
        return this.id != null ? calcReceiptCode(this.id) : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Receipt receipt = (Receipt) o;

        return id.equals(receipt.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return new StringJoiner(
                ", ", Receipt.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("createdTime=" + createdTime)
                .add("checkoutTime=" + checkoutTime)
                .add("status='" + status + "'")
                .add("sumTotal=" + sumTotal)
                .add("receiptItems=" + receiptItems)
                .add("user.id=" + (user != null ? user.getId() : null))
                .toString();
    }
}
