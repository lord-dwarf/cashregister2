package com.polinakulyk.cashregister2.controller.command;

import com.polinakulyk.cashregister2.controller.dto.RouteString;

import java.io.IOException;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.polinakulyk.cashregister2.controller.validator.ValidatorHelper.validStringNullable;
import static com.polinakulyk.cashregister2.util.Util.fromBase64;

public class GetErrorMessageCommand implements Command {

    private static final String ERROR_MESSAGE_PARAM = "errorMessage";
    private static final String ERROR_MESSAGE_ATTR = "errorMessage";

    @Override
    public Optional<RouteString> execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        var errorMessageOpt = validStringNullable(request, ERROR_MESSAGE_PARAM);

        // obtain error message from Base64 string
        if (errorMessageOpt.isPresent()) {
            try {
                var errorMessageDecoded = fromBase64(errorMessageOpt.get());
                request.setAttribute(ERROR_MESSAGE_ATTR, errorMessageDecoded);
            } catch (RuntimeException e) {
                // do nothing - show user the 400 error page with a general message "Bad Request"
            }
        }

        // forward to 400 error page
        return Optional.empty();
    }
}
