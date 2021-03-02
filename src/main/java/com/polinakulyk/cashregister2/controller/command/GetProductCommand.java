package com.polinakulyk.cashregister2.controller.command;

import com.polinakulyk.cashregister2.controller.dto.RouteString;
import com.polinakulyk.cashregister2.service.ProductService;

import java.io.IOException;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.polinakulyk.cashregister2.controller.validator.ValidatorHelper.validStringNotNull;

public class GetProductCommand implements Command {

    private static final String PRODUCT_ID_PARAM = "id";
    private static final String PRODUCT_ATTR = "product";

    private final ProductService productService = new ProductService();

    @Override
    public Optional<RouteString> execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // validate product id
        String productId = validStringNotNull(request, PRODUCT_ID_PARAM);
        var product = productService.findExistingById(productId);
        request.setAttribute(PRODUCT_ATTR, product);

        // no redirect
        return Optional.empty();
    }
}
