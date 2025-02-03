package common.command.commands;

import common.command.Command;
import common.command.CommandFactory;
import common.logger.CustomLogger;
import common.model.User;
import server.ClientHandler;

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

        clientHandler.getOutput().println("Usuario: " + currentUser.getUsername());
    }

    public static String getCommandName() {
        return COMMAND_NAME;
    }
}
