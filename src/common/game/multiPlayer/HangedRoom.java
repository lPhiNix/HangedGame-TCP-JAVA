package common.game.multiPlayer;

import server.thread.ClientHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Representa una sala de juego multijugador para la modalidad de ahorcado.
 * <p>
 * Una sala tiene un límite de 3 jugadores. Cuando la sala está llena, se inicia automáticamente
 * una partida multijugador.
 * </p>
 */
public class HangedRoom {
    private static final int MAX_USERS = 3; // Número máximo de jugadores en la sala.
    private final String roomName; // Nombre de la sala.
    private final List<ClientHandler> players; // Lista de jugadores en la sala.
    private MultiplayerHangedGame gameSession; // Sesión de juego en curso dentro de la sala.
    private boolean gameStarted = false; // Indica si el juego ha comenzado en esta sala.

    /**
     * Crea una nueva sala con un nombre y un jugador propietario.
     *
     * @param roomName Nombre de la sala.
     * @param owner    Jugador que crea la sala.
     */
    public HangedRoom(String roomName, ClientHandler owner) {
        this.roomName = roomName;
        this.players = new ArrayList<>();
        this.players.add(owner);  // El propietario se agrega primero.
        owner.setCurrentRoom(this); // Se asigna la sala al jugador propietario.
    }

    /**
     * Agrega un jugador a la sala si hay espacio disponible.
     * Si la sala alcanza el número máximo de jugadores, se inicia automáticamente la partida.
     *
     * @param player Jugador a agregar.
     */
    public synchronized void addPlayer(ClientHandler player) {
        // Si la sala ya está llena, se notifica al jugador.
        if (players.size() >= MAX_USERS) {
            player.getOutput().println("La sala está llena.");
            return;
        }

        // Notifica a los demás jugadores que un nuevo jugador ha entrado.
        player.sendMessageBoth(Level.INFO, player.getFormatedUser() + " ha entrado a la sala " + this.getRoomName());
        players.add(player); // Se agrega el jugador a la lista de jugadores.
        player.setCurrentRoom(this); // Se asigna la sala al nuevo jugador.

        // Se notifica a todos los jugadores sobre la llegada del nuevo jugador.
        broadcast("Jugador " + player.getCurrentUser().getUsername() + " se ha unido.");

        // Si se alcanza el número máximo de jugadores, comienza la partida.
        if (players.size() == MAX_USERS) {
            startGame();
        }
    }

    /**
     * Inicia la partida cuando se alcanza el número máximo de jugadores.
     */
    private void startGame() {
        gameStarted = true; // Se marca la partida como iniciada.
        gameSession = new MultiplayerHangedGame(players, players.get(0).getServiceRegister());
        // Se crea una nueva sesión de juego con los jugadores y el servicio de registro.
        broadcast("La partida ha comenzado.");
        try {
            gameSession.startGame(); // Se inicia el juego.
        } catch (Exception e) {
            broadcast("Error al iniciar la partida en la sala " + roomName); // En caso de error, se notifica.
        }
    }

    /**
     * Procesa la suposición de una consonante por parte de un jugador.
     *
     * @param consonant Consonante adivinada.
     * @param player    Jugador que realiza la suposición.
     */
    public synchronized void playerGuessConsonant(char consonant, ClientHandler player) {
        if (gameSession != null) {
            gameSession.guessConsonant(consonant, player); // Se pasa la suposición al juego.
        }
    }

    /**
     * Procesa la suposición de una vocal por parte de un jugador.
     *
     * @param vowel  Vocal adivinada.
     * @param player Jugador que realiza la suposición.
     */
    public synchronized void playerGuessVowel(char vowel, ClientHandler player) {
        if (gameSession != null) {
            gameSession.guessVowel(vowel, player); // Se pasa la suposición al juego.
        }
    }

    /**
     * Permite a un jugador intentar resolver el refrán.
     *
     * @param proverb Refrán propuesto como solución.
     * @param player  Jugador que intenta resolver el refrán.
     */
    public synchronized void playerResolve(String proverb, ClientHandler player) {
        if (gameSession != null) {
            gameSession.resolveProverb(proverb, player); // Se pasa la resolución del refrán al juego.
        }
    }

    /**
     * Elimina a un jugador de la sala y maneja su desconexión en la partida si es necesario.
     *
     * @param player   Jugador que se retira de la sala.
     * @param gameOver Indica si el juego ha finalizado.
     */
    public synchronized void removePlayer(ClientHandler player, boolean gameOver) {
        players.remove(player); // Se elimina al jugador de la lista.
        broadcast("Jugador " + player.getCurrentUser().getUsername() + " ha abandonado la partida.");

        player.setCurrentRoom(null); // Se desasocia la sala del jugador.

        // Si el juego no ha terminado, se maneja la desconexión.
        if (!gameOver) {
            if (gameSession != null) {
                gameSession.handlePlayerDisconnect(player); // Se maneja la desconexión del jugador en la partida.
            }

            // Si no quedan jugadores, se marca la sala como vacía.
            if (players.isEmpty()) {
                gameStarted = false;
            }
        }
    }

    /**
     * Envía un mensaje a todos los jugadores de la sala.
     *
     * @param message Mensaje a enviar.
     */
    public synchronized void broadcast(String message) {
        // Se envía el mensaje a todos los jugadores de la sala.
        for (ClientHandler player : players) {
            player.getOutput().println(message);
        }
    }

    /**
     * Devuelve la cantidad de jugadores en la sala.
     *
     * @return Representación en texto del número de jugadores actuales y el máximo permitido.
     */
    public String getPlayersAmount() {
        return "Players: (" + players.size() + "/" + MAX_USERS + ")";
    }

    /**
     * Verifica si la sala está vacía.
     *
     * @return {@code true} si no hay jugadores en la sala, {@code false} en caso contrario.
     */
    public boolean isEmpty() {
        return players.isEmpty();
    }

    /**
     * Obtiene el nombre de la sala.
     *
     * @return Nombre de la sala.
     */
    public String getRoomName() {
        return roomName;
    }

    /**
     * Verifica si el juego ha comenzado en la sala.
     *
     * @return {@code true} si la partida ha comenzado, {@code false} en caso contrario.
     */
    public boolean isGameStarted() {
        return gameStarted;
    }
}
