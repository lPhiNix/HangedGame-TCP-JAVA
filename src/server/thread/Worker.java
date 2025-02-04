package server.thread;

/**
 * Interfaz que define las operaciones básicas de una conexión dentro del servidor.
 * <p>
 * Esta interfaz proporciona un método para cerrar de manera segura la conexión con el cliente.
 * Las clases que implementen esta interfaz deben proporcionar la implementación específica
 * para gestionar la desconexión de un cliente en el servidor.
 * </p>
 *
 * @see AbstractWorker
 */
public interface Worker {
    /**
     * Cierra la conexión con el cliente de forma segura.
     * <p>
     * Este método debe garantizar que todos los recursos asociados a la conexión se liberen
     * correctamente antes de cerrar la conexión.
     * </p>
     */
    void closeConnection();
}
