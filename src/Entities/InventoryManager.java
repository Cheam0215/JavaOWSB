/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entities;

import java.util.List;
import Utility.FileManager;
import Utility.Remark;
import Utility.Status;
import Utility.UserRoles;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Sheng Ting
 */
public class InventoryManager extends User{
    
    private final FileManager fileManager;

    public InventoryManager(String userId, String username, String password) {
        super(userId, username, password, UserRoles.INVENTORY_MANAGER);
        this.fileManager = new FileManager();
    }
    
    public FileManager getFileManager() {
        return fileManager;
    }

   
    public void updateStock(String itemCode, int quantityReceived) {
        // 1. Check PO
        // 2. Check if item in database match with PO
        // 3. proceed with update using inventory method
        List<PurchaseOrder> poList = fileManager.readFile(
            fileManager.getPoFilePath(),
            line -> {
                String[] data = line.split(",");
               return new PurchaseOrder(data[0], data[1], data[2], data[3],
                    Integer.parseInt(data[4]), data[5], data[6], data[7], Status.valueOf(data[8]),
                    Double.parseDouble(data[9]), Remark.valueOf(data[10]));
            }
        );
        
        boolean poFound = false;
        PurchaseOrder matchedPO = null;
        
        for (PurchaseOrder po : poList){
            if (po.getItemCode().equals(itemCode) && po.getStatus().equals(Status.APPROVED)) {
                poFound = true;
                matchedPO = po;
                if (quantityReceived > po.getQuantity()) {
                throw new IllegalArgumentException(
                    "Received quantity (" + quantityReceived + 
                    ") exceeds PO quantity (" + po.getQuantity() + ")"
                );
            }
            break;
            }
        }
        
        
        if(!poFound){
            throw new IllegalArgumentException("No valid PO found for item code: " + itemCode);
        }
        
         // 3. Read items
        List<Item> itemList = fileManager.readFile(
            fileManager.getItemFilePath(),
            line -> {
                String[] data = line.split(",");
                return new Item(data[0], data[1],Integer.parseInt(data[2]), Double.parseDouble(data[3]));
            }
        );

        // 4. Update item quantity
        boolean itemFound = false;
        Item matchedItem = null;
        for (Item item : itemList) {
            if (item.getItemCode().equals(itemCode)) {
                itemFound = true;
                matchedItem = item;
                int newQuantity = item.getStockLevel() + quantityReceived;
                item.setStockLevel(newQuantity);
                break;
            }
        }

        if (!itemFound) {
            throw new IllegalArgumentException("Item not found in inventory: " + itemCode);
        }

        // 5. Perform updates transactionally
        try {
            // Update PO status
            matchedPO.setStatus(Status.RECEIVED);

            // Update PO file
            boolean poUpdated = fileManager.updateToFile(
                matchedPO,
                fileManager.getPoFilePath(),
                PurchaseOrder::getPoId,
                po -> String.join(",",
                    po.getPoId(),
                    po.getPrId(),
                    po.getRaisedBy(),
                    po.getItemCode(),
                    String.valueOf(po.getQuantity()),
                    po.getSupplierCode(),
                    po.getRequiredDate(),
                    po.getRequestedDate(),
                    po.getStatus().toString(),
                    String.valueOf(po.getPaymentAmount()),
                    po.getRemark().toString()
                ),
                line -> {
                    String[] data = line.split(",");
                    return new PurchaseOrder(data[0], data[1], data[2], data[3],
                        Integer.parseInt(data[4]), data[5], data[6], data[7], Status.valueOf(data[8]),
                        Double.parseDouble(data[9]), Remark.valueOf(data[10]));
                }
            );

            if (!poUpdated) {
                throw new RuntimeException("Failed to update PO file");
            }

            // Update item file (fixed to include supplierCode)
            boolean itemUpdated = fileManager.updateToFile(
                matchedItem,
                fileManager.getItemFilePath(),
                Item::getItemCode,
                item -> String.join(",",
                    item.getItemCode(),
                    item.getItemName(),
                    String.valueOf(item.getStockLevel()),
                    String.valueOf(item.getRetailPrice())
                ),
                line -> {
                    String[] data = line.split(",");
                    return new Item(data[0], data[1],Integer.parseInt(data[2]), Double.parseDouble(data[3]));
                }
            );

            if (!itemUpdated) {
                // Rollback PO update
                matchedPO.setStatus(Status.APPROVED);
                boolean poRollback = fileManager.updateToFile(
                    matchedPO,
                    fileManager.getPoFilePath(),
                    PurchaseOrder::getPoId,
                    po -> String.join(",",
                        po.getPoId(),
                        po.getPrId(),
                        po.getRaisedBy(),
                        po.getItemCode(),
                        String.valueOf(po.getQuantity()),
                        po.getSupplierCode(),
                        po.getRequiredDate(),
                        po.getRequestedDate(),
                        po.getStatus().toString(),
                        String.valueOf(po.getPaymentAmount()),
                        po.getRemark().toString()
                    ),
                    line -> {
                        String[] data = line.split(",");
                        return new PurchaseOrder(data[0], data[1], data[2], data[3],
                            Integer.parseInt(data[4]), data[5], data[6], data[7], Status.valueOf(data[8]),
                            Double.parseDouble(data[9]), Remark.valueOf(data[10]));
                    }
                );
                if (!poRollback) {
                    throw new RuntimeException("Failed to rollback PO file update after item update failure");
                }
                throw new RuntimeException("Failed to update item file");
            }
        } catch (Exception e) {
            // If anything fails before the PO update, no changes are persisted
            // If the PO update succeeds but the item update fails, the rollback above handles it
            throw new RuntimeException("Failed to update stock: " + e.getMessage(), e);
        }
    }
    
