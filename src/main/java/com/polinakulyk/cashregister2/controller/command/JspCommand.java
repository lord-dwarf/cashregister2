package com.polinakulyk.cashregister2.controller.command;

import com.polinakulyk.cashregister2.controller.api.Command;
import com.polinakulyk.cashregister2.controller.api.HttpRoute;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.polinakulyk.cashregister2.controller.Router.*;

public class JspCommand implements Command {

    private final String jspName;

    public JspCommand(String jspName) {
        this.jspName = jspName;
    }

    @Override
    public Optional<String> execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        forwardToJsp(request, response, jspName);
        return Optional.empty();
    }

    public static JspCommand commandThenJsp(Command initCommand, String jspName) {
        return new CommandThenJspCommand(initCommand, jspName);
    }

    public static final class CommandThenJspCommand extends JspCommand {

        private final Command initCommand;

        private CommandThenJspCommand(Command initCommand, String jspCommandPath) {
            super(jspCommandPath);
            this.initCommand = initCommand;
        }

        @Override
        public Optional<String> execute(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            var nextRoute = initCommand.execute(request, response);
            if (nextRoute.isEmpty()) {
                return super.execute(request, response);
            }
            return nextRoute;
        }
    }
}
