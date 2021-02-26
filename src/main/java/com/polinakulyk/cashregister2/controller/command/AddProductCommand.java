package com.polinakulyk.cashregister2.controller.command;

import com.polinakulyk.cashregister2.controller.api.Command;
import com.polinakulyk.cashregister2.controller.api.HttpRoute;
import com.polinakulyk.cashregister2.db.dto.ProductAmountUnit;
import com.polinakulyk.cashregister2.db.entity.Product;
import com.polinakulyk.cashregister2.security.AuthHelper;
import com.polinakulyk.cashregister2.service.ProductService;
import com.polinakulyk.cashregister2.service.ReportService;
import com.polinakulyk.cashregister2.util.Util;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.polinakulyk.cashregister2.controller.api.HttpRoute.*;
import static com.polinakulyk.cashregister2.db.dto.ProductAmountUnit.*;
import static com.polinakulyk.cashregister2.service.dto.ReportKind.X;
import static com.polinakulyk.cashregister2.util.Util.bigDecimalAmount;
import static com.polinakulyk.cashregister2.util.Util.bigDecimalMoney;
import static java.nio.charset.StandardCharsets.UTF_8;

public class AddProductCommand implements Command {

    private final ProductService productService = new ProductService();
    private final AuthHelper authHelper = new AuthHelper();

    @Override
    public Optional<String> execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String userId = authHelper.getUserFromSession(request).get().getId();
        var amountUnit = fromString(request.getParameter("amountUnit")).get();
        var product = new Product()
                .setCode(request.getParameter("code"))
                .setName(request.getParameter("name"))
                .setCategory(request.getParameter("category"))
                .setPrice(bigDecimalMoney(request.getParameter("price")))
                .setAmountAvailable(
                        bigDecimalAmount(request.getParameter("amountAvailable"), amountUnit))
                .setAmountUnit(amountUnit)
                .setDetails(request.getParameter("details"));

        productService.create(userId, product);
        return Optional.of(toRouteString(PRODUCTS_LIST));
    }
}
