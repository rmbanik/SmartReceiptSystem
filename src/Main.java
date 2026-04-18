import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        ArrayList<Account> accounts = new ArrayList<>();
        ReceiptManager manager = new ReceiptManager();

        Account loggedInUser = null;
        int choice = 0;
        boolean running = true;

        while (running) {
            if (loggedInUser == null) {
                System.out.println("\n========== Smart Receipt & Expense Management System ==========");
                System.out.println("1. Create Account");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Enter your choice: ");

                try {
                    choice = sc.nextInt();
                    sc.nextLine();

                    switch (choice) {
                        case 1:
                            try {
                                System.out.print("Enter User ID: ");
                                String userId = sc.nextLine();

                                System.out.print("Enter Full Name: ");
                                String fullName = sc.nextLine();

                                System.out.print("Enter Email: ");
                                String email = sc.nextLine();

                                System.out.print("Enter Username: ");
                                String username = sc.nextLine();

                                boolean exists = false;
                                for (Account acc : accounts) {
                                    if (acc.getUsername().equalsIgnoreCase(username)) {
                                        exists = true;
                                        break;
                                    }
                                }

                                if (exists) {
                                    System.out.println("Username already exists.");
                                    break;
                                }

                                System.out.print("Enter Password: ");
                                String password = sc.nextLine();

                                System.out.print("Enter Monthly Goal/Budget: ");
                                double goal = sc.nextDouble();
                                sc.nextLine();

                                Account account = new Account(userId, fullName, email, username, password, goal);
                                accounts.add(account);

                                System.out.println("Account created successfully.");
                            } catch (InputMismatchException e) {
                                System.out.println("Invalid input.");
                                sc.nextLine();
                            }
                            break;

                        case 2:
                            System.out.print("Enter Username: ");
                            String username = sc.nextLine();

                            System.out.print("Enter Password: ");
                            String password = sc.nextLine();

                            boolean loginSuccess = false;
                            for (Account acc : accounts) {
                                if (acc.getUsername().equalsIgnoreCase(username)
                                        && acc.getPassword().equals(password)) {
                                    loggedInUser = acc;
                                    loginSuccess = true;
                                    break;
                                }
                            }

                            if (loginSuccess) {
                                System.out.println("Login successful. Welcome, " + loggedInUser.getFullName());
                            } else {
                                System.out.println("Invalid username or password.");
                            }
                            break;

                        case 3:
                            System.out.println("Exiting system. Goodbye.");
                            running = false;
                            break;

                        default:
                            System.out.println("Invalid choice.");
                    }

                } catch (InputMismatchException e) {
                    System.out.println("Please enter a valid number.");
                    sc.nextLine();
                }

            } else {
                System.out.println("\n========== User Menu ==========");
                System.out.println("1. Add Receipt");
                System.out.println("2. View All Receipts");
                System.out.println("3. Search Receipt by Bill Number");
                System.out.println("4. Update Receipt");
                System.out.println("5. Delete Receipt");
                System.out.println("6. Show Overall Spending");
                System.out.println("7. Show Monthly Spending");
                System.out.println("8. Show Spending by Category");
                System.out.println("9. Show Monthly Spending by Category");
                System.out.println("10. Show Category Summary");
                System.out.println("11. Show Monthly Category Summary");
                System.out.println("12. Check Goal Status");
                System.out.println("13. Download Monthly Expense Report");
                System.out.println("14. Change Monthly Goal");
                System.out.println("15. Logout");
                System.out.print("Enter your choice: ");

                try {
                    choice = sc.nextInt();
                    sc.nextLine();

                    switch (choice) {
                        case 1:
                            try {
                                System.out.print("Enter Receipt ID: ");
                                String receiptId = sc.nextLine();

                                System.out.print("Enter Bill Number: ");
                                String billNumber = sc.nextLine();

                                System.out.print("Enter Date (YYYY-MM-DD): ");
                                String date = sc.nextLine();

                                System.out.print("Enter Amount: ");
                                double amount = sc.nextDouble();
                                sc.nextLine();

                                System.out.print("Enter Shop/Business Name: ");
                                String shopName = sc.nextLine();

                                System.out.print("Enter Category (Food/Shopping/Rent/Transport/etc): ");
                                String category = sc.nextLine();

                                System.out.print("Enter Payment Method: ");
                                String paymentMethod = sc.nextLine();

                                System.out.print("Enter Notes: ");
                                String notes = sc.nextLine();

                                Receipt receipt = new Receipt(
                                        receiptId,
                                        loggedInUser.getUsername(),
                                        billNumber,
                                        date,
                                        amount,
                                        shopName,
                                        category,
                                        paymentMethod,
                                        notes
                                );

                                manager.addReceipt(receipt);

                            } catch (InputMismatchException e) {
                                System.out.println("Invalid amount.");
                                sc.nextLine();
                            }
                            break;

                        case 2:
                            manager.viewAllReceipts(loggedInUser.getUsername());
                            break;

                        case 3:
                            System.out.print("Enter Bill Number: ");
                            String searchBill = sc.nextLine();
                            Receipt found = manager.searchByBillNumber(loggedInUser.getUsername(), searchBill);
                            if (found != null) {
                                System.out.println(found);
                            } else {
                                System.out.println("Receipt not found.");
                            }
                            break;

                        case 4:
                            System.out.print("Enter Bill Number to update: ");
                            String updateBill = sc.nextLine();
                            Receipt updateReceipt = manager.searchByBillNumber(loggedInUser.getUsername(), updateBill);

                            if (updateReceipt != null) {
                                try {
                                    System.out.print("Enter New Date (YYYY-MM-DD): ");
                                    updateReceipt.setDate(sc.nextLine());

                                    System.out.print("Enter New Amount: ");
                                    updateReceipt.setAmount(sc.nextDouble());
                                    sc.nextLine();

                                    System.out.print("Enter New Shop/Business Name: ");
                                    updateReceipt.setShopName(sc.nextLine());

                                    System.out.print("Enter New Category: ");
                                    updateReceipt.setCategory(sc.nextLine());

                                    System.out.print("Enter New Payment Method: ");
                                    updateReceipt.setPaymentMethod(sc.nextLine());

                                    System.out.print("Enter New Notes: ");
                                    updateReceipt.setNotes(sc.nextLine());

                                    System.out.println("Receipt updated successfully.");
                                } catch (InputMismatchException e) {
                                    System.out.println("Invalid input.");
                                    sc.nextLine();
                                }
                            } else {
                                System.out.println("Receipt not found.");
                            }
                            break;

                        case 5:
                            System.out.print("Enter Bill Number to delete: ");
                            String deleteBill = sc.nextLine();
                            boolean deleted = manager.deleteReceipt(loggedInUser.getUsername(), deleteBill);
                            if (deleted) {
                                System.out.println("Receipt deleted successfully.");
                            } else {
                                System.out.println("Receipt not found.");
                            }
                            break;

                        case 6:
                            double overall = manager.getOverallSpending(loggedInUser.getUsername());
                            System.out.println("Overall Spending: $" + overall);
                            break;

                        case 7:
                            System.out.print("Enter month (YYYY-MM): ");
                            String month = sc.nextLine();
                            double monthly = manager.getMonthlySpending(loggedInUser.getUsername(), month);
                            System.out.println("Monthly Spending for " + month + ": $" + monthly);
                            break;

                        case 8:
                            System.out.print("Enter category: ");
                            String category = sc.nextLine();
                            double categoryTotal = manager.getCategorySpending(loggedInUser.getUsername(), category);
                            System.out.println("Total spending for category " + category + ": $" + categoryTotal);
                            break;

                        case 9:
                            System.out.print("Enter month (YYYY-MM): ");
                            String monthCat = sc.nextLine();
                            System.out.print("Enter category: ");
                            String cat = sc.nextLine();
                            double monthlyCategory = manager.getMonthlyCategorySpending(loggedInUser.getUsername(), monthCat, cat);
                            System.out.println("Monthly spending for " + cat + " in " + monthCat + ": $" + monthlyCategory);
                            break;

                        case 10:
                            manager.showCategorySummary(loggedInUser.getUsername());
                            break;

                        case 11:
                            System.out.print("Enter month (YYYY-MM): ");
                            String monthSummary = sc.nextLine();
                            manager.showMonthlyCategorySummary(loggedInUser.getUsername(), monthSummary);
                            break;

                        case 12:
                            System.out.print("Enter month (YYYY-MM): ");
                            String goalMonth = sc.nextLine();
                            double monthTotal = manager.getMonthlySpending(loggedInUser.getUsername(), goalMonth);
                            System.out.println("Monthly Goal: $" + loggedInUser.getMonthlyGoal());
                            System.out.println("Spent in " + goalMonth + ": $" + monthTotal);

                            if (monthTotal < loggedInUser.getMonthlyGoal()) {
                                System.out.println("Status: You are within your spending goal.");
                            } else if (monthTotal == loggedInUser.getMonthlyGoal()) {
                                System.out.println("Status: You have exactly reached your goal.");
                            } else {
                                System.out.println("Status: You have exceeded your spending goal.");
                            }
                            break;

                        case 13:
                            System.out.print("Enter month (YYYY-MM): ");
                            String exportMonth = sc.nextLine();
                            String fileName = loggedInUser.getUsername() + "_" + exportMonth + "_report.txt";
                            FileManager.exportMonthlyReport(fileName, loggedInUser.getUsername(), exportMonth, manager, loggedInUser);
                            break;

                        case 14:
                            try {
                                System.out.print("Enter new monthly goal: ");
                                double newGoal = sc.nextDouble();
                                sc.nextLine();
                                loggedInUser.setMonthlyGoal(newGoal);
                                System.out.println("Monthly goal updated successfully.");
                            } catch (InputMismatchException e) {
                                System.out.println("Invalid goal amount.");
                                sc.nextLine();
                            }
                            break;

                        case 15:
                            loggedInUser = null;
                            System.out.println("Logged out successfully.");
                            break;

                        default:
                            System.out.println("Invalid choice.");
                    }

                } catch (InputMismatchException e) {
                    System.out.println("Please enter a valid number.");
                    sc.nextLine();
                }
            }
        }

        sc.close();
    }
}