package server.command.commands;

import common.logger.CustomLogger;
import server.command.Command;
import server.command.CommandFactory;
import server.service.services.CommandProcessor;
import server.service.services.RoomManager;
import server.thread.ClientHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Comando para gestionar partidas multijugador.
 * <p>
 * Permite a los usuarios autenticados crear, unirse o salir de salas de juego multijugador.
 * </p>
 *
 * @see Command
 * @see CommandFactory
 * @see CommandProcessor
 */
public class MultiplayerCommand implements Command {
    private static final Logger logger = CustomLogger.getLogger(MultiplayerCommand.class.getName());
    private static final String COMMAND_NAME = "multiplayer"; // Nombre del comando.
    private static final int parametersAmount = 2; // Cantidad esperada de parámetros en el comando (mínimo 2 para "create" y "join")

    /**
     * Ejecuta el comando de multijugador, permitiendo crear, unirse o salir de una sala.
     *
     * @param args          Argumentos proporcionados por el cliente.
     * @param clientHandler Cliente que ejecuta el comando.
     */
    @Override
    public void execute(String[] args, ClientHandler clientHandler) {
        // Validar la cantidad de argumentos y su estructura según la acción solicitada
        if (args.length < parametersAmount - 1 ||
                (args.length != parametersAmount - 1 && args[0].equals("leave")) ||
                (args.length != parametersAmount && !args[0].equals("leave"))) {

            clientHandler.getOutput().println("Ayuda: " + CommandFactory.getCommandSymbol() +
                    COMMAND_NAME + " <create|join|leave> [nombreSala]");
            return;
        }

        // Verificar si el usuario ha iniciado sesión
        if (clientHandler.getCurrentUser() == null) {
            clientHandler.getOutput().println("Debes iniciar sesión antes de jugar.");
            return;
        }

        // Verificar si el usuario ya está en una partida individual
        if (clientHandler.hasActiveSingleGame()) {
            clientHandler.getOutput().println("Ya estás en una partida individual.");
            return;
        }

        logger.log(Level.INFO, "Ejecutando comando " + CommandFactory.getCommandSymbol() + "{0} por " + clientHandler.getSocketAddress(), COMMAND_NAME);

        // Obtener el servicio de gestión de salas
        RoomManager roomManager = clientHandler.getServiceRegister().getService(RoomManager.class);
        String action = args[0];

        // Ejecutar la acción correspondiente según el primer argumento
        switch (action) {
            case "create" -> roomManager.createRoom(args[1], clientHandler);
            case "join" -> roomManager.joinRoom(args[1], clientHandler);
            case "leave" -> roomManager.leaveRoom(clientHandler, false);
            default -> clientHandler.getOutput().println("Ayuda: " + CommandFactory.getCommandSymbol() +
                    COMMAND_NAME + " <create|join|leave> [nombreSala]");
        }
    }

    /**
     * Obtiene el nombre del comando.
     *
     * @return Nombre del comando "multiplayer".
     */
    public static String getCommandName() {
        return COMMAND_NAME;
    }
}
