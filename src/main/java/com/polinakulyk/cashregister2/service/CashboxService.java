package com.polinakulyk.cashregister2.service;

import com.polinakulyk.cashregister2.db.Transactional;
import com.polinakulyk.cashregister2.db.dto.ShiftStatus;
import com.polinakulyk.cashregister2.db.entity.Cashbox;
import com.polinakulyk.cashregister2.db.entity.User;
import com.polinakulyk.cashregister2.db.dao.CashboxDao;
import com.polinakulyk.cashregister2.exception.CashRegisterException;
import com.polinakulyk.cashregister2.service.dto.ShiftStatusSummaryResponseDto;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.polinakulyk.cashregister2.db.dto.ShiftStatus.*;
import static com.polinakulyk.cashregister2.db.dto.ShiftStatus.ACTIVE;
import static com.polinakulyk.cashregister2.util.Util.*;
import static com.polinakulyk.cashregister2.util.Util.quote;
import static java.lang.String.format;

/**
 * Cashbox service.
 * <p>
 * The existence of {@link Cashbox} entity allows us to keep track of a current shift status.
 * Currently, application uses a single cash box, but can be extended to use multiple cash boxes.
 */
public class CashboxService {

    private static final Logger log = LoggerFactory.getLogger(CashboxService.class);

    private final CashboxDao cashboxDao = new CashboxDao();
    private final UserService userService = new UserService();

    /**
     * Update {@link Cashbox} shift status to {@link ShiftStatus#INACTIVE}.
     *
     * @param userId
     * @return
     */
    public ShiftStatusSummaryResponseDto deactivateShift(String userId) {
        log.debug("BEGIN Deactivate shift by user: '{}'", userId);
        try (Transactional t = Transactional.beginOrContinueTransaction()) {

            User user = userService.findExistingById(userId);

            Cashbox cashbox = user.getCashbox();
            validateShiftStatusTransitionToInactive(cashbox);

            var shiftStatusSummaryResponseDto =
                    updateShiftStatus(cashbox, INACTIVE);

            t.commitIfNeeded();
            log.info("DONE Deactivate shift by user: '{}', for cash box: '{}'",
                    userId, cashbox.getId());

            return shiftStatusSummaryResponseDto;
        }
    }

    private ShiftStatusSummaryResponseDto updateShiftStatus(
            Cashbox cashbox, ShiftStatus shiftStatus) {

        cashbox.setShiftStatus(shiftStatus);
        cashbox.setShiftStatusTime(now());
        cashbox = cashboxDao.update(cashbox);

        LocalDateTime shiftStatusTime = cashbox.getShiftStatusTime();
        return new ShiftStatusSummaryResponseDto()
                .setShiftStatus(cashbox.getShiftStatus())
                .setShiftStatusElapsedTime(calcElapsedTime(shiftStatusTime));
    }

    /*
     * Calculates elapsed time since the given time.
     */
    private String calcElapsedTime(LocalDateTime from) {
        long seconds = ChronoUnit.SECONDS.between(from, now());
        long days = seconds / (3600 * 24);
        seconds %= 3600 * 24;
        long hours = seconds / 3600;
        seconds %= 3600;
        long minutes = seconds / 60;
        seconds %= 60;
        return format(
                "%s%02d:%02d:%02d",
                days != 0 ? days + ":" : "",
                hours,
                minutes,
                seconds
        );
    }

    private static void validateShiftStatusTransitionToInactive(Cashbox cashbox) {
        ShiftStatus fromStatus = cashbox.getShiftStatus();
        if (ACTIVE != fromStatus) {
            throwOnIllegalShiftStatusTransition(fromStatus, INACTIVE);
        }
    }

    private static void throwOnIllegalShiftStatusTransition(
            ShiftStatus from, ShiftStatus to) {
        throw new CashRegisterException(quote(
                "Illegal shift status transition", from, to));
    }
}
