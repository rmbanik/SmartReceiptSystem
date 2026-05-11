public class Account extends User {
    private String username;
    private String password;
    private double monthlyGoal;

    public Account(String userId, String fullName, String email, String username, String password, double monthlyGoal) {
        super(userId, fullName, email);
        this.username = username;
        this.password = password;
        this.monthlyGoal = monthlyGoal;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public double getMonthlyGoal() {
        return monthlyGoal;
    }

    public void setMonthlyGoal(double monthlyGoal) {
        this.monthlyGoal = monthlyGoal;
    }

    @Override
    public void showRole() {
        System.out.println("Role: Account Holder");
    }

    @Override
    public String toString() {
        return "User ID: " + getUserId()
                + ", Name: " + getFullName()
                + ", Email: " + getEmail()
                + ", Username: " + username
                + ", Monthly Goal: $" + monthlyGoal;
    }
}