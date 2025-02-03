package common.command.commands;

import common.command.Command;
import common.command.CommandFactory;
import common.logger.CustomLogger;
import common.model.User;
import server.service.ClientHandler;

import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserCommand implements Command {
    private static final Logger logger = CustomLogger.getLogger(UserCommand.class.getName());
    private static final String COMMAND_NAME = "user";
    private static final int parametersAmount = 0;
    @Override
    public void execute(String[] args, ClientHandler clientHandler) {
        if (args.length != parametersAmount) {
            clientHandler.getOutput().println("Ayuda: " + CommandFactory.getCommandSymbol() +
                    COMMAND_NAME);
            return;
        }

        User currentUser = clientHandler.getCurrentUser();

        if (currentUser == null) {
            clientHandler.getOutput().println("Inicia sesión antes para utilizar esta funcion!");
            return;
        }

        logger.log(Level.INFO, "Ejecutando comando /{0}", COMMAND_NAME);

        PrintWriter output = clientHandler.getOutput();
        output.println("Usuario: " + currentUser.getUsername());
        output.println("Puntuación: " + currentUser.getScore());
        output.println("Victorias: " + currentUser.getWins());
        output.println("Derrotas: " + currentUser.getDefeats());
    }

    public static String getCommandName() {
        return COMMAND_NAME;
    }
}
