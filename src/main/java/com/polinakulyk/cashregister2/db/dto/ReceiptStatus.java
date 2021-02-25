package com.polinakulyk.cashregister2.db.dto;

import com.polinakulyk.cashregister2.exception.CashRegisterException;
import java.util.Optional;

import static com.polinakulyk.cashregister2.util.CashRegisterUtil.quote;

public enum ReceiptStatus {
    CREATED,
    COMPLETED,
    CANCELED;


    /**
     * Finds the enum value by a given integer.
     *
     * @param receiptStatusInt
     * @return
     */
    public static Optional<ReceiptStatus> fromInteger(Integer receiptStatusInt) {
        for (ReceiptStatus receiptStatus : ReceiptStatus.values()) {
            if (receiptStatus.ordinal() == receiptStatusInt) {
                return Optional.of(receiptStatus);
            }
        }
        return Optional.empty();
    }

    /**
     * Finds the existing enum value by a given integer, otherwise throws.
     *
     * @param receiptStatusInt
     * @return
     */
    public static ReceiptStatus fromExistingInteger(Integer receiptStatusInt) {
        return fromInteger(receiptStatusInt).orElseThrow(() ->
                new CashRegisterException(quote("Shift status not found", receiptStatusInt)));
    }
}
