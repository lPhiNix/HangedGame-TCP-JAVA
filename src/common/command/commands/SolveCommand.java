package common.command.commands;

import common.command.Command;
import common.logger.CustomLogger;
import server.ClientHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SolveCommand implements Command {
    private static final Logger logger = CustomLogger.getLogger(SolveCommand.class.getName());
    private static final String COMMAND_NAME = "solve";
    @Override
    public void execute(String[] args, ClientHandler clientHandler) {
        logger.log(Level.INFO, "Start /{0} command", COMMAND_NAME);

        if (!clientHandler.hasActiveGame()) {
            clientHandler.getOutput().println("No tienes una partida activa.");
            return;
        }

        if (args.length < 1) {
            clientHandler.getOutput().println("Uso: /solve <frase>");
            return;
        }

        String phrase = String.join(" ", args);
        clientHandler.getGameSession().resolveProverb(phrase);
    }

    public static String getCommandName() {
        return COMMAND_NAME;
    }
}
