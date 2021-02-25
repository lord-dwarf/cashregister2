package com.polinakulyk.cashregister2.controller;

import com.polinakulyk.cashregister2.controller.api.HttpMethod;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.polinakulyk.cashregister2.controller.MainRouter.getRouteStringFromMainServlet;
import static com.polinakulyk.cashregister2.controller.MainRouter.redirect;
import static com.polinakulyk.cashregister2.controller.api.HttpMethod.DELETE;
import static com.polinakulyk.cashregister2.controller.api.HttpMethod.GET;
import static com.polinakulyk.cashregister2.controller.api.HttpMethod.POST;
import static com.polinakulyk.cashregister2.controller.api.HttpMethod.PUT;
import static com.polinakulyk.cashregister2.controller.api.HttpRoute.ERROR_NOTFOUND;
import static com.polinakulyk.cashregister2.controller.api.HttpRoute.fromRouteString;

@WebServlet(name = "mainServlet", value = "/")
public class MainServlet extends HttpServlet {

    private MainRouter router;

    @Override
    public void init() {
        router = MainRouter.Router.INSTANCE;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doRequest(GET, request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doRequest(POST, request, response);
    }

    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doRequest(PUT, request, response);
    }

    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doRequest(DELETE, request, response);
    }

    public void doRequest(
            HttpMethod httpMethod, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        var routeString = getRouteStringFromMainServlet(request);
        var command = fromRouteString(routeString)
                .flatMap((httpRoute) ->
                        router.getCommand(httpMethod, httpRoute));
        if (command.isPresent()) {
            var redirectRoute = command.get().execute(request, response);
            if (redirectRoute.isPresent()) {
                redirect(request, response, redirectRoute.get());
            }
        } else {
            router.getCommand(GET, ERROR_NOTFOUND).get().execute(request, response);
        }
    }
}