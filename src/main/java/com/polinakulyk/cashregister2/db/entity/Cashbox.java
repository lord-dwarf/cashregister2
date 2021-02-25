package com.polinakulyk.cashregister2.db.entity;

import com.polinakulyk.cashregister2.db.dto.ShiftStatus;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class Cashbox implements Serializable {

    private String id;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotNull(message = "Shift status cannot be null")
    private ShiftStatus shiftStatus;

    @NotNull(message = "Shift status time cannot be null")
    private LocalDateTime shiftStatusTime;

    @NotNull(message = "Users cannot be null")
    private List<User> users = new ArrayList<>();

    public String getId() {
        return id;
    }

    public Cashbox setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Cashbox setName(String name) {
        this.name = name;
        return this;
    }

    public ShiftStatus getShiftStatus() {
        return shiftStatus;
    }

    public Cashbox setShiftStatus(ShiftStatus status) {
        this.shiftStatus = status;
        return this;
    }

    public LocalDateTime getShiftStatusTime() {
        return shiftStatusTime;
    }

    public Cashbox setShiftStatusTime(LocalDateTime shiftStatusTime) {
        this.shiftStatusTime = shiftStatusTime;
        return this;
    }

    public List<User> getUsers() {
        return users;
    }

    public Cashbox setUsers(List<User> users) {
        this.users = users;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cashbox cashbox = (Cashbox) o;

        return id.equals(cashbox.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return new StringJoiner(
                ", ", Cashbox.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("name='" + name + "'")
                .add("shiftStatus='" + shiftStatus + "'")
                .add("shiftStatusTime='" + shiftStatusTime + "'")
                .add("users=" + users)
                .toString();
    }
}
