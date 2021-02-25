package com.polinakulyk.cashregister2.db.dto;

import com.polinakulyk.cashregister2.exception.CashRegisterException;
import com.polinakulyk.cashregister2.security.dto.UserRole;
import java.util.Optional;

import static com.polinakulyk.cashregister2.util.CashRegisterUtil.quote;

public enum ShiftStatus {
    ACTIVE,
    INACTIVE;

    /**
     * Finds the existing enum ShiftStatus value by a given string.
     *
     * @param shiftStatusStr
     * @return
     */
    public static Optional<ShiftStatus> fromString(String shiftStatusStr) {
        for (ShiftStatus shiftStatus : ShiftStatus.values()) {
            if (shiftStatus.name().equals(shiftStatusStr)) {
                return Optional.of(shiftStatus);
            }
        }
        return Optional.empty();
    }

    /**
     * Finds the enum value by a given integer.
     *
     * @param shiftStatusInt
     * @return
     */
    public static Optional<ShiftStatus> fromInteger(Integer shiftStatusInt) {
        for (ShiftStatus shiftStatus : ShiftStatus.values()) {
            if (shiftStatus.ordinal() == shiftStatusInt) {
                return Optional.of(shiftStatus);
            }
        }
        return Optional.empty();
    }

    /**
     * Finds the existing enum value by a given integer, otherwise throws.
     *
     * @param shiftStatusInt
     * @return
     */
    public static ShiftStatus fromExistingInteger(Integer shiftStatusInt) {
        return fromInteger(shiftStatusInt).orElseThrow(() ->
                new CashRegisterException(quote("Shift status not found", shiftStatusInt)));
    }


}
