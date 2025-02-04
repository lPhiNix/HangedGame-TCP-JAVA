package server.command.commands;

import server.command.Command;
import server.command.CommandFactory;
import common.logger.CustomLogger;
import server.service.services.CommandProcessor;
import server.thread.ClientHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Comando para adivinar consonantes en una partida activa.
 * <p>
 * Este comando permite a los jugadores adivinar una consonante en partidas individuales
 * y multijugador activas.
 * </p>
 *
 * @see Command
 * @see CommandFactory
 * @see CommandProcessor
 */
public class ConsonantCommand implements Command {
    private static final Logger logger = CustomLogger.getLogger(ConsonantCommand.class.getName());
    private static final String COMMAND_NAME = "consonant"; // Nombre del comando.
    private static final int parametersAmount = 1; // Cantidad exacta de parámetros esperados (una consonante)

    /**
     * Ejecuta el comando de consonante, permitiendo a un jugador adivinar una letra en su partida activa.
     *
     * @param args          Argumentos proporcionados por el cliente (una sola consonante).
     * @param clientHandler Cliente que ejecuta el comando.
     */
    @Override
    public void execute(String[] args, ClientHandler clientHandler) {
        // Validar la cantidad de argumentos
        if (args.length != parametersAmount) {
            clientHandler.getOutput().println("Ayuda: " + CommandFactory.getCommandSymbol() +
                    COMMAND_NAME + " <consonante>");
            return;
        }

        logger.log(Level.INFO, "Ejecutando comando " + CommandFactory.getCommandSymbol() + "{0} por " + clientHandler.getSocketAddress(), COMMAND_NAME);

        // Verificar si el jugador tiene una partida activa
        if (!clientHandler.hasActiveSingleGame() && !clientHandler.hasActiveMultiplayerGame()) {
            clientHandler.getOutput().println("No tienes una partida activa.");
            return;
        }

        // Obtener la consonante introducida por el usuario
        char consonant = args[0].charAt(0);

        // Si el jugador está en una partida individual, se procesa la consonante
        if (clientHandler.hasActiveSingleGame() && !clientHandler.hasActiveMultiplayerGame()) {
            clientHandler.getGameSession().guessConsonant(consonant);
            return;
        }

        // Si el jugador está en una partida multijugador, se notifica a la sala
        if (clientHandler.hasActiveMultiplayerGame()) {
            clientHandler.getCurrentRoom().playerGuessConsonant(consonant, clientHandler);
        }
    }

    /**
     * Obtiene el nombre del comando.
     *
     * @return Nombre del comando "consonant".
     */
    public static String getCommandName() {
        return COMMAND_NAME;
    }
}
