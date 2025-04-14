/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entities;

/**
 *
 * @author Sheng Ting
 */
public class InventoryManager extends User{
    
    private final Inventory inventory;

    public InventoryManager(String userId, String username, String password) {
        super(userId, username, password, "INVENTORY_MANAGER");
        this.inventory = new Inventory();
    }

    public void updateStock(String itemCode, int quantityReceived) {
        // 1. Check PO
        // 2. Check if item in database match with PO
        // 3. proceed with update using inventory method
        
        inventory.updateStock();
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
