package server.command.commands;

import server.command.Command;
import server.command.CommandFactory;
import common.logger.CustomLogger;
import server.thread.ClientHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

public class VowelCommand implements Command {
    private static final Logger logger = CustomLogger.getLogger(VowelCommand.class.getName());
    private static final String COMMAND_NAME = "vowel";
    private static final int parametersAmount = 1;
    @Override
    public void execute(String[] args, ClientHandler clientHandler) {
        if (args.length != parametersAmount) {
            clientHandler.getOutput().println("Ayuda: " + CommandFactory.getCommandSymbol() +
                    COMMAND_NAME + " <vowel>");
            return;
        }

        logger.log(Level.INFO, "Ejecutando comando " + CommandFactory.getCommandSymbol() + "{0} por " + clientHandler.getSocketAddress(), COMMAND_NAME);

        if (!clientHandler.hasActiveSingleGame() && !clientHandler.hasActiveMultiplayerGame()) {
            clientHandler.getOutput().println("No tienes una partida activa.");
            return;
        }

        if (clientHandler.hasActiveSingleGame() && !clientHandler.hasActiveMultiplayerGame()) {
            char vowel = args[0].charAt(0);
            clientHandler.getGameSession().guessLetter(vowel, true);
            return;
        }

        if (clientHandler.hasActiveMultiplayerGame()) {
            char vowel = args[0].charAt(0);
            clientHandler.getCurrentRoom().playerGuessVowel(vowel, clientHandler);
        }
    }

    public static String getCommandName() {
        return COMMAND_NAME;
    }
}
