package server.thread;

import common.logger.CustomLogger;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase abstracta que representa una conexión en el servidor.
 * <p>
 * Se encarga de gestionar la comunicación con un cliente a través de un socket.
 * Cualquier clase que herede de esta debe implementar {@link #listen()} para manejar
 * la lógica específica de la comunicación.
 * </p>
 *
 * @see Worker
 * @see ClientHandler
 */
public abstract class AbstractWorker extends Thread implements Worker {
    protected static final Logger logger = CustomLogger.getLogger(AbstractWorker.class.getName());
    protected final Socket socket; // Socket de comunicación con el cliente.
    protected BufferedReader input; // Lector de entrada del socket para recibir datos del cliente.
    protected PrintWriter output; // Escritor de salida del socket para enviar datos al cliente.
    protected boolean isRunning = true; // Indica si el trabajador sigue en ejecución.

    /**
     * Constructor que inicializa el trabajador con un socket específico.
     *
     * @param socket Socket de conexión con el cliente.
     */
    public AbstractWorker(Socket socket) {
        this.socket = socket;
    }

    /**
     * Método que se ejecuta en un hilo independiente para gestionar la conexión del cliente.
     * <p>
     * Inicializa los flujos de entrada y salida y llama al método {@link #listen()}
     * para manejar la comunicación específica de la implementación concreta.
     * </p>
     */
    @Override
    public void run() {
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);

            listen(); // Llama al método abstracto que debe implementar cada subclase.

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error de conexión con cliente: " + e.getMessage());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al gestionar la entrada del cliente: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }

    /**
     * Método abstracto que cada subclase debe implementar para manejar la lógica de comunicación.
     */
    protected abstract void listen();

    /**
     * Cierra la conexión con el cliente de forma segura.
     */
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

    /**
     * Envía un mensaje tanto al cliente como al log del servidor.
     *
     * @param level   Nivel de log del mensaje.
     * @param message Mensaje a enviar.
     */
    public void sendMessageBoth(Level level, String message) {
        logger.log(level, message);
        output.println(message);
    }

    /**
     * Devuelve la dirección IP del cliente conectado.
     *
     * @return Dirección IP del cliente.
     */
    public InetAddress getSocketAddress() {
        return socket.getInetAddress();
    }

    /**
     * Establece el estado de ejecución del trabajador.
     *
     * @param running `true` para mantener el trabajador activo, `false` para detenerlo.
     */
    public void setRunning(boolean running) {
        this.isRunning = running;
    }

    /**
     * Obtiene el flujo de salida del cliente.
     *
     * @return Objeto {@link PrintWriter} para enviar datos al cliente.
     */
    public PrintWriter getOutput() {
        return output;
    }

    /**
     * Obtiene el flujo de entrada del cliente.
     *
     * @return Objeto {@link BufferedReader} para recibir datos del cliente.
     */
    public BufferedReader getInput() {
        return input;
    }
}

