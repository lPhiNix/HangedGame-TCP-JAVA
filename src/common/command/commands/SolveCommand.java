package common.command.commands;

import common.command.Command;
import common.command.CommandContext;
import common.logger.CustomLogger;

import java.util.logging.Logger;

public class SolveCommand implements Command {
    private static final Logger logger = CustomLogger.getLogger(SolveCommand.class.getName());
    private static final String COMMNAD_NAME = "solve";
    @Override
    public void execute(String[] args, CommandContext context) {

    }

    public static String getCommnadName() {
        return COMMNAD_NAME;
    }
}
