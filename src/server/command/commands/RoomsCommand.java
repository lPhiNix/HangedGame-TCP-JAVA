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
 * Comando que muestra las salas activas en el servidor.
 * <p>
 * Permite a un usuario autenticado ver una lista de todas las salas de juego activas.
 * </p>
 *
 * @see Command
 * @see CommandFactory
 * @see CommandProcessor
 */
public class RoomsCommand implements Command {
    private static final Logger logger = CustomLogger.getLogger(RoomsCommand.class.getName());
    private static final String COMMAND_NAME = "rooms"; // Nombre del comando "rooms"
    private static final int parametersAmount = 0; // Cantidad de parámetros esperados para este comando.

    /**
     * Ejecuta el comando para mostrar las salas activas en el servidor.
     *
     * @param args          Argumentos proporcionados por el cliente (no se esperan argumentos).
     * @param clientHandler Cliente que ejecuta el comando.
     */
    @Override
    public void execute(String[] args, ClientHandler clientHandler) {
        // Verificar que la cantidad de parámetros sea la esperada (0)
        if (args.length != parametersAmount) {
            clientHandler.getOutput().println("Ayuda: " + CommandFactory.getCommandSymbol() +
                    COMMAND_NAME);
            return;
        }

        logger.log(Level.INFO, "Ejecutando comando " + CommandFactory.getCommandSymbol() + "{0} por " + clientHandler.getSocketAddress(), COMMAND_NAME);

        // Verificar si el usuario ha iniciado sesión antes de ejecutar el comando
        if (clientHandler.getCurrentUser() == null) {
            clientHandler.getOutput().println("Inicia sesión antes para utilizar esta función!");
            return;
        }

        // Obtener el servicio RoomManager para listar las salas activas
        RoomManager roomManager = clientHandler.getServiceRegister().getService(RoomManager.class);
        roomManager.printAllActiveRooms(clientHandler);
    }

    /**
     * Obtiene el nombre del comando.
     *
     * @return Nombre del comando "rooms".
     */
    public static String getCommandName() {
        return COMMAND_NAME;
    }
}
