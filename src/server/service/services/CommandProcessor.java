package server.service.services;

import server.command.Command;
import server.command.CommandFactory;
import server.service.Service;
import server.thread.ClientHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servicio encargado de procesar y ejecutar comandos enviados por los clientes.
 * <p>
 * Utiliza la {@link CommandFactory} para instanciar y ejecutar los comandos dinámicamente.
 * </p>
 */
public class CommandProcessor implements Service {
    private static final Logger logger = Logger.getLogger(CommandProcessor.class.getName());
    private final CommandFactory commandFactory; // Fábrica de comandos encargada de la gestión de instancias de comandos.

    /**
     * Constructor que inicializa el procesador con una nueva fábrica de comandos.
     */
    public CommandProcessor() {
        this.commandFactory = new CommandFactory();
    }

    /**
     * Procesa un comando enviado por el cliente.
     *
     * @param commandLine   Línea de comando recibida.
     * @param clientHandler Cliente que envió el comando.
     * @throws Exception Si ocurre un error al ejecutar el comando.
     */
    public void processCommand(String commandLine, ClientHandler clientHandler) throws Exception {
        logger.log(Level.INFO, "Procesando comando: " + commandLine);

        String[] parsedCommand = commandLine.split("\\s+");
        if (parsedCommand.length == 0) {
            logger.log(Level.WARNING, "Comando no reconocido insertado por " + clientHandler.getFormatedUser());
            clientHandler.getOutput().println("Comando no reconocido.");
            return;
        }

        String commandName = parsedCommand[0];
        String[] commandParameters = getCommandParameters(parsedCommand);

        executeCommand(commandName, commandParameters, clientHandler);
    }

    /**
     * Ejecuta un comando utilizando la fábrica de comandos.
     *
     * @param commandName     Nombre del comando a ejecutar.
     * @param commandParameters Parámetros del comando.
     * @param clientHandler   Cliente que ejecuta el comando.
     * @throws Exception Si ocurre un error al instanciar o ejecutar el comando.
     */
    private void executeCommand(String commandName, String[] commandParameters, ClientHandler clientHandler) throws Exception {
        Command userCommand = commandFactory.createCommand(commandName);
        if (userCommand != null) {
            userCommand.execute(commandParameters, clientHandler);
        } else {
            logger.log(Level.WARNING, "Comando no reconocido insertado por " + clientHandler.getFormatedUser());
            clientHandler.getOutput().println("Comando no reconocido.");
        }
    }

    /**
     * Extrae los parámetros de un comando recibido.
     *
     * @param array Array de palabras del comando.
     * @return Un array con los parámetros del comando.
     */
    private static String[] getCommandParameters(String[] array) {
        if (array.length <= 1) {
            return new String[0];
        }
        return java.util.Arrays.copyOfRange(array, 1, array.length);
    }
}
