package server.command.commands;

import server.command.Command;
import server.command.CommandFactory;
import common.logger.CustomLogger;
import server.service.services.CommandProcessor;
import server.thread.ClientHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Comando para resolver un proverbio en una partida activa.
 * <p>
 * Este comando permite a los jugadores intentar resolver el refrán en partidas individuales
 * y multijugador. En el modo multijugador, un fallo implica la eliminación del jugador de la partida.
 * </p>
 *
 * @see Command
 * @see CommandFactory
 * @see CommandProcessor
 */
public class SolveCommand implements Command {
    private static final Logger logger = CustomLogger.getLogger(SolveCommand.class.getName());
    private static final String COMMAND_NAME = "solve"; // Nombre del comando.
    private static final int parametersMinimum = 1; // Cantidad mínima de parámetros esperados.

    /**
     * Ejecuta el comando para resolver un refrán en la partida actual.
     *
     * @param args          Argumentos proporcionados por el cliente (la frase completa del refrán).
     * @param clientHandler Cliente que ejecuta el comando.
     */
    @Override
    public void execute(String[] args, ClientHandler clientHandler) {
        // Validar que se introduzca al menos una palabra
        if (args.length < parametersMinimum) {
            clientHandler.getOutput().println("Ayuda: " + CommandFactory.getCommandSymbol() +
                    COMMAND_NAME + " <proverbio...>");
            return;
        }

        logger.log(Level.INFO, "Ejecutando comando " + CommandFactory.getCommandSymbol() + "{0} por " + clientHandler.getSocketAddress(), COMMAND_NAME);

        // Verificar si el jugador tiene una partida activa
        if (!clientHandler.hasActiveSingleGame() && !clientHandler.hasActiveMultiplayerGame()) {
            clientHandler.getOutput().println("No tienes una partida activa.");
            return;
        }

        // Obtener el refrán introducido por el usuario
        String proverb = String.join(" ", args);

        // Si el jugador está en una partida individual, se procesa el intento de resolución
        if (clientHandler.hasActiveSingleGame() && !clientHandler.hasActiveMultiplayerGame()) {
            clientHandler.getGameSession().resolveProverb(proverb);
            return;
        }

        // Si el jugador está en una partida multijugador, se notifica a la sala
        if (clientHandler.hasActiveMultiplayerGame()) {
            clientHandler.getCurrentRoom().playerResolve(proverb, clientHandler);
        }
    }

    /**
     * Obtiene el nombre del comando.
     *
     * @return Nombre del comando "solve".
     */
    public static String getCommandName() {
        return COMMAND_NAME;
    }
}
