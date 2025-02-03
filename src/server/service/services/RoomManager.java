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
            owner.sendMessageBoth(Level.WARNING, "La sala ya existe.");
            return null;
        }
        HangedRoom room = new HangedRoom(roomName, owner);
        rooms.put(roomName, room);
        return room;
    }

    public synchronized HangedRoom joinRoom(String roomName, ClientHandler player) {
        HangedRoom room = rooms.get(roomName);
        if (room == null) {
            player.sendMessageBoth(Level.WARNING, "La sala no existe.");
            return null;
        }
        room.addPlayer(player);
        return room;
    }

    public synchronized void leaveRoom(ClientHandler player) {
        HangedRoom room = player.getCurrentRoom();
        if (room != null) {
            room.removePlayer(player);
            if (room.isEmpty()) {
                rooms.remove(room.getRoomName());
            }
        }
    }
}
