package com.polinakulyk.cashregister2.controller.command;

import com.polinakulyk.cashregister2.controller.dto.RouteString;
import com.polinakulyk.cashregister2.service.ReceiptService;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.polinakulyk.cashregister2.controller.ControllerHelper.calcPaginationPagesTotal;
import static com.polinakulyk.cashregister2.controller.validator.ValidatorHelper.validIntegerNullable;

public class ListReceiptsCommand implements Command {

    private static final String RECEIPTS_ATTR = "receipts";

    private static final int ROWS_PER_PAGE = 10;
    private static final Integer DEFAULT_PAGE = 1;
    private static final String PAGES_TOTAL_ATTR = "pagesTotal";
    private static final String CURRENT_PAGE_PARAM = "currentPage";

    private final ReceiptService receiptService = new ReceiptService();

    @Override
    public Optional<RouteString> execute(HttpServletRequest request, HttpServletResponse response) {
        var currentPage = validIntegerNullable(request, CURRENT_PAGE_PARAM)
                .orElse(DEFAULT_PAGE);
        var receipts = receiptService.findWithPagination(currentPage, ROWS_PER_PAGE);
        var receiptsTotal = receiptService.count();
        var pagesTotal = calcPaginationPagesTotal(receiptsTotal, ROWS_PER_PAGE);

        request.setAttribute(RECEIPTS_ATTR, receipts);
        request.setAttribute(PAGES_TOTAL_ATTR, pagesTotal);

        // no redirect
        return Optional.empty();
    }
}
