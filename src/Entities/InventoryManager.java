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
public class InventoryManager extends User{
    
    private final Inventory inventory;
    private final FileManager fileManager;

    public InventoryManager(String userId, String username, String password) {
        super(userId, username, password, "INVENTORY_MANAGER");
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
                return new PurchaseOrder(data[0], data[1], data[2], 
                    data[3], Integer.parseInt(data[4]), data[5], data[6], Double.parseDouble(data[7]));
            }
        );
        
        boolean poFound = false;
        PurchaseOrder matchedPO = null;
        
        for (PurchaseOrder po : poList){
            if (po.getItemCode().equals(itemCode) && po.getStatus().equals("APPROVED")) {
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
        
         try {
             // Update PO status
            matchedPO.setStatus("RECEIVED");
            
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
                    po.getStatus(),
                    String.valueOf(po.getPaymentAmount())
                ),
                line -> {
                String[] data = line.split(",");
                return new PurchaseOrder(data[0], data[1], data[2], 
                    data[3], Integer.parseInt(data[4]), data[5], data[6], Double.parseDouble(data[7]));
                }
            );
            
            if (!poUpdated) {
                throw new RuntimeException("Failed to update PO file");
            }
            
            // Read items
            List<Item> itemList = fileManager.readFile(
                fileManager.getItemFilePath(),
                line -> {
                    String[] data = line.split(",");
                    return new Item(data[0], data[1], data[2], Integer.parseInt(data[3]));
                }
            );
            
            // Update item quantity
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
            
            // Update item file
            boolean itemUpdated = fileManager.updateToFile(
                matchedItem,
                fileManager.getItemFilePath(),
                Item::getItemCode,
                item -> String.join(",", 
                    item.getItemCode(),
                    item.getItemName(),
                    item.getSupplierId(),
                    String.valueOf(item.getStockLevel())
                ),
                line -> {
                    String[] data = line.split(",");
                    return new Item(data[0], data[1], data[2], Integer.parseInt(data[3]));
                }
            );
            
            if (!itemUpdated) {
                throw new RuntimeException("Failed to update item file");
            }
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to update stock: " + e.getMessage(), e);
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
    
}
