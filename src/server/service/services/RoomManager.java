package server.service.services;

import common.game.multiPlayer.HangedRoom;
import common.logger.CustomLogger;
import server.thread.ClientHandler;
import server.service.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servicio para gestionar las salas de juego en el modo multijugador.
 * <p>
 * Permite la creación, unión, salida y eliminación de salas activas.
 * Las salas son gestionadas de manera concurrente con {@link ConcurrentHashMap}.
 * </p>
 * @see HangedRoom
 */
public class RoomManager implements Service {
    private static final Logger logger = CustomLogger.getLogger(RoomManager.class.getName());

    // Mapa de salas activas, donde la clave es el nombre de la sala y la instancia de la propia sala.
    private final Map<String, HangedRoom> rooms = new ConcurrentHashMap<>();

    /**
     * Crea una nueva sala si no existe previamente.
     *
     * @param roomName Nombre de la sala a crear.
     * @param owner    Cliente que crea la sala.
     */
    public synchronized void createRoom(String roomName, ClientHandler owner) {
        if (rooms.containsKey(roomName)) {
            owner.getOutput().println("La sala ya existe.");
            return;
        }
        HangedRoom room = new HangedRoom(roomName, owner);
        rooms.put(roomName, room);
        owner.sendMessageBoth(Level.CONFIG, "Sala " + roomName + " creada con éxito.");
    }

    /**
     * Permite a un jugador unirse a una sala existente.
     *
     * @param roomName Nombre de la sala a la que se desea unir.
     * @param player   Cliente que intenta unirse a la sala.
     */
    public synchronized void joinRoom(String roomName, ClientHandler player) {
        HangedRoom room = rooms.get(roomName);
        if (room == null) {
            player.sendMessageBoth(Level.WARNING, "La sala no existe.");
            return;
        }
        room.addPlayer(player);
    }

    /**
     * Maneja la salida de un jugador de una sala.
     * Si la sala queda vacía tras la salida del jugador, se elimina automáticamente.
     *
     * @param player   Cliente que abandona la sala.
     * @param gameOver Indica si la sala debe cerrarse debido a que la partida ha terminado.
     */
    public synchronized void leaveRoom(ClientHandler player, boolean gameOver) {
        HangedRoom room = player.getCurrentRoom();
        if (room != null) {
            logger.log(Level.CONFIG, player.getFormatedUser() + " ha abandonado la sala " + room.getRoomName());
            player.getOutput().println("Has abandonado la sala " + room.getRoomName());

            if (!gameOver) {
                room.removePlayer(player, false);

                if (room.isEmpty()) {
                    rooms.remove(room.getRoomName());
                    logger.log(Level.CONFIG, "Sala " + room.getRoomName() + " eliminada por falta de jugadores.");
                }
            } else {
                room.removePlayer(player, true);
                rooms.remove(room.getRoomName());
                logger.log(Level.CONFIG, "Sala " + room.getRoomName() + " eliminada por juego terminado.");
            }
        }
    }

    /**
     * Envía al cliente una lista con todas las salas activas.
     *
     * @param client Cliente que solicita la lista de salas.
     */
    public synchronized void printAllActiveRooms(ClientHandler client) {
        if (rooms.isEmpty()) {
            client.getOutput().println("No hay salas activas.");
            return;
        }

        client.getOutput().println("Salas Activas:");
        for (String s : rooms.keySet()) {
            client.getOutput().println("Nombre: " + s + ", Jugadores: " + rooms.get(s).getPlayersAmount());
        }
    }
}
