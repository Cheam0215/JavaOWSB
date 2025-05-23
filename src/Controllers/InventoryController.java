/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

import Entities.FinanceManager;
import Entities.Item;
import Entities.ItemSupply;
import Entities.PurchaseOrder;
import Entities.SalesData;
import Entities.StockReportData;
import Entities.User;
import Interface.InventoryServices;
import Interface.InventoryVerificationServices;
import Interface.ItemServices;
import Interface.ItemSupplyServices;
import Interface.PurchaseOrderServices;
import Utility.Remark;
import Utility.Status;
import Utility.UserRoles;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Sheng Ting
 */
public class InventoryController implements InventoryServices, InventoryVerificationServices{
    
    private final PurchaseOrderServices poService;
    private final ItemServices itemService;
    private final ItemSupplyServices itemSupplyService;

    public InventoryController(PurchaseOrderServices poService, ItemServices itemService, ItemSupplyServices itemSupplyService) {
        this.poService = poService;
        this.itemService = itemService;
        this.itemSupplyService = itemSupplyService;
    }

    @Override
    public String receiveStockAndUpdateInventory(String itemCode, int quantityReceived, User performingUser) {
        // --- Authorization Check ---
        if (performingUser == null ||
            (performingUser.getRole() != UserRoles.INVENTORY_MANAGER && performingUser.getRole() != UserRoles.ADMINISTRATOR)) {
            return "Error: Unauthorized to perform stock update.";
        }

        // --- Input Validation ---
        if (itemCode == null || itemCode.trim().isEmpty()) {
            return "Error: Item code cannot be empty.";
        }
        if (quantityReceived <= 0) {
            return "Error: Quantity received must be a positive number.";
        }

        // 1. Find Approved PO using PurchaseOrderService
        PurchaseOrder matchedPO = poService.findApprovedPOByItemCode(itemCode);

        if (matchedPO == null) {
            return "Error: No approved Purchase Order found for item code: " + itemCode;
        }

        // 2. Validate quantity against PO
        if (quantityReceived > matchedPO.getQuantity()) {
            return "Error: Received quantity (" + quantityReceived +
                   ") exceeds PO quantity (" + matchedPO.getQuantity() + ") for PO ID: " + matchedPO.getPoId();
        }

        // 3. Find Item using ItemService
        Item matchedItem = itemService.getItemByCode(itemCode);

        if (matchedItem == null) {
            System.err.println("Data Inconsistency: Item code '" + itemCode + "' found in PO " + matchedPO.getPoId() +
                               " but not found in item inventory.");
            return "Error: Item with code '" + itemCode + "' not found in inventory, though a PO exists. Cannot update stock.";
        }

        // --- Attempt Transaction-like updates ---
        Status originalPOStatus = matchedPO.getStatus(); // For potential rollback

        // Update PO Status to RECEIVED via PurchaseOrderService
        boolean poStatusUpdated = poService.updatePurchaseOrderStatus(matchedPO.getPoId(), Status.RECEIVED); // Method returns boolean

        if (!poStatusUpdated) { // Check the boolean result
            return "Error: Failed to update Purchase Order status to RECEIVED for PO ID: " + matchedPO.getPoId() + ". Check service logs for details.";
        }

        // If PO update succeeded, proceed to update item stock
        int newStockLevel = matchedItem.getStockLevel() + quantityReceived;
        // Assuming itemService.updateItemStockLevel also returns boolean
        boolean itemStockUpdated = itemService.updateItemStockLevel(itemCode, newStockLevel);

        if (!itemStockUpdated) { // Check the boolean result
            // Rollback PO status
            boolean rollbackSuccess = poService.updatePurchaseOrderStatus(matchedPO.getPoId(), originalPOStatus);
            String baseErrorMessage = "Error: Failed to update item stock level for " + itemCode + ". Check service logs for details.";

            if (!rollbackSuccess) {
                System.err.println("CRITICAL ERROR: Failed to update item stock for " + itemCode +
                                   " AND failed to rollback PO status for PO ID: " + matchedPO.getPoId() +
                                   ". Manual database correction required.");
                return baseErrorMessage + " CRITICAL: Purchase Order status rollback also failed. Data may be inconsistent.";
            }
            return baseErrorMessage + " Purchase Order status has been successfully rolled back to " + originalPOStatus + ".";
        }

        // If both updates were successful
        return "Stock for item '" + itemCode + "' updated successfully by " + quantityReceived +
               " units. New stock level: " + newStockLevel + ". PO '" + matchedPO.getPoId() + "' marked as RECEIVED.";
    }
    
