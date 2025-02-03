package common.game.multiPlayer;

import server.thread.ClientHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class HangedRoom {
    private final int maxUsers = 2;
    private final String roomName;
    private final List<ClientHandler> players;
    private MultiplayerHangedGame gameSession;
    private boolean gameStarted = false;

    public HangedRoom(String roomName, ClientHandler owner) {
        this.roomName = roomName;
        this.players = new ArrayList<>();
        this.players.add(owner);
    }

    public synchronized void addPlayer(ClientHandler player) {
        if (players.size() >= maxUsers) {
            player.sendMessageBoth(Level.WARNING, "La sala está llena.");
            return;
        }
        players.add(player);
        player.setCurrentRoom(this);

        broadcast("Jugador " + player.getCurrentUser().getUsername() + " se ha unido.");

        if (players.size() == maxUsers) {
            startGame();
        }
    }

    private void startGame() {
        gameStarted = true;
        broadcast("La partida ha comenzado.");
        try {
            gameSession = new MultiplayerHangedGame(players, players.get(0).getServiceRegister());
        } catch (IOException e) {
            broadcast("Error al iniciar la partida.");
        }
    }

    public synchronized void playerGuessConsonant(ClientHandler player, char consonant) {
        if (gameSession != null) {
            gameSession.guessConsonant(consonant);
        }
    }

    public synchronized void playerGuessVowel(ClientHandler player, char vowel) {
        if (gameSession != null) {
            gameSession.guessVowel(vowel);
        }
    }

    public synchronized void playerResolve(ClientHandler player, String phrase) {
        if (gameSession != null) {
            gameSession.resolveProverb(phrase);
        }
    }

    public synchronized void removePlayer(ClientHandler player) {
        players.remove(player);
        broadcast("Jugador " + player.getCurrentUser().getUsername() + " ha abandonado la partida.");

        if (gameSession != null) {
            gameSession.handlePlayerDisconnect(player);
        }

        if (players.isEmpty()) {
            gameStarted = false;
        }
    }

    public synchronized void broadcast(String message) {
        for (ClientHandler player : players) {
            player.sendMessageBoth(Level.INFO, message);
        }
    }

    public boolean isEmpty() {
        return players.isEmpty();
    }

    public String getRoomName() {
        return roomName;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }
}
