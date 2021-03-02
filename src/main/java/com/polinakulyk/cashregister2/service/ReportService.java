package com.polinakulyk.cashregister2.service;

import com.polinakulyk.cashregister2.db.Transactional;
import com.polinakulyk.cashregister2.db.entity.Cashbox;
import com.polinakulyk.cashregister2.db.entity.User;
import com.polinakulyk.cashregister2.db.dao.ReceiptDao;
import com.polinakulyk.cashregister2.service.dto.ReportKind;
import com.polinakulyk.cashregister2.service.dto.XZReportResponseDto;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.polinakulyk.cashregister2.service.ServiceHelper.calcXZReportId;
import static com.polinakulyk.cashregister2.util.Util.now;

public class ReportService {

    private static final Logger log = LoggerFactory.getLogger(ReportService.class);

    private final UserService userService = new UserService();
    private final CashboxService cashboxService = new CashboxService();
    private final ReceiptDao receiptDao = new ReceiptDao();

    public XZReportResponseDto createXZReport(String userId, ReportKind reportKind) {
        try (Transactional t = Transactional.beginOrContinueTransaction()) {
            log.debug("BEGIN Create XZ report by user: '{}', of report kind: '{}'", userId, reportKind);

            User user = userService.findExistingById(userId);
            Cashbox cashbox = user.getCashbox();
            LocalDateTime shiftStartTime = cashbox.getShiftStatusTime();
            LocalDateTime reportCreatedTime = now();
            var receiptsStatDto =
                    receiptDao.getReceiptsStatInActiveShift(cashbox.getId());

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
                    .setNumReceiptsCompleted(receiptsStatDto.getCount())
                    .setSumTotal(receiptsStatDto.getSum());

            t.commitIfNeeded();
            log.info("DONE Create XZ report by user: '{}', of report kind: '{}', sum total: '{}'",
                    userId, reportKind, receiptsStatDto.getSum());
            return xzReportResponseDto;
        }
    }
}
