package common.game.score;

/**
 * Representa los diferentes niveles de puntuación en el juego.
 * <p>
 * Cada nivel tiene una cantidad de puntos asociados y un número mínimo de intentos
 * requeridos para alcanzarlo.
 * </p>
 */
public enum Score {
    MAX(150, 0), // Puntuación máxima (150 puntos) con 0 intentos mínimos.
    EXCELLENT(100, 5), // Puntuación excelente (100 puntos) con hasta 5 intentos.
    GOOD(70, 8), // Puntuación buena (70 puntos) con hasta 8 intentos.
    REGULAR(50, 12), // Puntuación regular (50 puntos) con hasta 12 intentos.
    BAD(0, 15); // Puntuación mala (0 puntos) para más de 12 intentos.

    private final int scoreAmount; // Cantidad de puntos otorgados en este nivel de puntuación.
    private final int minimumTries; // Número mínimo de intentos para obtener esta puntuación.

    /**
     * Constructor del enum Score.
     *
     * @param scoreAmount   Puntos otorgados.
     * @param minimumTries  Número mínimo de intentos para esta puntuación.
     */
    Score(int scoreAmount, int minimumTries) {
        this.scoreAmount = scoreAmount;
        this.minimumTries = minimumTries;
    }

    /**
     * Obtiene la cantidad de puntos de este nivel de puntuación.
     *
     * @return Cantidad de puntos.
     */
    public int getScoreAmount() {
        return scoreAmount;
    }

    /**
     * Obtiene el número mínimo de intentos para esta puntuación.
     *
     * @return Intentos mínimos requeridos.
     */
    public int getMinimumTries() {
        return minimumTries;
    }

    /**
     * Determina la puntuación correspondiente en función del número de intentos.
     *
     * @param tries Número de intentos realizados.
     * @return Nivel de puntuación correspondiente.
     */
    public static Score fromTries(int tries) {
        for (Score score : Score.values()) {
            if (tries <= score.minimumTries) {
                return score;
            }
        }
        return BAD;
    }
}
