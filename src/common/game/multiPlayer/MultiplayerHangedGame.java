package common.game.multiPlayer;

import common.game.score.ScoreManager;
import common.model.Proverb;
import common.model.User;
import server.thread.ClientHandler;
import server.service.ServiceRegister;
import server.service.services.ProverbManager;
import server.service.services.UserManager;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

public class MultiplayerHangedGame {
    private final List<ClientHandler> players;
    private final ProverbManager proverbManager;
    private final Proverb proverb;
    private final ScoreManager[] scoreManagers;
    private int currentTurnIndex = 0;
    private boolean gameOver = false;

    public MultiplayerHangedGame(List<ClientHandler> players, ServiceRegister serviceRegister) throws IOException {
        this.players = players;
        this.proverbManager = serviceRegister.getService(ProverbManager.class);

        // Elegir un proverbio aleatorio
        this.proverb = proverbManager.createProverb(new Random().nextInt(proverbManager.getProverbs().size()));

        // Inicializar los ScoreManagers de cada jugador
        UserManager userManager = serviceRegister.getService(UserManager.class);
        this.scoreManagers = new ScoreManager[players.size()];

        for (int i = 0; i < players.size(); i++) {
            User user = players.get(i).getCurrentUser();
            this.scoreManagers[i] = new ScoreManager(userManager, user, players.get(i).getOutput());
        }

        broadcast("La partida ha comenzado. Frase oculta: " + proverb);
        announceTurn();
    }

    public void guessConsonant(char consonant) {
        if (gameOver) return;

        ClientHandler currentPlayer = players.get(currentTurnIndex);
        ScoreManager currentScore = scoreManagers[currentTurnIndex];

        currentScore.incrementTries();
        boolean correct = proverb.guessConsonant(consonant);

        if (correct) {
            broadcast("¡" + currentPlayer.getCurrentUser().getUsername() + " ha acertado la consonante '" + consonant + "'!");
        } else {
            broadcast("La consonante '" + consonant + "' no está en la frase.");
            nextTurn();
        }

        broadcast("Frase actual: " + proverb);
        checkGameOver();
    }

    public void guessVowel(char vowel) {
        if (gameOver) return;

        ClientHandler currentPlayer = players.get(currentTurnIndex);
        ScoreManager currentScore = scoreManagers[currentTurnIndex];

        currentScore.incrementTries();
        boolean correct = proverb.guessVowel(vowel);

        if (correct) {
            broadcast("¡" + currentPlayer.getCurrentUser().getUsername() + " ha acertado la vocal '" + vowel + "'!");
        } else {
            broadcast("La vocal '" + vowel + "' no está en la frase.");
            nextTurn();
        }

        broadcast("Frase actual: " + proverb);
        checkGameOver();
    }

    public void resolveProverb(String phrase) {
        if (gameOver) return;

        ClientHandler currentPlayer = players.get(currentTurnIndex);
        ScoreManager currentScore = scoreManagers[currentTurnIndex];

        if (proverb.resolveProverb(phrase)) {
            broadcast("¡" + currentPlayer.getCurrentUser().getUsername() + " ha resuelto la frase correctamente! Frase: " + proverb.getText());
            currentScore.addScore();
            endGame();
        } else {
            broadcast(currentPlayer.getCurrentUser().getUsername() + " ha fallado en su intento de resolver la frase.");
            nextTurn();
        }
    }

    private void nextTurn() {
        currentTurnIndex = (currentTurnIndex + 1) % players.size();
        announceTurn();
    }

    private void announceTurn() {
        ClientHandler currentPlayer = players.get(currentTurnIndex);
        broadcast("Turno de " + currentPlayer.getCurrentUser().getUsername() + ".");
        currentPlayer.sendMessageBoth(Level.INFO, "Es tu turno. Escribe '/guess <letra>' para adivinar una consonante o vocal, o '/resolve <frase>' para resolver.");
    }

    private void checkGameOver() {
        if (proverb.isRevealed()) {
            broadcast("¡La frase ha sido completada! Frase: " + proverb.getText());
            endGame();
        }
    }

    private void endGame() {
        gameOver = true;
        broadcast("La partida ha terminado.");
    }

    private void broadcast(String message) {
        for (ClientHandler player : players) {
            player.sendMessageBoth(Level.INFO, message);
        }
    }

    public void handlePlayerDisconnect(ClientHandler player) {
        players.remove(player);
        broadcast("El jugador " + player.getCurrentUser().getUsername() + " se ha desconectado.");

        if (players.size() < 2) {
            broadcast("No hay suficientes jugadores para continuar. La partida termina.");
            gameOver = true;
        } else {
            if (players.indexOf(player) == currentTurnIndex) {
                nextTurn();
            }
        }
    }
}
