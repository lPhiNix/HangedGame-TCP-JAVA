package server.command.commands;

import server.command.Command;
import server.command.CommandFactory;
import common.logger.CustomLogger;
import server.service.services.CommandProcessor;
import server.thread.ClientHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Comando para iniciar una partida en el modo un jugador.
 * <p>
 * Permite a un usuario autenticado comenzar una partida individual siempre que no tenga una partida multijugador activa.
 * </p>
 *
 * @see Command
 * @see CommandFactory
 * @see CommandProcessor
 */
public class SinglePlayerCommand implements Command {
    private static final Logger logger = CustomLogger.getLogger(SinglePlayerCommand.class.getName());
    private static final String COMMAND_NAME = "singleplayer"; // Nombre del comando.
    private static final int parametersAmount = 0; // Cantidad de parámetros esperados para este comando.

    /**
     * Ejecuta el comando para iniciar una partida en solitario.
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
            clientHandler.getOutput().println("Debes iniciar sesión antes de jugar.");
            return;
        }

        // Verificar si el usuario tiene una partida multijugador activa
        if (clientHandler.hasActiveMultiplayerGame()) {
            clientHandler.getOutput().println("Sal de la sala para jugar una partida individual.");
            return;
        }

        // Iniciar la partida en solitario
        clientHandler.startGame();
    }

    /**
     * Obtiene el nombre del comando.
     *
     * @return Nombre del comando "singleplayer".
     */
    public static String getCommandName() {
        return COMMAND_NAME;
    }
}
