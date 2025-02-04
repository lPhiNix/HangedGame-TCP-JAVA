package common.game;

import common.model.Proverb;
import server.service.ServiceRegister;
import server.service.services.ProverbManager;

import java.util.Random;

/**
 * Representa una partida del juego de la ruleta.
 * <p>
 * Es una clase abstracta que proporciona la funcionalidad básica para gestionar un juego
 * en el que los jugadores intentan adivinar un refrán oculto.
 * </p>
 */
public abstract class HangedGame implements Game {
    protected final Proverb proverb; // Refrán actual en juego.
    protected boolean gameOver = false; // Indica si el juego ha finalizado.

    /**
     * Crea una instancia de un juego de la ruleta con un refrán aleatorio.
     *
     * @param serviceRegister Registro de servicios del servidor, usado para obtener un refrán.
     */
    protected HangedGame(ServiceRegister serviceRegister) {
        ProverbManager proverbManager = serviceRegister.getService(ProverbManager.class);
        this.proverb = proverbManager.createProverb(new Random().nextInt(proverbManager.getProverbs().size()));
    }
}
