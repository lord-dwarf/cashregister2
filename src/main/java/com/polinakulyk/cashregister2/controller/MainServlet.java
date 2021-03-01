package com.polinakulyk.cashregister2.controller;

import com.polinakulyk.cashregister2.controller.dto.HttpMethod;
import com.polinakulyk.cashregister2.controller.router.MainRouter;
import com.polinakulyk.cashregister2.controller.router.Router;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.polinakulyk.cashregister2.controller.router.RouterHelper.getRoutePathFromServletRequest;
import static com.polinakulyk.cashregister2.controller.router.RouterHelper.redirect;
import static com.polinakulyk.cashregister2.controller.dto.HttpMethod.DELETE;
import static com.polinakulyk.cashregister2.controller.dto.HttpMethod.GET;
import static com.polinakulyk.cashregister2.controller.dto.HttpMethod.POST;
import static com.polinakulyk.cashregister2.controller.dto.HttpMethod.PUT;
import static com.polinakulyk.cashregister2.controller.dto.HttpRoute.ERROR_NOTFOUND;
import static com.polinakulyk.cashregister2.controller.router.RouterHelper.httpRouteFromRoutePath;

/**
 * The purpose of {@link MainServlet} is to map incoming HTTP requests
 * to the appropriate {@link com.polinakulyk.cashregister2.controller.command.Command}
 * functions according to routes configured in {@link MainRouter}.
 *
 * Requests for all HTTP verbs go through a universal {@link MainServlet#doRequest} handler.
 */
@WebServlet(name = "mainServlet", value = "/")
public class MainServlet extends HttpServlet {

    private Router router;

    @Override
    public void init() {
        // get singleton of MainRouter prior to serving requests
        router = MainRouter.Singleton.INSTANCE;
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

    /**
     * {@link MainServlet#doRequest} handles all requests for servlet.
     *
     *
     * @param httpMethod
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void doRequest(
            HttpMethod httpMethod, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        var routePath = getRoutePathFromServletRequest(request);
        var command = httpRouteFromRoutePath(routePath)
                .flatMap(httpRoute ->
                        router.getCommand(httpMethod, httpRoute));
        if (command.isPresent()) {
            // execute command
            var routeString = command.get().execute(request, response);
            if (routeString.isPresent()) {
                // command returned redirect, do redirect
                redirect(request, response, routeString.get());
            }
        } else {
            // route not found by router, redirect 404
            redirect(request, response, ERROR_NOTFOUND);
        }
    }
}