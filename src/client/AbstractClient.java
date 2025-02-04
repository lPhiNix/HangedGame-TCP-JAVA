package client;

import server.command.CommandFactory;
import server.command.commands.ExitCommand;
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
    private volatile boolean running = true; // Control de ejecución

    // Colores ANSI para mejorar la visibilidad en la terminal
    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m";
    private static final String CYAN = "\u001B[36m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED = "\u001B[31m";

    public AbstractClient() {
        try {
            socket = new Socket(SERVER_HOST, SERVER_PORT);
            inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outputWriter = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.out.println(RED + "❌ Error al conectar con el servidor." + RESET);
            e.printStackTrace();
        }
    }

    @Override
    public void start() {
        if (socket == null) {
            return; // No se pudo conectar al servidor
        }

        System.out.println(GREEN + "🎰 Bienvenido al juego de la ruleta." + RESET);

        // Hilo para escuchar mensajes del servidor
        Thread inputThread = new Thread(this::listenToServer);
        inputThread.start();

        // Manejo de entrada del usuario en el hilo principal
        try {
            handleUserInput();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Escucha los mensajes del servidor y los muestra en la consola sin interferir con la entrada del usuario.
     */
    private void listenToServer() {
        try {
            String message;
            while (running && (message = inputReader.readLine()) != null) {
                printServerMessage(message);
            }
        } catch (IOException e) {
            System.out.println(RED + "❌ Conexión con el servidor perdida." + RESET);
        }
    }

    /**
     * Maneja la entrada del usuario sin romper la consola cuando llegan mensajes del servidor.
     */
    private void handleUserInput() throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        String exitCommandLine = CommandFactory.getCommandSymbol() + ExitCommand.getCommandName();
        String commandLine;

        while (running) {
            Thread.sleep(8);
            System.out.print(CYAN + "📝 > " + RESET);
            commandLine = scanner.nextLine().trim();

            if (commandLine.equals(exitCommandLine)) {
                shutdown();
                break;
            }

            sendCommand(commandLine);
        }
    }

    /**
     * Envía un comando al servidor.
     */
    private void sendCommand(String command) {
        outputWriter.println(command);
    }

    /**
     * Imprime los mensajes del servidor con formato adecuado.
     */
    private void printServerMessage(String message) {
        System.out.println("\n" + YELLOW + "📢 > " + message + RESET);
    }

    /**
     * Cierra la conexión con el servidor de forma segura.
     */
    private void shutdown() {
        running = false;
        try {
            if (socket != null) {
                socket.close();
            }
            System.out.println(GREEN + "🔌 Conexión cerrada. ¡Hasta la próxima!" + RESET);
        } catch (IOException e) {
            System.out.println(RED + "❌ Error al cerrar la conexión." + RESET);
        }
    }
}
