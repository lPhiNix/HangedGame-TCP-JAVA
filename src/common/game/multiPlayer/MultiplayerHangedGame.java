package common.game.multiPlayer;

import common.game.HangedGame;
import common.game.score.ScoreManager;
import common.model.User;
import server.service.services.RoomManager;
import server.thread.ClientHandler;
import server.service.ServiceRegister;
import server.service.services.UserManager;

import java.util.List;
import java.util.logging.Level;

/**
 * Representa una partida de ahorcado en la modalidad multijugador.
 * <p>
 * La clase extiende de {@link HangedGame} y gestiona el inicio de una partida.
 * </p>
 * <p>
 * Esta clase gestiona el flujo del juego con múltiples jugadores, permitiendo que se turnen para adivinar consonantes,
 * vocales o resolver el proverbio. Además, gestiona las puntuaciones y determina el ganador al final.
 * </p>
 */

// TODO refactorizar esta abominación
public class MultiplayerHangedGame extends HangedGame {
    private final List<ClientHandler> players; // Lista de jugadores en la partida.
    private final RoomManager roomManager; // Gestor de salas.
    private final ScoreManager[] scoreManagers; // Gestores de puntuación para cada jugador.
    private int currentTurnIndex = 0; // Índice del jugador cuyo turno es el actual.

    /**
     * Crea una nueva instancia de la partida multijugador.
     *
     * @param players         Lista de jugadores en la partida.
     * @param serviceRegister Registro de servicios para acceder a otras funcionalidades.
     */
    public MultiplayerHangedGame(List<ClientHandler> players, ServiceRegister serviceRegister) {
        super(serviceRegister);

        this.players = players;
        this.roomManager = serviceRegister.getService(RoomManager.class);
        this.scoreManagers = new ScoreManager[players.size()];

        UserManager userManager = serviceRegister.getService(UserManager.class);

        // Se inicializa el gestor de puntuaciones para cada jugador.
        for (int i = 0; i < players.size(); i++) {
            User user = players.get(i).getCurrentUser();
            this.scoreManagers[i] = new ScoreManager(userManager, user, players.get(i).getOutput());
        }
    }

    /**
     * Inicia la partida mostrando la frase oculta y anunciando el primer turno.
     */
    @Override
    public void startGame() {
        broadcast("Iniciando una nueva partida...");
        broadcast("Frase oculta: " + proverb); // Se muestra la frase oculta al inicio.
        announceTurn(); // Anuncia el turno del primer jugador.
    }

    /**
     * Procesa la suposición de una consonante por parte de un jugador.
     *
     * @param consonant Consonante adivinada.
     * @param player    Jugador que realiza la suposición.
     */
    public void guessConsonant(char consonant, ClientHandler player) {
        if (gameOver) return; // Si el juego terminó, no se procesan más intentos.

        ClientHandler currentPlayer = players.get(currentTurnIndex);

        // Verifica si es el turno del jugador
        if (!player.equals(currentPlayer)) {
            player.getOutput().println("No es tu turno");
            return;
        }

        ScoreManager currentScore = scoreManagers[currentTurnIndex];

        // Procesa la adivinanza de la consonante
        currentScore.incrementTries();
        boolean correct = proverb.guessConsonant(consonant);

        if (correct) {
            broadcast("¡" + currentPlayer.getCurrentUser().getUsername() + " ha acertado la consonante '" + consonant + "'!");
        } else {
            broadcast(currentPlayer.getCurrentUser().getUsername() + " ha fallado con la '" + consonant + "'.");
        }

        nextTurn(); // Cambia al siguiente turno.

        broadcast("Frase actual: " + proverb); // Muestra la frase actual.
        checkGameOver(); // Verifica si el juego ha terminado.
    }

    /**
     * Procesa la suposición de una vocal por parte de un jugador.
     *
     * @param vowel  Vocal adivinada.
     * @param player Jugador que realiza la suposición.
     */
    public void guessVowel(char vowel, ClientHandler player) {
        if (gameOver) return; // Si el juego terminó, no se procesan más intentos.

        ClientHandler currentPlayer = players.get(currentTurnIndex);

        // Verifica si es el turno del jugador
        if (!player.equals(currentPlayer)) {
            player.getOutput().println("No es tu turno");
            return;
        }

        ScoreManager currentScore = scoreManagers[currentTurnIndex];

        // Procesa la adivinanza de la vocal
        currentScore.incrementTries();
        boolean correct = proverb.guessVowel(vowel);

        if (correct) {
            broadcast("¡" + currentPlayer.getCurrentUser().getUsername() + " ha acertado la vocal '" + vowel + "'!");
        } else {
            broadcast(currentPlayer.getCurrentUser().getUsername() + " ha fallado con la '" + vowel + "'.");
        }

        nextTurn(); // Cambia al siguiente turno.

        broadcast("Frase actual: " + proverb); // Muestra la frase actual.
        checkGameOver(); // Verifica si el juego ha terminado.
    }

