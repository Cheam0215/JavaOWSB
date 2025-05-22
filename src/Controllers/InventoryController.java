/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

import Entities.FinanceManager;
import Entities.Item;
import Entities.ItemSupply;
import Entities.PurchaseOrder;
import Entities.User;
import Interface.InventoryServices;
import Interface.InventoryVerificationServices;
import Interface.ItemServices;
import Interface.ItemSupplyServices;
import Interface.PurchaseOrderServices;
import Utility.Status;
import Utility.UserRoles;
import java.util.ArrayList;
import java.util.List;

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
}



