/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entities;

/**
 *
 * @author Sheng Ting
 */
public class PurchaseOrder {
    private String poId;          
    private String prId;          
    private String itemCode;      
    private int quantity;         
    private String supplierCode;  
    private String status;        // e.g., "PENDING", "APPROVED"
    private double paymentAmount;

    public PurchaseOrder(String poId, String prId, String itemCode, int quantity, String supplierCode, String status) {
        this.poId           = poId;
        this.prId           = prId;
        this.itemCode       = itemCode;
        this.quantity       = quantity;
        this.supplierCode   = supplierCode;
        this.status         = status;
        this.paymentAmount  = paymentAmount;
    }

    public String getPoId() {
        return poId;
    }

    public void setPoId(String poId) {
        this.poId = poId;
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

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    // For FileManager compatibility
    @Override
    public String toString() {
        return poId + "," + prId + "," + itemCode + "," + quantity + "," + supplierCode + "," + status+ "," + paymentAmount;
    }

   
}
