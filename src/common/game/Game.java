package common.game;

/**
 * Interfaz de marcado para cualquier tipo de juego.
 * <p>
 * Todas las clases de juego deben implementar este método para definir su lógica de inicio.
 * Esta interfaz establece un contrato para los juegos que se puedan ejecutar dentro del sistema.
 * </p>
 */
public interface Game {
    /**
     * Inicia el juego.
     * <p>
     * Este método debe contener la lógica necesaria para preparar el juego y comenzar la ejecución.
     * Las clases que implementen esta interfaz deben definir el comportamiento específico para iniciar
     * el juego de acuerdo a sus reglas y configuraciones.
     * </p>
     */
    void startGame();
}
