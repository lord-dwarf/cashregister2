package com.polinakulyk.cashregister2.controller.command;

import com.polinakulyk.cashregister2.controller.api.RouteString;
import com.polinakulyk.cashregister2.service.ProductService;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

public class ListProductsCommand implements Command {

    private static final int ROWS_PER_PAGE = 10;

    private final ProductService productService = new ProductService();

    @Override
    public Optional<RouteString> execute(HttpServletRequest request, HttpServletResponse response) {
        var currentPage = Integer.parseInt(
                ofNullable(request.getParameter("currentPage")).orElse("1"));
        var products = productService.findWithPagination(currentPage, ROWS_PER_PAGE);
        var productsTotal = productService.count();
        var pagesTotal = productsTotal / ROWS_PER_PAGE;
        if (productsTotal % ROWS_PER_PAGE != 0) {
            ++pagesTotal;
        }
        request.setAttribute("products", products);
        request.setAttribute("pagesTotal", pagesTotal);
        return empty();
    }
}
