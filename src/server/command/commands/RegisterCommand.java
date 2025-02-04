package server.command.commands;

import server.command.Command;
import server.command.CommandFactory;
import common.logger.CustomLogger;
import common.model.User;
import server.service.services.UserManager;
import server.thread.ClientHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

public class RegisterCommand implements Command {
    private static final Logger logger = CustomLogger.getLogger(RegisterCommand.class.getName());
    private static final String COMMAND_NAME = "register";
    private static final int parametersAmount = 2;

    @Override
    public void execute(String[] args, ClientHandler clientHandler) {
        if (args.length != parametersAmount) {
            logger.log(Level.INFO, "Sintaxis incorrecta de /{0} command", COMMAND_NAME);
            clientHandler.getOutput().println("Ayuda: " + CommandFactory.getCommandSymbol() +
                    COMMAND_NAME + " <nombre> <contraseña>");
            return;
        }

        logger.log(Level.INFO, "Ejecutando comando " + CommandFactory.getCommandSymbol() + "{0} por " + clientHandler.getSocketAddress(), COMMAND_NAME);

        String username = args[0];
        String password = args[1];

        UserManager manager = clientHandler.getServiceRegister().getService(UserManager.class);

        User user = manager.authenticate(username, password);

        if (user == null && manager.registerUser(args[0], args[1])) {
            clientHandler.getOutput().println("Ususario registrado con exito!");
            logger.log(Level.INFO, clientHandler.getFormatedUser() + ": ususario registrado con exito!");
        } else {
            clientHandler.getOutput().println("El usuario introducido ya existe!");
            logger.log(Level.INFO, clientHandler.getFormatedUser() + ": el usuario introducido ya existe!");
        }
    }

    public static String getCommnadName() {
        return COMMAND_NAME;
    }
}
