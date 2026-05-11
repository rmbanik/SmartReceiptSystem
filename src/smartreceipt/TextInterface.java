package smartreceipt;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class TextInterface {
    private final Scanner scanner = new Scanner(System.in);
    private final FileManager fileManager = new FileManager();
    private final AccountService accountService = new AccountService(fileManager);

    private ReceiptManager receiptManager;
    private User currentUser;

    public void start() {
        System.out.println("\n=== Text-Based Interface ===");

        while (currentUser == null) {
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.print("Choose: ");

            String choice = scanner.nextLine();

            try {
                if ("1".equals(choice)) {
                    register();
                } else if ("2".equals(choice)) {
                    login();
                } else {
                    System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        receiptManager = new ReceiptManager(fileManager.loadReceipts(currentUser.getUsername()));
        menu();
    }

    private void register() {
        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        boolean created = accountService.register(username, password);

        if (created) {
            System.out.println("Account created.");
        } else {
            System.out.println("Username already exists.");
        }
    }

    private void login() {
        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        currentUser = accountService.login(username, password);

        if (currentUser == null) {
            System.out.println("Login failed.");
        } else {
            System.out.println("Welcome, " + currentUser.getUsername());
        }
    }

    private void menu() {
        while (true) {
            System.out.println("\n1. Add receipt");
            System.out.println("2. View all receipts");
            System.out.println("3. Search keyword");
            System.out.println("4. Binary search by bill number");
            System.out.println("5. Sort by date");
            System.out.println("6. Sort by amount");
            System.out.println("7. Monthly total");
            System.out.println("8. Delete receipt");
            System.out.println("9. Export monthly report");
            System.out.println("0. Exit");
            System.out.print("Choose: ");

            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1":
                        addReceipt();
                        break;
                    case "2":
                        print(receiptManager.getReceipts());
                        break;
                    case "3":
                        searchKeyword();
                        break;
                    case "4":
                        binarySearch();
                        break;
                    case "5":
                        print(receiptManager.sortByDate());
                        break;
                    case "6":
                        print(receiptManager.sortByAmount());
                        break;
                    case "7":
                        monthlyTotal();
                        break;
                    case "8":
                        deleteReceipt();
                        break;
                    case "9":
                        exportReport();
                        break;
                    case "0":
                        save();
                        return;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void addReceipt() {
        Receipt receipt = readReceipt();
        receiptManager.addReceipt(receipt);
        save();
        System.out.println("Receipt added.");
    }

    private Receipt readReceipt() {
        System.out.print("Bill no: ");
        String billNo = scanner.nextLine();

        System.out.print("Date (YYYY-MM-DD): ");
        LocalDate date = LocalDate.parse(scanner.nextLine());

        System.out.print("Shop name: ");
        String shop = scanner.nextLine();

        System.out.print("Category: ");
        String category = scanner.nextLine();

        System.out.print("Amount: ");
        double amount = Double.parseDouble(scanner.nextLine());

        System.out.print("Note: ");
        String note = scanner.nextLine();

        return new Receipt(billNo, date, shop, category, amount, note);
    }

    private void searchKeyword() {
        System.out.print("Keyword: ");
        String keyword = scanner.nextLine();

        print(receiptManager.searchKeyword(keyword));
    }

    private void binarySearch() {
        System.out.print("Bill no: ");
        String billNo = scanner.nextLine();

        Receipt found = receiptManager.binarySearchByBillNo(billNo);

        if (found == null) {
            System.out.println("Receipt not found.");
        } else {
            System.out.println(found);
        }
    }

    private void monthlyTotal() {
        System.out.print("Year: ");
        int year = Integer.parseInt(scanner.nextLine());

        System.out.print("Month: ");
        int month = Integer.parseInt(scanner.nextLine());

        System.out.println("Total: $" + String.format("%.2f", receiptManager.getMonthlyTotal(year, month)));
        System.out.println("Category totals: " + receiptManager.getCategoryTotals(year, month));
    }

    private void deleteReceipt() {
        System.out.print("Bill no to delete: ");
        String billNo = scanner.nextLine();

        boolean deleted = receiptManager.deleteReceipt(billNo);
        save();

        if (deleted) {
            System.out.println("Deleted.");
        } else {
            System.out.println("Receipt not found.");
        }
    }

    private void exportReport() {
        System.out.print("Year: ");
        int year = Integer.parseInt(scanner.nextLine());

        System.out.print("Month: ");
        int month = Integer.parseInt(scanner.nextLine());

        System.out.println("Exported to: " + fileManager.exportMonthlyReport(
                currentUser.getUsername(),
                receiptManager.getReceipts(),
                year,
                month
        ));
    }

    private void print(List<Receipt> receipts) {
        if (receipts.isEmpty()) {
            System.out.println("No receipts found.");
        }

        for (Receipt receipt : receipts) {
            System.out.println(receipt);
        }
    }

    private void save() {
        fileManager.saveReceipts(currentUser.getUsername(), receiptManager.getReceipts());
    }
}