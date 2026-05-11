package smartreceipt;

import javax.swing.SwingUtilities;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Smart Receipt Management System");
        System.out.println("1. Open GUI");
        System.out.println("2. Open Text-Based Interface");
        System.out.print("Choose option: ");

        String option = scanner.nextLine().trim();

        if ("2".equals(option)) {
            new TextInterface().start();
        } else {
            SwingUtilities.invokeLater(() -> new SmartReceiptGUI().setVisible(true));
        }
    }
}