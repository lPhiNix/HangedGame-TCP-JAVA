package server;

import common.command.Command;
import common.command.CommandFactory;
import common.logger.CustomLogger;
import common.model.User;
import common.util.ProverbManager;
import common.util.UserManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientHandler extends Thread {
    private static final Logger logger = CustomLogger.getLogger(ClientHandler.class.getName());
    private final CommandFactory commandFactory;
    private final ProverbManager proverbManager;
    private final UserManager userManager;
    private final Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private User currentUser;

    public ClientHandler(Socket socket, UserManager userManager, ProverbManager proverbManager) {
        this.socket = socket;
        this.userManager = userManager;
        this.proverbManager = proverbManager;
        this.commandFactory = new CommandFactory();
    }

    @Override
    public void run() {
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);

            handleCommands();

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error de conexión con cliente: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }

    private void handleCommands() throws Exception {
        logger.log(Level.INFO, "Esperando que el cliente ingrese un comando");

        String commandLine;
        while ((commandLine = input.readLine()) != null) {
            processCommand(commandLine);
        }
    }

    private void processCommand(String commandLine) throws Exception {
        logger.log(Level.INFO, "Comando recibido: " + commandLine);

        String[] parsedCommand = parseCommandLine(commandLine);
        if (parsedCommand.length == 0) {
            handleUnknownCommand(commandLine);
            return;
        }

        String commandName = parsedCommand[0];
        String[] commandParameters = getCommandParameters(parsedCommand);

        executeCommand(commandName, commandParameters, commandLine);
    }

    private void executeCommand(String commandName, String[] commandParameters, String commandLine) throws Exception {
        Command userCommand = commandFactory.createCommand(commandName);

        if (userCommand != null) {
            userCommand.execute(commandParameters, this);
        } else {
            handleUnknownCommand(commandLine);
        }
    }

    private void handleUnknownCommand(String commandLine) {
        output.println("Comando no reconocido.");
        logger.log(Level.WARNING, "Comando no reconocido: " + commandLine);
    }

    private String[] parseCommandLine(String commandLine) {
        return commandLine.split("\\s+");
    }

    private static String[] getCommandParameters(String[] array) {
        if (array == null || array.length <= 1) {
            return new String[0];
        }

        return Arrays.copyOfRange(array, 1, array.length);
    }

    private void closeConnection() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
                logger.info("Conexión cerrada para el usuario: " + currentUser.getUsername());
            }
        } catch (IOException e) {
            logger.severe("Error al cerrar conexión: " + e.getMessage());
        }
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public ProverbManager getProverbManager() {
        return proverbManager;
    }

    public PrintWriter getOutput() {
        return output;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
