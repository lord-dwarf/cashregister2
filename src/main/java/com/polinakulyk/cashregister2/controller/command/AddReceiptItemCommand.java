package com.polinakulyk.cashregister2.controller.command;

import com.polinakulyk.cashregister2.controller.dto.RouteString;
import com.polinakulyk.cashregister2.controller.validator.ProductValidator;
import com.polinakulyk.cashregister2.controller.validator.ValidatorHelper;
import com.polinakulyk.cashregister2.security.AuthHelper;
import com.polinakulyk.cashregister2.service.ReceiptService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.polinakulyk.cashregister2.controller.dto.HttpRoute.MYRECEIPTS_EDIT;
import static com.polinakulyk.cashregister2.controller.validator.ProductValidator.validProductAmountUnitNotNull;
import static com.polinakulyk.cashregister2.controller.validator.ValidatorHelper.validBigDecimalAmountNotNull;
import static com.polinakulyk.cashregister2.controller.validator.ValidatorHelper.validBigDecimalGreaterThanZero;
import static com.polinakulyk.cashregister2.controller.validator.ValidatorHelper.validBigDecimalLessThan;
import static com.polinakulyk.cashregister2.controller.validator.ValidatorHelper.validStringNotNull;
import static com.polinakulyk.cashregister2.security.AuthHelper.*;

public class AddReceiptItemCommand implements Command {

    private static String RECEIPT_ID_PARAM = "receiptId";
    private static String PRODUCT_ID_PARAM = "productId";
    private static String PRODUCT_AMOUNT_UNIT_PARAM = "productAmountUnit";
    private static String PRODUCT_AMOUNT_PARAM = "productAmount";
    private static String RECEIPT_ID_QUERY_STRING = "receiptId";
    private static BigDecimal RECEIPT_ITEM_AMOUNT_UPPER_BOUND = new BigDecimal(1_000);

    ReceiptService receiptService = new ReceiptService();

    @Override
    public Optional<RouteString> execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        var userId = getUserIdFromSession(request);
        // validate receipt id
        var receiptId = validStringNotNull(request, RECEIPT_ID_PARAM);
        // validate product id
        var productId = validStringNotNull(request, PRODUCT_ID_PARAM);
        // validate product amount unit
        var amountUnit = validProductAmountUnitNotNull(request, PRODUCT_AMOUNT_UNIT_PARAM);
        // validate product amount
        var amount = validBigDecimalAmountNotNull(request, PRODUCT_AMOUNT_PARAM, amountUnit);

        // validate receipt item amount within bounds
        validBigDecimalGreaterThanZero(amount, PRODUCT_AMOUNT_PARAM);
        validBigDecimalLessThan(amount, RECEIPT_ITEM_AMOUNT_UPPER_BOUND, PRODUCT_AMOUNT_PARAM);

        receiptService.addReceiptItem(userId, receiptId, productId, amount);

        // redirect to the receipt to which current receipt item has been added
        return Optional.of(RouteString.of(
                MYRECEIPTS_EDIT,
                "?" + RECEIPT_ID_QUERY_STRING + "=" + receiptId
        ));
    }
}
