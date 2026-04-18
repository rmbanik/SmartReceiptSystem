import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileManager {

    public static void exportMonthlyReport(String fileName, String username, String monthPrefix,
                                           ReceiptManager manager, Account account) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {

            writer.println("===== Monthly Expense Report =====");
            writer.println("Username: " + username);
            writer.println("Month: " + monthPrefix);
            writer.println("Account Name: " + account.getFullName());
            writer.println("Email: " + account.getEmail());
            writer.println("Monthly Goal: $" + account.getMonthlyGoal());
            writer.println();

            writer.println("Receipts:");
            for (Receipt receipt : manager.getAllReceipts()) {
                if (receipt.getUsername().equalsIgnoreCase(username)
                        && receipt.getDate().startsWith(monthPrefix)) {
                    writer.println(receipt);
                }
            }

            writer.println();
            double total = manager.getMonthlySpending(username, monthPrefix);
            writer.println("Total Monthly Spending: $" + total);

            if (total < account.getMonthlyGoal()) {
                writer.println("Goal Status: Within goal");
            } else if (total == account.getMonthlyGoal()) {
                writer.println("Goal Status: Goal exactly reached");
            } else {
                writer.println("Goal Status: Goal exceeded");
            }

            writer.println("==================================");

            System.out.println("Monthly report exported successfully to " + fileName);

        } catch (IOException e) {
            System.out.println("Error writing report file: " + e.getMessage());
        }
    }
}