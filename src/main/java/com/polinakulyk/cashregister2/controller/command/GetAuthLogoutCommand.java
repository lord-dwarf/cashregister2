package com.polinakulyk.cashregister2.controller.command;

import com.polinakulyk.cashregister2.controller.dto.RouteString;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.polinakulyk.cashregister2.controller.dto.HttpRoute.INDEX;
import static com.polinakulyk.cashregister2.security.AuthHelper.getUserIdFromSession;
import static com.polinakulyk.cashregister2.security.AuthHelper.getUserRoleFromSession;
import static com.polinakulyk.cashregister2.security.AuthHelper.removeUserFromSessionIfNeeded;

public class GetAuthLogoutCommand implements Command {

    private static final Logger log = LoggerFactory.getLogger(GetAuthLogoutCommand.class);

    @Override
    public Optional<RouteString> execute(HttpServletRequest request, HttpServletResponse response) {
        // get user id and role, for logging
        var userId = getUserIdFromSession(request);
        var userRole = getUserRoleFromSession(request);

        // remove user from session
        removeUserFromSessionIfNeeded(request);
        log.info("DONE Logout of user '{}' with role '{}'", userId, userRole);

        // redirect on home page
        return Optional.of(RouteString.of(INDEX));
    }
}
