/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entities;

import Utility.Status;

/**
 *
 * @author Sheng Ting
 */
public class PurchaseRequisition {
    private String prId;          
    private String itemCode;
    private String requestedBy; // ID of Sale's Manager
    private int quantity;         
    private String requiredDate;  
    private String supplierCode; 
    private String requestedDate;
    private Status status;

    public PurchaseRequisition(String prId, String itemCode, String requestedBy, int quantity, String requiredDate, String supplierCode, String requestedDate, Status status) {
        this.prId = prId;
        this.itemCode = itemCode;
        this.requestedBy = requestedBy;
        this.quantity = quantity;
        this.requiredDate = requiredDate;
        this.supplierCode = supplierCode;
        this.requestedDate = requestedDate;
        this.status = status;
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

    public String getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
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

    public String getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(String requestedDate) {
        this.requestedDate = requestedDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    
    
    
    
    // For FileManager compatibility
    @Override
    public String toString() {
        return prId + "," + itemCode + "," + quantity + "," + requiredDate + "," + supplierCode + "," + requestedDate + "," + status;
    }
    
}
