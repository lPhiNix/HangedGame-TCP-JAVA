package client;

import common.command.CommandFactory;
import common.command.commands.ExitCommand;
import common.model.User;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class AbstractClient {
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

    public void start() {
        try {
            Scanner scanner = new Scanner(System.in);

            // Autenticación
            System.out.println("Bienvenido al juego de la ruleta.");

            Thread inputThread = new Thread(() -> {
                while (true) {
                    try {
                        System.out.println(inputReader.readLine());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            inputThread.start();

            // Comando del cliente
            String commandLine = "";
            while (!commandLine.equals(CommandFactory.getCommandSymbol() + ExitCommand.getCommandName())) {
                System.out.print("> ");
                commandLine = scanner.nextLine().trim();
                outputWriter.println(commandLine);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}