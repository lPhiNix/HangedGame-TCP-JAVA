package client;

import server.command.CommandFactory;
import server.command.commands.ExitCommand;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Clase abstracta que representa al cliente en la aplicación del juego de la ruleta.
 * <p>
 * Establece la conexión con el servidor, maneja la entrada y salida de datos y
 * escucha los mensajes del servidor.
 * </p>
 */
public class Client {
    // Constantes para la conexión al servidor
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 2050;

    // Atributos para manejar la conexión y comunicación con el servidor
    private Socket socket;
    private BufferedReader inputReader;
    private PrintWriter outputWriter;
    private volatile boolean running = true; // Control de ejecución del cliente

    // Colores ANSI para mejorar la visibilidad en la terminal
    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m";
    private static final String CYAN = "\u001B[36m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED = "\u001B[31m";

    /**
     * Constructor que establece la conexión con el servidor y prepara los flujos de entrada y salida.
     */
    public Client() {
        try {
            socket = new Socket(SERVER_HOST, SERVER_PORT); // Conexión al servidor
            inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Lee mensajes del servidor
            outputWriter = new PrintWriter(socket.getOutputStream(), true); // Envía comandos al servidor
        } catch (IOException e) {
            System.out.println(RED + "\n❌ Error al conectar con el servidor." + RESET);
        }
    }

    /**
     * Inicia la ejecución del cliente. Establece la conexión, muestra el mensaje de bienvenida
     * y crea un hilo para escuchar mensajes del servidor mientras se maneja la entrada del usuario.
     */
    public void start() {
        if (socket == null) {
            return; // No se pudo conectar al servidor
        }

        System.out.println(GREEN + "🎰 Bienvenido al juego de la ruleta." + RESET);
        System.out.println(CYAN + "📝 Usa \"/help\" para obtener información sobre los comandos disponibles." + RESET);

        // Hilo para escuchar los mensajes del servidor mientras el usuario interactúa
        Thread inputThread = new Thread(this::listenToServer);
        inputThread.start();

        // Manejo de la entrada del usuario en el hilo principal
        try {
            handleUserInput();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Escucha los mensajes enviados por el servidor y los imprime en la consola.
     * Este hilo se ejecuta mientras el cliente está en funcionamiento.
     */
    private void listenToServer() {
        try {
            String message;
            while (running && (message = inputReader.readLine()) != null) {
                printServerMessage(message); // Imprime el mensaje del servidor
            }
        } catch (IOException e) {
            System.out.println(RED + "\n❌ Conexión con el servidor perdida." + RESET);
        }
    }

    /**
     * Maneja la entrada del usuario desde la consola.
     * Envía comandos al servidor o realiza la desconexión si se recibe el comando adecuado.
     */
    private void handleUserInput() throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        String exitCommandLine = CommandFactory.getCommandSymbol() + ExitCommand.getCommandName(); // Comando de salida
        String commandLine;

        while (running) {
            Thread.sleep(10); // Pausa para evitar bloqueos
            System.out.print(CYAN + "📝 > " + RESET); // Muestra el prompt para la entrada del usuario
            commandLine = scanner.nextLine().trim(); // Lee la línea de entrada

            // Si se ingresa el comando de salida, cierra la conexión
            if (commandLine.equals(exitCommandLine)) {
                shutdown();
                break;
            }

            sendCommand(commandLine); // Envía el comando al servidor
        }
    }

    /**
     * Envía un comando al servidor para su procesamiento.
     *
     * @param command El comando a enviar al servidor.
     */
    private void sendCommand(String command) {
        outputWriter.println(command); // Envia el comando al servidor
    }

    /**
     * Imprime los mensajes del servidor en la consola con un formato especial.
     *
     * @param message El mensaje recibido del servidor.
     */
    private void printServerMessage(String message) {
        System.out.println("\n" + YELLOW + "📢 > " + message + RESET); // Imprime el mensaje del servidor
    }

    /**
     * Cierra la conexión con el servidor de forma segura.
     * Marca el cliente como no en ejecución y cierra los flujos y el socket.
     */
    private void shutdown() {
        running = false; // Detiene la ejecución del cliente
        try {
            if (socket != null) {
                socket.close(); // Cierra el socket de la conexión
            }
            System.out.println(GREEN + "🔌 Conexión cerrada. ¡Hasta la próxima!" + RESET);
        } catch (IOException e) {
            System.out.println(RED + "\n❌ Error al cerrar la conexión." + RESET);
        }
    }
}
