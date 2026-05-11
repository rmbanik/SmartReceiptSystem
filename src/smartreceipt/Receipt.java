package smartreceipt;

import java.time.LocalDate;

public class Receipt {
    private String billNo;
    private LocalDate date;
    private String shopName;
    private String category;
    private double amount;
    private String note;

    public Receipt(String billNo, LocalDate date, String shopName, String category, double amount, String note) {
        this.billNo = billNo;
        this.date = date;
        this.shopName = shopName;
        this.category = category;
        this.amount = amount;
        this.note = note == null ? "" : note;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note == null ? "" : note;
    }

    public String toCsv() {
        return safe(billNo) + "," + date + "," + safe(shopName) + "," + safe(category) + "," + amount + "," + safe(note);
    }

    public static Receipt fromCsv(String line) {
        String[] p = line.split(",", -1);
        if (p.length < 6) {
            throw new IllegalArgumentException("Receipt row is incomplete");
        }

        return new Receipt(
                unsaf(p[0]),
                LocalDate.parse(p[1]),
                unsaf(p[2]),
                unsaf(p[3]),
                Double.parseDouble(p[4]),
                unsaf(p[5])
        );
    }

    private static String safe(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\").replace(",", "\\c");
    }

    private static String unsaf(String value) {
        return value.replace("\\c", ",").replace("\\\\", "\\");
    }


    public String toString() {
        return billNo + " | " + date + " | " + shopName + " | " + category + " | $" + String.format("%.2f", amount);
    }
}