package com.polinakulyk.cashregister2.controller.command;

import com.polinakulyk.cashregister2.controller.api.Command;
import com.polinakulyk.cashregister2.controller.api.HttpRoute;
import com.polinakulyk.cashregister2.db.dto.ProductAmountUnit;
import com.polinakulyk.cashregister2.security.AuthHelper;
import com.polinakulyk.cashregister2.service.ReceiptService;
import com.polinakulyk.cashregister2.util.Util;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.polinakulyk.cashregister2.controller.api.HttpRoute.*;
import static com.polinakulyk.cashregister2.util.Util.bigDecimalAmount;

public class AddReceiptItemCommand implements Command {

    ReceiptService receiptService = new ReceiptService();
    AuthHelper authHelper = new AuthHelper();

    @Override
    public Optional<String> execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        var userId = authHelper.getUserFromSession(request).get().getId();
        var receiptId = request.getParameter("receiptId");
        var productId = request.getParameter("productId");
        var amountUnit =
                ProductAmountUnit.fromString(request.getParameter("productAmountUnit")).get();
        var amount =
                bigDecimalAmount(request.getParameter("productAmount"), amountUnit);

        receiptService.addReceiptItem(userId, receiptId, productId, amount);

        return Optional.of(toRouteString(MYRECEIPTS_EDIT) + "?receiptId=" + receiptId);
    }
}
