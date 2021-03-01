package com.polinakulyk.cashregister2.controller.command;

import com.polinakulyk.cashregister2.controller.dto.RouteString;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Unit of work for presentation layer, that is called by
 * {@link com.polinakulyk.cashregister2.controller.MainServlet} according to the routes registered in
 * {@link com.polinakulyk.cashregister2.controller.router.MainRouter}.
 */
@FunctionalInterface
public interface Command {
    /**
     * The sole method of our unit of work to be executed.
     * May return a hint for {@link com.polinakulyk.cashregister2.controller.MainServlet} to perform
     * a redirect to the provided {@link RouteString} (that may include path + optional query params).
     *
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    Optional<RouteString> execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException;

}
