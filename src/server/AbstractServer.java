package server;

import common.logger.CustomLogger;
import common.util.ProverbManager;
import common.util.UserManager;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AbstractServer implements Server {
    private static final Logger logger = CustomLogger.getLogger(AbstractServer.class.getName());
    private final int port;
    private final int maxUsers;
    private final ExecutorService threadPool;
    private final UserManager userManager;
    private final ProverbManager proverbManager;

    public AbstractServer(int port, int maxUsers, String proverbsFileName) {
        this.port = port;
        this.maxUsers = maxUsers;

        threadPool = Executors.newFixedThreadPool(maxUsers);
        userManager = new UserManager();
        proverbManager = new ProverbManager(proverbsFileName);
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
                threadPool.submit(new ClientHandler(clientSocket, userManager, proverbManager)); // Crear un nuevo hilo para cada cliente
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error al iniciar el servidor: {0}", e.getMessage());
        }
    }
}
