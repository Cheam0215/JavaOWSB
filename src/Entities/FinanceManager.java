/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entities;

import java.util.List;

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

                // Validate supplier if changed
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

    public String verifyInventoryUpdate(String poId) throws IllegalArgumentException {
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
                if (!po.getStatus().equals(Status.APPROVED)) {
                    return "Purchase Order " + poId + " is not approved";
                }
                List<Item> items = fileManager.readFile(
                    fileManager.getItemFilePath(),
                    line -> {
                        String[] data = line.split(",");
                        if (data.length < 4) return null;
                        try {
                            return new Item(
                                data[0], data[1],
                                Integer.parseInt(data[2]),
                                Double.parseDouble(data[3])
                            );
                        } catch (Exception e) {
                            return null;
                        }
                    }
                );
                for (Item item : items) {
                    if (item != null && item.getItemCode().equals(po.getItemCode())) {
                        return "Inventory update verified for PO " + poId;
                    }
                }
                return "Item " + po.getItemCode() + " not found in inventory";
            }
        }
        return "Purchase Order " + poId + " not found";
    }

    public String processPayment(String poId, double amount) throws IllegalArgumentException {
        if (poId == null || poId.trim().isEmpty()) {
            throw new IllegalArgumentException("PO ID cannot be empty");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Payment amount must be positive");
        }

        String verificationResult = verifyInventoryUpdate(poId);
        if (!verificationResult.startsWith("Inventory update verified")) {
            return verificationResult;
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
                po.setPaymentAmount(amount);
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
                return success ? "Payment of RM" + String.format("%.2f", amount) + " processed for PO " + poId : "Failed to process payment for PO " + poId;
            }
        }
        return "Purchase Order " + poId + " not found";
    }

    public String generateFinancialReport() {
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

        List<SalesData> salesList = fileManager.readFile(
            fileManager.getSalesDataFilePath(),
            line -> {
                String[] data = line.split(",");
                if (data.length < 6) return null;
                try {
                    return new SalesData(
                        data[0], data[1],
                        Integer.parseInt(data[2]),
                        Double.parseDouble(data[3]),
                        data[4],
                        Double.parseDouble(data[5])
                    );
                } catch (Exception e) {
                    return null;
                }
            }
        );

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

    public String viewPurchaseRequisition() {
        List<PurchaseRequisition> prList = fileManager.readFile(
            fileManager.getPrFilePath(),
            line -> {
                String[] data = line.split(",");
                if (data.length < 7) return null;
                try {
                    return new PurchaseRequisition(
                        data[0], data[1], data[2],
                        Integer.parseInt(data[3]), data[4], data[5],
                        Status.valueOf(data[6])
                    );
                } catch (Exception e) {
                    return null;
                }
            }
        );

        StringBuilder view = new StringBuilder("Purchase Requisitions:\n");
        view.append("PR ID | Item Code | Requested By | Quantity | Required Date | Request Date | Status\n");
        view.append("--------------------------------------------------------------------------------\n");

        for (PurchaseRequisition pr : prList) {
            if (pr != null) {
                view.append(String.format("%s | %s | %s | %d | %s | %s | %s\n",
                    pr.getPrId(), pr.getItemCode(), pr.getRequestedBy(), pr.getQuantity(),
                    pr.getRequiredDate(), pr.getRequestedDate(), pr.getStatus()));
            }
        }
        return view.toString();
    }

    public String viewPurchaseOrder() {
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

        StringBuilder view = new StringBuilder("Purchase Orders:\n");
        view.append("PO ID | PR ID | Item Code | Quantity | Supplier | Status | Payment Amount | Date\n");
        view.append("---------------------------------------------------------------------------\n");

        for (PurchaseOrder po : poList) {
            if (po != null) {
                view.append(String.format("%s | %s | %s | %d | %s | %s | RM%.2f | %s\n",
                    po.getPoId(), po.getPrId(), po.getItemCode(), 
                    po.getQuantity(), po.getSupplierCode(), po.getStatus(), 
                    po.getPaymentAmount(), po.getRequestedDate()));
            }
        }
        return view.toString();
    }
}