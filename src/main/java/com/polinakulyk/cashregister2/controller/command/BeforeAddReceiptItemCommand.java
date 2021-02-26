package com.polinakulyk.cashregister2.controller.command;

import com.polinakulyk.cashregister2.controller.api.Command;
import com.polinakulyk.cashregister2.controller.api.HttpRoute;
import com.polinakulyk.cashregister2.security.AuthHelper;
import com.polinakulyk.cashregister2.service.ReceiptService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BeforeAddReceiptItemCommand implements Command {

    @Override
    public Optional<String> execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        request.setAttribute("products", new ArrayList());
        var receiptId = request.getParameter("receiptId");
        request.setAttribute("receiptId", receiptId);
        return Optional.empty();
    }
}