    @Override
    public List<FinanceManager.InventoryItem> verifyInventoryUpdate() {
        
        List<Item> itemList = itemService.getAllItems();
        List<ItemSupply> supplyList = itemSupplyService.getAllItemSupply();

        List<FinanceManager.InventoryItem> inventoryItems = new ArrayList<>();
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
            inventoryItems.add(new FinanceManager.InventoryItem(
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
    
    @Override
    public StockReportData displayStockReport(List<PurchaseOrder> poList, List<SalesData> salesList, List<Item> itemList, String startDate, String endDate) {
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        Map<String, Integer> stockInQty = new HashMap<>();
        Map<String, Double> stockInValue = new HashMap<>();
        List<PurchaseOrder> stockInPOs = new ArrayList<>();
        try {
            LocalDate start = LocalDate.parse(startDate, dtf);
            LocalDate end = LocalDate.parse(endDate, dtf);
            for (PurchaseOrder po : poList) {
                if (po != null) {
                    System.out.println("Processing PO: " + po.getPoId() + ", Status: " + po.getStatus() + 
                                     ", Date: " + po.getRequiredDate() + ", Order Date: " + po.getRequestedDate());
                    try {
                        LocalDate requiredDate = LocalDate.parse(po.getRequiredDate(), dtf);
                        if ((po.getStatus().equals(Status.RECEIVED) || po.getStatus().equals(Status.PAID)) && 
                            !requiredDate.isBefore(start) && !requiredDate.isAfter(end)) {
                            stockInPOs.add(po);
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

        Map<String, Integer> stockOutQty = new HashMap<>();
        Map<String, Double> stockOutValue = new HashMap<>();
        List<SalesData> stockOutSales = new ArrayList<>();
        try {
            LocalDate start = LocalDate.parse(startDate, dtf);
            LocalDate end = LocalDate.parse(endDate, dtf);
            for (SalesData sales : salesList) {
                if (sales != null) {
                    try {
                        LocalDate saleDate = LocalDate.parse(sales.getDate(), dtf);
                        if (!saleDate.isBefore(start) && !saleDate.isAfter(end)) {
                            stockOutSales.add(sales);
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


    return new StockReportData(stockInPOs, stockOutSales, itemList, stockInQty, stockInValue, stockOutQty, stockOutValue);
}
    
    @Override
    public Map<String, Integer> calculateStockReport(List<Item> itemList, List<PurchaseOrder> poList, List<SalesData> salesList, String startDate, String endDate, DefaultTableModel tableModel, JTable jTable1) {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        Map<String, Integer> stockInQty = new HashMap<>();
        try {
            LocalDate start = LocalDate.parse(startDate, dtf);
            LocalDate end = LocalDate.parse(endDate, dtf);
            for (PurchaseOrder po : poList) {
                if (po != null && EnumSet.of(Status.RECEIVED, Status.PAID).contains(po.getStatus())) {
                    LocalDate requiredDate = LocalDate.parse(po.getRequiredDate(), dtf);
                    if (!requiredDate.isBefore(start) && !requiredDate.isAfter(end)) {
                        stockInQty.merge(po.getItemCode(), po.getQuantity(), Integer::sum);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing date range: " + e.getMessage());
        }


        Map<String, Integer> stockOutQty = new HashMap<>();
        try {
            LocalDate start = LocalDate.parse(startDate, dtf);
            LocalDate end = LocalDate.parse(endDate, dtf);
            for (SalesData sales : salesList) {
                if (sales != null) {
                    LocalDate saleDate = LocalDate.parse(sales.getDate(), dtf);
                    if (!saleDate.isBefore(start) && !saleDate.isAfter(end)) {
                        stockOutQty.merge(sales.getItemCode(), sales.getQuantitySold(), Integer::sum);
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

}




