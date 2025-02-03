package common.model;

public class User {
    private String username;
    private String password;
    private int score;
    private int wins;
    private int defeats;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.score = 0;
        this.wins = 0;
        this.defeats = 0;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public int getScore() { return score; }
    public void addScore(int points) { this.score += points; }

    public int getWins() {
        return wins;
    }

    public int getDefeats() {
        return defeats;
    }

    public void win(int wins) {
        this.wins += wins;
    }
    public void defeat(int defeats) {
        this.defeats += defeats;
    }
}
