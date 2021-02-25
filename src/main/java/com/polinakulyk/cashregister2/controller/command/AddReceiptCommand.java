package com.polinakulyk.cashregister2.controller.command;

import com.polinakulyk.cashregister2.controller.api.Command;
import com.polinakulyk.cashregister2.controller.api.HttpRoute;
import com.polinakulyk.cashregister2.db.entity.Product;
import com.polinakulyk.cashregister2.db.entity.Receipt;
import com.polinakulyk.cashregister2.security.AuthHelper;
import com.polinakulyk.cashregister2.service.ProductService;
import com.polinakulyk.cashregister2.service.ReceiptService;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.polinakulyk.cashregister2.controller.api.HttpRoute.PRODUCTS_LIST;
import static com.polinakulyk.cashregister2.db.dto.ProductAmountUnit.fromString;

public class AddReceiptCommand implements Command {

    private final ReceiptService receiptService = new ReceiptService();
    private final AuthHelper authHelper = new AuthHelper();

    @Override
    public Optional<HttpRoute> execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String userId = authHelper.getUserFromSession(request).get().getId();
        var receipt = receiptService.createReceipt(userId);
        request.setAttribute("receipt", receipt);
        return Optional.empty();
    }
}
