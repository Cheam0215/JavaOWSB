/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entities;

import java.util.List;

import Utility.FileManager;

/**
 *
 * @author Sheng Ting
 */
public class FinanceManager extends User {
    private final FileManager fileManager;
    private final Inventory inventory;

    public FinanceManager(String userId, String username, String password) {
        super(userId, username, password, "FINANCE_MANAGER");
        this.fileManager = new FileManager();
        this.inventory = new Inventory();
    }

    public boolean approvePurchaseOrder(String poId, int newQuantity, String newSupplierCode) {
        List<PurchaseOrder> poList = fileManager.readFile(
            fileManager.getPoFilePath(),
            line -> {
                String[] data = line.split(",");
                return new PurchaseOrder(data[0], data[1], data[2], 
                    Integer.parseInt(data[3]), data[4], data[5], Double.parseDouble(data[6]), data[7], data[8], data[9]);
            }
        );

        for (PurchaseOrder po : poList) {
            if (po.getPoId().equals(poId)) {
                if (po.getStatus().equals("PENDING")) {
                    if (newSupplierCode != null && !newSupplierCode.equals(po.getSupplierCode())) {
                        List<Supplier> suppliers = fileManager.readFile(
                            fileManager.getSupplierFilePath(),
                            line -> {
                                String[] data = line.split(",");
                                Supplier supp = new Supplier(data[0], data[1]);
                                if (!data[2].equals("NONE")) {
                                    String[] items = data[2].split(";");
                                    for (String itemId : items) supp.addItemId(itemId);
                                }
                                return supp;
                            }
                        );
                        boolean validSupplier = false;
                        for (Supplier supp : suppliers) {
                            if (supp.getSupplierCode().equals(newSupplierCode) && 
                                supp.getItemIds().contains(po.getItemCode())) {
                                validSupplier = true;
                                break;
                            }
                        }
                        if (!validSupplier) {
                            System.out.println("Invalid supplier for item: " + po.getItemCode());
                            return false;
                        }
                        po.setSupplierCode(newSupplierCode);
                    }
                    
                    if (newQuantity > 0) {
                        po.setQuantity(newQuantity);
                    }
                    
                    po.setStatus("APPROVED");
                    return fileManager.updateToFile(
                        po, fileManager.getPoFilePath(),
                        PurchaseOrder::getPoId, PurchaseOrder::toString,
                        line -> {
                            String[] data = line.split(",");
                            return new PurchaseOrder(data[0], data[1], data[2], 
                                Integer.parseInt(data[3]), data[4], data[5], Double.parseDouble(data[6]), data[7], data[8], data[9]);
                        }
                    );
                }
                System.out.println("Purchase Order: " + poId + " is already " + po.getStatus());
                return false;
            }
        }
        System.out.println("Purchase Order: " + poId + " not found");
        return false;
    }

    public boolean verifyInventoryUpdate(String poId) {
        List<PurchaseOrder> poList = fileManager.readFile(
            fileManager.getPoFilePath(),
            line -> {
                String[] data = line.split(",");
                return new PurchaseOrder(data[0], data[1], data[2], 
                    Integer.parseInt(data[3]), data[4], data[5], Double.parseDouble(data[6]), data[7], data[8], data[9]);
            }
        );

        for (PurchaseOrder po : poList) {
            if (po.getPoId().equals(poId) && po.getStatus().equals("APPROVED")) {
                List<Item> items = inventory.viewItems();
                for (Item item : items) {
                    if (item.getItemCode().equals(po.getItemCode())) {
                        return true;
                    }
                }
                System.out.println("Item: " + po.getItemCode() + " not found in inventory");
                return false;
            }
        }
        System.out.println("Purchase Order: " + poId + " not found");
        return false;
    }

    public boolean processPayment(String poId, double amount) {
        if (amount <= 0) {
            System.out.println("Invalid payment amount");
            return false;
        }

        if (verifyInventoryUpdate(poId)) {
            System.out.println("Processing payment of RM" + amount + " for Purchase Order: " + poId);
            List<PurchaseOrder> poList = fileManager.readFile(
                fileManager.getPoFilePath(),
                line -> {
                    String[] data = line.split(",");
                    return new PurchaseOrder(data[0], data[1], data[2], 
                        Integer.parseInt(data[3]), data[4], data[5], Double.parseDouble(data[6]), data[7], data[8], data[9]);
                }
            );

            for (PurchaseOrder po : poList) {
                if (po.getPoId().equals(poId)) {
                    po.setPaymentAmount(amount);
                    po.setStatus("PAID");
                    return fileManager.updateToFile(
                        po, fileManager.getPoFilePath(),
                        PurchaseOrder::getPoId, PurchaseOrder::toString,
                        line -> {
                            String[] data = line.split(",");
                            return new PurchaseOrder(data[0], data[1], data[2], 
                                Integer.parseInt(data[3]), data[4], data[5], Double.parseDouble(data[6]), data[7], data[8], data[9]);

                        }
                    );
                }
            }
        }
        return false;
    }

