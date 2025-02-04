package server.command.commands;

import server.command.Command;
import server.command.CommandFactory;
import common.logger.CustomLogger;
import server.thread.ClientHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SolveCommand implements Command {
    private static final Logger logger = CustomLogger.getLogger(SolveCommand.class.getName());
    private static final String COMMAND_NAME = "solve";
    private static final int parametersMinimum = 1;
    @Override
    public void execute(String[] args, ClientHandler clientHandler) {
        if (args.length < parametersMinimum) {
            clientHandler.getOutput().println("Ayuda: " + CommandFactory.getCommandSymbol() +
                    COMMAND_NAME + " <proverbio...>");
            return;
        }

        logger.log(Level.INFO, "Ejecutando comando " + CommandFactory.getCommandSymbol() + "{0} por " + clientHandler.getSocketAddress(), COMMAND_NAME);


        if (!clientHandler.hasActiveSingleGame() && !clientHandler.hasActiveMultiplayerGame()) {
            clientHandler.getOutput().println("No tienes una partida activa.");
            return;
        }

        if (clientHandler.hasActiveSingleGame() && !clientHandler.hasActiveMultiplayerGame()) {
            String proverb = String.join(" ", args);
            clientHandler.getGameSession().resolveProverb(proverb);
            return;
        }

        if (clientHandler.hasActiveMultiplayerGame()) {
            String proverb = String.join(" ", args);
            clientHandler.getCurrentRoom().playerResolve(proverb);
        }
    }

    public static String getCommandName() {
        return COMMAND_NAME;
    }
}
