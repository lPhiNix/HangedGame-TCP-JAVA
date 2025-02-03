package common.game;

import common.model.Proverb;
import common.model.User;
import common.util.ProverbManager;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class HangedGame {
    private final ProverbManager proverbManager;
    private final PrintWriter output;
    private final User user;
    private Proverb proverb;
    private boolean gameOver = false;

    public HangedGame(ProverbManager proverbManager, PrintWriter output, User user) {
        this.proverbManager = proverbManager;
        this.output = output;
        this.user = user;
    }

    public void startGame() throws IOException {
        output.println("Iniciando una nueva partida...");

        proverb = proverbManager.createProverb(new Random().nextInt(proverbManager.getProverbs().size()));
        output.println("La frase oculta es: " + proverb);
    }

    public boolean guessConsonant(char consonant) {
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
        if (proverb.getText().equalsIgnoreCase(phrase)) {
            output.println("¡Felicidades! Has resuelto el proverbio.");
            user.addScore(10);
            output.println("Tu puntuación final es: " + user.getScore());
        } else {
            output.println("Respuesta incorrecta. Fin del juego.");
        }
        gameOver = true;
    }

    public boolean isGameOver() {
        return gameOver;
    }
}
