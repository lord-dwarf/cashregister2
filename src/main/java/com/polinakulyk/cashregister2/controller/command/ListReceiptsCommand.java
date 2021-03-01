package com.polinakulyk.cashregister2.controller.command;

import com.polinakulyk.cashregister2.controller.dto.RouteString;
import com.polinakulyk.cashregister2.service.ReceiptService;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

public class ListReceiptsCommand implements Command {

    private static final int ROWS_PER_PAGE = 10;

    private final ReceiptService receiptService = new ReceiptService();

    @Override
    public Optional<RouteString> execute(HttpServletRequest request, HttpServletResponse response) {
        var currentPage = Integer.parseInt(
                ofNullable(request.getParameter("currentPage")).orElse("1"));
        var receipts = receiptService.findWithPagination(currentPage, ROWS_PER_PAGE);
        var receiptsTotal = receiptService.count();
        var pagesTotal = receiptsTotal / ROWS_PER_PAGE;
        if (receiptsTotal % ROWS_PER_PAGE != 0) {
            ++pagesTotal;
        }
        request.setAttribute("receipts", receipts);
        request.setAttribute("pagesTotal", pagesTotal);
        return empty();
    }
}