    public void rejectPurchaseOrder(String poId, Remark rejectionReason) {
        List<PurchaseOrder> poList = fileManager.readFile(
            fileManager.getPoFilePath(),
            line -> {
                String[] data = line.split(",");
                return new PurchaseOrder(data[0], data[1], data[2], data[3],
                    Integer.parseInt(data[4]), data[5], data[6], data[7], Status.valueOf(data[8]),
                    Double.parseDouble(data[9]), Remark.valueOf(data[10]));
            }
        );

        boolean poFound = false;
        PurchaseOrder matchedPO = null;

        for (PurchaseOrder po : poList) {
             if (po.getPoId().equals(poId) && po.getStatus().equals(Status.APPROVED)) {
                poFound = true;
                matchedPO = po;
                break;
            }
        }

        if (!poFound) {
            throw new IllegalArgumentException("No valid APPROVED PO found for PO ID: " + poId);
        }

        try {
            matchedPO.setStatus(Status.REJECTED); // Use Status enum
            matchedPO.setRemark(rejectionReason);

            boolean poUpdated = fileManager.updateToFile(
                matchedPO,
                fileManager.getPoFilePath(),
                PurchaseOrder::getPoId,
                po -> String.join(",",
                     po.getPoId(),
                    po.getPrId(),
                    po.getRaisedBy(),
                    po.getItemCode(),
                    String.valueOf(po.getQuantity()),
                    po.getSupplierCode(),
                    po.getRequiredDate(),
                    po.getRequestedDate(),
                    po.getStatus().toString(),
                    String.valueOf(po.getPaymentAmount()),
                    po.getRemark().toString()
                ),
                line -> {
                    String[] data = line.split(",");
                    return new PurchaseOrder(data[0], data[1], data[2], data[3],
                    Integer.parseInt(data[4]), data[5], data[6], data[7], Status.valueOf(data[8]),
                    Double.parseDouble(data[9]), Remark.valueOf(data[10]));
                }
            );

            if (!poUpdated) {
                throw new RuntimeException("Failed to update PO file with rejection");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to reject PO: " + e.getMessage(), e);
        }
    
    }
    
    
    public void displayStockReport(String name, String startDate, String endDate) {
        String manager = name;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        System.out.println("Generating report for range: " + startDate + " to " + endDate);

        // Read purchase orders to calculate stock in for the range
        List<PurchaseOrder> poList = fileManager.readFile(
            fileManager.getPoFilePath(),
            line -> {
                if (line.trim().isEmpty() || !line.contains(",")) {
                    System.out.println("Skipping invalid PO line: " + line);
                    return null;
                }
                String[] data = line.split(",");
                if (data.length < 11) {
                    System.out.println("Skipping PO line with insufficient columns: " + line);
                    return null;
                }
                System.out.println("Reading PO line: " + String.join(",", data));
                try {
                    return new PurchaseOrder(data[0], data[1], data[2], data[3],
                        Integer.parseInt(data[4]), data[5], data[6], data[7], Status.valueOf(data[8]),
                        Double.parseDouble(data[9]), Remark.valueOf(data[10]));
                } catch (Exception e) {
                    System.err.println("Error creating PO from line '" + String.join(",", data) + "': " + e.getMessage());
                    return null;
                }
            }
        );
        System.out.println("Total POs read: " + poList.size());

        Map<String, Integer> stockInQty = new HashMap<>();
        Map<String, Double> stockInValue = new HashMap<>();
        try {
            LocalDate start = LocalDate.parse(startDate, dtf);
            LocalDate end = LocalDate.parse(endDate, dtf);
            for (PurchaseOrder po : poList) {
                if (po != null) {
                    System.out.println("Processing PO: " + po.getPoId() + ", Status: " + po.getStatus() + 
                                     ", Date: " + po.getRequiredDate() + ", Order Date: " + po.getRequestedDate());
                    try {
                        LocalDate requiredDate = LocalDate.parse(po.getRequiredDate(), dtf);
                        if (po.getStatus().equals(Status.RECEIVED) && 
                            !requiredDate.isBefore(start) && !requiredDate.isAfter(end)) {
                            stockInQty.merge(po.getItemCode(), po.getQuantity(), Integer::sum);
                            stockInValue.merge(po.getItemCode(), po.getPaymentAmount(), Double::sum);
                            System.out.println("Added to Stock In: " + po.getItemCode() + ", Qty: " + po.getQuantity());
                        }
                    } catch (Exception e) {
                        System.err.println("Error parsing requiredDate for PO " + po.getPoId() + ": " + e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing date range: " + e.getMessage());
        }
        System.out.println("Stock In Qty: " + stockInQty);
        System.out.println("Stock In Value: " + stockInValue);

        // Read sales to calculate stock out for the range
        List<SalesData> salesList = fileManager.readFile(
            fileManager.getSalesDataFilePath(),
            line -> {
                if (line.trim().isEmpty() || line.startsWith("salesId")) return null;
                String[] data = line.split(",");
                if (data.length < 6) {
                    System.out.println("Skipping Sale line with insufficient columns: " + line);
                    return null;
                }
                System.out.println("Reading Sale line: " + String.join(",", data));
                try {
                    return new SalesData(data[0], data[1],
                        Integer.parseInt(data[2]), Double.parseDouble(data[3]), data[4], Double.parseDouble(data[5]));
                } catch (Exception e) {
                    System.err.println("Error creating Sale from line '" + String.join(",", data) + "': " + e.getMessage());
                    return null;
                }
            }
        );

        Map<String, Integer> stockOutQty = new HashMap<>();
        Map<String, Double> stockOutValue = new HashMap<>();
        try {
            LocalDate start = LocalDate.parse(startDate, dtf);
            LocalDate end = LocalDate.parse(endDate, dtf);
            for (SalesData sales : salesList) {
                if (sales != null) {
                    try {
                        LocalDate saleDate = LocalDate.parse(sales.getDate(), dtf);
                        if (!saleDate.isBefore(start) && !saleDate.isAfter(end)) {
                            stockOutQty.merge(sales.getItemCode(), sales.getQuantitySold(), Integer::sum);
                            stockOutValue.merge(sales.getItemCode(), sales.getTotalAmount(), Double::sum);
                        }
                    } catch (Exception e) {
                        System.err.println("Error parsing sale date for Sale " + sales.getSalesId() + ": " + e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing date range: " + e.getMessage());
        }
        System.out.println("Stock Out Qty: " + stockOutQty);
        System.out.println("Stock Out Value: " + stockOutValue);

        // Read current inventory
        List<Item> itemList = fileManager.readFile(
            fileManager.getItemFilePath(),
            line -> {
                if (line.trim().isEmpty() || line.startsWith("itemcode")) return null;
                String[] data = line.split(",");
                if (data.length < 4) {
                    System.out.println("Skipping Item line with insufficient columns: " + line);
                    return null;
                }
                System.out.println("Reading Item line: " + String.join(",", data));
                try {
                    return new Item(data[0], data[1], Integer.parseInt(data[2]), Double.parseDouble(data[3]));
                } catch (Exception e) {
                    System.err.println("Error creating Item from line '" + String.join(",", data) + "': " + e.getMessage());
                    return null;
                }
            }
        );
        System.out.println("Items loaded: " + itemList.size());

        // Create the new Swing window
        JFrame reportFrame = new JFrame("Stock Report - " + startDate + " to " + endDate);
        reportFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        reportFrame.setSize(800, 600);
        reportFrame.setLayout(new BorderLayout());

        // Create a panel to hold all components
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Summary information
        JLabel titleLabel = new JLabel("Stock Report - " + startDate + " to " + endDate);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(titleLabel);

        JLabel generatedByLabel = new JLabel("Generated by: " + manager);
        generatedByLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        mainPanel.add(generatedByLabel);

        JLabel stockInValueLabel = new JLabel("Total Stock In Value: $" + 
            String.format("%.2f", stockInValue.values().stream().mapToDouble(Double::doubleValue).sum()));
        stockInValueLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        mainPanel.add(stockInValueLabel);

        JLabel stockOutValueLabel = new JLabel("Total Stock Out Value: $" + 
            String.format("%.2f", stockOutValue.values().stream().mapToDouble(Double::doubleValue).sum()));
        stockOutValueLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        mainPanel.add(stockOutValueLabel);

        mainPanel.add(Box.createVerticalStrut(10)); // Spacer

        // Stock In Table
        JLabel stockInLabel = new JLabel("Stock In");
        stockInLabel.setFont(new Font("Arial", Font.BOLD, 14));
        mainPanel.add(stockInLabel);

        DefaultTableModel stockInModel = new DefaultTableModel(
            new String[]{"Item Code", "Quantity", "Supplier Code", "Date", "Payment Amount"}, 0);
        JTable stockInTable = new JTable(stockInModel);
        try {
            LocalDate start = LocalDate.parse(startDate, dtf);
            LocalDate end = LocalDate.parse(endDate, dtf);
            for (PurchaseOrder po : poList) {
                if (po != null) {
                    try {
                        LocalDate requiredDate = LocalDate.parse(po.getRequiredDate(), dtf);
                        if (po.getStatus().equals(Status.RECEIVED) && 
                            !requiredDate.isBefore(start) && !requiredDate.isAfter(end)) {
                            stockInModel.addRow(new Object[]{
                                po.getItemCode(),
                                po.getQuantity(),
                                po.getSupplierCode(),
                                po.getRequiredDate(),
                                String.format("%.2f", po.getPaymentAmount())
                            });
                        }
                    } catch (Exception e) {
                        System.err.println("Error parsing requiredDate for PO " + po.getPoId() + " in table: " + e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing date range: " + e.getMessage());
        }
        JScrollPane stockInScrollPane = new JScrollPane(stockInTable);
        stockInScrollPane.setPreferredSize(new Dimension(750, 100));
        mainPanel.add(stockInScrollPane);

        mainPanel.add(Box.createVerticalStrut(10)); // Spacer

        // Stock Out Table
        JLabel stockOutLabel = new JLabel("Stock Out");
        stockOutLabel.setFont(new Font("Arial", Font.BOLD, 14));
        mainPanel.add(stockOutLabel);

        DefaultTableModel stockOutModel = new DefaultTableModel(
            new String[]{"Item Code", "Quantity Sold", "Retail Price", "Date", "Total Amount"}, 0);
        JTable stockOutTable = new JTable(stockOutModel);
        try {
            LocalDate start = LocalDate.parse(startDate, dtf);
            LocalDate end = LocalDate.parse(endDate, dtf);
            for (SalesData sales : salesList) {
                if (sales != null) {
                    try {
                        LocalDate saleDate = LocalDate.parse(sales.getDate(), dtf);
                        if (!saleDate.isBefore(start) && !saleDate.isAfter(end)) {
                            stockOutModel.addRow(new Object[]{
                                sales.getItemCode(),
                                sales.getQuantitySold(),
                                String.format("%.2f", sales.getRetailPrice()),
                                sales.getDate(),
                                String.format("%.2f", sales.getTotalAmount())
                            });
                        }
                    } catch (Exception e) {
                        System.err.println("Error parsing sale date for Sale " + sales.getSalesId() + " in table: " + e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing date range: " + e.getMessage());
        }
        JScrollPane stockOutScrollPane = new JScrollPane(stockOutTable);
        stockOutScrollPane.setPreferredSize(new Dimension(750, 100));
        mainPanel.add(stockOutScrollPane);

        mainPanel.add(Box.createVerticalStrut(10)); // Spacer

        // Inventory Summary Table
        JLabel summaryLabel = new JLabel("Inventory Summary");
        summaryLabel.setFont(new Font("Arial", Font.BOLD, 14));
        mainPanel.add(summaryLabel);

        DefaultTableModel summaryModel = new DefaultTableModel(
            new String[]{"Item Code", "Item Name", "Current Stock", "Stock In", "Stock Out", "Net Change", "Updated Stock"}, 0);
        JTable summaryTable = new JTable(summaryModel);
        for (Item item : itemList) {
            if (item != null) {
                String itemCode = item.getItemCode();
                int currentStock = item.getStockLevel();
                int stockIn = stockInQty.getOrDefault(itemCode, 0);
                int stockOut = stockOutQty.getOrDefault(itemCode, 0);
                int netChange = stockIn - stockOut;
                int updatedStock = currentStock + netChange;
                summaryModel.addRow(new Object[]{
                    itemCode,
                    item.getItemName(),
                    currentStock,
                    stockIn,
                    stockOut,
                    netChange,
                    updatedStock
                });
            }
        }
        JScrollPane summaryScrollPane = new JScrollPane(summaryTable);
        summaryScrollPane.setPreferredSize(new Dimension(750, 150));
        mainPanel.add(summaryScrollPane);

        // Add the main panel to a scroll pane in case the content is too large
        JScrollPane mainScrollPane = new JScrollPane(mainPanel);
        reportFrame.add(mainScrollPane, BorderLayout.CENTER);

        // Make the frame visible
        reportFrame.setLocationRelativeTo(null); // Center the window
        reportFrame.setVisible(true);
    }

    
    public Map<String, Integer> calculateStockReport(String startDate, String endDate, DefaultTableModel tableModel, JTable jTable1) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Read current inventory
        List<Item> itemList = fileManager.readFile(
            fileManager.getItemFilePath(),
            line -> {
                if (line.trim().isEmpty() || line.startsWith("itemcode")) return null;
                String[] data = line.split(",");
                if (data.length < 4) {
                    System.out.println("Skipping Item line with insufficient columns: " + line);
                    return null;
                }
                System.out.println("Reading Item line: " + String.join(",", data));
                try {
                    return new Item(data[0], data[1], Integer.parseInt(data[2]), Double.parseDouble(data[3]));
                } catch (Exception e) {
                    System.err.println("Error creating Item from line '" + String.join(",", data) + "': " + e.getMessage());
                    return null;
                }
            }
        );

        // Read purchase orders to calculate stock in
        List<PurchaseOrder> poList = fileManager.readFile(
            fileManager.getPoFilePath(),
            line -> {
                if (line.trim().isEmpty() || !line.contains(",")) {
                    System.out.println("Skipping invalid PO line: " + line);
                    return null;
                }
                String[] data = line.split(",");
                if (data.length < 11) {
                    System.out.println("Skipping PO line with insufficient columns: " + line);
                    return null;
                }
                System.out.println("Reading PO line: " + String.join(",", data));
                try {
                    return new PurchaseOrder(data[0], data[1], data[2], data[3],
                        Integer.parseInt(data[4]), data[5], data[6], data[7], Status.valueOf(data[8]),
                        Double.parseDouble(data[9]), Remark.valueOf(data[10]));
                } catch (Exception e) {
                    System.err.println("Error creating PO from line '" + String.join(",", data) + "': " + e.getMessage());
                    return null;
                }
            }
        );

        Map<String, Integer> stockInQty = new HashMap<>();
        try {
            LocalDate start = LocalDate.parse(startDate, dtf);
            LocalDate end = LocalDate.parse(endDate, dtf);
            for (PurchaseOrder po : poList) {
                if (po != null && po.getStatus().equals(Status.RECEIVED)) {
                    LocalDate requiredDate = LocalDate.parse(po.getRequiredDate(), dtf);
                    if (!requiredDate.isBefore(start) && !requiredDate.isAfter(end)) {
                        stockInQty.merge(po.getItemCode(), po.getQuantity(), Integer::sum);
                        System.out.println("Added to Stock In: " + po.getItemCode() + ", Qty: " + po.getQuantity() + 
                                         " for date " + po.getRequiredDate() + " in range " + startDate + " to " + endDate);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing date range: " + e.getMessage());
        }
        System.out.println("Stock In Qty for range " + startDate + " to " + endDate + ": " + stockInQty);

        // Read sales to calculate stock out
        List<SalesData> salesList = fileManager.readFile(
            fileManager.getSalesDataFilePath(),
            line -> {
                if (line.trim().isEmpty() || line.startsWith("salesId")) return null;
                String[] data = line.split(",");
                if (data.length < 6) {
                    System.out.println("Skipping Sale line with insufficient columns: " + line);
                    return null;
                }
                System.out.println("Reading Sale line: " + String.join(",", data));
                try {
                    return new SalesData(data[0], data[1],
                        Integer.parseInt(data[2]), Double.parseDouble(data[3]), data[4], Double.parseDouble(data[5]));
                } catch (Exception e) {
                    System.err.println("Error creating Sale from line '" + String.join(",", data) + "': " + e.getMessage());
                    return null;
                }
            }
        );

        Map<String, Integer> stockOutQty = new HashMap<>();
        try {
            LocalDate start = LocalDate.parse(startDate, dtf);
            LocalDate end = LocalDate.parse(endDate, dtf);
            for (SalesData sales : salesList) {
                if (sales != null) {
                    LocalDate saleDate = LocalDate.parse(sales.getDate(), dtf);
                    if (!saleDate.isBefore(start) && !saleDate.isAfter(end)) {
                        stockOutQty.merge(sales.getItemCode(), sales.getQuantitySold(), Integer::sum);
                        System.out.println("Added to Stock Out: " + sales.getItemCode() + ", Qty: " + sales.getQuantitySold() + 
                                         " for date " + sales.getDate() + " in range " + startDate + " to " + endDate);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing date range: " + e.getMessage());
        }
        System.out.println("Stock Out Qty for range " + startDate + " to " + endDate + ": " + stockOutQty);

        // Populate table
        tableModel.setRowCount(0); // Clear existing rows
        for (Item item : itemList) {
            if (item != null) {
                String itemCode = item.getItemCode();
                int currentStock = item.getStockLevel();
                int stockIn = stockInQty.getOrDefault(itemCode, 0);
                int stockOut = stockOutQty.getOrDefault(itemCode, 0);
                int netChange = stockIn - stockOut;
                int updatedStock = currentStock + netChange;

                tableModel.addRow(new Object[] {
                    itemCode,
                    item.getItemName(),
                    currentStock,
                    stockIn,
                    stockOut,
                    netChange,
                    updatedStock
                });
            }
        }
        tableModel.fireTableDataChanged();
        jTable1.repaint();

        return stockInQty; // Return for potential future use
    }
    
    public void displayMenu() {
        
    }
    
}
