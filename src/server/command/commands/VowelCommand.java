package server.command.commands;

import server.command.Command;
import server.command.CommandFactory;
import common.logger.CustomLogger;
import server.service.services.CommandProcessor;
import server.thread.ClientHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Comando para adivinar vocales en una partida activa.
 * <p>
 * Este comando permite a los jugadores adivinar una vocal en partidas individuales
 * y multijugador activas. En el modo multijugador, adivinar vocales tiene un costo en puntos.
 * </p>
 *
 * @see Command
 * @see CommandFactory
 * @see CommandProcessor
 */
public class VowelCommand implements Command {
    private static final Logger logger = CustomLogger.getLogger(VowelCommand.class.getName());
    private static final String COMMAND_NAME = "vowel"; // Nombre del comando.
    private static final int parametersAmount = 1; // Cantidad exacta de parámetros esperados (una vocal)

    /**
     * Ejecuta el comando de vocal, permitiendo a un jugador adivinar una letra en su partida activa.
     *
     * @param args          Argumentos proporcionados por el cliente (una sola vocal).
     * @param clientHandler Cliente que ejecuta el comando.
     */
    @Override
    public void execute(String[] args, ClientHandler clientHandler) {
        // Validar la cantidad de argumentos
        if (args.length != parametersAmount) {
            clientHandler.getOutput().println("Ayuda: " + CommandFactory.getCommandSymbol() +
                    COMMAND_NAME + " <vowel>");
            return;
        }

        logger.log(Level.INFO, "Ejecutando comando " + CommandFactory.getCommandSymbol() + "{0} por " + clientHandler.getSocketAddress(), COMMAND_NAME);

        // Verificar si el jugador tiene una partida activa
        if (!clientHandler.hasActiveSingleGame() && !clientHandler.hasActiveMultiplayerGame()) {
            clientHandler.getOutput().println("No tienes una partida activa.");
            return;
        }

        // Obtener la vocal introducida por el usuario
        char vowel = args[0].charAt(0);

        // Si el jugador está en una partida individual, se procesa la vocal
        if (clientHandler.hasActiveSingleGame() && !clientHandler.hasActiveMultiplayerGame()) {
            clientHandler.getGameSession().guessVowel(vowel);
            return;
        }

        // Si el jugador está en una partida multijugador, se notifica a la sala
        if (clientHandler.hasActiveMultiplayerGame()) {
            clientHandler.getCurrentRoom().playerGuessVowel(vowel, clientHandler);
        }
    }

    /**
     * Obtiene el nombre del comando.
     *
     * @return Nombre del comando "vowel".
     */
    public static String getCommandName() {
        return COMMAND_NAME;
    }
}
