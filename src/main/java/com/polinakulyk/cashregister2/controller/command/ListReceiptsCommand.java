package com.polinakulyk.cashregister2.controller.command;

import com.polinakulyk.cashregister2.controller.api.Command;
import com.polinakulyk.cashregister2.controller.api.HttpRoute;
import com.polinakulyk.cashregister2.service.ProductService;
import com.polinakulyk.cashregister2.service.ReceiptService;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ListReceiptsCommand implements Command {

    private final ReceiptService receiptService = new ReceiptService();

    @Override
    public Optional<HttpRoute> execute(HttpServletRequest request, HttpServletResponse response) {
        var receipts = receiptService.findAll();
        request.setAttribute("receipts", receipts);
        return Optional.empty();
    }
}
