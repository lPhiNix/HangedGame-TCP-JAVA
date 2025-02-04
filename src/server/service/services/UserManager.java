package server.service.services;

import common.model.User;
import server.service.Service;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Servicio para gestionar usuarios en el servidor.
 * <p>
 * Permite registrar, autenticar y actualizar la información de los usuarios.
 * Los datos de los usuarios se almacenan en un archivo de texto para persistencia.
 * </p>
 *
 * @see User
 */
public class UserManager implements Service {
    private static final String FILE_PATH = "users.txt"; // Ruta del archivo donde se almacenan los datos de los usuarios.
    private final Map<String, User> users; // Mapa que almacena los usuarios registrados, indexados por su nombre de usuario.

    /**
     * Constructor que inicializa el gestor de usuarios y carga los datos desde el archivo.
     */
    public UserManager() {
        users = new HashMap<>();
        loadUsersFromFile();
    }

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param username Nombre de usuario.
     * @param password Contraseña del usuario.
     * @return {@code true} si el usuario se registró correctamente, {@code false} si el usuario ya existe.
     */
    public boolean registerUser(String username, String password) {
        if (users.containsKey(username)) {
            return false; // No se permite registrar un usuario existente
        }
        User user = new User(username, password);
        users.put(username, user);
        saveUsersToFile(); // Guarda los datos actualizados en el archivo
        return true;
    }

    /**
     * Autentica un usuario con su nombre y contraseña.
     *
     * @param username Nombre de usuario.
     * @param password Contraseña del usuario.
     * @return Instancia del usuario autenticado o {@code null} si las credenciales no son válidas.
     */
    public User authenticate(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    /**
     * Actualiza las estadísticas de un usuario en el sistema.
     * <p>
     * Se sobrescribe la información del usuario en la estructura de datos
     * y se guarda en el archivo para persistencia.
     * </p>
     *
     * @param newUser Usuario con los datos actualizados.
     */
    public void updateStatisticsUser(User newUser) {
        users.put(newUser.getUsername(), newUser);
        saveUsersToFile();
    }

    /**
     * Carga los usuarios almacenados desde el archivo de texto.
     * <p>
     * Cada usuario está almacenado en formato CSV: <br>
     * {@code username,password,score,wins,defeats}
     * </p>
     */
    private void loadUsersFromFile() {
        File file = new File(FILE_PATH);

        // Si el archivo no existe, no hay usuarios que cargar
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                // Se espera que cada línea tenga exactamente 5 valores
                if (parts.length == 5) {
                    String username = parts[0];
                    String password = parts[1];
                    int score = Integer.parseInt(parts[2]);
                    int wins = Integer.parseInt(parts[3]);
                    int defeats = Integer.parseInt(parts[4]);

                    // Se crea el objeto usuario y se asignan los valores
                    User user = new User(username, password);
                    user.addScore(score);
                    user.win(wins);
                    user.defeat(defeats);

                    // Se añade al mapa de usuarios
                    users.put(username, user);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de usuarios: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Guarda los datos de los usuarios en el archivo de texto.
     * <p>
     * Cada usuario se almacena en formato CSV:
     * {@code username,password,score,wins,defeats}
     * </p>
     */
    public void saveUsersToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (User user : users.values()) {
                writer.write(user.getUsername() + ","
                        + user.getPassword() + ","
                        + user.getScore() + ","
                        + user.getWins() + ","
                        + user.getDefeats());
                writer.newLine(); // Se añade una nueva línea después de cada usuario
            }
        } catch (IOException e) {
            System.err.println("Error al guardar el archivo de usuarios: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
