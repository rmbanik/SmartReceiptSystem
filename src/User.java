public abstract class User {
    private String userId;
    private String fullName;
    private String email;

    public User(String userId, String fullName, String email) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public abstract void showRole();
}