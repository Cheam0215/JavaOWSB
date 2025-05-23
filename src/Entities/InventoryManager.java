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
import java.util.ArrayList;
import java.util.EnumSet;
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
    
    
//    public StockReportData displayStockReport(String startDate, String endDate) {
//    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//
//    System.out.println("Generating report for range: " + startDate + " to " + endDate);
//
//    // Read purchase orders to calculate stock in for the range
//    List<PurchaseOrder> poList = fileManager.readFile(
//        fileManager.getPoFilePath(),
//        line -> {
//            if (line.trim().isEmpty() || !line.contains(",")) {
//                System.out.println("Skipping invalid PO line: " + line);
//                return null;
//            }
//            String[] data = line.split(",");
//            if (data.length < 11) {
//                System.out.println("Skipping PO line with insufficient columns: " + line);
//                return null;
//            }
//            System.out.println("Reading PO line: " + String.join(",", data));
//            try {
//                return new PurchaseOrder(data[0], data[1], data[2], data[3],
//                    Integer.parseInt(data[4]), data[5], data[6], data[7], Status.valueOf(data[8]),
//                    Double.parseDouble(data[9]), Remark.valueOf(data[10]));
//            } catch (Exception e) {
//                System.err.println("Error creating PO from line '" + String.join(",", data) + "': " + e.getMessage());
//                return null;
//            }
//        }
//    );
//    System.out.println("Total POs read: " + poList.size());
//
//    Map<String, Integer> stockInQty = new HashMap<>();
//    Map<String, Double> stockInValue = new HashMap<>();
//    List<PurchaseOrder> stockInPOs = new ArrayList<>();
//    try {
//        LocalDate start = LocalDate.parse(startDate, dtf);
//        LocalDate end = LocalDate.parse(endDate, dtf);
//        for (PurchaseOrder po : poList) {
//            if (po != null) {
//                System.out.println("Processing PO: " + po.getPoId() + ", Status: " + po.getStatus() + 
//                                 ", Date: " + po.getRequiredDate() + ", Order Date: " + po.getRequestedDate());
//                try {
//                    LocalDate requiredDate = LocalDate.parse(po.getRequiredDate(), dtf);
//                    if ((po.getStatus().equals(Status.RECEIVED) || po.getStatus().equals(Status.PAID)) && 
//                        !requiredDate.isBefore(start) && !requiredDate.isAfter(end)) {
//                        stockInPOs.add(po);
//                        stockInQty.merge(po.getItemCode(), po.getQuantity(), Integer::sum);
//                        stockInValue.merge(po.getItemCode(), po.getPaymentAmount(), Double::sum);
//                        System.out.println("Added to Stock In: " + po.getItemCode() + ", Qty: " + po.getQuantity());
//                    }
//                } catch (Exception e) {
//                    System.err.println("Error parsing requiredDate for PO " + po.getPoId() + ": " + e.getMessage());
//                }
//            }
//        }
//    } catch (Exception e) {
//        System.err.println("Error parsing date range: " + e.getMessage());
//    }
//    System.out.println("Stock In Qty: " + stockInQty);
//    System.out.println("Stock In Value: " + stockInValue);
//
//    // Read sales to calculate stock out for the range
//    List<SalesData> salesList = fileManager.readFile(
//        fileManager.getSalesDataFilePath(),
//        line -> {
//            if (line.trim().isEmpty() || line.startsWith("salesId")) return null;
//            String[] data = line.split(",");
//            if (data.length < 6) {
//                System.out.println("Skipping Sale line with insufficient columns: " + line);
//                return null;
//            }
//            System.out.println("Reading Sale line: " + String.join(",", data));
//            try {
//                return new SalesData(data[0], data[1],
//                    Integer.parseInt(data[2]), Double.parseDouble(data[3]), data[4], Double.parseDouble(data[5]));
//            } catch (Exception e) {
//                System.err.println("Error creating Sale from line '" + String.join(",", data) + "': " + e.getMessage());
//                return null;
//            }
//        }
//    );
//
//    Map<String, Integer> stockOutQty = new HashMap<>();
//    Map<String, Double> stockOutValue = new HashMap<>();
//    List<SalesData> stockOutSales = new ArrayList<>();
//    try {
//        LocalDate start = LocalDate.parse(startDate, dtf);
//        LocalDate end = LocalDate.parse(endDate, dtf);
//        for (SalesData sales : salesList) {
//            if (sales != null) {
//                try {
//                    LocalDate saleDate = LocalDate.parse(sales.getDate(), dtf);
//                    if (!saleDate.isBefore(start) && !saleDate.isAfter(end)) {
//                        stockOutSales.add(sales);
//                        stockOutQty.merge(sales.getItemCode(), sales.getQuantitySold(), Integer::sum);
//                        stockOutValue.merge(sales.getItemCode(), sales.getTotalAmount(), Double::sum);
//                    }
//                } catch (Exception e) {
//                    System.err.println("Error parsing sale date for Sale " + sales.getSalesId() + ": " + e.getMessage());
//                }
//            }
//        }
//    } catch (Exception e) {
//        System.err.println("Error parsing date range: " + e.getMessage());
//    }
//    System.out.println("Stock Out Qty: " + stockOutQty);
//    System.out.println("Stock Out Value: " + stockOutValue);
//
//    // Read current inventory
//    List<Item> itemList = fileManager.readFile(
//        fileManager.getItemFilePath(),
//        line -> {
//            if (line.trim().isEmpty() || line.startsWith("itemcode")) return null;
//            String[] data = line.split(",");
//            if (data.length < 4) {
//                System.out.println("Skipping Item line with insufficient columns: " + line);
//                return null;
//            }
//            System.out.println("Reading Item line: " + String.join(",", data));
//            try {
//                return new Item(data[0], data[1], Integer.parseInt(data[2]), Double.parseDouble(data[3]));
//            } catch (Exception e) {
//                System.err.println("Error creating Item from line '" + String.join(",", data) + "': " + e.getMessage());
//                return null;
//            }
//        }
//    );
//    System.out.println("Items loaded: " + itemList.size());
//
//    return new StockReportData(stockInPOs, stockOutSales, itemList, stockInQty, stockInValue, stockOutQty, stockOutValue);
//}
//    
//    
//    
//    public Map<String, Integer> calculateStockReport(String startDate, String endDate, DefaultTableModel tableModel, JTable jTable1) {
//
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//
//        // Read current inventory
//        List<Item> itemList = fileManager.readFile(
//            fileManager.getItemFilePath(),
//            line -> {
//                if (line.trim().isEmpty() || line.startsWith("itemcode")) return null;
//                String[] data = line.split(",");
//                if (data.length < 4) {
//                    System.out.println("Skipping Item line with insufficient columns: " + line);
//                    return null;
//                }
//                System.out.println("Reading Item line: " + String.join(",", data));
//                try {
//                    return new Item(data[0], data[1], Integer.parseInt(data[2]), Double.parseDouble(data[3]));
//                } catch (Exception e) {
//                    System.err.println("Error creating Item from line '" + String.join(",", data) + "': " + e.getMessage());
//                    return null;
//                }
//            }
//        );
//
//        // Read purchase orders to calculate stock in
//        List<PurchaseOrder> poList = fileManager.readFile(
//            fileManager.getPoFilePath(),
//            line -> {
//                if (line.trim().isEmpty() || !line.contains(",")) {
//                    System.out.println("Skipping invalid PO line: " + line);
//                    return null;
//                }
//                String[] data = line.split(",");
//                if (data.length < 11) {
//                    System.out.println("Skipping PO line with insufficient columns: " + line);
//                    return null;
//                }
//                System.out.println("Reading PO line: " + String.join(",", data));
//                try {
//                    return new PurchaseOrder(data[0], data[1], data[2], data[3],
//                        Integer.parseInt(data[4]), data[5], data[6], data[7], Status.valueOf(data[8]),
//                        Double.parseDouble(data[9]), Remark.valueOf(data[10]));
//                } catch (Exception e) {
//                    System.err.println("Error creating PO from line '" + String.join(",", data) + "': " + e.getMessage());
//                    return null;
//                }
//            }
//        );
//
//        Map<String, Integer> stockInQty = new HashMap<>();
//        try {
//            LocalDate start = LocalDate.parse(startDate, dtf);
//            LocalDate end = LocalDate.parse(endDate, dtf);
//            for (PurchaseOrder po : poList) {
//                if (po != null && EnumSet.of(Status.RECEIVED, Status.PAID).contains(po.getStatus())) {
//                    LocalDate requiredDate = LocalDate.parse(po.getRequiredDate(), dtf);
//                    if (!requiredDate.isBefore(start) && !requiredDate.isAfter(end)) {
//                        stockInQty.merge(po.getItemCode(), po.getQuantity(), Integer::sum);
//                        System.out.println("Added to Stock In: " + po.getItemCode() + ", Qty: " + po.getQuantity() + 
//                                         " for date " + po.getRequiredDate() + " in range " + startDate + " to " + endDate);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            System.err.println("Error parsing date range: " + e.getMessage());
//        }
//        System.out.println("Stock In Qty for range " + startDate + " to " + endDate + ": " + stockInQty);
//
//        // Read sales to calculate stock out
//        List<SalesData> salesList = fileManager.readFile(
//            fileManager.getSalesDataFilePath(),
//            line -> {
//                if (line.trim().isEmpty() || line.startsWith("salesId")) return null;
//                String[] data = line.split(",");
//                if (data.length < 6) {
//                    System.out.println("Skipping Sale line with insufficient columns: " + line);
//                    return null;
//                }
//                System.out.println("Reading Sale line: " + String.join(",", data));
//                try {
//                    return new SalesData(data[0], data[1],
//                        Integer.parseInt(data[2]), Double.parseDouble(data[3]), data[4], Double.parseDouble(data[5]));
//                } catch (Exception e) {
//                    System.err.println("Error creating Sale from line '" + String.join(",", data) + "': " + e.getMessage());
//                    return null;
//                }
//            }
//        );
//
//        Map<String, Integer> stockOutQty = new HashMap<>();
//        try {
//            LocalDate start = LocalDate.parse(startDate, dtf);
//            LocalDate end = LocalDate.parse(endDate, dtf);
//            for (SalesData sales : salesList) {
//                if (sales != null) {
//                    LocalDate saleDate = LocalDate.parse(sales.getDate(), dtf);
//                    if (!saleDate.isBefore(start) && !saleDate.isAfter(end)) {
//                        stockOutQty.merge(sales.getItemCode(), sales.getQuantitySold(), Integer::sum);
//                        System.out.println("Added to Stock Out: " + sales.getItemCode() + ", Qty: " + sales.getQuantitySold() + 
//                                         " for date " + sales.getDate() + " in range " + startDate + " to " + endDate);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            System.err.println("Error parsing date range: " + e.getMessage());
//        }
//        System.out.println("Stock Out Qty for range " + startDate + " to " + endDate + ": " + stockOutQty);
//
//        // Populate table
//        tableModel.setRowCount(0); // Clear existing rows
//        for (Item item : itemList) {
//            if (item != null) {
//                String itemCode = item.getItemCode();
//                int currentStock = item.getStockLevel();
//                int stockIn = stockInQty.getOrDefault(itemCode, 0);
//                int stockOut = stockOutQty.getOrDefault(itemCode, 0);
//                int netChange = stockIn - stockOut;
//                int updatedStock = currentStock + netChange;
//
//                tableModel.addRow(new Object[] {
//                    itemCode,
//                    item.getItemName(),
//                    currentStock,
//                    stockIn,
//                    stockOut,
//                    netChange,
//                    updatedStock
//                });
//            }
//        }
//        tableModel.fireTableDataChanged();
//        jTable1.repaint();
//
//        return stockInQty; // Return for potential future use
//    }

    }