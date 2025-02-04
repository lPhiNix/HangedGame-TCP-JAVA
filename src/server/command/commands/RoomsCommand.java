package server.command.commands;

import common.logger.CustomLogger;
import server.command.Command;
import server.command.CommandFactory;
import server.service.services.RoomManager;
import server.service.services.UserManager;
import server.thread.ClientHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

public class RoomsCommand implements Command {
    private static final Logger logger = CustomLogger.getLogger(RoomsCommand.class.getName());
    private static final String COMMAND_NAME = "rooms";
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
            clientHandler.getOutput().println("Inicia sesión antes para utilizar esta funcion!");
            return;
        }

        RoomManager roomManager = clientHandler.getServiceRegister().getService(RoomManager.class);
        roomManager.printAllActiveRooms(clientHandler);
    }

    public static String getCommandName() {
        return COMMAND_NAME;
    }
}
