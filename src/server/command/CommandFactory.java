package server.command;

import server.command.commands.*;
import common.logger.CustomLogger;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Fábrica de comandos que gestiona la creación y registro de comandos en el servidor.
 * <p>
 * Proporciona un sistema para registrar y recuperar comandos basados en su nombre.
 * Cada comando registrado puede ser instanciado y ejecutado en el servidor según se
 * requiera. Los comandos suelen estar asociados a diferentes acciones que los clientes
 * pueden ejecutar durante la partida.
 * </p>
 */
public class CommandFactory {
    private static final Logger logger = CustomLogger.getLogger(CommandFactory.class.getName());
    private static final String COMMAND_SYMBOL = "/"; // Símbolo que indica el inicio de un comando.

    // Mapa de comandos registrados, donde la clave es el nombre del comando y el valor es su clase.
    private final Map<String, Class<? extends Command>> commands;

    /**
     * Constructor que inicializa la fábrica y registra los comandos disponibles.
     * <p>
     * En este constructor se registran todos los comandos necesarios para el
     * funcionamiento del servidor. Esto incluye comandos como el inicio de sesión,
     * la resolución de proverbios, las acciones en el juego, etc.
     * </p>
     */
    public CommandFactory() {
        commands = new HashMap<>();

        // Registro de todos los comandos que utiliza el servidor
        registerCommand(HelpCommand.getCommandName(), HelpCommand.class);
        registerCommand(LoginCommand.getCommandName(), LoginCommand.class);
        registerCommand(RegisterCommand.getCommnadName(), RegisterCommand.class);
        registerCommand(UserCommand.getCommandName(), UserCommand.class);
        registerCommand(SinglePlayerCommand.getCommandName(), SinglePlayerCommand.class);
        registerCommand(MultiplayerCommand.getCommandName(), MultiplayerCommand.class);
        registerCommand(ConsonantCommand.getCommandName(), ConsonantCommand.class);
        registerCommand(VowelCommand.getCommandName(), VowelCommand.class);
        registerCommand(SolveCommand.getCommandName(), SolveCommand.class);
        registerCommand(RoomsCommand.getCommandName(), RoomsCommand.class);
        registerCommand(ExitCommand.getCommandName(), ExitCommand.class);
    }

    /**
     * Registra un comando en la fábrica.
     * <p>
     * Este método permite añadir nuevos comandos al sistema. Cada comando está asociado
     * con un nombre que se utilizará para identificarlo, y una clase que implementa
     * la lógica de ejecución del comando.
     * </p>
     *
     * @param commandName  Nombre del comando.
     * @param commandClass Clase que implementa el comando.
     */
    public void registerCommand(String commandName, Class<? extends Command> commandClass) {
        commands.put(commandName, commandClass);
    }

    /**
     * Obtiene la clase de un comando registrado.
     * <p>
     * Este método permite obtener la clase que corresponde a un comando previamente
     * registrado, lo que facilita la ejecución del mismo.
     * </p>
     *
     * @param command Nombre del comando.
     * @return Clase del comando si está registrado, {@code null} si no existe.
     */
    public Class<? extends Command> getCommand(String command) {
        return commands.get(command);
    }

    /**
     * Crea una instancia de un comando a partir de su nombre.
     * <p>
     * Este método utiliza la reflexión para crear una nueva instancia del comando
     * correspondiente al nombre proporcionado. Si el comando no está registrado o
     * hay un error, se devuelve {@code null}.
     * </p>
     *
     * @param commandName Nombre del comando a crear.
     * @return Instancia del comando si se encuentra registrado, {@code null} si no existe.
     * @throws Exception Si hay un error al instanciar el comando.
     */
    public Command createCommand(String commandName) throws Exception {
        if (commandName.startsWith(COMMAND_SYMBOL)) {
            commandName = commandName.substring(1); // Eliminar el prefijo del comando
        } else {
            logger.log(Level.WARNING, "Comando no reconocido: {0}", commandName);
            return null;
        }

        Class<? extends Command> commandClass = commands.get(commandName);
        if (commandClass != null) {
            return commandClass.getConstructor().newInstance();
        }

        logger.log(Level.WARNING, "Comando no reconocido: {0}", commandName);
        return null;
    }

    /**
     * Obtiene el símbolo de prefijo de los comandos.
     * <p>
     * Este símbolo se utiliza para identificar los comandos dentro de un mensaje
     * enviado por el cliente. El prefijo por defecto es el carácter "/".
     * </p>
     *
     * @return Símbolo de comando.
     */
    public static String getCommandSymbol() {
        return COMMAND_SYMBOL;
    }
}
