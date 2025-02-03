package server.service.services;

import common.model.User;
import server.service.Service;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class UserManager implements Service {
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

    public void updateStatatisticsUser(User newUser) {
        String username = newUser.getUsername();

        for (String thatName : users.keySet()) {
            if (thatName.equals(username)) {
                users.put(username, newUser);
            }
        }

        saveUsersToFile();
    }

    private void loadUsersFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    String username = parts[0];
                    String password = parts[1];
                    int score = Integer.parseInt(parts[2]);
                    int wins = Integer.parseInt(parts[3]);
                    int defeats = Integer.parseInt(parts[4]);
                    User user = new User(username, password);
                    user.addScore(score);
                    user.win(wins);
                    user.defeat(defeats);
                    users.put(username, user);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveUsersToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (User user : users.values()) {
                writer.write(user.getUsername() + ","
                        + user.getPassword() + ","
                        + user.getScore() + ","
                        + user.getWins() + ","
                        + user.getDefeats());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}