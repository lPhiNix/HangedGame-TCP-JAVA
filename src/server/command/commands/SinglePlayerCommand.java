package server.command.commands;

import server.command.Command;
import server.command.CommandFactory;
import common.logger.CustomLogger;
import server.thread.ClientHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SinglePlayerCommand implements Command {
    private static final Logger logger = CustomLogger.getLogger(SinglePlayerCommand.class.getName());
    private static final String COMMAND_NAME = "singleplayer";
    private static final int parametersAmount = 0;

    @Override
    public void execute(String[] args, ClientHandler clientHandler) {
        if (args.length != parametersAmount) {
            clientHandler.getOutput().println("Ayuda: " + CommandFactory.getCommandSymbol() +
                    COMMAND_NAME);
            return;
        }

        logger.log(Level.INFO, "Ejecutando comando " + CommandFactory.getCommandSymbol() + "{0} por " + clientHandler.getSocketAddress(), COMMAND_NAME);

        if (clientHandler.getCurrentUser() == null) {
            clientHandler.getOutput().println("Debes iniciar sesión antes de jugar.");
            return;
        }

        if (clientHandler.hasActiveMultiplayerGame()) {
            clientHandler.getOutput().println("Sal de la sala para jugar una partida individual.");
        }

        clientHandler.startGame();
    }

    public static String getCommandName() {
        return COMMAND_NAME;
    }
}
