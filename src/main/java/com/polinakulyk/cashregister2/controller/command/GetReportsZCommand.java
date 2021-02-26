package com.polinakulyk.cashregister2.controller.command;

import com.polinakulyk.cashregister2.controller.api.Command;
import com.polinakulyk.cashregister2.controller.api.HttpRoute;
import com.polinakulyk.cashregister2.security.AuthHelper;
import com.polinakulyk.cashregister2.service.ReportService;
import com.polinakulyk.cashregister2.service.dto.ReportKind;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.polinakulyk.cashregister2.service.dto.ReportKind.*;
import static com.polinakulyk.cashregister2.service.dto.ReportKind.X;
import static java.nio.charset.StandardCharsets.UTF_8;

public class GetReportsZCommand implements Command {

    private final ReportService reportService = new ReportService();
    private final AuthHelper authHelper = new AuthHelper();

    @Override
    public Optional<String> execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String userId = authHelper.getUserFromSession(request).get().getId();
        var zReport = reportService.createXZReport(userId, Z);

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + "report" + Z + "\"");
        response.getOutputStream().write(zReport.toString().getBytes(UTF_8));

        return Optional.empty();
    }
}
