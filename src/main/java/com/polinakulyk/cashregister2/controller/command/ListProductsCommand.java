package com.polinakulyk.cashregister2.controller.command;

import com.polinakulyk.cashregister2.controller.api.Command;
import com.polinakulyk.cashregister2.controller.api.HttpRoute;
import com.polinakulyk.cashregister2.security.AuthHelper;
import com.polinakulyk.cashregister2.service.ProductService;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.polinakulyk.cashregister2.controller.api.HttpRoute.INDEX;

public class ListProductsCommand implements Command {

    private final ProductService productService = new ProductService();

    @Override
    public Optional<String> execute(HttpServletRequest request, HttpServletResponse response) {
        var products = productService.findAll();
        request.setAttribute("products", products);
        return Optional.empty();
    }
}
