package com.polinakulyk.cashregister2.controller.command;

import com.polinakulyk.cashregister2.controller.api.Command;
import com.polinakulyk.cashregister2.security.AuthHelper;
import com.polinakulyk.cashregister2.service.ReceiptService;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.polinakulyk.cashregister2.controller.api.HttpRoute.MYRECEIPTS_LIST;
import static com.polinakulyk.cashregister2.controller.api.HttpRoute.toRouteString;

public class CompleteMyReceiptCommand implements Command {

    ReceiptService receiptService = new ReceiptService();
    AuthHelper authHelper = new AuthHelper();

    @Override
    public Optional<String> execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        var userId = authHelper.getUserFromSession(request).get().getId();
        var receiptId = request.getParameter("receiptId");

        receiptService.completeReceipt(userId, receiptId);

        return Optional.of(toRouteString(MYRECEIPTS_LIST));
    }
}
