package com.polinakulyk.cashregister2.service;

import com.polinakulyk.cashregister2.db.Transactional;
import com.polinakulyk.cashregister2.db.dto.ReceiptStatus;
import com.polinakulyk.cashregister2.db.entity.Product;
import com.polinakulyk.cashregister2.db.entity.Receipt;
import com.polinakulyk.cashregister2.db.entity.ReceiptItem;
import com.polinakulyk.cashregister2.db.entity.User;
import com.polinakulyk.cashregister2.db.dao.ReceiptDao;
import com.polinakulyk.cashregister2.exception.CashRegisterException;
import com.polinakulyk.cashregister2.exception.CashRegisterEntityNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.polinakulyk.cashregister2.db.DbHelper.calcCostByPriceAndAmount;
import static com.polinakulyk.cashregister2.db.dto.ReceiptStatus.*;
import static com.polinakulyk.cashregister2.db.dto.ShiftStatus.*;
import static com.polinakulyk.cashregister2.service.ServiceHelper.isReceiptInActiveShift;
import static com.polinakulyk.cashregister2.util.Util.ZERO_MONEY;
import static com.polinakulyk.cashregister2.util.Util.add;
import static com.polinakulyk.cashregister2.util.Util.generateUuid;
import static com.polinakulyk.cashregister2.util.Util.now;
import static com.polinakulyk.cashregister2.util.Util.quote;
import static com.polinakulyk.cashregister2.util.Util.subtract;

public class ReceiptService {

    private static final Logger log = LoggerFactory.getLogger(ReceiptService.class);

    private final ReceiptDao receiptDao = new ReceiptDao();
    private final ProductService productService = new ProductService();
    private final UserService userService = new UserService();

    public List<Receipt> findWithPagination(int page, int rowsPerPage) {
        var receipts = receiptDao.findAllWithPagination(
                rowsPerPage, (page - 1) * rowsPerPage);

        log.debug("DONE Find receipts with pagination: {}", receipts.size());
        return receipts;
    }

    public int count() {
        var receiptsTotal = receiptDao.count();

        log.debug("DONE Count receipts: {}", receiptsTotal);
        return receiptsTotal;
    }

    public List<Receipt> findByTellerWithPagination(String tellerId, int page, int rowsPerPage) {

        // filter teller's receipts that belong to the active shift, with pagination
        var receipts = receiptDao.findByTellerWithPagination(
                tellerId, rowsPerPage, (page - 1) * rowsPerPage);

        log.debug("DONE Find receipts by teller with pagination: '{}', size: {}",
                tellerId, receipts.size());
        return receipts;
    }

    public int countByTeller(String tellerId) {
        var receiptsTotal = receiptDao.countByTeller(tellerId);

        log.debug("DONE Count receipts by teller: '{}', size: {}", tellerId, receiptsTotal);
        return receiptsTotal;
    }

    /**
     * Find the existing receipt by id, otherwise throw
     * {@link CashRegisterEntityNotFoundException}.
     * <p>
     * Used as a way to retrieve the receipt entity that must be present. Otherwise the specific
     * exception is thrown, that will result in HTTP 404.
     *
     * @param receiptId
     * @return
     */
    public Receipt findExistingById(String receiptId) {
        try (Transactional t = Transactional.beginOrContinueTransaction()) {
            var receipt = receiptDao.findById(receiptId).orElseThrow(() ->
                    new CashRegisterEntityNotFoundException(receiptId));

            t.commitIfNeeded();
            log.debug("DONE Find existing receipt: '{}'", receiptId);

            return receipt;
        }
    }

    public Receipt createReceipt(String tellerId) {
        try (Transactional t = Transactional.beginOrContinueTransaction()) {
            log.debug("BEGIN Create receipt by user: '{}'", tellerId);
            User user = userService.findExistingById(tellerId);

            validateIsUserShiftActive(user);

            var receipt = receiptDao.insert(tellerId, new Receipt()
                    .setStatus(CREATED)
                    .setCreatedTime(now())
                    .setSumTotal(ZERO_MONEY)
                    .setUser(user)
            );

            t.commitIfNeeded();
            log.info("DONE Create receipt by user: '{}', receipt: '{}'", tellerId, receipt.getId());

            return receipt;
        }
    }

