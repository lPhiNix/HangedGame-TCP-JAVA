package server;

/**
 * Interfaz que define la estructura básica de un servidor TCP.
 * Cualquier servidor que implemente esta interfaz debe proporcionar
 * una implementación del método {@link #start()}.
 */
public interface Server {
    /**
     * Inicia el servidor y comienza a escuchar conexiones entrantes.
     */
    void start();
}
