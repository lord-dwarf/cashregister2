package com.polinakulyk.cashregister2.controller.command;

import com.polinakulyk.cashregister2.controller.dto.RouteString;
import com.polinakulyk.cashregister2.security.AuthHelper;
import com.polinakulyk.cashregister2.service.ReceiptService;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

public class ListMyReceiptsCommand implements Command {

    private static final int ROWS_PER_PAGE = 10;

    private final ReceiptService receiptService = new ReceiptService();
    private final AuthHelper authHelper = new AuthHelper();

    @Override
    public Optional<RouteString> execute(HttpServletRequest request, HttpServletResponse response) {
        var userId = authHelper.getUserIdFromSession(request);
        var currentPage = Integer.parseInt(
                ofNullable(request.getParameter("currentPage")).orElse("1"));
        var receipts =
                receiptService.findByTellerWithPagination(userId, currentPage, ROWS_PER_PAGE);
        var receiptsTotal = receiptService.countByTeller(userId);
        var pagesTotal = receiptsTotal / ROWS_PER_PAGE;
        if (receiptsTotal % ROWS_PER_PAGE != 0) {
            ++pagesTotal;
        }
        request.setAttribute("receipts", receipts);
        request.setAttribute("pagesTotal", pagesTotal);
        return empty();
    }
}
