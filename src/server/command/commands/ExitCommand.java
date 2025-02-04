package server.command.commands;

import server.command.Command;
import server.command.CommandFactory;
import common.logger.CustomLogger;
import server.thread.ClientHandler;
import server.service.services.*;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Comando para finalizar la sesión del cliente.
 * <p>
 * Este comando se encarga de detener la ejecución del cliente, cerrando su conexión con el servidor
 * de manera segura. El comando es ejecutado cuando el cliente desea salir de la aplicación.
 * </p>
 *
 * @see Command
 * @see CommandFactory
 * @see CommandProcessor
 */
public class ExitCommand implements Command {
    private static final Logger logger = CustomLogger.getLogger(ExitCommand.class.getName());
    private static final String COMMAND_NAME = "exit"; // Nombre del comando.
    private static final int parametersAmount = 0;  // Cantidad de parámetros esperados para este comando.

    /**
     * Ejecuta el comando de salida.
     * <p>
     * Este método verifica que el cliente no haya proporcionado parámetros adicionales. Si la cantidad
     * de parámetros es incorrecta, muestra la ayuda para el comando. Si es correcta, el comando termina
     * la ejecución del cliente y cierra la conexión.
     * </p>
     *
     * @param args          Argumentos proporcionados por el cliente para el comando.
     * @param clientHandler El cliente que ejecuta el comando.
     */
    @Override
    public void execute(String[] args, ClientHandler clientHandler) {
        if (args.length != parametersAmount) {
            clientHandler.getOutput().println("Ayuda: " + CommandFactory.getCommandSymbol() +
                    COMMAND_NAME);
            return;
        }

        logger.log(Level.INFO, "Ejecutando comando " + CommandFactory.getCommandSymbol() + "{0} por " + clientHandler.getSocketAddress(), COMMAND_NAME);

        // Detener la ejecución del cliente y cerrar la conexión
        clientHandler.setRunning(false);
    }

    /**
     * Obtiene el nombre del comando.
     * <p>
     * Este método devuelve el nombre del comando, utilizado para su registro y ejecución.
     * </p>
     *
     * @return Nombre del comando "exit".
     */
    public static String getCommandName() {
        return COMMAND_NAME;
    }
}
