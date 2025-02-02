package common.util;

import common.model.User;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class UserManager {
    private static final String FILE_PATH = "users.txt";
    private final Map<String, User> users;

    public UserManager() {
        users = new HashMap<>();
        loadUsersFromFile();
    }

    public boolean registerUser(String username, String password) {
        if (users.containsKey(username)) return false;
        User user = new User(username, password);
        users.put(username, user);
        saveUsersToFile();
        return true;
    }

    public User authenticate(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    private void loadUsersFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String username = parts[0];
                    String password = parts[1];
                    int score = Integer.parseInt(parts[2]);
                    User user = new User(username, password);
                    user.addScore(score);
                    users.put(username, user);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveUsersToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (User user : users.values()) {
                writer.write(user.getUsername() + "," + user.getPassword() + "," + user.getScore());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}