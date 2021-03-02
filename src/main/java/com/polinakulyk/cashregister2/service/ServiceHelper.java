package com.polinakulyk.cashregister2.service;

import com.polinakulyk.cashregister2.db.entity.Cashbox;
import com.polinakulyk.cashregister2.db.entity.Receipt;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static com.polinakulyk.cashregister2.db.dto.ShiftStatus.ACTIVE;
import static com.polinakulyk.cashregister2.util.Util.now;

public final class ServiceHelper {

    private ServiceHelper() {
    }

    /**
     * Determines whether the given receipt belongs to a currently active shift of a
     * receipt user's cash box.
     *
     * @param receipt
     * @return
     */
    public static boolean isReceiptInActiveShift(Receipt receipt) {
        Cashbox cashbox = receipt.getUser().getCashbox();
        LocalDateTime receiptCreatedTime = receipt.getCreatedTime();
        LocalDateTime shiftStartTime = cashbox.getShiftStatusTime();

        return ACTIVE == cashbox.getShiftStatus() && (
                receiptCreatedTime.isAfter(shiftStartTime)
                        || receiptCreatedTime.isEqual(shiftStartTime));
    }

    /**
     * Provides relatively unique id for X and Z reports based on report created time.
     * <p>
     * The result is the number of minutes passed since 2021-01-01 00:00:00,
     * up to report created time. The result is a positive ~6-7 digits string.
     * Newly generated ids are guaranteed to be no less than previously generated ones.
     *
     * @param reportCreatedTime
     * @return
     */
    public static String calcXZReportId(LocalDateTime reportCreatedTime) {
        LocalDateTime beginOf2021 = now().withYear(2021).withMonth(1).truncatedTo(ChronoUnit.DAYS);
        return "" + ChronoUnit.MINUTES.between(beginOf2021, reportCreatedTime);
    }
}
