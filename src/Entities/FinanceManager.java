/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entities;

import java.util.List;
import java.util.ArrayList;

import Utility.FileManager;
import Utility.UserRoles;
import Utility.Remark;
import Utility.Status;
/**
 *
 * @author Sheng Ting
 */
public class FinanceManager extends User {
    private final FileManager fileManager;

    public FinanceManager(String userId, String username, String password) {
        super(userId, username, password, UserRoles.FINANCE_MANAGER);
        this.fileManager = new FileManager();
    }

    public String approvePurchaseOrder(String poId, int newQuantity, String newSupplierCode) throws IllegalArgumentException {
        if (poId == null || poId.trim().isEmpty()) {
            throw new IllegalArgumentException("PO ID cannot be empty");
        }
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }

        List<PurchaseOrder> poList = fileManager.readFile(
            fileManager.getPoFilePath(),
            line -> {
                String[] data = line.split(",");
                if (data.length < 11) return null;
                try {
                    return new PurchaseOrder(
                        data[0], data[1], data[2], data[3],
                        Integer.parseInt(data[4]), data[5], data[6], data[7],
                        Status.valueOf(data[8]), Double.parseDouble(data[9]),
                        Remark.valueOf(data[10])
                    );
                } catch (Exception e) {
                    return null;
                }
            }
        );

