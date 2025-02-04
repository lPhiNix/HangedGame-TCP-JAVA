package common.game.score;

import common.model.User;
import server.service.services.UserManager;

import java.io.PrintWriter;

/**
 * Gestiona el sistema de puntuación de un usuario durante una partida.
 * <p>
 * Se encarga de registrar los intentos, calcular la puntuación final y actualizar
 * las estadísticas del usuario en el sistema.
 * </p>
 */
public class ScoreManager {
    private final UserManager userManager; // Administrador de usuarios para actualizar estadísticas.
    private final User currentUser; // Usuario actual cuyo puntaje se está gestionando.
    private final PrintWriter output; // Salida de texto donde se imprimen los mensajes de puntuación.
    private int tries; // Contador de intentos realizados en la partida actual.

    /**
     * Crea un gestor de puntuaciones para un usuario en la partida.
     *
     * @param userManager  Administrador de usuarios para actualizar estadísticas.
     * @param currentUser  Usuario actual que juega la partida.
     * @param output       Salida de texto donde se imprimen los mensajes.
     */
    public ScoreManager(UserManager userManager, User currentUser, PrintWriter output) {
        this.userManager = userManager;
        this.currentUser = currentUser;
        this.output = output;
        this.tries = 0;
    }

    /**
     * Incrementa el número de intentos realizados.
     */
    public void incrementTries() {
        tries++;
    }

    /**
     * Calcula y agrega la puntuación al usuario en función de los intentos realizados.
     */
    public void addScore() {
        Score score = Score.fromTries(tries);
        currentUser.addScore(score.getScoreAmount());
    }

    /**
     * Obtiene la cantidad de intentos realizados en la partida actual.
     *
     * @return Número de intentos.
     */
    public int getTries() {
        return tries;
    }

    /**
     * Restablece el contador de intentos a cero.
     */
    public void resetTries() {
        tries = 0;
    }

    /**
     * Imprime el puntaje final del usuario y actualiza sus estadísticas en el sistema.
     *
     * @param hasWin Indica si el usuario ha ganado la partida.
     */
    public void printFinalScore(boolean hasWin) {
        if (hasWin) {
            Score score = Score.fromTries(tries);
            output.println("Has logrado un puntaje de: " + score.getScoreAmount());
            output.println("Puntuación final: " + currentUser.getScore());

            currentUser.win(1);
        } else {
            output.println("Respuesta incorrecta. Fin del juego.");
            currentUser.defeat(1);
        }

        userManager.updateStatisticsUser(currentUser);
    }
}
