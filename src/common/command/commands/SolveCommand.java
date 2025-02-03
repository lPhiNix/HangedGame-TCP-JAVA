package common.command.commands;

import common.command.Command;
import common.command.CommandFactory;
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
                    COMMAND_NAME + " <frase...>");
            return;
        }

        logger.log(Level.INFO, "Ejecutando comando /{0}", COMMAND_NAME);

        if (!clientHandler.hasActiveGame()) {
            clientHandler.getOutput().println("No tienes una partida activa.");
            return;
        }

        String phrase = String.join(" ", args);
        clientHandler.getGameSession().resolveProverb(phrase);
    }

    public static String getCommandName() {
        return COMMAND_NAME;
    }
}
