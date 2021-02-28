package com.polinakulyk.cashregister2.controller.command;

import com.polinakulyk.cashregister2.controller.api.RouteString;
import com.polinakulyk.cashregister2.util.Util;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.polinakulyk.cashregister2.util.Util.fromBase64;

public class GetErrorMessageCommand implements Command {

    @Override
    public Optional<RouteString> execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        var errorMessage = request.getParameter("errorMessage");
        if (errorMessage != null) {
            try {
                errorMessage = fromBase64(errorMessage);
            } catch (RuntimeException e) {
                errorMessage = null;
            }
        }
        if (errorMessage != null) {
            request.setAttribute("errorMessage", errorMessage);
        }
        return Optional.empty();
    }
}