    /**
     * Permite a un jugador intentar resolver el proverbio completo.
     *
     * @param phrase Refrán propuesto como solución.
     * @param player Jugador que intenta resolver la frase.
     */
    public void resolveProverb(String phrase, ClientHandler player) {
        if (gameOver) return; // Si el juego terminó, no se procesan más intentos.

        ClientHandler currentPlayer = players.get(currentTurnIndex);

        // Verifica si es el turno del jugador
        if (!player.equals(currentPlayer)) {
            player.getOutput().println("No es tu turno");
            return;
        }

        ScoreManager currentScore = scoreManagers[currentTurnIndex];

        // Intenta resolver el proverbio
        if (proverb.resolveProverb(phrase)) {
            broadcast("¡" + currentPlayer.getCurrentUser().getUsername() + " ha resuelto la frase correctamente! Frase: " + this.proverb.getText());
            currentScore.addScore(); // Se añade puntuación si se resuelve correctamente.
            endGame(); // Finaliza el juego si se resuelve el proverbio.
        } else {
            broadcast(currentPlayer.getCurrentUser().getUsername() + " ha fallado al intentar resolver la frase.");
            broadcast("Lo siento, pero esa no es la frase correcta.");
            nextTurn(); // Cambia al siguiente turno si la respuesta es incorrecta.
        }
    }

    /**
     * Cambia al siguiente turno en el juego.
     */
    private void nextTurn() {
        currentTurnIndex = (currentTurnIndex + 1) % players.size();
        announceTurn(); // Anuncia el turno del siguiente jugador.
    }

    /**
     * Obtiene al jugador cuyo turno es el actual.
     *
     * @return Jugador cuyo turno es el actual.
     */
    private ClientHandler isTurn() {
        return players.get(currentTurnIndex);
    }

    /**
     * Anuncia el turno del jugador actual.
     */
    private void announceTurn() {
        ClientHandler currentPlayer = players.get(currentTurnIndex);
        broadcast("Turno de " + currentPlayer.getCurrentUser().getUsername() + ".");
        currentPlayer.sendMessageBoth(Level.INFO, "Es tu turno.");
    }

    /**
     * Verifica si el juego ha terminado.
     */
    private void checkGameOver() {
        if (proverb.isRevealed()) {
            broadcast("¡La frase ha sido completada! Frase: " + proverb.getText());
            endGame(); // Termina el juego si la frase ha sido completada.
        }
    }

    /**
     * Finaliza el juego, determinando al ganador y mostrando la puntuación final.
     */
    private void endGame() {
        gameOver = true;
        broadcast("La partida ha terminado.");

        // Determina el ganador
        ClientHandler winner = players.get(currentTurnIndex);
        ScoreManager winnerScore = scoreManagers[currentTurnIndex];

        winnerScore.printFinalScore(true); // Muestra la puntuación final del ganador.

        broadcast("El ganador ha sido: " + winner.getCurrentUser().getUsername());

        // Muestra la puntuación final de los demás jugadores.
        for (int i = 0; i < players.size(); i++) {
            if (i != currentTurnIndex) {
                scoreManagers[i].printFinalScore(false);
            }
        }

        // Deja a los jugadores en la sala
        for (ClientHandler player : players) {
            roomManager.leaveRoom(player, true);
        }
    }

    /**
     * Envía un mensaje a todos los jugadores en la partida.
     *
     * @param message Mensaje a enviar.
     */
    private void broadcast(String message) {
        for (ClientHandler player : players) {
            player.getOutput().println(message);
        }
    }

    /**
     * Maneja la desconexión de un jugador durante la partida.
     *
     * @param player Jugador que se ha desconectado.
     */
    public void handlePlayerDisconnect(ClientHandler player) {
        players.remove(player);

        // Si quedan menos de 2 jugadores, finaliza la partida.
        if (players.size() < 2) {
            broadcast("No hay suficientes jugadores para continuar. La partida termina.");
            roomManager.leaveRoom(players.get(0), true);
            gameOver = true;
        }

        // Si el jugador desconectado es el que tiene el turno, pasa al siguiente.
        if (isTurn().equals(player)) {
            nextTurn();
        } else {
            announceTurn();
        }
    }
}