    public Receipt addReceiptItem(
            String userId,
            String receiptId,
            String receiptItemProductId,
            BigDecimal receiptItemAmount
    ) {
        try (Transactional t = Transactional.beginOrContinueTransaction()) {

            log.debug("BEGIN Add receipt item by user: '{}', in receipt: '{}', for product: '{}'",
                    userId, receiptId, receiptItemProductId);

            Receipt receipt = findExistingById(receiptId);

            validateShiftStatus(receipt);
            validateIsReceiptItemsModificationAllowed(receipt);

            Product product = productService.findExistingById(receiptItemProductId);

            // find an already existing receipt item with the same product,
            // to prevent adding duplicate receipt items
            Optional<ReceiptItem> existingReceiptItemOpt = receipt.getReceiptItems().stream()
                    .filter(ri -> product.getId().equals(ri.getProduct().getId()))
                    .findFirst();

            ReceiptItem receiptItem;
            if (existingReceiptItemOpt.isPresent()) {

                // update existing receipt item
                receiptItem = existingReceiptItemOpt.get();
                receiptItem.setAmount(add(receiptItem.getAmount(), receiptItemAmount));
            } else {

                // create receipt item by copying some fields from product, then add to receipt
                receiptItem = new ReceiptItem()
                        .setId(generateUuid())
                        .setReceipt(receipt)
                        .setProduct(product)
                        .setName(product.getName())
                        .setAmount(receiptItemAmount)
                        .setAmountUnit(product.getAmountUnit())
                        .setPrice(product.getPrice());
                receipt.getReceiptItems().add(receiptItem);
            }

            // validate product amount at the stage after receipt item amount finalized
            validateProductAmountNotExceeded(receiptItem);

            // calculate and increase receipt price total
            BigDecimal sumTotalIncrease = calcCostByPriceAndAmount(
                    receiptItem.getPrice(), receiptItemAmount);
            receipt.setSumTotal(add(receipt.getSumTotal(), sumTotalIncrease));

            // update receipt and associated receipt item
            receipt = receiptDao.update(receipt.getUser().getId(), receipt);
            if (existingReceiptItemOpt.isEmpty()) {
                receiptDao.insertReceiptItem(receiptId, receiptItemProductId, receiptItem);
            } else {
                receiptDao.updateReceiptItem(receiptId, receiptItemProductId, receiptItem);
            }

            t.commitIfNeeded();
            log.info("DONE Add receipt item by user: '{}', in receipt: '{}', receipt item: '{}'",
                    userId, receiptId, receiptItem.getId());

            return receipt;
        }
    }

    public Receipt completeReceipt(String userId, String receiptId) {
        try (Transactional t = Transactional.beginOrContinueTransaction()) {
            log.debug("BEGIN Complete receipt by user: '{}', receipt: '{}'", userId, receiptId);

            Receipt receipt = findExistingById(receiptId);

            validateShiftStatus(receipt);
            validateReceiptStatusTransitionToCompleted(receipt);

            // decrease amount available for products in receipt items
            var productsToUpdateAmountAvailable = new ArrayList<Product>();
            for (ReceiptItem receiptItem : receipt.getReceiptItems()) {

                validateProductAmountNotExceeded(receiptItem);

                // decrease amount available for product
                Product receiptItemProduct = receiptItem.getProduct();
                receiptItemProduct.setAmountAvailable(
                        subtract(receiptItemProduct.getAmountAvailable(), receiptItem.getAmount()));
                productsToUpdateAmountAvailable.add(receiptItemProduct);
            }

            // set status
            receipt.setStatus(COMPLETED);
            receipt.setCheckoutTime(now());

            for (Product product : productsToUpdateAmountAvailable) {
                productService.update(product);
            }
            receipt = receiptDao.update(userId, receipt);

            t.commitIfNeeded();
            log.info("DONE Complete receipt by user: '{}', receipt: '{}'", userId, receiptId);

            return receipt;
        }
    }

