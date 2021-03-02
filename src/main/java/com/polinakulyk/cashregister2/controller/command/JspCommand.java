package com.polinakulyk.cashregister2.controller.command;

import com.polinakulyk.cashregister2.controller.dto.RouteString;
import com.polinakulyk.cashregister2.controller.router.RouterHelper;

import java.io.IOException;
import java.util.Optional;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static java.util.Objects.requireNonNull;

/**
 * Forwards incoming request to the JSP page which is specified at the point of command creation.
 * <p>
 * EXAMPLE:
 * addCommand(GET, INDEX, JspCommand.of("index.jsp"), any());
 */
public final class JspCommand implements Command {

    private final String jspName;

    private JspCommand(String jspName) {
        requireNonNull(jspName, "JSP name must not be null");
        this.jspName = jspName;
    }

    public static JspCommand of(String jspName) {
        return new JspCommand(jspName);
    }

    @Override
    public Optional<RouteString> execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RouterHelper.forwardToJsp(request, response, jspName);
        // proceed to a possible next command (without redirect)

        // empty = no redirect
        return Optional.empty();
    }
}