    public String generateFinancialReport() {
        // Read Purchase Orders
        List<PurchaseOrder> poList = fileManager.readFile(
            fileManager.getPoFilePath(),
            line -> {
                String[] data = line.split(",");
                return new PurchaseOrder(data[0], data[1], data[2], 
                    Integer.parseInt(data[3]), data[4], data[5], Double.parseDouble(data[6]), data[7], data[8], data[9]);
            }
        );

        List<SalesData> salesList = fileManager.readFile(
            fileManager.getSalesDataFilePath(),
            line -> {
                String[] data = line.split(",");
                return new SalesData(data[0], data[1], 
                    Integer.parseInt(data[2]), Double.parseDouble(data[3]), data[4]);
            }
        );

        double totalRevenue = 0;
        for (SalesData sale : salesList) {
            totalRevenue += sale.getTotalAmount();
        }

        double totalExpenses = 0;
        for (PurchaseOrder po : poList) {
            if (po.getStatus().equals("PAID")) {
                totalExpenses += po.getPaymentAmount();
            }
        }

        double profitLoss = totalRevenue - totalExpenses;

        StringBuilder report = new StringBuilder("Standard Financial Report\n");
        report.append("========================================\n\n");

        report.append("Financial Summary:\n");
        report.append(String.format("Total Revenue: RM%.2f\n", totalRevenue));
        report.append(String.format("Total Expenses (Paid POs): RM%.2f\n", totalExpenses));
        report.append(String.format("Profit/Loss: RM%.2f\n", profitLoss));
        report.append("----------------------------------------\n\n");

        report.append("Sales Details:\n");
        report.append("Sales ID | Item Code | Quantity Sold | Unit Price | Date | Total Amount\n");
        report.append("-------------------------------------------------------------------\n");
        for (SalesData sale : salesList) {
            report.append(String.format("%s | %s | %d | RM%.2f | %s | RM%.2f\n",
                sale.getSalesId(), sale.getItemCode(), sale.getQuantitySold(),
                sale.getUnitPrice(), sale.getDate(), sale.getTotalAmount()));
        }
        report.append("----------------------------------------\n\n");

        report.append("Purchase Order Details:\n");
        report.append("PO ID | Item Code | Quantity | Supplier | Status | Payment Amount | Date\n");
        report.append("---------------------------------------------------------------------------\n");
        for (PurchaseOrder po : poList) {
            report.append(String.format("%s | %s | %d | %s | %s | RM%.2f | %s\n",
                po.getPoId(), po.getItemCode(), po.getQuantity(),
                po.getSupplierCode(), po.getStatus(), po.getPaymentAmount(), po.getRequestedDate()));
        }

        return report.toString();
    }

    public String viewPurchaseRequisition() {
        List<PurchaseRequisition> prList = fileManager.readFile(
            fileManager.getPrFilePath(),
            line -> {
                String[] data = line.split(",");
                return new PurchaseRequisition(data[0], data[1], 
                    Integer.parseInt(data[2]), data[3], data[4], data[5]);
            }
        );

        StringBuilder view = new StringBuilder("Purchase Requisitions:\n");
        view.append("PR ID | Item Code | Quantity | Required Date | Supplier | Request Date\n");
        view.append("-------------------------------------------------\n");

        for (PurchaseRequisition pr : prList) {
            view.append(String.format("%s | %s | %d | %s | %s | %s\n",
                pr.getPrId(), pr.getItemCode(), pr.getQuantity(),
                pr.getRequiredDate(), pr.getSupplierCode(), pr.getRequestDate()));
        }
        return view.toString();
    }

    public String viewPurchaseOrder() {
        List<PurchaseOrder> poList = fileManager.readFile(
            fileManager.getPoFilePath(),
            line -> {
                String[] data = line.split(",");
                return new PurchaseOrder(data[0], data[1], data[2], 
                    Integer.parseInt(data[3]), data[4], data[5], Double.parseDouble(data[6]), data[7], data[8], data[9]);
            }
        );

        StringBuilder view = new StringBuilder("Purchase Orders:\n");
        view.append("PO ID | PR ID | Item Code | Quantity | Supplier | Status | Payment Amount | Date\n");
        view.append("---------------------------------------------------------------------------\n");

        for (PurchaseOrder po : poList) {
            view.append(String.format("%s | %s | %s | %d | %s | %s | RM%.2f | %s\n",
                po.getPoId(), po.getPrId(), po.getItemCode(), 
                po.getQuantity(), po.getSupplierCode(), po.getStatus(), po.getPaymentAmount(), po.getRequestedDate()));
        }
        return view.toString();
    }
}

