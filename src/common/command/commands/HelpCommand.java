package common.command.commands;

import common.command.Command;
import common.logger.CustomLogger;
import server.service.ClientHandler;

import java.util.logging.Logger;

public class HelpCommand implements Command {
    private static final Logger logger = CustomLogger.getLogger(HelpCommand.class.getName());
    private static final String COMMAND_NAME = "help";
    private static final int parametersAmount = 0;
    @Override
    public void execute(String[] args, ClientHandler clientHandler) {

    }

    public static String getCommandName() {
        return COMMAND_NAME;
    }
}
