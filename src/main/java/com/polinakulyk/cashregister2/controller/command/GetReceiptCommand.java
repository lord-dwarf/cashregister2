package com.polinakulyk.cashregister2.controller.command;

import com.polinakulyk.cashregister2.controller.api.Command;
import com.polinakulyk.cashregister2.controller.api.HttpRoute;
import com.polinakulyk.cashregister2.service.ReceiptService;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetReceiptCommand implements Command {

    private final ReceiptService receiptService = new ReceiptService();

    @Override
    public Optional<HttpRoute> execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String receiptId = request.getParameter("id");
        var receipt = receiptService.findById(receiptId);
        if (receipt.isEmpty()) {
            return Optional.of(HttpRoute.ERROR_NOTFOUND);
        }
        request.setAttribute("receipt", receipt.get());
        return Optional.empty();
    }
}
