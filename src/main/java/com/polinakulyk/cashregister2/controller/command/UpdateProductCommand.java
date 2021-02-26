package com.polinakulyk.cashregister2.controller.command;

import com.polinakulyk.cashregister2.controller.api.Command;
import com.polinakulyk.cashregister2.controller.api.HttpRoute;
import com.polinakulyk.cashregister2.db.entity.Product;
import com.polinakulyk.cashregister2.service.ProductService;
import com.polinakulyk.cashregister2.util.Util;
import java.math.BigDecimal;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.polinakulyk.cashregister2.controller.api.HttpRoute.PRODUCTS_LIST;
import static com.polinakulyk.cashregister2.controller.api.HttpRoute.toRouteString;
import static com.polinakulyk.cashregister2.db.dto.ProductAmountUnit.fromString;
import static com.polinakulyk.cashregister2.util.Util.bigDecimalAmount;
import static com.polinakulyk.cashregister2.util.Util.bigDecimalMoney;

public class UpdateProductCommand implements Command {

    private final ProductService productService = new ProductService();

    @Override
    public Optional<String> execute(HttpServletRequest request, HttpServletResponse response) {
        var amountUnit = fromString(request.getParameter("amountUnit")).get();
        var product = new Product()
                .setId(request.getParameter("id"))
                .setCode(request.getParameter("code"))
                .setName(request.getParameter("name"))
                .setCategory(request.getParameter("category"))
                .setPrice(bigDecimalMoney(request.getParameter("price")))
                .setAmountAvailable(
                        bigDecimalAmount(request.getParameter("amountAvailable"), amountUnit))
                .setAmountUnit(amountUnit)
                .setDetails(request.getParameter("details"));

        productService.update(product);
        return Optional.of(toRouteString(PRODUCTS_LIST));
    }
}
