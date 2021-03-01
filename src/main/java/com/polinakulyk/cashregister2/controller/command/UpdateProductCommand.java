package com.polinakulyk.cashregister2.controller.command;

import com.polinakulyk.cashregister2.controller.dto.RouteString;
import com.polinakulyk.cashregister2.db.dto.ProductAmountUnit;
import com.polinakulyk.cashregister2.db.entity.Product;
import com.polinakulyk.cashregister2.service.ProductService;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.polinakulyk.cashregister2.controller.dto.HttpRoute.PRODUCTS_LIST;
import static com.polinakulyk.cashregister2.util.Util.bigDecimalAmount;
import static com.polinakulyk.cashregister2.util.Util.bigDecimalMoney;

public class UpdateProductCommand implements Command {

    private final ProductService productService = new ProductService();

    @Override
    public Optional<RouteString> execute(HttpServletRequest request, HttpServletResponse response) {
        var amountUnit =
                ProductAmountUnit.fromExistingString(request.getParameter("amountUnit"));
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
        return Optional.of(RouteString.of(PRODUCTS_LIST));
    }
}
