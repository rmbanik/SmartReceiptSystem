import java.util.ArrayList;
import java.util.HashMap;

public class ReceiptManager {
    private ArrayList<Receipt> receipts;

    public ReceiptManager() {
        receipts = new ArrayList<>();
    }

    public void addReceipt(Receipt receipt) {
        receipts.add(receipt);
        System.out.println("Receipt added successfully.");
    }

    public ArrayList<Receipt> getAllReceipts() {
        return receipts;
    }

    public void viewAllReceipts(String username) {
        boolean found = false;

        for (Receipt receipt : receipts) {
            if (receipt.getUsername().equalsIgnoreCase(username)) {
                System.out.println(receipt);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No receipts found.");
        }
    }

    public Receipt searchByBillNumber(String username, String billNumber) {
        for (Receipt receipt : receipts) {
            if (receipt.getUsername().equalsIgnoreCase(username)
                    && receipt.getBillNumber().equalsIgnoreCase(billNumber)) {
                return receipt;
            }
        }
        return null;
    }

    public boolean deleteReceipt(String username, String billNumber) {
        Receipt receipt = searchByBillNumber(username, billNumber);
        if (receipt != null) {
            receipts.remove(receipt);
            return true;
        }
        return false;
    }

    public double getOverallSpending(String username) {
        double total = 0;

        for (Receipt receipt : receipts) {
            if (receipt.getUsername().equalsIgnoreCase(username)) {
                total += receipt.getAmount();
            }
        }

        return total;
    }

    public double getMonthlySpending(String username, String monthPrefix) {
        double total = 0;

        for (Receipt receipt : receipts) {
            if (receipt.getUsername().equalsIgnoreCase(username)
                    && receipt.getDate().startsWith(monthPrefix)) {
                total += receipt.getAmount();
            }
        }

        return total;
    }

    public double getCategorySpending(String username, String category) {
        double total = 0;

        for (Receipt receipt : receipts) {
            if (receipt.getUsername().equalsIgnoreCase(username)
                    && receipt.getCategory().equalsIgnoreCase(category)) {
                total += receipt.getAmount();
            }
        }

        return total;
    }

    public double getMonthlyCategorySpending(String username, String monthPrefix, String category) {
        double total = 0;

        for (Receipt receipt : receipts) {
            if (receipt.getUsername().equalsIgnoreCase(username)
                    && receipt.getDate().startsWith(monthPrefix)
                    && receipt.getCategory().equalsIgnoreCase(category)) {
                total += receipt.getAmount();
            }
        }

        return total;
    }

    public void showCategorySummary(String username) {
        HashMap<String, Double> summary = new HashMap<>();

        for (Receipt receipt : receipts) {
            if (receipt.getUsername().equalsIgnoreCase(username)) {
                String category = receipt.getCategory();
                summary.put(category, summary.getOrDefault(category, 0.0) + receipt.getAmount());
            }
        }

        if (summary.isEmpty()) {
            System.out.println("No category data found.");
            return;
        }

        System.out.println("\n----- Category Spending Summary -----");
        for (String category : summary.keySet()) {
            System.out.println(category + ": $" + summary.get(category));
        }
    }

    public void showMonthlyCategorySummary(String username, String monthPrefix) {
        HashMap<String, Double> summary = new HashMap<>();

        for (Receipt receipt : receipts) {
            if (receipt.getUsername().equalsIgnoreCase(username)
                    && receipt.getDate().startsWith(monthPrefix)) {
                String category = receipt.getCategory();
                summary.put(category, summary.getOrDefault(category, 0.0) + receipt.getAmount());
            }
        }

        if (summary.isEmpty()) {
            System.out.println("No monthly category data found.");
            return;
        }

        System.out.println("\n----- Monthly Category Spending Summary -----");
        for (String category : summary.keySet()) {
            System.out.println(category + ": $" + summary.get(category));
        }
    }
}