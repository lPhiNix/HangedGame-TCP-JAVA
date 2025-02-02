package common.command.commands;

import common.command.Command;
import common.command.CommandFactory;
import common.logger.CustomLogger;
import common.model.User;
import common.util.UserManager;
import server.ClientHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginCommand implements Command {
    private static final Logger logger = CustomLogger.getLogger(LoginCommand.class.getName());
    private static final String COMMAND_NAME = "login";
    private static final int parametersAmount = 2;
    @Override
    public void execute(String[] args, ClientHandler clientHandler) {
        if (args.length != parametersAmount) {
            clientHandler.getOutput().println("Ayuda: " + CommandFactory.getCommandSymbol() +
                    COMMAND_NAME + " <username> <password>");
            return;
        }

        logger.log(Level.INFO, "Ejecutando /{0} command", COMMAND_NAME);

        String username = args[0];
        String password = args[1];

        UserManager manager = clientHandler.getUserManager();

        User user = manager.authenticate(username, password);

        if (user != null) {
            logger.log(Level.INFO, "Ususario logueado con exito!");
            clientHandler.setCurrentUser(user);
        } else {
            logger.log(Level.WARNING, "El usuario no existe!");
        }
    }

    public static String getCommandName() {
        return COMMAND_NAME;
    }
}
