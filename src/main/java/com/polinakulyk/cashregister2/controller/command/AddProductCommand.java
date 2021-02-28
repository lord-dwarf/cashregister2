package com.polinakulyk.cashregister2.controller.command;

import com.polinakulyk.cashregister2.controller.api.RouteString;
import com.polinakulyk.cashregister2.db.dto.ProductAmountUnit;
import com.polinakulyk.cashregister2.db.entity.Product;
import com.polinakulyk.cashregister2.security.AuthHelper;
import com.polinakulyk.cashregister2.service.ProductService;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.polinakulyk.cashregister2.controller.api.HttpRoute.*;
import static com.polinakulyk.cashregister2.db.dto.ProductAmountUnit.*;
import static com.polinakulyk.cashregister2.util.Util.bigDecimalAmount;
import static com.polinakulyk.cashregister2.util.Util.bigDecimalMoney;

public class AddProductCommand implements Command {

    private final ProductService productService = new ProductService();
    private final AuthHelper authHelper = new AuthHelper();

    @Override
    public Optional<RouteString> execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String userId = authHelper.getUserIdFromSession(request);
        var amountUnit =
                ProductAmountUnit.fromExistingString(request.getParameter("amountUnit"));
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
        return Optional.of(RouteString.of(PRODUCTS_LIST));
    }
}
