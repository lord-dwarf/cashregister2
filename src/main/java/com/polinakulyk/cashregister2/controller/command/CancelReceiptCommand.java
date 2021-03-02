package com.polinakulyk.cashregister2.controller.command;

import com.polinakulyk.cashregister2.controller.dto.RouteString;
import com.polinakulyk.cashregister2.security.AuthHelper;
import com.polinakulyk.cashregister2.service.ReceiptService;

import java.io.IOException;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.polinakulyk.cashregister2.controller.dto.HttpRoute.RECEIPTS_LIST;
import static com.polinakulyk.cashregister2.controller.validator.ValidatorHelper.validStringNotNull;

public class CancelReceiptCommand implements Command {

    private static final String RECEIPT_ID_PARAM = "receiptId";

    ReceiptService receiptService = new ReceiptService();
    AuthHelper authHelper = new AuthHelper();

    @Override
    public Optional<RouteString> execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        var userId = authHelper.getUserIdFromSession(request);
        // validate receipt id
        var receiptId = validStringNotNull(request, RECEIPT_ID_PARAM);
        // cancel receipt
        receiptService.cancelReceipt(userId, receiptId);

        // redirect to the list of receipts
        return Optional.of(RouteString.of(RECEIPTS_LIST));
    }
}
