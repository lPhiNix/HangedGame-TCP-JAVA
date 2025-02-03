package common.command.commands;

import common.command.Command;
import common.command.CommandFactory;
import common.logger.CustomLogger;
import server.ClientHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

public class StartSingleGameCommand implements Command {
    private static final Logger logger = CustomLogger.getLogger(StartSingleGameCommand.class.getName());
    private static final String COMMAND_NAME = "singlegame";
    private static final int parametersAmount = 0;

    @Override
    public void execute(String[] args, ClientHandler clientHandler) {
        if (args.length != parametersAmount) {
            clientHandler.getOutput().println("Ayuda: " + CommandFactory.getCommandSymbol() +
                    COMMAND_NAME);
            return;
        }

        logger.log(Level.INFO, "Ejecutando comando /{0}", COMMAND_NAME);

        clientHandler.startGame();
    }

    public static String getCommandName() {
        return COMMAND_NAME;
    }
}
