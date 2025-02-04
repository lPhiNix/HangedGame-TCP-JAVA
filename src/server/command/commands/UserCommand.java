package server.command.commands;

import server.command.Command;
import server.command.CommandFactory;
import common.logger.CustomLogger;
import common.model.User;
import server.service.services.CommandProcessor;
import server.thread.ClientHandler;

import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Comando que permite al usuario consultar su información.
 * <p>
 * Este comando muestra el nombre de usuario, la puntuación,
 * el número de victorias y derrotas del usuario autenticado.
 * </p>
 *
 * @see Command
 * @see CommandFactory
 * @see CommandProcessor
 */
public class UserCommand implements Command {
    private static final Logger logger = CustomLogger.getLogger(UserCommand.class.getName());
    private static final String COMMAND_NAME = "user"; // Nombre del comando.
    private static final int parametersAmount = 0; // Cantidad de parámetros esperados para este comando.

    /**
     * Ejecuta el comando para mostrar la información del usuario autenticado.
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

        // Obtener el usuario actual del cliente
        User currentUser = clientHandler.getCurrentUser();

        // Verificar si el usuario ha iniciado sesión
        if (currentUser == null) {
            clientHandler.getOutput().println("Inicia sesión antes para utilizar esta función!");
            return;
        }

        logger.log(Level.INFO, "Ejecutando comando " + CommandFactory.getCommandSymbol() + "{0} por " + clientHandler.getSocketAddress(), COMMAND_NAME);

        // Obtener el flujo de salida del cliente y enviar la información del usuario
        PrintWriter output = clientHandler.getOutput();
        output.println("Usuario: " + currentUser.getUsername());
        output.println("Puntuación: " + currentUser.getScore());
        output.println("Victorias: " + currentUser.getWins());
        output.println("Derrotas: " + currentUser.getDefeats());
    }

    /**
     * Obtiene el nombre del comando.
     *
     * @return Nombre del comando "user".
     */
    public static String getCommandName() {
        return COMMAND_NAME;
    }
}
