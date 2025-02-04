package common.game;

import common.game.Score;
import common.model.User;
import server.service.services.UserManager;

import java.io.PrintWriter;

public class ScoreManager {
    private final UserManager userManager;
    private final User currentUser;
    private final PrintWriter output;
    private int tries;

    public ScoreManager(UserManager userManager, User currentUser, PrintWriter output) {
        this.userManager = userManager;
        this.currentUser = currentUser;
        this.output = output;
        this.tries = 0;
    }

    public void incrementTries() {
        tries++;
    }

    public void addScore() {
        Score score = Score.fromTries(tries);
        currentUser.addScore(score.getScoreAmount());
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

            output.println("Puntuación final: " + currentUser.getScore());

            currentUser.win(1);
        } else {
            output.println("Respuesta incorrecta. Fin del juego.");

            currentUser.defeat(1);
        }

        userManager.updateStatatisticsUser(currentUser);
    }
}

