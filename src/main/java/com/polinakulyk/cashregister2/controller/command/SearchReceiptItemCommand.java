package com.polinakulyk.cashregister2.controller.command;

import com.polinakulyk.cashregister2.controller.dto.RouteString;
import com.polinakulyk.cashregister2.service.ProductService;

import java.io.IOException;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.polinakulyk.cashregister2.controller.validator.ProductValidator.validProductFilterKindNotNull;
import static com.polinakulyk.cashregister2.controller.validator.ValidatorHelper.validStringNotNull;
import static com.polinakulyk.cashregister2.controller.validator.ValidatorHelper.validStringNullable;

public class SearchReceiptItemCommand implements Command {

    private static final String PRODUCT_FILTER_KIND_PARAM = "productFilterKind";
    private static final String PRODUCT_FILTER_VALUE_PARAM = "filterValue";
    private static final String PRODUCTS_ATTR = "products";
    private static final String RECEIPT_ID_PARAM = "receiptId";
    private static final String RECEIPT_ID_ATTR = "receiptId";

    private final ProductService productService = new ProductService();

    @Override
    public Optional<RouteString> execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // validate product filter kind
        var productFilterKind =
                validProductFilterKindNotNull(request, PRODUCT_FILTER_KIND_PARAM);
        // empty search fields -> show first group of products
        var filterValue =
                validStringNullable(request, PRODUCT_FILTER_VALUE_PARAM)
                .orElse("");

        var products = productService.findByFilter(productFilterKind, filterValue);
        request.setAttribute(PRODUCTS_ATTR, products);

        var receiptId = validStringNotNull(request, RECEIPT_ID_PARAM);
        request.setAttribute(RECEIPT_ID_ATTR, receiptId);

        // no redirect
        return Optional.empty();
    }
}
