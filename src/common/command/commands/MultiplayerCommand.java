package common.command.commands;

import common.command.Command;
import common.logger.CustomLogger;
import server.service.ClientHandler;

import java.util.logging.Logger;

public class MultiplayerCommand implements Command {
    private static final Logger logger = CustomLogger.getLogger(MultiplayerCommand.class.getName());
    private static final String COMMAND_NAME = "multiplayer";
    private static final int parametersAmount = 1;
    @Override
    public void execute(String[] args, ClientHandler clientHandler) {

    }

    public static String getCommandName() {
        return COMMAND_NAME;
    }
}
