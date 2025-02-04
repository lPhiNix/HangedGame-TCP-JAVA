package server.service.services;

import common.game.multiPlayer.HangedRoom;
import server.thread.ClientHandler;
import server.service.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class RoomManager implements Service {
    private final Map<String, HangedRoom> rooms = new ConcurrentHashMap<>();

    public synchronized HangedRoom createRoom(String roomName, ClientHandler owner) {
        if (rooms.containsKey(roomName)) {
            owner.getOutput().println("La sala ya existe.");
            return null;
        }
        HangedRoom room = new HangedRoom(roomName, owner);
        rooms.put(roomName, room);
        owner.sendMessageBoth(Level.CONFIG, "Sala " + roomName + " creada con exito.");
        return room;
    }

    public synchronized HangedRoom joinRoom(String roomName, ClientHandler player) {
        HangedRoom room = rooms.get(roomName);
        if (room == null) {
            player.sendMessageBoth(Level.WARNING, "La sala no existe.");
            return null;
        }
        player.sendMessageBoth(Level.INFO, player.getFormatedUser() + " ha entrado a la sala " + room.getRoomName() + " " + room.getPlayersAmount());
        room.addPlayer(player);
        return room;
    }

    public synchronized void leaveRoom(ClientHandler player, boolean gameOver) {
        HangedRoom room = player.getCurrentRoom();
        if (room != null) {
            if (!gameOver) {
                room.removePlayer(player, false);

                if (room.isEmpty()) {
                    rooms.remove(room.getRoomName());
                }
            } else {
                room.removePlayer(player, true);
                rooms.remove(room.getRoomName());
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
