package server.thread;

import common.logger.CustomLogger;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractWorker extends Thread implements Worker {
    protected static final Logger logger = CustomLogger.getLogger(AbstractWorker.class.getName());
    protected final Socket socket;
    protected BufferedReader input;
    protected PrintWriter output;
    protected boolean isRunning = true;

    public AbstractWorker(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);

            listen();

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error de conexión con cliente: " + e.getMessage());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al gestionar la entrada del cliente: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }

    protected abstract void listen();

    @Override
    public void closeConnection() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
                logger.info("Conexión cerrada para: " + socket.getInetAddress());
            }
        } catch (IOException e) {
            logger.severe("Error al cerrar conexión: " + e.getMessage());
        } finally {
            this.interrupt();
        }
    }

    public void sendMessageBoth(Level level, String message) {
        logger.log(level, message);
        output.println(message);
    }

    public void setRunning(boolean running) {
        this.isRunning = running;
    }

    public PrintWriter getOutput() {
        return output;
    }

    public BufferedReader getInput() {
        return input;
    }
}
