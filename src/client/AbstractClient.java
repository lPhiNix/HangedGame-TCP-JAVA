package client;

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
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            inputThread.start();

            // Comando del cliente
            while (true) {
                System.out.print("> ");
                String commandLine = scanner.nextLine().trim();
                outputWriter.println(commandLine);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}