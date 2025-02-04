package server;

import common.logger.CustomLogger;
import server.service.ServiceRegister;
import server.thread.ClientHandler;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementación del servidor TCP para el juego de la ruleta.
 * <p>
 * Se encarga de gestionar conexiones de clientes, manejar múltiples hilos y
 * administrar los servicios centrales del juego.
 * </p>
 *
 * @see Server
 * @see ClientHandler
 * @see ServiceRegister
 */
public class HangedServer implements Server {
    private static final Logger logger = CustomLogger.getLogger(HangedServer.class.getName());
    private final int port; // Puerto en el que el servidor escuchará conexiones entrantes.
    private final int maxUsers; // Número máximo de usuarios simultáneos permitidos.
    private final ExecutorService threadPool; // Pool de hilos que gestiona múltiples clientes simultáneamente.
    private final ServiceRegister serviceRegister; // Registro de servicios disponibles para los clientes.

    /**
     * Crea un nuevo servidor en el puerto especificado con una cantidad máxima de usuarios
     * que se pueden conectar simultaneamente a él.
     *
     * @param port     Puerto en el que el servidor escuchará.
     * @param maxUsers Número máximo de clientes permitidos simultáneamente.
     */
    public HangedServer(int port, int maxUsers) {
        this.port = port;
        this.maxUsers = maxUsers;
        this.serviceRegister = new ServiceRegister();

        // Se crea un pool de hilos con un número fijo de conexiones simultáneas.
        threadPool = Executors.newFixedThreadPool(maxUsers);
    }

    /**
     * Inicia el servidor, permitiendo la conexión de múltiples clientes.
     * <p>
     * El servidor acepta conexiones hasta el máximo permitido y crea un hilo
     * para cada cliente usando {@link ClientHandler}.
     * </p>
     */
    @Override
    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.log(Level.INFO, "Servidor escuchando en el puerto: " + port);

            while (true) {
                // Controla que no se exceda el número máximo de usuarios activos.
                if (Thread.activeCount() > maxUsers) {
                    logger.log(Level.WARNING, "Máximo de conexiones alcanzado: {0}. Esperando para aceptar nuevas conexiones.", maxUsers);
                    continue; // Realiza una nueva iteración para evitar la nueva instanciación de un hilo.
                }

                // Espera y acepta una nueva conexión de cliente.
                Socket clientSocket = serverSocket.accept();
                logger.log(Level.INFO, "Nueva conexión aceptada desde: {0}", clientSocket.getInetAddress());

                // Crea y asigna un nuevo manejador de clientes en un hilo separado.
                threadPool.submit(new ClientHandler(clientSocket, serviceRegister));
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error al iniciar el servidor: {0}", e.getMessage());
        }
    }
}