        for (PurchaseOrder po : poList) {
            if (po != null && po.getPoId().equals(poId)) {
                if (!po.getStatus().equals(Status.PENDING)) {
                    return "Purchase Order " + poId + " is already " + po.getStatus();
                }

                if (newSupplierCode != null && !newSupplierCode.isEmpty() && !newSupplierCode.equals(po.getSupplierCode())) {
                    List<ItemSupply> itemSupplies = fileManager.readFile(
                        fileManager.getItemSupplyFilePath(),
                        line -> {
                            String[] data = line.split(",");
                            if (data.length < 4) return null;
                            try {
                                return new ItemSupply(data[0], data[1], data[2], Double.parseDouble(data[3]));
                            } catch (Exception e) {
                                return null;
                            }
                        }
                    );
                    boolean validSupplier = false;
                    for (ItemSupply supply : itemSupplies) {
                        if (supply != null && 
                            supply.getItemCode().equals(po.getItemCode()) && 
                            supply.getSupplierCode().equals(newSupplierCode)) {
                            validSupplier = true;
                            break;
                        }
                    }
                    if (!validSupplier) {
                        return "Supplier " + newSupplierCode + " does not supply item " + po.getItemCode();
                    }
                    po.setSupplierCode(newSupplierCode);
                }

                if (newQuantity > 0) {
                    po.setQuantity(newQuantity);
                }

                po.setStatus(Status.APPROVED);
                boolean success = fileManager.updateToFile(
                    po, fileManager.getPoFilePath(),
                    PurchaseOrder::getPoId, PurchaseOrder::toString,
                    line -> {
                        String[] data = line.split(",");
                        if (data.length < 11) return null;
                        try {
                            return new PurchaseOrder(
                                data[0], data[1], data[2], data[3],
                                Integer.parseInt(data[4]), data[5], data[6], data[7],
                                Status.valueOf(data[8]), Double.parseDouble(data[9]),
                                Remark.valueOf(data[10])
                            );
                        } catch (Exception e) {
                            return null;
                        }
                    }
                );
                return success ? "Purchase Order " + poId + " approved successfully" : "Failed to update Purchase Order " + poId;
            }
        }
        return "Purchase Order " + poId + " not found";
    }

    public String rejectPurchaseOrder(String poId) throws IllegalArgumentException {
        if (poId == null || poId.trim().isEmpty()) {
            throw new IllegalArgumentException("PO ID cannot be empty");
        }

        List<PurchaseOrder> poList = fileManager.readFile(
            fileManager.getPoFilePath(),
            line -> {
                String[] data = line.split(",");
                if (data.length < 11) return null;
                try {
                    return new PurchaseOrder(
                        data[0], data[1], data[2], data[3],
                        Integer.parseInt(data[4]), data[5], data[6], data[7],
                        Status.valueOf(data[8]), Double.parseDouble(data[9]),
                        Remark.valueOf(data[10])
                    );
                } catch (Exception e) {
                    return null;
                }
            }
        );

        for (PurchaseOrder po : poList) {
            if (po != null && po.getPoId().equals(poId)) {
                if (!po.getStatus().equals(Status.PENDING)) {
                    return "Purchase Order " + poId + " is already " + po.getStatus();
                }

                po.setStatus(Status.REJECTED);
                po.setRemark(Remark.REJECTED_BY_FINANCE_MANAGER);
                boolean success = fileManager.updateToFile(
                    po, fileManager.getPoFilePath(),
                    PurchaseOrder::getPoId, PurchaseOrder::toString,
                    line -> {
                        String[] data = line.split(",");
                        if (data.length < 11) return null;
                        try {
                            return new PurchaseOrder(
                                data[0], data[1], data[2], data[3],
                                Integer.parseInt(data[4]), data[5], data[6], data[7],
                                Status.valueOf(data[8]), Double.parseDouble(data[9]),
                                Remark.valueOf(data[10])
                            );
                        } catch (Exception e) {
                            return null;
                        }
                    }
                );
                return success ? "Purchase Order " + poId + " rejected successfully" : "Failed to reject Purchase Order " + poId;
            }
        }
        return "Purchase Order " + poId + " not found";
    }
    
    public String payPurchaseOrder(String poId) throws IllegalArgumentException {
        if (poId == null || poId.trim().isEmpty()) {
            throw new IllegalArgumentException("PO ID cannot be empty");
        }

        List<PurchaseOrder> poList = fileManager.readFile(
            fileManager.getPoFilePath(),
            line -> {
                String[] data = line.split(",");
                if (data.length < 11) return null;
                try {
                    return new PurchaseOrder(
                        data[0], data[1], data[2], data[3],
                        Integer.parseInt(data[4]), data[5], data[6], data[7],
                        Status.valueOf(data[8]), Double.parseDouble(data[9]),
                        Remark.valueOf(data[10])
                    );
                } catch (Exception e) {
                    return null;
                }
            }
        );

        for (PurchaseOrder po : poList) {
            if (po != null && po.getPoId().equals(poId)) {
                if (!po.getStatus().equals(Status.RECEIVED)) {
                    return "Only RECEIVED Purchase Order can be selected.";
                }

                po.setStatus(Status.PAID);
                boolean success = fileManager.updateToFile(
                    po, fileManager.getPoFilePath(),
                    PurchaseOrder::getPoId, PurchaseOrder::toString,
                    line -> {
                        String[] data = line.split(",");
                        if (data.length < 11) return null;
                        try {
                            return new PurchaseOrder(
                                data[0], data[1], data[2], data[3],
                                Integer.parseInt(data[4]), data[5], data[6], data[7],
                                Status.valueOf(data[8]), Double.parseDouble(data[9]),
                                Remark.valueOf(data[10])
                            );
                        } catch (Exception e) {
                            return null;
                        }
                    }
                );
                return success ? "Payment for Purchase Order: " + poId + " was successful." : "Failed to process payment for Purchase Order " + poId;
            }
        }
        return "Purchase Order " + poId + " not found";
    }

    public List<PurchaseOrder> getPurchaseOrders() {
        List<PurchaseOrder> poList = fileManager.readFile(
        fileManager.getPoFilePath(),
        line -> {
            String[] data = line.split(",");
            if (data.length < 11) {
                System.err.println("Invalid PO line: " + line);
                return null;
            }
            try {
                PurchaseOrder po = new PurchaseOrder(
                    data[0], data[1], data[2], data[3],
                    Integer.parseInt(data[4]), data[5], data[6], data[7],
                    Status.valueOf(data[8]), Double.parseDouble(data[9]),
                    Remark.valueOf(data[10])
                );
                System.out.println("Parsed PO: " + po.getPoId());
                return po;
            } catch (Exception e) {
                System.err.println("Error parsing PO line: " + line + " | Error: " + e.getMessage());
                return null;
            }
        }
    );
    System.out.println("Total POs read: " + poList.size());
    return poList;
    }
    
    public List<PurchaseRequisition> getPurchaseRequisitions() {
        List<PurchaseRequisition> prList = fileManager.readFile(
            fileManager.getPrFilePath(),
            line -> {
                String[] data = line.split(",");
                if (data.length < 7) {
                    System.err.println("Invalid PR line: " + line);
                    return null;
                }
                try {
                    PurchaseRequisition pr = new PurchaseRequisition(
                        data[0], data[1], data[2],
                        Integer.parseInt(data[3]), data[4], data[5],
                        Status.valueOf(data[6])
                    );
                    System.out.println("Parsed PR: " + pr.getPrId());
                    return pr;
                } catch (Exception e) {
                    System.err.println("Error parsing PR line: " + line + " | Error: " + e.getMessage());
                    return null;
                }
            }
        );
        System.out.println("Total PRs read: " + prList.size());
        return prList;
    }

    public List<SalesData> getSalesData() {
        List<SalesData> salesList = fileManager.readFile(
            fileManager.getSalesDataFilePath(),
            line -> {
                String[] data = line.split(",");
                if (data.length < 6) {
                    System.err.println("Invalid sales data: " + line);
                    return null;
                }
                try {
                    SalesData sale = new SalesData(
                        data[0], data[1],
                        Integer.parseInt(data[2]),
                        Double.parseDouble(data[3]),
                        data[4],
                        Double.parseDouble(data[5])
                    );
                    System.out.println("Parsed Sales: " + sale.getSalesId());
                    return sale;
                } catch (Exception e) {
                    System.err.println("Error parsing sales data: " + line + " | Error: " + e.getMessage());
                    return null;
                }
            }
        );
        System.out.println("Total Sales read: " + salesList.size());
        return salesList;
    }

    public List<InventoryItem> verifyInventoryUpdate() {
        List<Item> itemList = fileManager.readFile(
            fileManager.getItemFilePath(),
            line -> {
                String[] data = line.split(",");
                if (data.length < 4) {
                    System.err.println("Invalid item data: " + line);
                    return null;
                }
                try {
                    int stockLevel = Integer.parseInt(data[2]);
                    double retailPrice = Double.parseDouble(data[3]);
                    if (stockLevel < 0 || retailPrice < 0) {
                        System.err.println("Invalid stock level or retail price for item: " + data[0]);
                        return null;
                    }
                    return new Item(data[0], data[1], stockLevel, retailPrice);
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing item: " + line);
                    return null;
                }
            }
        );

        List<ItemSupply> supplyList = fileManager.readFile(
            fileManager.getItemSupplyFilePath(),
            line -> {
                String[] data = line.split(",");
                if (data.length < 4) {
                    System.err.println("Invalid item supply data: " + line);
                    return null;
                }
                try {
                    double unitPrice = Double.parseDouble(data[3]);
                    if (unitPrice < 0) {
                        System.err.println("Negative unit price for item: " + data[0]);
                        return null;
                    }
                    return new ItemSupply(data[0], data[1], data[2], unitPrice);
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing item supply: " + line);
                    return null;
                }
            }
        );

        List<InventoryItem> inventoryItems = new ArrayList<>();
        for (Item item : itemList) {
            if (item == null) continue;
            String supplierCode = "N/A";
            double unitPrice = 0.0;
            for (ItemSupply supply : supplyList) {
                if (supply != null && supply.getItemCode().equals(item.getItemCode())) {
                    supplierCode = supply.getSupplierCode();
                    unitPrice = supply.getUnitPrice();
                    break;
                }
            }
            inventoryItems.add(new InventoryItem(
                item.getItemCode(),
                item.getItemName(),
                item.getStockLevel(),
                supplierCode,
                unitPrice,
                item.getRetailPrice()
            ));
        }

        System.out.println("Verified inventory items: " + inventoryItems.size());
        return inventoryItems;
    }

    public static class InventoryItem {
        private final String itemCode;
        private final String itemName;
        private final int stockLevel;
        private final String supplierCode;
        private final double unitPrice;
        private final double retailPrice;

        public InventoryItem(String itemCode, String itemName, int stockLevel,
                             String supplierCode, double unitPrice, double retailPrice) {
            this.itemCode = itemCode;
            this.itemName = itemName;
            this.stockLevel = stockLevel;
            this.supplierCode = supplierCode;
            this.unitPrice = unitPrice;
            this.retailPrice = retailPrice;
        }

        public String getItemCode() {
            return itemCode;
        }

        public String getItemName() {
            return itemName;
        }

        public int getStockLevel() {
            return stockLevel;
        }

        public String getSupplierCode() {
            return supplierCode;
        }

        public double getUnitPrice() {
            return unitPrice;
        }

        public double getRetailPrice() {
            return retailPrice;
        }
    }

    public double calculateProfitLoss(int month, int year) {
        List<SalesData> salesList = getSalesData();

        List<PurchaseOrder> poList = getPurchaseOrders();

        double totalRevenue = 0;
        for (SalesData sale : salesList) {
            if (sale != null && isInMonth(sale.getDate(), month, year)) {
                totalRevenue += sale.getTotalAmount();
            }
        }

        double totalExpenses = 0;
        for (PurchaseOrder po : poList) {
            if (po != null && po.getStatus().equals(Status.PAID) && isInMonth(po.getRequiredDate(), month, year)) {
                totalExpenses += po.getPaymentAmount();
            }
        }

        return totalRevenue - totalExpenses;
    }

    private boolean isInMonth(String date, int month, int year) {
        try {
            String[] parts = date.contains("-") ? date.split("-") : null;
            if (parts == null || parts.length < 3) return false;

            int dateMonth, dateYear;
            if (parts[0].length() == 4) {
                dateYear = Integer.parseInt(parts[0]);
                dateMonth = Integer.parseInt(parts[1]);
            } else {
                dateYear = Integer.parseInt(parts[2]);
                dateMonth = Integer.parseInt(parts[1]);
            }

            return dateMonth == (month + 1) && dateYear == year;
        } catch (Exception e) {
            System.err.println("Error parsing date: " + date);
            return false;
        }
    }

    public String generateFinancialReport() {
        List<PurchaseOrder> poList = getPurchaseOrders();

        List<SalesData> salesList = getSalesData();

        double totalRevenue = 0;
        for (SalesData sale : salesList) {
            if (sale != null) {
                totalRevenue += sale.getTotalAmount();
            }
        }

        double totalExpenses = 0;
        for (PurchaseOrder po : poList) {
            if (po != null && po.getStatus().equals(Status.PAID)) {
                totalExpenses += po.getPaymentAmount();
            }
        }

        double profitLoss = totalRevenue - totalExpenses;

        StringBuilder report = new StringBuilder("Financial Report\n");
        report.append("========================================\n\n");

        report.append("Financial Summary:\n");
        report.append(String.format("Total Revenue: RM%.2f\n", totalRevenue));
        report.append(String.format("Total Expenses (Paid POs): RM%.2f\n", totalExpenses));
        report.append(String.format("Profit/Loss: RM%.2f\n", profitLoss));
        report.append("----------------------------------------\n\n");

        report.append("Sales Details:\n");
        report.append("Sales ID | Item Code | Quantity Sold | Retail Price | Date | Total Amount\n");
        report.append("-------------------------------------------------------------------\n");
        for (SalesData sale : salesList) {
            if (sale != null) {
                report.append(String.format("%s | %s | %d | RM%.2f | %s | RM%.2f\n",
                    sale.getSalesId(), sale.getItemCode(), sale.getQuantitySold(),
                    sale.getRetailPrice(), sale.getDate(), sale.getTotalAmount()));
            }
        }
        report.append("----------------------------------------\n\n");

        report.append("Purchase Order Details:\n");
        report.append("PO ID | Item Code | Quantity | Supplier | Status | Payment Amount | Date\n");
        report.append("---------------------------------------------------------------------------\n");
        for (PurchaseOrder po : poList) {
            if (po != null) {
                report.append(String.format("%s | %s | %d | %s | %s | RM%.2f | %s\n",
                    po.getPoId(), po.getItemCode(), po.getQuantity(),
                    po.getSupplierCode(), po.getStatus(), po.getPaymentAmount(), po.getRequestedDate()));
            }
        }

        return report.toString();
    }

    public FileManager getFileManager() {
        return fileManager;
    }
    
}