package com.polinakulyk.cashregister2.controller.command;

import com.polinakulyk.cashregister2.controller.api.Command;
import com.polinakulyk.cashregister2.controller.api.HttpRoute;
import com.polinakulyk.cashregister2.service.ProductService;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetProductCommand implements Command {

    private final ProductService productService = new ProductService();

    @Override
    public Optional<HttpRoute> execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String productId = request.getParameter("id");
        var product = productService.findById(productId);
        if (product.isEmpty()) {
            return Optional.of(HttpRoute.ERROR_NOTFOUND);
        }
        request.setAttribute("product", product.get());
        return Optional.empty();
    }
}
