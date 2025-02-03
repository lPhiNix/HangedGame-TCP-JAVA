package common.game;

import common.model.Proverb;
import common.model.User;
import server.service.ServiceRegister;
import server.service.services.ProverbManager;
import server.service.services.UserManager;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class HangedGame {
    private final ProverbManager proverbManager;
    private final ScoreManager scoreManager;
    private final PrintWriter output;
    private Proverb proverb;
    private boolean gameOver = false;

    public HangedGame(PrintWriter output, User user, ServiceRegister serviceRegister) {
        this.proverbManager = (ProverbManager) serviceRegister.getService(ProverbManager.class);
        this.output = output;

        UserManager userManager = (UserManager) serviceRegister.getService(UserManager.class);
        this.scoreManager = new ScoreManager(userManager, user, output);
    }

    public void startGame() throws IOException {
        output.println("Iniciando una nueva partida...");
        proverb = proverbManager.createProverb(new Random().nextInt(proverbManager.getProverbs().size()));
        output.println("La frase oculta es: " + proverb);
        scoreManager.resetTries();
    }

    public boolean guessConsonant(char consonant) {
        scoreManager.incrementTries();
        boolean correct = proverb.guessConsonant(consonant);
        if (correct) {
            output.println("¡Correcto!");
        } else {
            output.println("Incorrecto.");
        }
        output.println(proverb);
        return correct;
    }

    public boolean guessVowel(char vowel) {
        scoreManager.incrementTries();
        boolean correct = proverb.guessVowel(vowel);
        if (correct) {
            output.println("¡Correcto!");
        } else {
            output.println("Incorrecto.");
        }
        output.println(proverb);
        return correct;
    }

    public void resolveProverb(String phrase) {
        if (proverb.resolveProverb(phrase)) {
            output.println("¡Felicidades! Has resuelto el proverbio.");
            scoreManager.addScore();
            scoreManager.printFinalScore(true);
        } else {
            scoreManager.printFinalScore(false);
        }
        gameOver = true;
    }

    public boolean isGameOver() {
        return gameOver;
    }
}
