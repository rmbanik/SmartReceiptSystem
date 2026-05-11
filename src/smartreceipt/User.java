package smartreceipt;

public class User {
    private final String username;
    private final String password;

    public User(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }

        this.username = username.trim();
        this.password = password.trim();
    }

    public String getUsername() {
        return username;
    }

    public boolean passwordMatches(String typedPassword) {
        return password.equals(typedPassword);
    }

    public String toCsv() {
        return username + "," + password;
    }

    public static User fromCsv(String line) {
        String[] p = line.split(",", -1);

        if (p.length < 2) {
            throw new IllegalArgumentException("User row is incomplete");
        }

        return new User(p[0], p[1]);
    }
}