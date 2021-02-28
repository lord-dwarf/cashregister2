package com.polinakulyk.cashregister2.controller.command;

import com.polinakulyk.cashregister2.controller.api.RouteString;
import com.polinakulyk.cashregister2.controller.router.RouterHelper;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static java.util.Objects.requireNonNull;

public final class JspCommand implements Command {

    private final String jspName;

    private JspCommand(String jspName) {
        requireNonNull(jspName, "JSP name must not be null");
        this.jspName = jspName;
    }

    @Override
    public Optional<RouteString> execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RouterHelper.forwardToJsp(request, response, jspName);
        return Optional.empty();
    }

    public static JspCommand of(String jspName) {
        return new JspCommand(jspName);
    }
}
