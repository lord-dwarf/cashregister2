package com.polinakulyk.cashregister2.controller.command;

import com.polinakulyk.cashregister2.controller.dto.RouteString;
import com.polinakulyk.cashregister2.service.ProductService;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.polinakulyk.cashregister2.controller.dto.HttpRoute.PRODUCTS_LIST;
import static com.polinakulyk.cashregister2.controller.validator.ProductValidator.validProductFromRequest;

public class UpdateProductCommand implements Command {

    private final ProductService productService = new ProductService();

    @Override
    public Optional<RouteString> execute(HttpServletRequest request, HttpServletResponse response) {
        // validate product fields
        var product = validProductFromRequest(request);
        productService.update(product);

        // redirect to list of products
        return Optional.of(RouteString.of(PRODUCTS_LIST));
    }
}
