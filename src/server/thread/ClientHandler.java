package server.thread;

import common.game.multiPlayer.HangedRoom;
import server.service.ServiceRegister;
import server.service.services.CommandProcessor;
import common.game.singlePlayer.SinglePlayerHangedGame;
import common.model.User;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;

public class ClientHandler extends AbstractWorker {
    private final ServiceRegister serviceRegister;
    private SinglePlayerHangedGame gameSession;
    private HangedRoom currentRoom;
    private User currentUser;

    public ClientHandler(Socket socket, ServiceRegister serviceRegister) {
        super(socket);
        this.serviceRegister = serviceRegister;
    }

    @Override
    protected void listen() {
        try {
            handleCommands();
        } catch (Exception e) {
            logger.severe("Error al realizar lectura en " + socket.getInetAddress() + ": " + e.getMessage());
        }
    }

    private void handleCommands() throws Exception {
        logger.log(Level.INFO, "Esperando comandos del cliente " + getFormatedUser());

        String commandLine;
        while (isRunning && (commandLine = input.readLine()) != null) {
            CommandProcessor commandProcessor = serviceRegister.getService(CommandProcessor.class);
            commandProcessor.processCommand(commandLine, this);
        }
    }

    public void startGame() {
        gameSession = new SinglePlayerHangedGame(output, currentUser, serviceRegister);
        try {
            gameSession.startGame();
        } catch (IOException e) {
            sendMessageBoth(Level.SEVERE, "Error al iniciar partida individual por " + getFormatedUser());
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public String getFormatedUser() {
        if (currentUser == null) {
            return "Invitado " + socket.getInetAddress();
        } else {
            return currentUser.getUsername();
        }
    }

    public SinglePlayerHangedGame getGameSession() {
        return gameSession;
    }

    public void setGameSession(SinglePlayerHangedGame gameSession) {
        this.gameSession = gameSession;
    }

    public HangedRoom getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(HangedRoom currentRoom) {
        this.currentRoom = currentRoom;
    }

    public ServiceRegister getServiceRegister() {
        return serviceRegister;
    }

    public boolean hasActiveSingleGame() {
        return gameSession != null && !gameSession.isGameOver();
    }

    public boolean hasActiveMultiplayerGame() {
        return currentRoom != null && !currentRoom.isEmpty();
    }
}
