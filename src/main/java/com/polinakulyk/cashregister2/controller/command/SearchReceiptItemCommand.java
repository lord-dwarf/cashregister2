package com.polinakulyk.cashregister2.controller.command;

import com.polinakulyk.cashregister2.controller.dto.RouteString;
import com.polinakulyk.cashregister2.service.ProductService;
import com.polinakulyk.cashregister2.service.dto.ProductFilterKind;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SearchReceiptItemCommand implements Command {

    private final ProductService productService = new ProductService();

    @Override
    public Optional<RouteString> execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        var productFilterKind =
                ProductFilterKind.fromExistingString(request.getParameter("productFilterKind"));

        var filterValue = request.getParameter("filterValue");
        var products = productService.findByFilter(productFilterKind, filterValue);
        request.setAttribute("products", products);

        var receiptId = request.getParameter("receiptId");
        request.setAttribute("receiptId", receiptId);
        return Optional.empty();
    }
}
