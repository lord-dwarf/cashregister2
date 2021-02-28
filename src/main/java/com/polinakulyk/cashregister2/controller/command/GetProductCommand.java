package com.polinakulyk.cashregister2.controller.command;

import com.polinakulyk.cashregister2.controller.api.RouteString;
import com.polinakulyk.cashregister2.service.ProductService;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.polinakulyk.cashregister2.controller.api.HttpRoute.ERROR_NOTFOUND;

public class GetProductCommand implements Command {

    private final ProductService productService = new ProductService();

    @Override
    public Optional<RouteString> execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String productId = request.getParameter("id");
        var product = productService.findExistingById(productId);
        request.setAttribute("product", product);
        return Optional.empty();
    }
}
