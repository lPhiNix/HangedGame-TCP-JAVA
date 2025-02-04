package server.command.commands;

import common.logger.CustomLogger;
import server.command.Command;
import server.command.CommandFactory;
import server.service.services.RoomManager;
import server.thread.ClientHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MultiplayerCommand implements Command {
    private static final Logger logger = CustomLogger.getLogger(MultiplayerCommand.class.getName());
    private static final String COMMAND_NAME = "multiplayer";
    private static final int parametersAmount = 2;
    @Override
    public void execute(String[] args, ClientHandler clientHandler) {
        if ((args.length != parametersAmount - 1 && args[0].equals("leave")) ||
                (args.length != parametersAmount && !args[0].equals("leave"))) {
            clientHandler.getOutput().println("Ayuda: " + CommandFactory.getCommandSymbol() +
                    COMMAND_NAME + " <create|join|leave> [nombreSala]");
            return;
        }

        if (clientHandler.getCurrentUser() == null) {
            clientHandler.getOutput().println("Debes iniciar sesión antes de jugar.");
            return;
        }

        if (clientHandler.hasActiveSingleGame()) {
            clientHandler.getOutput().println("Ya estas en una partida individual.");
            return;
        }

        logger.log(Level.INFO, "Ejecutando comando " + CommandFactory.getCommandSymbol() + "{0} por " + clientHandler.getSocketAddress(), COMMAND_NAME);

        RoomManager roomManager = clientHandler.getServiceRegister().getService(RoomManager.class);
        String action = args[0];

        switch (action) {
            case "create" -> roomManager.createRoom(args[1], clientHandler);
            case "join" -> roomManager.joinRoom(args[1], clientHandler);
            case "leave" -> roomManager.leaveRoom(clientHandler, false);
            default -> clientHandler.getOutput().println("Ayuda: " + CommandFactory.getCommandSymbol() +
                    COMMAND_NAME + " <create|join|leave> [nombreSala]");
        }
    }

    public static String getCommandName() {
        return COMMAND_NAME;
    }
}
