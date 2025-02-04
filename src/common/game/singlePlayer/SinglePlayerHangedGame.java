package common.game.singlePlayer;

import common.game.HangedGame;
import common.game.score.ScoreManager;
import common.model.User;
import server.service.ServiceRegister;
import server.service.services.UserManager;

import java.io.PrintWriter;

public class SinglePlayerHangedGame extends HangedGame {
    private final ScoreManager scoreManager;

    public SinglePlayerHangedGame(PrintWriter output, User user, ServiceRegister serviceRegister) {
        super(serviceRegister, output);
        UserManager userManager = serviceRegister.getService(UserManager.class);
        this.scoreManager = new ScoreManager(userManager, user, output);
    }

    public void startGame() {
        output.println("Iniciando una nueva partida...");
        output.println("Frase oculta: " + proverb);
        scoreManager.resetTries();
    }
}