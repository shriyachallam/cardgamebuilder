package oogasalad.builder.backend.firebase;

public class PlayerBioRecord {

    private String username;
    private int lost;
    private int made;
    private int won;
    private UserRole role;
    public enum UserRole{
        ADMIN,
        MEMBER,
        PREMIUM,
        GUEST
    }

    public PlayerBioRecord(String username, int lost, int made, int won, UserRole role) {
        this.username = username;
        this.lost = lost;
        this.made = made;
        this.won = won;
        this.role = role;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setLost(int lost) {
        this.lost = lost;
    }

    public void setMade(int made) {
        this.made = made;
    }

    public void setWon(int won) {
        this.won = won;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public int getLost() {
        return lost;
    }

    public int getMade() {
        return made;
    }

    public int getWon() {
        return won;
    }

    public UserRole getRole() {
        return role;
    }
}
