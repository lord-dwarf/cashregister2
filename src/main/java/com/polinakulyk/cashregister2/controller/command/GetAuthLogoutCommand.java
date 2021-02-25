package com.polinakulyk.cashregister2.controller.command;

import com.polinakulyk.cashregister2.controller.api.Command;
import com.polinakulyk.cashregister2.controller.api.HttpRoute;
import com.polinakulyk.cashregister2.security.AuthHelper;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.polinakulyk.cashregister2.controller.api.HttpRoute.INDEX;

public class GetAuthLogoutCommand implements Command {
    private static final Logger log = LoggerFactory.getLogger(GetAuthLogoutCommand.class);

    private final AuthHelper authHelper = new AuthHelper();

    @Override
    public Optional<HttpRoute> execute(HttpServletRequest request, HttpServletResponse response) {
        var user = authHelper.getUserFromSession(request).get();
        authHelper.removeUserFromSessionIfNeeded(request);
        log.info("DONE Logout of user '{}' with role '{}'", user.getId(), user.getRole());
        return Optional.of(INDEX);
    }
}
