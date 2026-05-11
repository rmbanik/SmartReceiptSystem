package smartreceipt;

import java.util.List;

public class AccountService {
    private final FileManager fileManager;
    private final List<User> users;

    public AccountService(FileManager fileManager) {
        this.fileManager = fileManager;
        this.users = fileManager.loadUsers();
    }

    public boolean register(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username.trim())) {
                return false;
            }
        }

        users.add(new User(username, password));
        fileManager.saveUsers(users);
        return true;
    }

    public User login(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username.trim()) && user.passwordMatches(password)) {
                return user;
            }
        }

        return null;
    }
}