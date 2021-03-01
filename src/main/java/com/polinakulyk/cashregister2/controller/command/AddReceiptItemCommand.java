package com.polinakulyk.cashregister2.controller.command;

import com.polinakulyk.cashregister2.controller.dto.RouteString;
import com.polinakulyk.cashregister2.db.dto.ProductAmountUnit;
import com.polinakulyk.cashregister2.security.AuthHelper;
import com.polinakulyk.cashregister2.service.ReceiptService;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.polinakulyk.cashregister2.controller.dto.HttpRoute.*;
import static com.polinakulyk.cashregister2.util.Util.bigDecimalAmount;

public class AddReceiptItemCommand implements Command {

    ReceiptService receiptService = new ReceiptService();
    AuthHelper authHelper = new AuthHelper();

    @Override
    public Optional<RouteString> execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        var userId = authHelper.getUserIdFromSession(request);
        var receiptId = request.getParameter("receiptId");
        var productId = request.getParameter("productId");
        var amountUnit =
                ProductAmountUnit.fromExistingString(request.getParameter("productAmountUnit"));
        var amount =
                bigDecimalAmount(request.getParameter("productAmount"), amountUnit);

        receiptService.addReceiptItem(userId, receiptId, productId, amount);

        return Optional.of(RouteString.of(MYRECEIPTS_EDIT, "?receiptId=" + receiptId));
    }
}
