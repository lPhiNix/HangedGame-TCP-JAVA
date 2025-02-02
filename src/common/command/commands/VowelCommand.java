package common.command.commands;

import common.command.Command;
import common.logger.CustomLogger;
import server.ClientHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

public class VowelCommand implements Command {
    private static final Logger logger = CustomLogger.getLogger(VowelCommand.class.getName());
    private static final String COMMAND_NAME = "vowel";
    @Override
    public void execute(String[] args, ClientHandler clientHandler) {
        logger.log(Level.INFO, "Start /{0} comand", COMMAND_NAME);
    }

    public static String getCommandName() {
        return COMMAND_NAME;
    }
}
