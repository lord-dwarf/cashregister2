package com.polinakulyk.cashregister2.db.entity;

import com.polinakulyk.cashregister2.db.dto.ShiftStatus;

import java.time.LocalDateTime;
import java.util.StringJoiner;

/**
 * Main purpose is to hold information about a shift (status and status change time).
 */
public class Cashbox {
    private String id;
    private String name;
    private ShiftStatus shiftStatus;
    private LocalDateTime shiftStatusTime;

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
                .toString();
    }
}
