package Entities;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Sheng Ting
 */
public class PurchaseManager extends User{
    
    public PurchaseManager(String userId, String username, String password) {
        super(userId, username, password, "PURCHASE_MANAGER");
    }
    
    public String viewItems() {
        return null;
    }
    
    public String viewSuppliers() {
        return null;
    }
    
    public String viewPurchaseRequisition() {
        return null;
    }
    
    public String generatePurchaseOrder() {
        return null;
    }
    
    public boolean editPurchaseOrder() {
        return true;
    }
    
    public boolean deletePurchaseOrder() {
        return true;
    }
    public String viewPurchaseOrder() {
        return null;
    }
    
    
    
}
