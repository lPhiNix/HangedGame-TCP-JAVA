package common.game;

import common.model.Proverb;
import server.service.ServiceRegister;
import server.service.services.ProverbManager;
import java.io.PrintWriter;
import java.util.Random;

public abstract class HangedGame implements Game {
    protected final Proverb proverb;
    protected boolean gameOver = false;
    protected final PrintWriter output;

    protected HangedGame(ServiceRegister serviceRegister, PrintWriter output) {
        ProverbManager proverbManager = serviceRegister.getService(ProverbManager.class);
        this.proverb = proverbManager.createProverb(new Random().nextInt(proverbManager.getProverbs().size()));
        this.output = output;
    }

    public void guessLetter(char letter, boolean isVowel) {
        if (gameOver) return;
        boolean correct = isVowel ? proverb.guessVowel(letter) : proverb.guessConsonant(letter);

        output.println(correct ? "¡Correcto!" : "Incorrecto.");
        output.println("Frase actual: " + proverb);
        checkGameOver();
    }

    public void resolveProverb(String phrase) {
        if (gameOver) return;
        if (proverb.resolveProverb(phrase)) {
            output.println("¡Felicidades! Has resuelto el proverbio.");
            endGame();
        } else {
            output.println("Lo siento, pero esa no es la frase correcta.");
        }
    }

    protected void checkGameOver() {
        if (proverb.isRevealed()) {
            output.println("¡La frase ha sido completada! Frase: " + proverb.getText());
            endGame();
        }
    }

    protected void endGame() {
        gameOver = true;
        output.println("La partida ha terminado.");
    }

    public boolean isGameOver() {
        return gameOver;
    }
}
