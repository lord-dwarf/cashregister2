package com.polinakulyk.cashregister2.controller.command;

import com.polinakulyk.cashregister2.controller.dto.RouteString;
import com.polinakulyk.cashregister2.service.ReceiptService;

import java.io.IOException;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.polinakulyk.cashregister2.controller.validator.ValidatorHelper.validStringNotNull;

public class GetReceiptCommand implements Command {

    private static final String RECEIPT_ID_PARAM = "receiptId";
    private static final String RECEIPT_ATTR = "receipt";

    private final ReceiptService receiptService = new ReceiptService();

    @Override
    public Optional<RouteString> execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // validate receipt id
        String receiptId = validStringNotNull(request, RECEIPT_ID_PARAM);
        var receipt = receiptService.findExistingById(receiptId);
        request.setAttribute(RECEIPT_ATTR, receipt);

        // no redirect
        return Optional.empty();
    }
}
