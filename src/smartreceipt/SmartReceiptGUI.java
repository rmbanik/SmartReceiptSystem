package smartreceipt;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class SmartReceiptGUI extends JFrame {
    private final FileManager fileManager = new FileManager();
    private final AccountService accountService = new AccountService(fileManager);

    private User currentUser;
    private ReceiptManager receiptManager;

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel root = new JPanel(cardLayout);

    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField searchField;
    private JLabel statusLabel;

    public SmartReceiptGUI() {
        setTitle("Smart Receipt Management System");
        setSize(950, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        root.add(loginPanel(), "login");
        root.add(dashboardPanel(), "dashboard");

        add(root);
    }

    private JPanel loginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Smart Receipt Login", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));

        JTextField username = new JTextField(20);
        JPasswordField password = new JPasswordField(20);

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Create Account");

        JLabel message = new JLabel(" ", SwingConstants.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        panel.add(new JLabel("Username"), gbc);

        gbc.gridx = 1;
        panel.add(username, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Password"), gbc);

        gbc.gridx = 1;
        panel.add(password, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(loginButton, gbc);

        gbc.gridx = 1;
        panel.add(registerButton, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        panel.add(message, gbc);

        loginButton.addActionListener(e -> {
            try {
                currentUser = accountService.login(username.getText(), new String(password.getPassword()));

                if (currentUser == null) {
                    message.setText("Invalid username or password.");
                } else {
                    receiptManager = new ReceiptManager(fileManager.loadReceipts(currentUser.getUsername()));
                    refreshTable(receiptManager.getReceipts());
                    status("Logged in as " + currentUser.getUsername());
                    cardLayout.show(root, "dashboard");
                }

            } catch (Exception ex) {
                message.setText(ex.getMessage());
            }
        });

        registerButton.addActionListener(e -> {
            try {
                boolean created = accountService.register(username.getText(), new String(password.getPassword()));

                if (created) {
                    message.setText("Account created. You can login now.");
                } else {
                    message.setText("Username already exists.");
                }

            } catch (Exception ex) {
                message.setText(ex.getMessage());
            }
        });

        return panel;
    }

    private JPanel dashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel title = new JLabel("Receipt Dashboard");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        panel.add(title, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{
                "Bill No", "Date", "Shop", "Category", "Amount", "Note"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(25);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel controls = new JPanel(new GridLayout(3, 1, 5, 5));
        controls.add(firstButtonRow());
        controls.add(secondButtonRow());

        statusLabel = new JLabel("Ready");
        controls.add(statusLabel);

        panel.add(controls, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel firstButtonRow() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton add = new JButton("Add");
        JButton edit = new JButton("Edit Selected");
        JButton delete = new JButton("Delete Selected");
        JButton all = new JButton("View All");
        JButton logout = new JButton("Logout");

        row.add(add);
        row.add(edit);
        row.add(delete);
        row.add(all);
        row.add(logout);

        add.addActionListener(e -> openReceiptDialog(null));
        edit.addActionListener(e -> editSelected());
        delete.addActionListener(e -> deleteSelected());

        all.addActionListener(e -> refreshTable(receiptManager.getReceipts()));

        logout.addActionListener(e -> {
            saveReceipts();
            currentUser = null;
            cardLayout.show(root, "login");
        });

        return row;
    }

    private JPanel secondButtonRow() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));

        searchField = new JTextField(18);

        JButton search = new JButton("Search Keyword");
        JButton binarySearch = new JButton("Find Bill No");

        JComboBox<String> sortBox = new JComboBox<>(new String[]{
                "Sort by Date",
                "Sort by Amount",
                "Sort by Shop",
                "Sort by Bill No"
        });

        JButton monthly = new JButton("Monthly Total");
        JButton export = new JButton("Export Monthly Report");

        row.add(new JLabel("Search:"));
        row.add(searchField);
        row.add(search);
        row.add(binarySearch);
        row.add(sortBox);
        row.add(monthly);
        row.add(export);

        search.addActionListener(e -> refreshTable(receiptManager.searchKeyword(searchField.getText())));

        binarySearch.addActionListener(e -> {
            Receipt found = receiptManager.binarySearchByBillNo(searchField.getText());

            if (found == null) {
                showMessage("Receipt not found.");
            } else {
                refreshTable(List.of(found));
            }
        });

        sortBox.addActionListener(e -> {
            String selected = (String) sortBox.getSelectedItem();

            if (selected.contains("Date")) {
                refreshTable(receiptManager.sortByDate());
            } else if (selected.contains("Amount")) {
                refreshTable(receiptManager.sortByAmount());
            } else if (selected.contains("Shop")) {
                refreshTable(receiptManager.sortByShop());
            } else {
                refreshTable(receiptManager.sortByBillNo());
            }
        });

        monthly.addActionListener(e -> showMonthlyTotal());
        export.addActionListener(e -> exportReport());

        return row;
    }

    private void openReceiptDialog(Receipt existing) {
        JTextField billNo = new JTextField(existing == null ? "" : existing.getBillNo());
        JTextField date = new JTextField(existing == null ? LocalDate.now().toString() : existing.getDate().toString());
        JTextField shop = new JTextField(existing == null ? "" : existing.getShopName());
        JTextField category = new JTextField(existing == null ? "" : existing.getCategory());
        JTextField amount = new JTextField(existing == null ? "" : String.valueOf(existing.getAmount()));
        JTextField note = new JTextField(existing == null ? "" : existing.getNote());

        JPanel form = new JPanel(new GridLayout(6, 2, 5, 5));

        form.add(new JLabel("Bill No"));
        form.add(billNo);

        form.add(new JLabel("Date (YYYY-MM-DD)"));
        form.add(date);

        form.add(new JLabel("Shop"));
        form.add(shop);

        form.add(new JLabel("Category"));
        form.add(category);

        form.add(new JLabel("Amount"));
        form.add(amount);

        form.add(new JLabel("Note"));
        form.add(note);

        int option = JOptionPane.showConfirmDialog(
                this,
                form,
                existing == null ? "Add Receipt" : "Edit Receipt",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (option == JOptionPane.OK_OPTION) {
            try {
                Receipt receipt = new Receipt(
                        billNo.getText(),
                        LocalDate.parse(date.getText()),
                        shop.getText(),
                        category.getText(),
                        Double.parseDouble(amount.getText()),
                        note.getText()
                );

                if (existing == null) {
                    receiptManager.addReceipt(receipt);
                } else {
                    receiptManager.updateReceipt(existing.getBillNo(), receipt);
                }

                saveReceipts();
                refreshTable(receiptManager.getReceipts());

                if (existing == null) {
                    status("Receipt added.");
                } else {
                    status("Receipt updated.");
                }

            } catch (Exception ex) {
                showMessage("Error: " + ex.getMessage());
            }
        }
    }

    private void editSelected() {
        int row = table.getSelectedRow();

        if (row < 0) {
            showMessage("Please select a receipt first.");
            return;
        }

        String billNo = String.valueOf(tableModel.getValueAt(row, 0));
        Receipt receipt = receiptManager.binarySearchByBillNo(billNo);

        if (receipt != null) {
            openReceiptDialog(receipt);
        }
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();

        if (row < 0) {
            showMessage("Please select a receipt first.");
            return;
        }

        String billNo = String.valueOf(tableModel.getValueAt(row, 0));

        int confirm = JOptionPane.showConfirmDialog(this, "Delete receipt " + billNo + "?");

        if (confirm == JOptionPane.YES_OPTION) {
            receiptManager.deleteReceipt(billNo);
            saveReceipts();
            refreshTable(receiptManager.getReceipts());
            status("Receipt deleted.");
        }
    }

    private void showMonthlyTotal() {
        String y = JOptionPane.showInputDialog(this, "Year", String.valueOf(LocalDate.now().getYear()));
        String m = JOptionPane.showInputDialog(this, "Month", String.valueOf(LocalDate.now().getMonthValue()));

        if (y == null || m == null) {
            return;
        }

        int year = Integer.parseInt(y);
        int month = Integer.parseInt(m);

        StringBuilder text = new StringBuilder();

        text.append("Monthly total: $")
                .append(String.format("%.2f", receiptManager.getMonthlyTotal(year, month)))
                .append("\n\nCategory totals:\n");

        for (Map.Entry<String, Double> entry : receiptManager.getCategoryTotals(year, month).entrySet()) {
            text.append(entry.getKey())
                    .append(": $")
                    .append(String.format("%.2f", entry.getValue()))
                    .append("\n");
        }

        showMessage(text.toString());
    }

    private void exportReport() {
        String y = JOptionPane.showInputDialog(this, "Year", String.valueOf(LocalDate.now().getYear()));
        String m = JOptionPane.showInputDialog(this, "Month", String.valueOf(LocalDate.now().getMonthValue()));

        if (y == null || m == null) {
            return;
        }

        Path path = fileManager.exportMonthlyReport(
                currentUser.getUsername(),
                receiptManager.getReceipts(),
                Integer.parseInt(y),
                Integer.parseInt(m)
        );

        showMessage("Report exported to " + path.toAbsolutePath());
    }

    private void refreshTable(List<Receipt> receipts) {
        tableModel.setRowCount(0);

        for (Receipt r : receipts) {
            tableModel.addRow(new Object[]{
                    r.getBillNo(),
                    r.getDate(),
                    r.getShopName(),
                    r.getCategory(),
                    String.format("%.2f", r.getAmount()),
                    r.getNote()
            });
        }

        status("Showing " + receipts.size() + " receipt(s).");
    }

    private void saveReceipts() {
        if (currentUser != null && receiptManager != null) {
            fileManager.saveReceipts(currentUser.getUsername(), receiptManager.getReceipts());
        }
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    private void status(String message) {
        if (statusLabel != null) {
            statusLabel.setText(message);
        }
    }
}