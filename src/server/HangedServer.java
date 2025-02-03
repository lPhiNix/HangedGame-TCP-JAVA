package server;

import common.logger.CustomLogger;
import server.service.ClientHandler;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HangedServer implements Server {
    private static final Logger logger = CustomLogger.getLogger(HangedServer.class.getName());
    private final int port;
    private final int maxUsers;
    private final ExecutorService threadPool;

    public HangedServer(int port, int maxUsers) {
        this.port = port;
        this.maxUsers = maxUsers;

        threadPool = Executors.newFixedThreadPool(maxUsers);
    }

    @Override
    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.log(Level.INFO, "Servidor escuchando en el puerto: " + port);

            while (true) {
                if (Thread.activeCount() > maxUsers) {
                    logger.log(Level.WARNING, "Máximo de conexiones alcanzado: {0}. Esperando para aceptar nuevas conexiones.", maxUsers);
                    continue;
                }

                Socket clientSocket = serverSocket.accept();
                logger.log(Level.INFO, "Nueva conexión aceptada desde: {0}", clientSocket.getInetAddress());
                threadPool.submit(new ClientHandler(clientSocket)); // Crear un nuevo hilo para cada cliente
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error al iniciar el servidor: {0}", e.getMessage());
        }
    }
}
