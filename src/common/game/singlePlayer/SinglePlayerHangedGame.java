package common.game.singlePlayer;

import common.game.HangedGame;
import common.game.score.ScoreManager;
import common.model.User;
import server.service.ServiceRegister;
import server.service.services.UserManager;

import java.io.PrintWriter;

public class SinglePlayerHangedGame extends HangedGame {
    private final ScoreManager scoreManager;
    private final PrintWriter output;

    public SinglePlayerHangedGame(PrintWriter output, User user, ServiceRegister serviceRegister) {
        super(serviceRegister);

        this.output = output;
        UserManager userManager = serviceRegister.getService(UserManager.class);
        this.scoreManager = new ScoreManager(userManager, user, output);
    }

    @Override
    public void startGame() {
        output.println("Iniciando una nueva partida...");
        output.println("Frase oculta: " + proverb);
        scoreManager.resetTries();
    }

    public void guessConsonant(char consonant) {
        if (gameOver) return;

        scoreManager.incrementTries();
        boolean correct = proverb.guessConsonant(consonant);

        if (correct) {
            output.println("¡Correcto!");
        } else {
            output.println("Incorrecto.");
        }

        output.println("Frase actual: " + proverb);
    }

    public void guessVowel(char vowel) {
        if (gameOver) return;

        scoreManager.incrementTries();
        boolean correct = proverb.guessVowel(vowel);

        if (correct) {
            output.println("¡Correcto!");
        } else {
            output.println("Incorrecto.");
        }

        output.println("Frase actual: " + proverb);
    }

    public void resolveProverb(String phrase) {
        if (proverb.resolveProverb(phrase)) {
            output.println("¡Felicidades! Has resuelto el proverbio.");
            scoreManager.addScore();
            scoreManager.printFinalScore(true);
        } else {
            output.println("Lo siento, pero esa no es la frase correcta.");
            scoreManager.printFinalScore(false);
        }
        gameOver = true;
        output.println("La partida ha terminado.");
    }

    public boolean isGameOver() {
        return gameOver;
    }
}
