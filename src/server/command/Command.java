package server.command;

import server.thread.ClientHandler;

/**
 * Interfaz para definir comandos ejecutables por los clientes del servidor.
 * <p>
 * Cada comando debe implementar este método para definir su comportamiento específico.
 * Esta interfaz es utilizada para permitir que el servidor ejecute distintas acciones
 * basadas en los comandos enviados por los clientes.
 * </p>
 */
public interface Command {
    /**
     * Ejecuta el comando con los argumentos proporcionados.
     * <p>
     * Este método es el punto de entrada para ejecutar cualquier lógica asociada con
     * el comando. Los comandos pueden variar, por ejemplo, para interactuar con el
     * juego, manipular el estado de la partida o gestionar la conexión del cliente.
     * </p>
     *
     * @param args Argumentos del comando. Estos pueden ser necesarios para
     *             ejecutar el comando (por ejemplo, un parámetro de entrada
     *             o una opción).
     * @param clientHandler Cliente que ejecuta el comando. Permite interactuar con el
     *                      cliente que envió el comando (por ejemplo, para enviarle
     *                      respuestas o notificaciones).
     */
    void execute(String[] args, ClientHandler clientHandler);
}
