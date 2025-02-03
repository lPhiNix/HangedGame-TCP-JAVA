package common.command.commands;

import common.command.Command;
import common.logger.CustomLogger;
import server.ClientHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

public class StartSingleGameCommand implements Command {
    private static final Logger logger = CustomLogger.getLogger(StartSingleGameCommand.class.getName());
    private static final String COMMAND_NAME = "singlegame";

    @Override
    public void execute(String[] args, ClientHandler clientHandler) {
        logger.log(Level.INFO, "Start /{0} command", COMMAND_NAME);

        clientHandler.startGame();
    }

    public static String getCommandName() {
        return COMMAND_NAME;
    }
}
