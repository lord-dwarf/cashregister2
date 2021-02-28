package com.polinakulyk.cashregister2.controller.command;

import com.polinakulyk.cashregister2.controller.api.RouteString;
import com.polinakulyk.cashregister2.exception.CashRegisterEntityNotFoundException;
import com.polinakulyk.cashregister2.service.ReceiptService;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.polinakulyk.cashregister2.controller.api.HttpRoute.ERROR_NOTFOUND;

public class GetReceiptCommand implements Command {

    private final ReceiptService receiptService = new ReceiptService();

    @Override
    public Optional<RouteString> execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String receiptId = request.getParameter("receiptId");
        var receipt = receiptService.findExistingById(receiptId);
        request.setAttribute("receipt", receipt);
        return Optional.empty();
    }
}
