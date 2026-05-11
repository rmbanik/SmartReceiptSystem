package smartreceipt;

import java.time.LocalDate;
import java.util.*;

public class ReceiptManager {
    private final List<Receipt> receipts;

    public ReceiptManager(List<Receipt> receipts) {
        this.receipts = receipts == null ? new ArrayList<>() : receipts;
    }

    public List<Receipt> getReceipts() {
        return receipts;
    }

    public void addReceipt(Receipt receipt) {
        validate(receipt);

        if (findIndexByBillNo(receipt.getBillNo()) >= 0) {
            throw new IllegalArgumentException("Bill number already exists");
        }

        receipts.add(receipt);
    }

    public boolean updateReceipt(String billNo, Receipt updated) {
        validate(updated);

        int index = findIndexByBillNo(billNo);

        if (index < 0) {
            return false;
        }

        receipts.set(index, updated);
        return true;
    }

    public boolean deleteReceipt(String billNo) {
        int index = findIndexByBillNo(billNo);

        if (index < 0) {
            return false;
        }

        receipts.remove(index);
        return true;
    }

    public List<Receipt> searchKeyword(String keyword) {
        String k = keyword == null ? "" : keyword.toLowerCase();
        List<Receipt> result = new ArrayList<>();

        for (Receipt r : receipts) {
            if (r.getBillNo().toLowerCase().contains(k)
                    || r.getShopName().toLowerCase().contains(k)
                    || r.getCategory().toLowerCase().contains(k)
                    || r.getNote().toLowerCase().contains(k)) {
                result.add(r);
            }
        }

        return result;
    }

    public Receipt binarySearchByBillNo(String billNo) {
        List<Receipt> sorted = sortByBillNo();

        int low = 0;
        int high = sorted.size() - 1;

        while (low <= high) {
            int mid = (low + high) / 2;
            int cmp = sorted.get(mid).getBillNo().compareToIgnoreCase(billNo);

            if (cmp == 0) {
                return sorted.get(mid);
            }

            if (cmp < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return null;
    }

    public List<Receipt> sortByDate() {
        return mergeSort(receipts, Comparator.comparing(Receipt::getDate));
    }

    public List<Receipt> sortByAmount() {
        return mergeSort(receipts, Comparator.comparingDouble(Receipt::getAmount));
    }

    public List<Receipt> sortByShop() {
        return mergeSort(receipts, Comparator.comparing(Receipt::getShopName, String.CASE_INSENSITIVE_ORDER));
    }

    public List<Receipt> sortByBillNo() {
        return mergeSort(receipts, Comparator.comparing(Receipt::getBillNo, String.CASE_INSENSITIVE_ORDER));
    }

    public double getMonthlyTotal(int year, int month) {
        double total = 0;

        for (Receipt r : receipts) {
            if (r.getDate().getYear() == year && r.getDate().getMonthValue() == month) {
                total += r.getAmount();
            }
        }

        return total;
    }

    public Map<String, Double> getCategoryTotals(int year, int month) {
        Map<String, Double> totals = new TreeMap<>();

        for (Receipt r : receipts) {
            if (r.getDate().getYear() == year && r.getDate().getMonthValue() == month) {
                totals.put(r.getCategory(), totals.getOrDefault(r.getCategory(), 0.0) + r.getAmount());
            }
        }

        return totals;
    }

    private int findIndexByBillNo(String billNo) {
        for (int i = 0; i < receipts.size(); i++) {
            if (receipts.get(i).getBillNo().equalsIgnoreCase(billNo)) {
                return i;
            }
        }

        return -1;
    }

    private void validate(Receipt receipt) {
        if (receipt == null) {
            throw new IllegalArgumentException("Receipt is required");
        }

        if (receipt.getBillNo() == null || receipt.getBillNo().trim().isEmpty()) {
            throw new IllegalArgumentException("Bill number is required");
        }

        if (receipt.getDate() == null || receipt.getDate().isAfter(LocalDate.now().plusDays(1))) {
            throw new IllegalArgumentException("Valid date is required");
        }

        if (receipt.getShopName() == null || receipt.getShopName().trim().isEmpty()) {
            throw new IllegalArgumentException("Shop name is required");
        }

        if (receipt.getCategory() == null || receipt.getCategory().trim().isEmpty()) {
            throw new IllegalArgumentException("Category is required");
        }

        if (receipt.getAmount() <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
    }

    private List<Receipt> mergeSort(List<Receipt> data, Comparator<Receipt> comparator) {
        if (data.size() <= 1) {
            return new ArrayList<>(data);
        }

        int mid = data.size() / 2;

        List<Receipt> left = mergeSort(new ArrayList<>(data.subList(0, mid)), comparator);
        List<Receipt> right = mergeSort(new ArrayList<>(data.subList(mid, data.size())), comparator);

        return merge(left, right, comparator);
    }

    private List<Receipt> merge(List<Receipt> left, List<Receipt> right, Comparator<Receipt> comparator) {
        List<Receipt> merged = new ArrayList<>();

        int i = 0;
        int j = 0;

        while (i < left.size() && j < right.size()) {
            if (comparator.compare(left.get(i), right.get(j)) <= 0) {
                merged.add(left.get(i));
                i++;
            } else {
                merged.add(right.get(j));
                j++;
            }
        }

        while (i < left.size()) {
            merged.add(left.get(i));
            i++;
        }

        while (j < right.size()) {
            merged.add(right.get(j));
            j++;
        }

        return merged;
    }
}