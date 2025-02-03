package common.game;

import common.model.User;
import server.service.UserManager;

import java.io.PrintWriter;

public class ScoreManager {
    private final User user;
    private final PrintWriter output;
    private int tries;

    public ScoreManager(User user, PrintWriter output) {
        this.user = user;
        this.output = output;
        this.tries = 0;
    }

    public void incrementTries() {
        tries++;
    }

    public void addScore() {
        Score score = Score.fromTries(tries);
        user.addScore(score.getScoreAmount());
    }

    public int getTries() {
        return tries;
    }

    public void resetTries() {
        tries = 0;
    }

    public void printFinalScore(boolean hasWin) {
        // Imprimimos el puntaje final del jugador

        if (hasWin) {
            Score score = Score.fromTries(tries);
            output.println("Has logrado un puntaje de: " + score.getScoreAmount());

            output.println("Puntuación final: " + user.getScore());

            user.win(1);
        } else {
            output.println("Respuesta incorrecta. Fin del juego.");

            user.defeat(1);
        }

        UserManager.getInstance().updateStatatisticsUser(user);
    }
}

