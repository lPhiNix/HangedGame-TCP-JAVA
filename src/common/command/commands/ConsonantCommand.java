package common.command.commands;

import common.command.Command;
import common.command.CommandFactory;
import common.logger.CustomLogger;
import server.service.ClientHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ConsonantCommand implements Command {
    private static final Logger logger = CustomLogger.getLogger(ConsonantCommand.class.getName());
    private static final String COMMAND_NAME = "consonant";
    private static final int parametersAmount = 1;
    @Override
    public void execute(String[] args, ClientHandler clientHandler) {
        if (args.length != parametersAmount) {
            clientHandler.getOutput().println("Ayuda: " + CommandFactory.getCommandSymbol() +
                    COMMAND_NAME + " <consonante>");
            return;
        }

        logger.log(Level.INFO, "Ejecutando comando /{0}", COMMAND_NAME);

        if (!clientHandler.hasActiveGame()) {
            clientHandler.getOutput().println("No tienes una partida activa.");
            return;
        }

        char consonant = args[0].charAt(0);
        clientHandler.getGameSession().guessConsonant(consonant);
    }

    public static String getCommandName() {
        return COMMAND_NAME;
    }
}
