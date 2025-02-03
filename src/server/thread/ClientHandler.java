package server.thread;

import server.service.ServiceRegister;
import server.service.services.CommandProcessor;
import common.game.HangedGame;
import common.model.User;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;

public class ClientHandler extends AbstractWorker {
    private final ServiceRegister serviceRegister;
    private HangedGame gameSession;
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
            throw new RuntimeException(e);
        }
    }

    private void handleCommands() throws Exception {
        logger.log(Level.INFO, "Esperando comandos del cliente");

        String commandLine;
        while (isRunning && (commandLine = input.readLine()) != null) {
            CommandProcessor commandProcessor = (CommandProcessor) serviceRegister.getService(CommandProcessor.class);
            commandProcessor.processCommand(commandLine, this);
        }
    }

    public void startGame() {
        if (currentUser == null) {
            output.println("Debes iniciar sesión antes de jugar.");
            return;
        }

        gameSession = new HangedGame(output, currentUser, serviceRegister);
        try {
            gameSession.startGame();
        } catch (IOException e) {
            output.println("Error iniciando el juego.");
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public HangedGame getGameSession() {
        return gameSession;
    }

    public ServiceRegister getServiceRegister() {
        return serviceRegister;
    }

    public boolean hasActiveGame() {
        return gameSession != null && !gameSession.isGameOver();
    }
}
