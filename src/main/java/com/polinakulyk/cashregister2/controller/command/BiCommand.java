package com.polinakulyk.cashregister2.controller.command;

import com.polinakulyk.cashregister2.controller.dto.RouteString;

import java.io.IOException;
import java.util.Optional;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static java.util.Objects.requireNonNull;

/**
 * Consists of two commands to execute.
 */
public final class BiCommand implements Command {

    private final Command firstCommand;
    private final Command secondCommand;

    private BiCommand(Command firstCommand, Command secondCommand) {
        // both commands must be present
        requireNonNull(firstCommand, "First command must not be null");
        requireNonNull(secondCommand, "Second command must not be null");
        this.firstCommand = firstCommand;
        this.secondCommand = secondCommand;
    }

    @Override
    public Optional<RouteString> execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        var nextRoute = firstCommand.execute(request, response);
        // if first command returns non-empty Optional, then redirect is needed
        if (nextRoute.isPresent()) {
            // hint to MainServlet to respond with a redirect
            return nextRoute;
        }
        // second command also can return a redirect hint
        return secondCommand.execute(request, response);
    }

    public static BiCommand of(Command firstCommand, Command secondCommand) {
        return new BiCommand(firstCommand, secondCommand);
    }
}
