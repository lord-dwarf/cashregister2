package com.polinakulyk.cashregister2.controller.command;

import com.polinakulyk.cashregister2.controller.dto.RouteString;
import com.polinakulyk.cashregister2.security.AuthHelper;
import com.polinakulyk.cashregister2.service.UserService;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.polinakulyk.cashregister2.controller.dto.HttpRoute.INDEX;
import static com.polinakulyk.cashregister2.controller.validator.ValidatorHelper.validStringNotNull;
import static com.polinakulyk.cashregister2.security.AuthHelper.*;

public class LoginCommand implements Command {

    private static final String LOGIN_PARAM = "login";
    private static final String PASSWORD_PARAM = "password";

    private final UserService userService = new UserService();

    @Override
    public Optional<RouteString> execute(HttpServletRequest request, HttpServletResponse response) {
        // validate login and password are present
        var login = validStringNotNull(request, LOGIN_PARAM);
        var password = validStringNotNull(request, PASSWORD_PARAM);
        // log in user
        var user = userService.login(login, password);
        // set user into session
        putUserIntoSession(user, request);

        // redirect to home
        return Optional.of(RouteString.of(INDEX));
    }
}
