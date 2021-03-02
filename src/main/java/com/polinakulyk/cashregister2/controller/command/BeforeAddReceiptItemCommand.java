package com.polinakulyk.cashregister2.controller.command;

import com.polinakulyk.cashregister2.controller.dto.RouteString;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.polinakulyk.cashregister2.controller.validator.ValidatorHelper.validStringNotNull;

/**
 * Captures the receipt id to which receipt item is going to be added.
 * The receipt id is needed for "additem.jsp" to being able to add receipt item and return
 * to the JSP page of the original receipt.
 */
public class BeforeAddReceiptItemCommand implements Command {

    private static final String PRODUCTS_ATTR = "products";
    private static final String RECEIPT_ID_ATTR = "receiptId";
    private static final String RECEIPT_ID_PARAM = "receiptId";

    @Override
    public Optional<RouteString> execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        request.setAttribute(PRODUCTS_ATTR, new ArrayList<>());
        // validate receipt id
        var receiptId = validStringNotNull(request, RECEIPT_ID_PARAM);
        request.setAttribute(RECEIPT_ID_ATTR, receiptId);

        // no redirect
        return Optional.empty();
    }
}
