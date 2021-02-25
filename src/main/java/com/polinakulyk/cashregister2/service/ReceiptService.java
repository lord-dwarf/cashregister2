package com.polinakulyk.cashregister2.service;

import com.polinakulyk.cashregister2.db.dto.ReceiptStatus;
import com.polinakulyk.cashregister2.db.dto.ShiftStatus;
import com.polinakulyk.cashregister2.db.entity.Receipt;
import com.polinakulyk.cashregister2.db.entity.User;
import com.polinakulyk.cashregister2.db.repository.ReceiptRepository;
import com.polinakulyk.cashregister2.exception.CashRegisterException;
import com.polinakulyk.cashregister2.util.CashRegisterUtil;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.polinakulyk.cashregister2.db.dto.ReceiptStatus.*;
import static com.polinakulyk.cashregister2.db.dto.ShiftStatus.*;
import static com.polinakulyk.cashregister2.service.ServiceHelper.isReceiptInActiveShift;
import static com.polinakulyk.cashregister2.util.CashRegisterUtil.ZERO_MONEY;
import static com.polinakulyk.cashregister2.util.CashRegisterUtil.now;
import static java.util.stream.Collectors.toList;

public class ReceiptService {
    private static final Logger log = LoggerFactory.getLogger(ReceiptService.class);

    private final ReceiptRepository receiptRepository = new ReceiptRepository();
    private final UserService userService = new UserService();

    public List<Receipt> findAll() {
        var receipts = receiptRepository.findAll();

        log.debug("DONE Find receipts: {}", receipts.size());
        return receipts;
    }

    public List<Receipt> findAllByTellerId(String tellerId) {

        // filter teller's receipts that belong to the active shift
        var receipts = receiptRepository.findAll().stream()
                .filter(r -> tellerId.equals(r.getUser().getId()) && isReceiptInActiveShift(r))
                .collect(toList());

        log.debug("DONE Find receipts by teller: '{}', size: {}", tellerId, receipts.size());
        return receipts;
    }

    public Optional<Receipt> findById(String receiptId) {
        var receipt = receiptRepository.findById(receiptId);

        if (receipt.isEmpty()) {
            log.info("DONE Can't find receipt with id: {}", receiptId);
            return receipt;
        }

        log.debug("DONE Find receipt with id: {}", receiptId);
        return receipt;
    }

    public Receipt createReceipt(String tellerId) {
        log.debug("BEGIN Create receipt by user: '{}'", tellerId);
        User user = userService.findExistingById(tellerId);

        validateIsUserShiftActive(user);

        var receipt = receiptRepository.insert(tellerId, new Receipt()
                .setStatus(CREATED)
                .setCreatedTime(now())
                .setSumTotal(ZERO_MONEY)
                .setUser(user)
        );

        log.info("DONE Create receipt by user: '{}', receipt: '{}'", tellerId, receipt.getId());
        return receipt;
    }

    private static void validateIsUserShiftActive(User user) {
        if (ACTIVE != user.getCashbox().getShiftStatus()) {
            throw new CashRegisterException("User shift status must be active");
        }
    }
}
