package com.polinakulyk.cashregister2.controller.command;

import com.polinakulyk.cashregister2.controller.api.Command;
import com.polinakulyk.cashregister2.db.dto.ProductAmountUnit;
import com.polinakulyk.cashregister2.db.entity.Product;
import com.polinakulyk.cashregister2.service.ProductService;
import com.polinakulyk.cashregister2.service.dto.ProductFilterKind;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.polinakulyk.cashregister2.controller.api.HttpRoute.MYRECEIPTS_EDIT;
import static com.polinakulyk.cashregister2.controller.api.HttpRoute.toRouteString;

public class SearchReceiptItemCommand implements Command {

    private final ProductService productService = new ProductService();

    @Override
    public Optional<String> execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        var productFilterKind = ProductFilterKind.fromString(
                request.getParameter("productFilterKind")).get();
        var filterValue = request.getParameter("filterValue");
        var products = productService.findByFilter(productFilterKind, filterValue);
        request.setAttribute("products", products);

        var receiptId = request.getParameter("receiptId");
        request.setAttribute("receiptId", receiptId);
        return Optional.empty();
    }
}
