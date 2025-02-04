package common.game.singlePlayer;

import common.game.HangedGame;
import common.game.score.ScoreManager;
import common.model.User;
import server.service.ServiceRegister;
import server.service.services.UserManager;

import java.io.PrintWriter;

/**
 * Representa una partida de ahorcado en la modalidad de un solo jugador.
 * <p>
 * La clase extiende de {@link HangedGame} y gestiona el inicio de una partida, las suposiciones del jugador
 * y el cálculo de la puntuación en el juego de ahorcado.
 * </p>
 */
public class SinglePlayerHangedGame extends HangedGame {
    private final ScoreManager scoreManager; // Gestión de la puntuación durante la partida.
    private final PrintWriter output; // Canal de salida para mostrar mensajes al jugador.

    /**
     * Crea una nueva instancia del juego de ahorcado para un solo jugador.
     *
     * @param output         Canal de salida para el jugador.
     * @param user           Usuario que juega la partida.
     * @param serviceRegister Registro de servicios para acceder a otras funcionalidades.
     */
    public SinglePlayerHangedGame(PrintWriter output, User user, ServiceRegister serviceRegister) {
        super(serviceRegister); // Llama al constructor de la clase base.

        this.output = output;
        UserManager userManager = serviceRegister.getService(UserManager.class);
        this.scoreManager = new ScoreManager(userManager, user, output); // Se inicializa el gestor de puntuaciones.
    }

    /**
     * Inicia el juego mostrando la frase oculta y restableciendo los intentos.
     */
    @Override
    public void startGame() {
        output.println("Iniciando una nueva partida...");
        output.println("Frase oculta: " + proverb); // Muestra la frase oculta (proverbio).
        scoreManager.resetTries(); // Resetea el contador de intentos.
    }

    /**
     * Procesa la suposición de una consonante por parte del jugador.
     *
     * @param consonant Consonante adivinada.
     */
    public void guessConsonant(char consonant) {
        if (gameOver) return; // Si el juego terminó, no se procesan más intentos.

        scoreManager.incrementTries(); // Incrementa el número de intentos.
        boolean correct = proverb.guessConsonant(consonant); // Se verifica si la consonante es correcta.

        if (correct) {
            output.println("¡Correcto!"); // Mensaje si la consonante es correcta.
        } else {
            output.println("Incorrecto."); // Mensaje si la consonante es incorrecta.
        }

        output.println("Frase actual: " + proverb); // Muestra la frase actual con los avances.
    }

    /**
     * Procesa la suposición de una vocal por parte del jugador.
     *
     * @param vowel Vocal adivinada.
     */
    public void guessVowel(char vowel) {
        if (gameOver) return; // Si el juego terminó, no se procesan más intentos.

        scoreManager.incrementTries(); // Incrementa el número de intentos.
        boolean correct = proverb.guessVowel(vowel); // Se verifica si la vocal es correcta.

        if (correct) {
            output.println("¡Correcto!"); // Mensaje si la vocal es correcta.
        } else {
            output.println("Incorrecto."); // Mensaje si la vocal es incorrecta.
        }

        output.println("Frase actual: " + proverb); // Muestra la frase actual con los avances.
    }

    /**
     * Permite al jugador intentar resolver el proverbio completo.
     *
     * @param phrase Proverbio que el jugador intenta resolver.
     */
    public void resolveProverb(String phrase) {
        if (proverb.resolveProverb(phrase)) {
            output.println("¡Felicidades! Has resuelto el proverbio.");
            scoreManager.addScore(); // Si resuelve correctamente, se añade la puntuación.
            scoreManager.printFinalScore(true); // Se imprime la puntuación final.
        } else {
            output.println("Lo siento, pero esa no es la frase correcta.");
            scoreManager.printFinalScore(false); // Si no resuelve correctamente, se muestra el mensaje de error y la puntuación.
        }
        gameOver = true; // Marca el juego como terminado.
        output.println("La partida ha terminado.");
    }

    /**
     * Verifica si la partida ha terminado.
     *
     * @return {@code true} si el juego ha terminado, {@code false} en caso contrario.
     */
    public boolean isGameOver() {
        return gameOver;
    }
}
