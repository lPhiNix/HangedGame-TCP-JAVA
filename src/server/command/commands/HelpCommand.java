package server.command.commands;

import server.command.Command;
import server.command.CommandFactory;
import common.logger.CustomLogger;
import server.service.services.CommandProcessor;
import server.thread.ClientHandler;

import java.util.logging.Logger;

/**
 * Comando para mostrar la lista de comandos disponibles junto con su descripción.
 *
 * @see Command
 * @see CommandFactory
 * @see CommandProcessor
 */
public class HelpCommand implements Command {
    private static final String COMMAND_NAME = "help"; // Nombre del comando.
    private static final int parametersAmount = 0; // Cantidad esperada de parámetros.

    /**
     * Ejecuta el comando "help", mostrando una lista de comandos disponibles.
     *
     * @param args           Argumentos del comando.
     * @param clientHandler  Cliente que ejecuta el comando.
     */
    @Override
    public void execute(String[] args, ClientHandler clientHandler) {
        if (args.length != parametersAmount) {
            clientHandler.getOutput().println("Uso: " + CommandFactory.getCommandSymbol() + COMMAND_NAME);
            return;
        }

        String separator = "============================================================================================================";
        clientHandler.getOutput().println(separator);
        clientHandler.getOutput().println("                                           LISTA DE COMANDOS");
        clientHandler.getOutput().println(separator);

        // Lista de comandos disponibles con su descripción
        clientHandler.getOutput().println(formatCommand("register <nombre> <constraseña>", "Permite a un cliente registrarse en el servidor."));
        clientHandler.getOutput().println(formatCommand("login <nombre> <contraseña>", "Permite iniciar sesión con un usuario anteriormente registrado"));
        clientHandler.getOutput().println(formatCommand("user", "Muestra la información del usuario actual."));
        clientHandler.getOutput().println(formatCommand("rooms", "Lista las salas activas en el servidor."));
        clientHandler.getOutput().println(formatCommand("singleplayer", "Inicia una partida en solitario."));
        clientHandler.getOutput().println(formatCommand("multiplayer <create|join|leave> [nombreSala]", "Gestiona partidas multijugador."));
        clientHandler.getOutput().println(formatCommand("consonant <letra>", "Adivina una consonante en la partida actual."));
        clientHandler.getOutput().println(formatCommand("vowel <letra>", "Compra y adivina una vocal en la partida actual."));
        clientHandler.getOutput().println(formatCommand("solve <proverbio...>", "Intenta resolver el refrán en la partida actual."));

        clientHandler.getOutput().println(separator);
    }

    /**
     * Formatea un comando y su descripción para mejorar la legibilidad en la salida.
     *
     * @param command     Sintaxis del comando.
     * @param description Descripción breve del comando.
     * @return Cadena formateada con el comando y su descripción.
     */
    private String formatCommand(String command, String description) {
        return String.format("%-50s %s", CommandFactory.getCommandSymbol() + command, description);
    }

    /**
     * Obtiene el nombre del comando.
     *
     * @return Nombre del comando "help".
     */
    public static String getCommandName() {
        return COMMAND_NAME;
    }
}
