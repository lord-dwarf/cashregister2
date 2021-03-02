package com.polinakulyk.cashregister2.controller.command;

import com.polinakulyk.cashregister2.controller.dto.RouteString;
import com.polinakulyk.cashregister2.security.AuthHelper;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.polinakulyk.cashregister2.controller.dto.HttpRoute.INDEX;

public class GetAuthLogoutCommand implements Command {

    private static final Logger log = LoggerFactory.getLogger(GetAuthLogoutCommand.class);

    private final AuthHelper authHelper = new AuthHelper();

    @Override
    public Optional<RouteString> execute(HttpServletRequest request, HttpServletResponse response) {
        // get user id and role, for logging
        var userId = authHelper.getUserIdFromSession(request);
        var userRole = authHelper.getUserRoleFromSession(request);

        // remove user from session
        authHelper.removeUserFromSessionIfNeeded(request);
        log.info("DONE Logout of user '{}' with role '{}'", userId, userRole);

        // redirect on home page
        return Optional.of(RouteString.of(INDEX));
    }
}
