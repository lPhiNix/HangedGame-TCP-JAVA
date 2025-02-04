package server.command.commands;

import server.command.Command;
import server.command.CommandFactory;
import common.logger.CustomLogger;
import common.model.User;
import server.service.services.CommandProcessor;
import server.service.services.UserManager;
import server.thread.ClientHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Comando para registrar un nuevo usuario en el servidor.
 * <p>
 * Este comando permite a un cliente registrarse en el servidor proporcionando un nombre de usuario
 * y una contraseña. Si el nombre de usuario ya existe, se le informa al cliente; si el registro
 * es exitoso, se le notifica al cliente.
 * </p>
 *
 * @see Command
 * @see CommandFactory
 * @see CommandProcessor
 */
public class RegisterCommand implements Command {
    private static final Logger logger = CustomLogger.getLogger(RegisterCommand.class.getName());
    private static final String COMMAND_NAME = "register"; // Nombre del comando.
    private static final int parametersAmount = 2;  // Cantidad de parámetros esperados para este comando (nombre de usuario y contraseña)

    /**
     * Ejecuta el comando de registro de usuario.
     * <p>
     * Este método verifica que el cliente haya proporcionado los parámetros correctos. Si el cliente
     * proporciona el nombre de usuario y la contraseña, se intenta registrar al usuario. Si el registro
     * es exitoso, se notifica al cliente.
     * </p>
     *
     * @param args          Argumentos proporcionados por el cliente para el comando.
     * @param clientHandler El cliente que ejecuta el comando.
     */
    @Override
    public void execute(String[] args, ClientHandler clientHandler) {
        // Verificar que la cantidad de parámetros sea la esperada (2)
        if (args.length != parametersAmount) {
            logger.log(Level.INFO, "Sintaxis incorrecta de /{0} command", COMMAND_NAME);
            clientHandler.getOutput().println("Ayuda: " + CommandFactory.getCommandSymbol() +
                    COMMAND_NAME + " <nombre> <contraseña>");
            return;
        }

        logger.log(Level.INFO, "Ejecutando comando " + CommandFactory.getCommandSymbol() + "{0} por " + clientHandler.getSocketAddress(), COMMAND_NAME);

        // Obtener el nombre de usuario y la contraseña
        String username = args[0];
        String password = args[1];

        // Obtener el gestor de usuarios del servicio registrado
        UserManager manager = clientHandler.getServiceRegister().getService(UserManager.class);

        // Intentar autenticar al usuario
        User user = manager.authenticate(username, password);

        // Si el usuario no existe, intentar registrarlo
        if (user == null && manager.registerUser(args[0], args[1])) {
            clientHandler.getOutput().println("Ususario registrado con exito!");
            logger.log(Level.INFO, clientHandler.getFormatedUser() + ": ususario registrado con exito!");
        } else {
            // Si el usuario ya existe
            clientHandler.getOutput().println("El usuario introducido ya existe!");
            logger.log(Level.INFO, clientHandler.getFormatedUser() + ": el usuario introducido ya existe!");
        }
    }

    /**
     * Obtiene el nombre del comando.
     * <p>
     * Este método devuelve el nombre del comando, utilizado para su registro y ejecución.
     * </p>
     *
     * @return Nombre del comando "register".
     */
    public static String getCommnadName() {
        return COMMAND_NAME;
    }
}
