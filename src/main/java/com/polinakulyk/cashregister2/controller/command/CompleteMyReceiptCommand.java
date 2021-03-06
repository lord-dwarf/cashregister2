package com.polinakulyk.cashregister2.controller.command;

import com.polinakulyk.cashregister2.controller.dto.RouteString;
import com.polinakulyk.cashregister2.service.ReceiptService;

import java.io.IOException;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.polinakulyk.cashregister2.controller.dto.HttpRoute.MYRECEIPTS_LIST;
import static com.polinakulyk.cashregister2.controller.validator.ValidatorHelper.validStringNotNull;
import static com.polinakulyk.cashregister2.security.AuthHelper.getUserIdFromSession;

public class CompleteMyReceiptCommand implements Command {

    private static final String RECEIPT_ID_PARAM = "receiptId";

    ReceiptService receiptService = new ReceiptService();

    @Override
    public Optional<RouteString> execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        var userId = getUserIdFromSession(request);
        // validate receipt id
        var receiptId = validStringNotNull(request, RECEIPT_ID_PARAM);
        // complete receipt
        receiptService.completeReceipt(userId, receiptId);

        // redirect to the user's list of receipts
        return Optional.of(RouteString.of(MYRECEIPTS_LIST));
    }
}
