package com.polinakulyk.cashregister2.controller.command;

import com.polinakulyk.cashregister2.controller.api.Command;
import com.polinakulyk.cashregister2.controller.api.HttpRoute;
import com.polinakulyk.cashregister2.db.entity.Product;
import com.polinakulyk.cashregister2.service.ProductService;
import java.math.BigDecimal;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.polinakulyk.cashregister2.controller.api.HttpRoute.PRODUCTS_LIST;
import static com.polinakulyk.cashregister2.db.dto.ProductAmountUnit.fromString;

public class UpdateProductCommand implements Command {

    private final ProductService productService = new ProductService();

    @Override
    public Optional<HttpRoute> execute(HttpServletRequest request, HttpServletResponse response) {
        var product = new Product()
                .setId(request.getParameter("id"))
                .setCode(request.getParameter("code"))
                .setName(request.getParameter("name"))
                .setCategory(request.getParameter("category"))
                .setPrice(new BigDecimal(request.getParameter("price")))
                .setAmountAvailable(new BigDecimal(request.getParameter("amountAvailable")))
                .setAmountUnit(fromString(request.getParameter("amountUnit")).get())
                .setDetails(request.getParameter("details"));

        productService.update(product);
        return Optional.of(PRODUCTS_LIST);
    }
}
