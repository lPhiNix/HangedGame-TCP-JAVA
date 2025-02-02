package common.command.commands;

import common.command.Command;
import common.command.CommandFactory;
import common.logger.CustomLogger;
import server.ClientHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ExitCommand implements Command {
    private static final Logger logger = CustomLogger.getLogger(ExitCommand.class.getName());
    private static final String COMMAND_NAME = "exit";
    private static final int parametersAmount = 0;
    @Override
    public void execute(String[] args, ClientHandler clientHandler) {
        if (args.length != parametersAmount) {
            clientHandler.getOutput().println("Ayuda: " + CommandFactory.getCommandSymbol() +
                    COMMAND_NAME);
            return;
        }

        logger.log(Level.INFO, "Start /{0} command", COMMAND_NAME);

        clientHandler.setRunning(false);
        clientHandler.closeConnection();
    }

    public static String getCommandName() {
        return COMMAND_NAME;
    }
}
