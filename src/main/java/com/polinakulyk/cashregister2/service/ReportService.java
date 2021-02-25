package com.polinakulyk.cashregister2.service;

import com.polinakulyk.cashregister2.db.DbHelper;
import com.polinakulyk.cashregister2.db.dto.ReceiptStatus;
import com.polinakulyk.cashregister2.db.entity.Cashbox;
import com.polinakulyk.cashregister2.db.entity.Receipt;
import com.polinakulyk.cashregister2.db.entity.User;
import com.polinakulyk.cashregister2.exception.CashRegisterException;
import com.polinakulyk.cashregister2.security.AuthHelper;
import com.polinakulyk.cashregister2.service.dto.ReportKind;
import com.polinakulyk.cashregister2.service.dto.XZReportResponseDto;
import com.polinakulyk.cashregister2.util.CashRegisterUtil;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.polinakulyk.cashregister2.db.DbHelper.calcReceiptCode;
import static com.polinakulyk.cashregister2.db.dto.ReceiptStatus.CANCELED;
import static com.polinakulyk.cashregister2.db.dto.ReceiptStatus.COMPLETED;
import static com.polinakulyk.cashregister2.service.ServiceHelper.calcXZReportId;
import static com.polinakulyk.cashregister2.util.CashRegisterUtil.*;
import static java.util.stream.Collectors.toList;

public class ReportService {
    private static final Logger log = LoggerFactory.getLogger(ReportService.class);

    private final UserService userService = new UserService();
    private final CashboxService cashboxService = new CashboxService();
    private final ReceiptService receiptService = new ReceiptService();

    public XZReportResponseDto createXZReport(String userId, ReportKind reportKind) {
        log.debug("BEGIN Create XZ report by user: '{}', of report kind: '{}'", userId, reportKind);

        User user = userService.findExistingById(userId);
        Cashbox cashbox = user.getCashbox();
        LocalDateTime shiftStartTime = cashbox.getShiftStatusTime();
        LocalDateTime reportCreatedTime = now();
        List<Receipt> receiptsCompleted =
                receiptService.findAll()
                        .stream()
                        .filter(ServiceHelper::isReceiptInActiveShift)
                        .filter(ReportService::validateReceiptStatusForXZReport)
                        .filter(r -> COMPLETED == r.getStatus())
                        .collect(toList());

        BigDecimal sum = ZERO_MONEY;
        for (Receipt receipt : receiptsCompleted) {
            sum = add(sum, receipt.getSumTotal());
        }

        if (ReportKind.Z == reportKind) {
            cashboxService.deactivateShift(userId);
        }

        var xzReportResponseDto = new XZReportResponseDto()
                .setReportId(calcXZReportId(reportCreatedTime))
                .setReportKind(reportKind)
                .setCashboxName(cashbox.getName())
                .setShiftStartTime(shiftStartTime)
                .setCreatedTime(reportCreatedTime)
                .setCreatedBy(user.getFullName())
                .setNumReceiptsCompleted(receiptsCompleted.size())
                .setSumTotal(sum);

        log.info("DONE Create XZ report by user: '{}', of report kind: '{}', sum total: '{}'",
                userId, reportKind, sum);
        return xzReportResponseDto;
    }

    private static boolean validateReceiptStatusForXZReport(Receipt receipt) {
        if (!List.of(COMPLETED, CANCELED).contains(receipt.getStatus())) {
            throw new CashRegisterException(quote(
                    "Receipt must be either completed or canceled",
                    calcReceiptCode(receipt.getId())));
        }
        return true;
    }
}
