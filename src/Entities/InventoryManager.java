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

/**
 *
 * @author Sheng Ting
 */
public class InventoryManager extends User{
    
    private final Inventory inventory;
    private final FileManager fileManager;

    public InventoryManager(String userId, String username, String password) {
        super(userId, username, password, UserRoles.INVENTORY_MANAGER);
        this.inventory = new Inventory();
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
                return new Item(data[0], data[1], data[2], Integer.parseInt(data[3]),
                               Double.parseDouble(data[4]), Double.parseDouble(data[5]));
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
                    item.getSupplierCode(), // Added missing supplierCode
                    String.valueOf(item.getStockLevel()),
                    String.valueOf(item.getRetailPrice()),
                    String.valueOf(item.getUnitPrice())
                ),
                line -> {
                    String[] data = line.split(",");
                    return new Item(data[0], data[1], data[2], Integer.parseInt(data[3]),
                                   Double.parseDouble(data[4]), Double.parseDouble(data[5]));
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
    
    public void trackLowStock() {
        inventory.checkLowStock();
    }
    
    public String viewPurchaseOrder(){
        return null;
    }

    public String generateStockReport() {
        return inventory.generateStockReport();
    }
    
    public void displayMenu() {
        
    };
    
}
