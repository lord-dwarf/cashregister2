package com.polinakulyk.cashregister2.controller.command;

import com.polinakulyk.cashregister2.controller.api.Command;
import com.polinakulyk.cashregister2.controller.api.HttpRoute;
import com.polinakulyk.cashregister2.db.dto.ProductAmountUnit;
import com.polinakulyk.cashregister2.db.entity.Product;
import com.polinakulyk.cashregister2.security.AuthHelper;
import com.polinakulyk.cashregister2.service.ProductService;
import com.polinakulyk.cashregister2.service.ReportService;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.polinakulyk.cashregister2.controller.api.HttpRoute.*;
import static com.polinakulyk.cashregister2.db.dto.ProductAmountUnit.*;
import static com.polinakulyk.cashregister2.service.dto.ReportKind.X;
import static java.nio.charset.StandardCharsets.UTF_8;

public class AddProductCommand implements Command {

    private final ProductService productService = new ProductService();
    private final AuthHelper authHelper = new AuthHelper();

    @Override
    public Optional<HttpRoute> execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String userId = authHelper.getUserFromSession(request).get().getId();
        var product = new Product()
                .setCode(request.getParameter("code"))
                .setName(request.getParameter("name"))
                .setCategory(request.getParameter("category"))
                .setPrice(new BigDecimal(request.getParameter("price")))
                .setAmountAvailable(new BigDecimal(request.getParameter("amountAvailable")))
                .setAmountUnit(fromString(request.getParameter("amountUnit")).get())
                .setDetails(request.getParameter("details"));

        productService.create(userId, product);
        return Optional.of(PRODUCTS_LIST);
    }
}
