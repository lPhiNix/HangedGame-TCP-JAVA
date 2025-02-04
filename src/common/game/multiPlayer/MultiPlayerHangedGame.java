package common.game.multiPlayer;

import common.game.HangedGame;
import common.game.score.ScoreManager;
import server.service.services.RoomManager;
import server.thread.ClientHandler;
import server.service.ServiceRegister;
import server.service.services.UserManager;

import java.util.List;
import java.util.logging.Level;

public class MultiPlayerHangedGame extends HangedGame {
    private final List<ClientHandler> players;
    private final RoomManager roomManager;
    private final ScoreManager[] scoreManagers;
    private int currentTurnIndex = 0;

    public MultiPlayerHangedGame(List<ClientHandler> players, ServiceRegister serviceRegister) {
        super(serviceRegister, players.get(0).getOutput());
        this.players = players;
        this.roomManager = serviceRegister.getService(RoomManager.class);
        this.scoreManagers = new ScoreManager[players.size()];
        UserManager userManager = serviceRegister.getService(UserManager.class);

        for (int i = 0; i < players.size(); i++) {
            this.scoreManagers[i] = new ScoreManager(userManager, players.get(i).getCurrentUser(), players.get(i).getOutput());
        }

        broadcast("Iniciando una nueva partida...");
        broadcast("Frase oculta: " + proverb);
        announceTurn();
    }

    public void guessLetter(char letter, boolean isVowel, ClientHandler player) {
        if (gameOver) return;
        if (!player.equals(players.get(currentTurnIndex))) {
            player.getOutput().println("No es tu turno");
            return;
        }
        super.guessLetter(letter, isVowel);
        nextTurn();
    }

    public void resolveProverb(String phrase, ClientHandler player) {
        if (gameOver) return;
        if (!player.equals(players.get(currentTurnIndex))) {
            player.getOutput().println("No es tu turno");
            return;
        }
        super.resolveProverb(phrase);
    }

    private ClientHandler isTurnOf() {
        return players.get(currentTurnIndex);
    }

    private void nextTurn() {
        if (!gameOver) {
            currentTurnIndex = (currentTurnIndex + 1) % players.size();
            announceTurn();
        }
    }

    private void announceTurn() {
        ClientHandler currentPlayer = players.get(currentTurnIndex);
        broadcast("Turno de " + currentPlayer.getCurrentUser().getUsername() + ".");
        currentPlayer.sendMessageBoth(Level.INFO, "Es tu turno.");
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

        if (isTurnOf().equals(player)) {
            nextTurn();
        } else {
            announceTurn();
        }
    }
}