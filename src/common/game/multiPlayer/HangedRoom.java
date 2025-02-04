package common.game.multiPlayer;

import server.thread.ClientHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HangedRoom {
    private static final int MAX_USERS = 2;
    private final String roomName;
    private final List<ClientHandler> players;
    private MultiplayerHangedGame gameSession;
    private boolean gameStarted = false;

    public HangedRoom(String roomName, ClientHandler owner) {
        this.roomName = roomName;
        this.players = new ArrayList<>();
        this.players.add(owner);
        owner.setCurrentRoom(this);
    }

    public synchronized void addPlayer(ClientHandler player) {
        if (players.size() >= MAX_USERS) {
            player.getOutput().println("La sala está llena.");
            return;
        }
        players.add(player);
        player.setCurrentRoom(this);

        broadcast("Jugador " + player.getCurrentUser().getUsername() + " se ha unido.");

        if (players.size() == MAX_USERS) {
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

    public synchronized void playerGuessConsonant(char consonant) {
        if (gameSession != null) {
            gameSession.guessConsonant(consonant);
        }
    }

    public synchronized void playerGuessVowel(char vowel) {
        if (gameSession != null) {
            gameSession.guessVowel(vowel);
        }
    }

    public synchronized void playerResolve(String proverb) {
        if (gameSession != null) {
            gameSession.resolveProverb(proverb);
        }
    }

    public synchronized void removePlayer(ClientHandler player, boolean gameOver) {
        players.remove(player);
        broadcast("Jugador " + player.getCurrentUser().getUsername() + " ha abandonado la partida.");

        player.setCurrentRoom(null);

        if (!gameOver) {
            if (gameSession != null) {
                gameSession.handlePlayerDisconnect(player);
            }

            if (players.isEmpty()) {
                gameStarted = false;
            }
        }
    }

    public synchronized void broadcast(String message) {
        for (ClientHandler player : players) {
            player.getOutput().println(message);
        }
    }

    public String getPlayersAmount() {
        return "Players: (" + players.size() + "/" + MAX_USERS + ")";
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
