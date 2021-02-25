package com.polinakulyk.cashregister2.controller.api;

import java.io.IOException;
import java.util.Optional;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@FunctionalInterface
public interface Command {

    Optional<HttpRoute> execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException;

}
