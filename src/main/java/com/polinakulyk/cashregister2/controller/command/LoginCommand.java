package com.polinakulyk.cashregister2.controller.command;

import com.polinakulyk.cashregister2.controller.api.RouteString;
import com.polinakulyk.cashregister2.security.AuthHelper;
import com.polinakulyk.cashregister2.service.UserService;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.polinakulyk.cashregister2.controller.api.HttpRoute.*;

public class LoginCommand implements Command {

    private final UserService userService = new UserService();
    private final AuthHelper authHelper = new AuthHelper();

    @Override
    public Optional<RouteString> execute(HttpServletRequest request, HttpServletResponse response) {
        var login = request.getParameter("login");
        var password = request.getParameter("password");
        var user = userService.login(login, password);
        authHelper.putUserIntoSession(user, request);
        return Optional.of(RouteString.of(INDEX));
    }
}
