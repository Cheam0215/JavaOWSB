/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entities;

/**
 *
 * @author Sheng Ting
 */
public class PurchaseRequisition {
    private String prId;          
    private String itemCode;      
    private int quantity;         
    private String requiredDate;  
    private String supplierCode; 

    public PurchaseRequisition(String prId, String itemCode, int quantity, String requiredDate, String supplierCode) {
        this.prId = prId;
        this.itemCode = itemCode;
        this.quantity = quantity;
        this.requiredDate = requiredDate;
        this.supplierCode = supplierCode;
    }

    public String getPrId() {
        return prId;
    }

    public void setPrId(String prId) {
        this.prId = prId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getRequiredDate() {
        return requiredDate;
    }

    public void setRequiredDate(String requiredDate) {
        this.requiredDate = requiredDate;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }
    
    // For FileManager compatibility
    @Override
    public String toString() {
        return prId + "," + itemCode + "," + quantity + "," + requiredDate + "," + supplierCode;
    }
    
}
