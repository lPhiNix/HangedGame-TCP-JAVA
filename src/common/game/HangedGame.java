package common.game;

import common.model.Proverb;
import server.service.ServiceRegister;
import server.service.services.ProverbManager;

import java.util.Random;

public abstract class HangedGame implements Game {
    protected final Proverb proverb;
    protected boolean gameOver = false;

    protected HangedGame(ServiceRegister serviceRegister) {
        ProverbManager proverbManager = serviceRegister.getService(ProverbManager.class);
        this.proverb = proverbManager.createProverb(new Random().nextInt(proverbManager.getProverbs().size()));
    }
}
