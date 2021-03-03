package com.polinakulyk.cashregister2.db.entity;

import com.polinakulyk.cashregister2.security.dto.UserRole;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class User {
    private String id;
    private String username;
    private String password;
    private UserRole role;
    private String fullName;
    private List<Receipt> receipts = new ArrayList<>();
    private Cashbox cashbox;

    public String getId() {
        return id;
    }

    public User setId(String id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public User setUsername(String login) {
        this.username = login;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public UserRole getRole() {
        return role;
    }

    public User setRole(UserRole role) {
        this.role = role;
        return this;
    }

    public String getFullName() {
        return fullName;
    }

    public User setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public List<Receipt> getReceipts() {
        return receipts;
    }

    public User setReceipts(List<Receipt> receipts) {
        this.receipts = receipts;
        return this;
    }

    public Cashbox getCashbox() {
        return cashbox;
    }

    public User setCashbox(Cashbox cashbox) {
        this.cashbox = cashbox;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", User.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("username='" + username + "'")
                .add("password='" + password + "'")
                .add("role='" + role + "'")
                .add("fullName='" + fullName + "'")
                .add("receipts=" + receipts)
                .add("cashbox.id=" + (cashbox != null ? cashbox.getId() : null))
                .toString();
    }
}
