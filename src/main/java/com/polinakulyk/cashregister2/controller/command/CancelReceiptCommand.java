package com.polinakulyk.cashregister2.controller.command;

import com.polinakulyk.cashregister2.controller.dto.RouteString;
import com.polinakulyk.cashregister2.security.AuthHelper;
import com.polinakulyk.cashregister2.service.ReceiptService;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.polinakulyk.cashregister2.controller.dto.HttpRoute.RECEIPTS_LIST;

public class CancelReceiptCommand implements Command {

    ReceiptService receiptService = new ReceiptService();
    AuthHelper authHelper = new AuthHelper();

    @Override
    public Optional<RouteString> execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        var userId = authHelper.getUserIdFromSession(request);
        var receiptId = request.getParameter("receiptId");

        receiptService.cancelReceipt(userId, receiptId);

        return Optional.of(RouteString.of(RECEIPTS_LIST));
    }
}
