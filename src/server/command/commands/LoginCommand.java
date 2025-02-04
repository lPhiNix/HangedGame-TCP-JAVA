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
 * Comando para iniciar sesión en el servidor.
 * <p>
 * Este comando permite a un cliente iniciar sesión en el servidor proporcionando un nombre de usuario
 * y una contraseña. Si las credenciales son correctas, se establece la sesión del usuario.
 * </p>
 *
 * @see Command
 * @see CommandFactory
 * @see CommandProcessor
 */
public class LoginCommand implements Command {
    private static final Logger logger = CustomLogger.getLogger(LoginCommand.class.getName());
    private static final String COMMAND_NAME = "login"; // Nombre del comando.
    private static final int parametersAmount = 2;  // Cantidad de parámetros esperados para este comando (nombre de usuario y contraseña)

    /**
     * Ejecuta el comando de inicio de sesión.
     * <p>
     * Este método verifica que el cliente haya proporcionado los parámetros correctos. Si el cliente
     * proporciona el nombre de usuario y la contraseña, se intenta autenticar. Si la autenticación es
     * exitosa, se establece al usuario como el actual.
     * </p>
     *
     * @param args          Argumentos proporcionados por el cliente para el comando.
     * @param clientHandler El cliente que ejecuta el comando.
     */
    @Override
    public void execute(String[] args, ClientHandler clientHandler) {
        // Verificar que la cantidad de parámetros sea la esperada (2)
        if (args.length != parametersAmount) {
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

        // Si la autenticación es exitosa
        if (user != null) {
            clientHandler.setCurrentUser(user);
            clientHandler.getOutput().println("Sesión iniciada con exito!");
            logger.log(Level.INFO, clientHandler.getFormatedUser() + ": sesión iniciada con exito!");
        } else {
            // Si el usuario no se encuentra
            clientHandler.getOutput().println("El usuario introducido no existe!");
            logger.log(Level.INFO, clientHandler.getFormatedUser() + ": El usuario introducido no existe!");
        }
    }

    /**
     * Obtiene el nombre del comando.
     * <p>
     * Este método devuelve el nombre del comando, utilizado para su registro y ejecución.
     * </p>
     *
     * @return Nombre del comando "login".
     */
    public static String getCommandName() {
        return COMMAND_NAME;
    }
}
