package common.game.multiPlayer;

import common.game.score.ScoreManager;
import common.model.Proverb;
import common.model.User;
import server.service.services.RoomManager;
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
    private final RoomManager roomManager;
    private final Proverb proverb;
    private final ScoreManager[] scoreManagers;
    private int currentTurnIndex = 0;
    private boolean gameOver = false;

    public MultiplayerHangedGame(List<ClientHandler> players, ServiceRegister serviceRegister) throws IOException {
        this.players = players;
        this.proverbManager = serviceRegister.getService(ProverbManager.class);
        this.roomManager = serviceRegister.getService(RoomManager.class);

        this.proverb = proverbManager.createProverb(new Random().nextInt(proverbManager.getProverbs().size()));

        UserManager userManager = serviceRegister.getService(UserManager.class);
        this.scoreManagers = new ScoreManager[players.size()];

        for (int i = 0; i < players.size(); i++) {
            User user = players.get(i).getCurrentUser();
            this.scoreManagers[i] = new ScoreManager(userManager, user, players.get(i).getOutput());
        }

        broadcast("Iniciando una nueva partida...");
        broadcast("Frase oculta: " + proverb);
        announceTurn();
    }

    public void guessConsonant(char consonant, ClientHandler player) {
        if (gameOver) return;

        ClientHandler currentPlayer = players.get(currentTurnIndex);

        if (!player.equals(currentPlayer)) {
            player.getOutput().println("No es tu turno");
            return;
        }

        ScoreManager currentScore = scoreManagers[currentTurnIndex];

        currentScore.incrementTries();
        boolean correct = proverb.guessConsonant(consonant);

        if (correct) {
            broadcast("¡" + currentPlayer.getCurrentUser().getUsername() + " ha acertado la consonante '" + consonant + "'!");
        } else {
            broadcast(currentPlayer.getCurrentUser().getUsername() + " ha fallado con la consonante '" + consonant + "'.");
            nextTurn();
        }

        broadcast("Frase actual: " + proverb);
        checkGameOver();
    }

    public void guessVowel(char vowel, ClientHandler player) {
        if (gameOver) return;

        ClientHandler currentPlayer = players.get(currentTurnIndex);

        if (!player.equals(currentPlayer)) {
            player.getOutput().println("No es tu turno");
            return;
        }

        ScoreManager currentScore = scoreManagers[currentTurnIndex];

        currentScore.incrementTries();
        boolean correct = proverb.guessVowel(vowel);

        if (correct) {
            broadcast("¡" + currentPlayer.getCurrentUser().getUsername() + " ha acertado la vocal '" + vowel + "'!");
        } else {
            broadcast(currentPlayer.getCurrentUser().getUsername() + " ha fallado con la vocal '" + vowel + "'.");
            nextTurn();
        }

        broadcast("Frase actual: " + proverb);
        checkGameOver();
    }

    public void resolveProverb(String phrase, ClientHandler player) {
        if (gameOver) return;

        ClientHandler currentPlayer = players.get(currentTurnIndex);

        if (!player.equals(currentPlayer)) {
            player.getOutput().println("No es tu turno");
            return;
        }

        ScoreManager currentScore = scoreManagers[currentTurnIndex];

        if (this.proverb.resolveProverb(phrase)) {
            broadcast("¡" + currentPlayer.getCurrentUser().getUsername() + " ha resuelto la frase correctamente! Frase: " + this.proverb.getText());
            currentScore.addScore();
            endGame();
        } else {
            broadcast(currentPlayer.getCurrentUser().getUsername() + " ha fallado al intentar resolver la frase.");
            broadcast("Lo siento, pero esa no es la frase correcta.");
            nextTurn();
        }
    }

    private void nextTurn() {
        currentTurnIndex = (currentTurnIndex + 1) % players.size();
        announceTurn();
    }

    private ClientHandler isTurn() {
        return players.get(currentTurnIndex);
    }

    private void announceTurn() {
        ClientHandler currentPlayer = players.get(currentTurnIndex);
        broadcast("Turno de " + currentPlayer.getCurrentUser().getUsername() + ".");
        currentPlayer.sendMessageBoth(Level.INFO, "Es tu turno.");
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

        // Determinar el ganador
        ClientHandler winner = players.get(currentTurnIndex);
        ScoreManager winnerScore = scoreManagers[currentTurnIndex];

        winnerScore.printFinalScore(true);

        broadcast("El ganador ha sido: " + winner.getCurrentUser().getUsername());

        for (int i = 0; i < players.size(); i++) {
            if (i != currentTurnIndex) {
                scoreManagers[i].printFinalScore(false);
            }
        }

        for (ClientHandler player : players) {
            roomManager.leaveRoom(player, true);
        }
    }

    private void broadcast(String message) {
        for (ClientHandler player : players) {
            player.getOutput().println(message);
        }
    }

    public void handlePlayerDisconnect(ClientHandler player) {
        players.remove(player);

        if (players.size() < 2) {
            broadcast("No hay suficientes jugadores para continuar. La partida termina.");
            roomManager.leaveRoom(players.get(0), true);
            gameOver = true;
        }

        if (isTurn().equals(player)) {
            nextTurn();
        } else {
            announceTurn();
        }
    }
}
