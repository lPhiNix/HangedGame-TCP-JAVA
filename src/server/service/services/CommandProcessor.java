package server.service.services;

import server.command.Command;
import server.command.CommandFactory;
import server.service.Service;
import server.thread.ClientHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandProcessor implements Service {
    private static final Logger logger = Logger.getLogger(CommandProcessor.class.getName());
    private final CommandFactory commandFactory;

    public CommandProcessor() {
        this.commandFactory = new CommandFactory();
    }

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

    private void executeCommand(String commandName, String[] commandParameters, ClientHandler clientHandler) throws Exception {
        Command userCommand = commandFactory.createCommand(commandName);
        if (userCommand != null) {
            userCommand.execute(commandParameters, clientHandler);
        } else {
            logger.log(Level.WARNING, "Comando no reconocido insertado por " + clientHandler.getFormatedUser());
            clientHandler.getOutput().println("Comando no reconocido.");
        }
    }

    private static String[] getCommandParameters(String[] array) {
        if (array.length <= 1) {
            return new String[0];
        }
        return java.util.Arrays.copyOfRange(array, 1, array.length);
    }
}
