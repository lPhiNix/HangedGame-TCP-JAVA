package common.command;

import server.service.ClientHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandProcessor {
    private static final Logger logger = Logger.getLogger(CommandProcessor.class.getName());
    private final CommandFactory commandFactory;

    public CommandProcessor() {
        this.commandFactory = new CommandFactory();
    }

    public void processCommand(String commandLine, ClientHandler clientHandler) throws Exception {
        logger.log(Level.INFO, "Procesando comando: " + commandLine);

        String[] parsedCommand = commandLine.split("\\s+");
        if (parsedCommand.length == 0) {
            clientHandler.sendMessageBoth(Level.WARNING, "Comando no reconocido.");
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
            clientHandler.sendMessageBoth(Level.WARNING, "Comando no reconocido.");
        }
    }

    private static String[] getCommandParameters(String[] array) {
        if (array.length <= 1) {
            return new String[0];
        }
        return java.util.Arrays.copyOfRange(array, 1, array.length);
    }
}
