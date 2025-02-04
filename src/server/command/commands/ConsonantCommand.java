package server.command.commands;

import server.command.Command;
import server.command.CommandFactory;
import common.logger.CustomLogger;
import server.thread.ClientHandler;

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

        logger.log(Level.INFO, "Ejecutando comando " + CommandFactory.getCommandSymbol() + "{0} por " + clientHandler.getSocketAddress(), COMMAND_NAME);

        if (!clientHandler.hasActiveSingleGame() && !clientHandler.hasActiveMultiplayerGame()) {
            clientHandler.getOutput().println("No tienes una partida activa.");
            return;
        }

        if (clientHandler.hasActiveSingleGame() && !clientHandler.hasActiveMultiplayerGame()) {
            char consonant = args[0].charAt(0);
            clientHandler.getGameSession().guessConsonant(consonant);
            return;
        }

        if (clientHandler.hasActiveMultiplayerGame()) {
            char consonant = args[0].charAt(0);
            clientHandler.getCurrentRoom().playerGuessConsonant(consonant, clientHandler);
        }
    }

    public static String getCommandName() {
        return COMMAND_NAME;
    }
}
