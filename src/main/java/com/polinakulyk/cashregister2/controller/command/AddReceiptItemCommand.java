package com.polinakulyk.cashregister2.controller.command;

import com.polinakulyk.cashregister2.controller.dto.RouteString;
import com.polinakulyk.cashregister2.security.AuthHelper;
import com.polinakulyk.cashregister2.service.ReceiptService;

import java.io.IOException;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.polinakulyk.cashregister2.controller.dto.HttpRoute.MYRECEIPTS_EDIT;
import static com.polinakulyk.cashregister2.controller.validator.ProductValidator.validProductAmountUnitNotNull;
import static com.polinakulyk.cashregister2.controller.validator.ValidatorHelper.validBigDecimalAmountNotNull;
import static com.polinakulyk.cashregister2.controller.validator.ValidatorHelper.validBigDecimalGreaterThanZero;
import static com.polinakulyk.cashregister2.controller.validator.ValidatorHelper.validStringNotNull;

public class AddReceiptItemCommand implements Command {

    private static String RECEIPT_ID_PARAM = "receiptId";
    private static String PRODUCT_ID_PARAM = "productId";
    private static String PRODUCT_AMOUNT_UNIT_PARAM = "productAmountUnit";
    private static String PRODUCT_AMOUNT_PARAM = "productAmount";
    private static String RECEIPT_ID_QUERY_STRING = "receiptId";

    ReceiptService receiptService = new ReceiptService();
    AuthHelper authHelper = new AuthHelper();

    @Override
    public Optional<RouteString> execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        var userId = authHelper.getUserIdFromSession(request);
        // validate receipt id
        var receiptId = validStringNotNull(request, RECEIPT_ID_PARAM);
        // validate product id
        var productId = validStringNotNull(request, PRODUCT_ID_PARAM);
        // validate product amount unit
        var amountUnit = validProductAmountUnitNotNull(request, PRODUCT_AMOUNT_UNIT_PARAM);
        // validate product amount
        var amount = validBigDecimalAmountNotNull(request, PRODUCT_AMOUNT_PARAM, amountUnit);
        // validate amount > 0
        validBigDecimalGreaterThanZero(amount, PRODUCT_AMOUNT_PARAM);

        receiptService.addReceiptItem(userId, receiptId, productId, amount);

        // redirect to the receipt to which current receipt item has been added
        return Optional.of(RouteString.of(
                MYRECEIPTS_EDIT,
                "?" + RECEIPT_ID_QUERY_STRING + "=" + receiptId
        ));
    }
}
