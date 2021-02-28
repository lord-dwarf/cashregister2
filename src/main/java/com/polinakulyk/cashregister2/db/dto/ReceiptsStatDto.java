package com.polinakulyk.cashregister2.db.dto;

import java.math.BigDecimal;
import java.util.StringJoiner;

public class ReceiptsStatDto {

    private final BigDecimal sum;
    private final int count;

    public ReceiptsStatDto(BigDecimal sum, int count) {
        this.sum = sum;
        this.count = count;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public int getCount() {
        return count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReceiptsStatDto that = (ReceiptsStatDto) o;

        if (count != that.count) return false;
        return sum.equals(that.sum);
    }

    @Override
    public int hashCode() {
        int result = sum.hashCode();
        result = 31 * result + count;
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(
                ", ", ReceiptsStatDto.class.getSimpleName() + "[", "]")
                .add("sum=" + sum)
                .add("count=" + count)
                .toString();
    }
}
