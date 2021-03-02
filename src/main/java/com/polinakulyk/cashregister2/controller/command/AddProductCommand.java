package com.polinakulyk.cashregister2.controller.command;

import com.polinakulyk.cashregister2.controller.dto.RouteString;
import com.polinakulyk.cashregister2.security.AuthHelper;
import com.polinakulyk.cashregister2.service.ProductService;

import java.io.IOException;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.polinakulyk.cashregister2.controller.dto.HttpRoute.PRODUCTS_LIST;
import static com.polinakulyk.cashregister2.controller.validator.ProductValidator.validProductFromRequestWithoutId;
import static com.polinakulyk.cashregister2.security.AuthHelper.*;

public class AddProductCommand implements Command {

    private final ProductService productService = new ProductService();

    @Override
    public Optional<RouteString> execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String userId = getUserIdFromSession(request);
        // validate product fields
        productService.create(userId, validProductFromRequestWithoutId(request));

        // redirect to product list
        return Optional.of(RouteString.of(PRODUCTS_LIST));
    }
}
