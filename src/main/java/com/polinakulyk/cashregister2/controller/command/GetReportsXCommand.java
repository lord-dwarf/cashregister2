package com.polinakulyk.cashregister2.controller.command;

import com.polinakulyk.cashregister2.controller.api.RouteString;
import com.polinakulyk.cashregister2.security.AuthHelper;
import com.polinakulyk.cashregister2.service.ReportService;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.polinakulyk.cashregister2.service.dto.ReportKind.X;
import static java.nio.charset.StandardCharsets.UTF_8;

public class GetReportsXCommand implements Command {

    private final ReportService reportService = new ReportService();
    private final AuthHelper authHelper = new AuthHelper();

    @Override
    public Optional<RouteString> execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String userId = authHelper.getUserIdFromSession(request);
        var xReport = reportService.createXZReport(userId, X);

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + "report" + X + "\"");
        response.getOutputStream().write(xReport.toString().getBytes(UTF_8));

        return Optional.empty();
    }
}
