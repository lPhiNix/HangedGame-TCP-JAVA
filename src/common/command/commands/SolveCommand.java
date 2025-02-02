package common.command.commands;

import common.command.Command;
import common.logger.CustomLogger;
import server.ClientHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SolveCommand implements Command {
    private static final Logger logger = CustomLogger.getLogger(SolveCommand.class.getName());
    private static final String COMMAND_NAME = "solve";
    @Override
    public void execute(String[] args, ClientHandler clientHandlert) {
        logger.log(Level.INFO, "Start /{0} command", COMMAND_NAME);
    }

    public static String getCommandName() {
        return COMMAND_NAME;
    }
}
