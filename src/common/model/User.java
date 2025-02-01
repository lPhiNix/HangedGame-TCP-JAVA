package common.model;

public class User {
    private String username;
    private String password;
    private int score;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.score = 0;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public int getScore() { return score; }
    public void addScore(int points) { this.score += points; }
}
