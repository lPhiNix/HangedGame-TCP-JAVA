package server.thread;

import common.game.multiPlayer.HangedRoom;
import server.service.ServiceRegister;
import server.service.services.CommandProcessor;
import common.game.singlePlayer.SinglePlayerHangedGame;
import common.model.User;

import java.net.Socket;
import java.util.logging.Level;

/**
 * Representa un cliente en el servidor.
 * <p>
 * Esta clase se encarga de recibir comandos del cliente, procesarlos y gestionar
 * tanto partidas individuales como en grupo.
 * </p>
 *
 * @see AbstractWorker
 * @see CommandProcessor
 * @see SinglePlayerHangedGame
 * @see HangedRoom
 */
public class ClientHandler extends AbstractWorker {
    private final ServiceRegister serviceRegister; // Registro de servicios disponibles en el servidor.
    private SinglePlayerHangedGame gameSession; // Partida actual del jugador en el modo individual.
    private HangedRoom currentRoom; // Sala en la que el cliente está participando en el modo multijugador.
    private User currentUser; // Usuario autenticado asociado a este cliente.

    /**
     * Constructor que inicializa un manejador de clientes con un socket y un registro de servicios.
     *
     * @param socket          Socket de conexión con el cliente.
     * @param serviceRegister Registro de servicios disponibles en el servidor.
     */
    public ClientHandler(Socket socket, ServiceRegister serviceRegister) {
        super(socket);
        this.serviceRegister = serviceRegister;
    }

    /**
     * Escucha comandos enviados por el cliente y los procesa.
     */
    @Override
    protected void listen() {
        try {
            handleCommands();
        } catch (Exception e) {
            logger.severe("Error al realizar lectura en " + socket.getInetAddress() + ": " + e.getMessage());
        }
    }

    /**
     * Procesa los comandos enviados por el cliente y los ejecuta.
     *
     * @throws Exception Si ocurre un error durante el procesamiento.
     */
    private void handleCommands() throws Exception {
        logger.log(Level.INFO, "Esperando comandos del cliente " + getFormatedUser());

        String commandLine;
        while (isRunning && (commandLine = input.readLine()) != null) {
            CommandProcessor commandProcessor = serviceRegister.getService(CommandProcessor.class);
            commandProcessor.processCommand(commandLine, this);
        }
    }

    /**
     * Inicia una partida en modo individual para el cliente.
     */
    public void startGame() {
        gameSession = new SinglePlayerHangedGame(output, currentUser, serviceRegister);
        try {
            gameSession.startGame();
        } catch (Exception e) {
            sendMessageBoth(Level.SEVERE, "Error al iniciar partida individual por " + getFormatedUser());
        }
    }

    /**
     * Devuelve un identificador del usuario, si no está autenticado se usa su IP.
     *
     * @return Nombre de usuario o "Invitado + IP".
     */
    public String getFormatedUser() {
        return (currentUser == null) ? "Invitado " + socket.getInetAddress() : currentUser.getUsername();
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
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

