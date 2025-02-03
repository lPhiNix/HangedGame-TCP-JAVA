package client;

import common.command.CommandFactory;
import common.command.commands.ExitCommand;
import common.model.User;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class AbstractClient implements Client {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 2050;
    private Socket socket;
    private BufferedReader inputReader;
    private PrintWriter outputWriter;
    private User currentUser;

    public AbstractClient() {
        try {
            // Conectar al servidor
            socket = new Socket(SERVER_HOST, SERVER_PORT);
            inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outputWriter = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start() throws IOException {
        try {
            Scanner scanner = new Scanner(System.in);

            // Autenticación
            System.out.println("Bienvenido al juego de la ruleta.");

            Thread inputThread = new Thread(() -> {
                String line = "";
                while (line != null) {
                    try {
                        line = inputReader.readLine();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    if (line != null) {
                        System.out.println(line);
                    }
                }
            });

            inputThread.start();

            String exitCommandLine = CommandFactory.getCommandSymbol() + ExitCommand.getCommandName();
            String commandLine = "";

            while (!commandLine.equals(exitCommandLine)) {
                System.out.print("> ");
                commandLine = scanner.nextLine().trim();
                outputWriter.println(commandLine);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}