    public Receipt cancelReceipt(String userId, String receiptId) {
        try (Transactional t = Transactional.beginOrContinueTransaction()) {
            log.debug("BEGIN Cancel receipt by user: '{}', receipt: '{}'", userId, receiptId);

            Receipt receipt = findExistingById(receiptId);

            validateShiftStatus(receipt);
            validateReceiptStatusTransitionToCanceled(receipt);

            var productsToUpdateAmountAvailable = new ArrayList<Product>();
            if (COMPLETED == receipt.getStatus()) {

                // increase amount available for products in receipt items
                for (ReceiptItem receiptItem : receipt.getReceiptItems()) {

                    // increase amount available for product
                    Product receiptItemProduct = receiptItem.getProduct();
                    receiptItemProduct.setAmountAvailable(
                            add(receiptItemProduct.getAmountAvailable(), receiptItem.getAmount()));
                    productsToUpdateAmountAvailable.add(receiptItemProduct);
                }
            }

            // set status
            receipt.setStatus(CANCELED);
            receipt.setCheckoutTime(now());

            for (Product product : productsToUpdateAmountAvailable) {
                productService.update(product);
            }
            receipt = receiptDao.update(userId, receipt);

            t.commitIfNeeded();
            log.info("DONE Cancel receipt by user: '{}', receipt: '{}'", userId, receiptId);

            return receipt;
        }
    }

    private static void validateShiftStatus(Receipt receipt) {

        // user shift must be active, receipt must belong to an active shift
        validateIsUserShiftActive(receipt.getUser());
        validateIsReceiptInActiveShift(receipt);
    }

    private static void validateIsUserShiftActive(User user) {
        if (ACTIVE != user.getCashbox().getShiftStatus()) {
            throw new CashRegisterException("User shift status must be active");
        }
    }

    private static void validateIsReceiptInActiveShift(Receipt receipt) {
        if (!isReceiptInActiveShift(receipt)) {
            throw new CashRegisterException("Receipt must belong to an active shift");
        }
    }

    private static void validateIsReceiptItemsModificationAllowed(Receipt receipt) {
        if (CREATED != receipt.getStatus()) {
            throw new CashRegisterException(quote(
                    "Receipt status does not allow modification of receipt items",
                    receipt.getStatus()));
        }
    }

    private static void validateProductAmountNotExceeded(ReceiptItem receiptItem) {
        BigDecimal receiptItemAmount = receiptItem.getAmount();
        BigDecimal productAmountAvailable = receiptItem.getProduct().getAmountAvailable();

        // validate that receipt item amount does not exceed the product amount available
        if (receiptItemAmount.compareTo(productAmountAvailable) > 0) {
            throw new CashRegisterException(quote(
                    "Receipt item amount exceeds product amount available",
                    receiptItemAmount,
                    productAmountAvailable));
        }
    }

    private static void validateReceiptStatusTransitionToCanceled(Receipt receipt) {
        ReceiptStatus fromStatus = receipt.getStatus();
        if (CANCELED == fromStatus) {
            throwOnIllegalReceiptStatusTransition(fromStatus, CANCELED);
        }
    }

    private static void validateReceiptStatusTransitionToCompleted(Receipt receipt) {
        ReceiptStatus fromStatus = receipt.getStatus();
        if (CREATED != fromStatus) {
            throwOnIllegalReceiptStatusTransition(fromStatus, CREATED);
        }
        if (receipt.getReceiptItems().isEmpty()) {
            throw new CashRegisterException("Receipt without items cannot be completed");
        }
    }

    private static void throwOnIllegalReceiptStatusTransition(
            ReceiptStatus from, ReceiptStatus to) {
        throw new CashRegisterException(quote(
                "Illegal receipt status transition", from, to));
    }
}
