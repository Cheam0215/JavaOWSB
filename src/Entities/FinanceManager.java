/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entities;

/**
 *
 * @author Sheng Ting
 */
public class FinanceManager extends User{
   final private Inventory inventory;
    
    public FinanceManager(String userId, String username, String password) {
        super(userId, username, password, "FINANCE_MANAGER");
        this.inventory = new Inventory();
    }
    
    public boolean approvePurchaseOrder(String poId) {
        return true;
    }

    public boolean verifyInventoryUpdate(String poId) {
        return true;
    }

    public boolean processPayment(String poId, double amount) {
        return true;
    }

    public String generateFinancialReport() {
        return null;
    }

    public String viewPurchaseRequisition() {
        return null;
    }

  
    public String viewPurchaseOrder() {
        return null;
    }
}

