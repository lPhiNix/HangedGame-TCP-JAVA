package server.service.services;

import common.game.multiPlayer.HangedRoom;
import common.logger.CustomLogger;
import server.thread.ClientHandler;
import server.service.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RoomManager implements Service {
    private static final Logger logger = CustomLogger.getLogger(RoomManager.class.getName());
    private final Map<String, HangedRoom> rooms = new ConcurrentHashMap<>();

    public synchronized void createRoom(String roomName, ClientHandler owner) {
        if (rooms.containsKey(roomName)) {
            owner.getOutput().println("La sala ya existe.");
            return;
        }
        HangedRoom room = new HangedRoom(roomName, owner);
        rooms.put(roomName, room);
        owner.sendMessageBoth(Level.CONFIG, "Sala " + roomName + " creada con exito.");
    }

    public synchronized void joinRoom(String roomName, ClientHandler player) {
        HangedRoom room = rooms.get(roomName);
        if (room == null) {
            player.sendMessageBoth(Level.WARNING, "La sala no existe.");
            return;
        }
        room.addPlayer(player);
    }

    public synchronized void leaveRoom(ClientHandler player, boolean gameOver) {
        HangedRoom room = player.getCurrentRoom();
        if (room != null) {
            logger.log(Level.CONFIG, player.getFormatedUser() + " ha abandonado la sala " + room.getRoomName());
            player.getOutput().println("Has abandonado la sala " + room.getRoomName());

            if (!gameOver) {
                room.removePlayer(player, false);

                if (room.isEmpty()) {
                    rooms.remove(room.getRoomName());
                    logger.log(Level.CONFIG, "Sala " + room.getRoomName() + "eliminada por falta de jugadores.");
                }
            } else {
                room.removePlayer(player, true);
                rooms.remove(room.getRoomName());
                logger.log(Level.CONFIG, "Sala " + room.getRoomName() + "eliminada por juego terminado.");
            }
        }
    }

    public synchronized void printAllActiveRooms(ClientHandler client) {
        if (rooms.isEmpty()) {
            client.getOutput().println("No hay salas activas.");
            return;
        }

        client.getOutput().println("Salas Activas: ");
        for (String s : rooms.keySet()) {
            client.getOutput().println("Name: " + s + ", " + rooms.get(s).getPlayersAmount());
        }
    }
}
