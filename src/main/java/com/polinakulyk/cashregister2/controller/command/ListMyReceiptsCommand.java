package com.polinakulyk.cashregister2.controller.command;

import com.polinakulyk.cashregister2.controller.api.Command;
import com.polinakulyk.cashregister2.controller.api.HttpRoute;
import com.polinakulyk.cashregister2.security.AuthHelper;
import com.polinakulyk.cashregister2.service.ProductService;
import com.polinakulyk.cashregister2.service.ReceiptService;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ListMyReceiptsCommand implements Command {

    private final ReceiptService receiptService = new ReceiptService();
    private final AuthHelper authHelper = new AuthHelper();

    @Override
    public Optional<String> execute(HttpServletRequest request, HttpServletResponse response) {
        var userId = authHelper.getUserFromSession(request).get().getId();
        var receipts = receiptService.findAllByTellerId(userId);
        request.setAttribute("receipts", receipts);
        return Optional.empty();
    }
}
