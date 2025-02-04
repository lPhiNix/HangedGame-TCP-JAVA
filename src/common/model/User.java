package common.model;

/**
 * Representa a un usuario dentro del sistema de juego.
 * <p>
 * Almacena la información del usuario, su puntuación y su historial de partidas ganadas y perdidas.
 * </p>
 */
public class User {
    private final String username; // Nombre de usuario.
    private final String password; // Contraseña del usuario.
    private int score; // Puntuación total del usuario.
    private int wins; // Número de partidas ganadas.
    private int defeats; // Número de partidas perdidas.

    /**
     * Crea un nuevo usuario con un nombre y contraseña.
     *
     * @param username Nombre del usuario.
     * @param password Contraseña del usuario.
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.score = 0;
        this.wins = 0;
        this.defeats = 0;
    }

    /** @return Nombre de usuario. */
    public String getUsername() {
        return username;
    }

    /** @return Contraseña del usuario. */
    public String getPassword() {
        return password;
    }

    /** @return Puntuación del usuario. */
    public int getScore() {
        return score;
    }

    /**
     * Agrega puntos a la puntuación del usuario.
     *
     * @param points Puntos a agregar.
     */
    public void addScore(int points) {
        this.score += points;
    }

    /** @return Número de partidas ganadas. */
    public int getWins() {
        return wins;
    }

    /** @return Número de partidas perdidas. */
    public int getDefeats() {
        return defeats;
    }

    /**
     * Incrementa las partidas ganadas.
     *
     * @param wins Número de victorias a agregar.
     */
    public void win(int wins) {
        this.wins += wins;
    }

    /**
     * Incrementa las partidas perdidas.
     *
     * @param defeats Número de derrotas a agregar.
     */
    public void defeat(int defeats) {
        this.defeats += defeats;
    }
}
