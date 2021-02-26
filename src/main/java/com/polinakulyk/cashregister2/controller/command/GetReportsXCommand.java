package com.polinakulyk.cashregister2.controller.command;

import com.polinakulyk.cashregister2.controller.api.Command;
import com.polinakulyk.cashregister2.controller.api.HttpRoute;
import com.polinakulyk.cashregister2.security.AuthHelper;
import com.polinakulyk.cashregister2.service.ReceiptService;
import com.polinakulyk.cashregister2.service.ReportService;
import com.polinakulyk.cashregister2.service.dto.ReportKind;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.polinakulyk.cashregister2.service.dto.ReportKind.*;
import static java.nio.charset.StandardCharsets.UTF_8;

public class GetReportsXCommand implements Command {

    private final ReportService reportService = new ReportService();
    private final AuthHelper authHelper = new AuthHelper();

    @Override
    public Optional<String> execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String userId = authHelper.getUserFromSession(request).get().getId();
        var xReport = reportService.createXZReport(userId, X);

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition","attachment; filename=\"" + "report" + X + "\"");
        response.getOutputStream().write(xReport.toString().getBytes(UTF_8));

        return Optional.empty();
    }
}
