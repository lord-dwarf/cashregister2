package com.polinakulyk.cashregister2.controller.command;

import com.polinakulyk.cashregister2.controller.api.Command;
import com.polinakulyk.cashregister2.security.AuthHelper;
import com.polinakulyk.cashregister2.service.ReceiptService;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.polinakulyk.cashregister2.controller.api.HttpRoute.RECEIPTS_LIST;
import static com.polinakulyk.cashregister2.controller.api.HttpRoute.toRouteString;
import static java.nio.charset.StandardCharsets.UTF_8;

public class GetErrorMessageCommand implements Command {

    @Override
    public Optional<String> execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        var errorMessage = request.getParameter("errorMessage");
        if (errorMessage != null) {
            try {
                errorMessage = new String(
                        Base64.getUrlDecoder().decode(errorMessage.getBytes(UTF_8)), UTF_8);
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
