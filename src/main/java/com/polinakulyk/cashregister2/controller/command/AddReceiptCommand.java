package com.polinakulyk.cashregister2.controller.command;

import com.polinakulyk.cashregister2.controller.dto.RouteString;
import com.polinakulyk.cashregister2.security.AuthHelper;
import com.polinakulyk.cashregister2.service.ReceiptService;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddReceiptCommand implements Command {

    private final ReceiptService receiptService = new ReceiptService();
    private final AuthHelper authHelper = new AuthHelper();

    @Override
    public Optional<RouteString> execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String userId = authHelper.getUserIdFromSession(request);
        var receipt = receiptService.createReceipt(userId);
        request.setAttribute("receipt", receipt);
        return Optional.empty();
    }
}
