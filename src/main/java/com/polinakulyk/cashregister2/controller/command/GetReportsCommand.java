package com.polinakulyk.cashregister2.controller.command;

import com.polinakulyk.cashregister2.controller.dto.RouteString;
import com.polinakulyk.cashregister2.security.AuthHelper;
import com.polinakulyk.cashregister2.service.ReportService;
import com.polinakulyk.cashregister2.service.dto.ReportKind;

import java.io.IOException;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;

public class GetReportsCommand implements Command {

    private static final String MIME_TYPE_OCTET_STREAM = "application/octet-stream";

    private final ReportService reportService = new ReportService();
    private final AuthHelper authHelper = new AuthHelper();

    private final ReportKind reportKind;

    public GetReportsCommand(ReportKind reportKind) {
        requireNonNull(reportKind);
        this.reportKind = reportKind;
    }

    @Override
    public Optional<RouteString> execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String userId = authHelper.getUserIdFromSession(request);
        var report = reportService.createXZReport(userId, reportKind);

        // write report string into response output stream
        response.setContentType(MIME_TYPE_OCTET_STREAM);
        response.setHeader(
                "Content-Disposition",
                String.format("attachment; filename=\"%s\"", "report" + report.getReportKind())
        );
        response.getOutputStream().write(report.toString().getBytes(UTF_8));

        // no redirect
        return Optional.empty();
    }
}
