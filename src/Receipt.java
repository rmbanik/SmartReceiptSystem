public class Receipt {
    private String receiptId;
    private String username;
    private String billNumber;
    private String date;
    private double amount;
    private String shopName;
    private String category;
    private String paymentMethod;
    private String notes;

    public Receipt(String receiptId, String username, String billNumber, String date, double amount,
                   String shopName, String category, String paymentMethod, String notes) {
        this.receiptId = receiptId;
        this.username = username;
        this.billNumber = billNumber;
        this.date = date;
        this.amount = amount;
        this.shopName = shopName;
        this.category = category;
        this.paymentMethod = paymentMethod;
        this.notes = notes;
    }

    public String getReceiptId() {
        return receiptId;
    }

    public String getUsername() {
        return username;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public String getDate() {
        return date;
    }

    public double getAmount() {
        return amount;
    }

    public String getShopName() {
        return shopName;
    }

    public String getCategory() {
        return category;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getNotes() {
        return notes;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "Receipt ID: " + receiptId
                + ", Username: " + username
                + ", Bill No: " + billNumber
                + ", Date: " + date
                + ", Amount: $" + amount
                + ", Shop: " + shopName
                + ", Category: " + category
                + ", Payment: " + paymentMethod
                + ", Notes: " + notes;
    }
}