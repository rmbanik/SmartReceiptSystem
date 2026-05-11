package smartreceipt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private final Path dataDir;

    public FileManager() {
        this(Paths.get("data"));
    }

    public FileManager(Path dataDir) {
        this.dataDir = dataDir;

        try {
            Files.createDirectories(dataDir);
        } catch (IOException e) {
            throw new RuntimeException("Could not create data folder", e);
        }
    }

    public List<User> loadUsers() {
        Path file = dataDir.resolve("users.csv");
        List<User> users = new ArrayList<>();

        if (!Files.exists(file)) {
            return users;
        }

        try (BufferedReader br = Files.newBufferedReader(file)) {
            String line;

            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    users.add(User.fromCsv(line));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not load users", e);
        }

        return users;
    }

    public void saveUsers(List<User> users) {
        Path file = dataDir.resolve("users.csv");

        try (BufferedWriter bw = Files.newBufferedWriter(file)) {
            for (User u : users) {
                bw.write(u.toCsv());
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not save users", e);
        }
    }

    public List<Receipt> loadReceipts(String username) {
        Path file = receiptFile(username);
        List<Receipt> receipts = new ArrayList<>();

        if (!Files.exists(file)) {
            return receipts;
        }

        try (BufferedReader br = Files.newBufferedReader(file)) {
            String line;

            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    receipts.add(Receipt.fromCsv(line));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not load receipts", e);
        }

        return receipts;
    }

    public void saveReceipts(String username, List<Receipt> receipts) {
        Path file = receiptFile(username);

        try (BufferedWriter bw = Files.newBufferedWriter(file)) {
            for (Receipt r : receipts) {
                bw.write(r.toCsv());
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not save receipts", e);
        }
    }

    public Path exportMonthlyReport(String username, List<Receipt> receipts, int year, int month) {
        Path file = dataDir.resolve(username + "_monthly_report_" + year + "_" + month + ".txt");

        try (BufferedWriter bw = Files.newBufferedWriter(file)) {
            bw.write("Smart Receipt Monthly Report");
            bw.newLine();
            bw.write("User: " + username + " | Month: " + year + "-" + String.format("%02d", month));
            bw.newLine();
            bw.write("--------------------------------------------------");
            bw.newLine();

            double total = 0;

            for (Receipt r : receipts) {
                if (r.getDate().getYear() == year && r.getDate().getMonthValue() == month) {
                    bw.write(r.toString());
                    bw.newLine();
                    total += r.getAmount();
                }
            }

            bw.write("--------------------------------------------------");
            bw.newLine();
            bw.write("Total: $" + String.format("%.2f", total));

        } catch (IOException e) {
            throw new RuntimeException("Could not export report", e);
        }

        return file;
    }

    private Path receiptFile(String username) {
        return dataDir.resolve(username + "_receipts.csv");
    }
}