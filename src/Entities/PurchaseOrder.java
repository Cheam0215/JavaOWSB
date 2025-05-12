/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entities;
import Utility.Date;
import Utility.Remark;
import Utility.Status;
/**
 *
 * @author Sheng Ting
 */

public class PurchaseOrder {
    private String poId;          
    private String prId;
    private String raisedBy; 
    private String itemCode;      
    private int quantity;         
    private String supplierCode;  
    private String requiredDate;
    private String requestedDate;
    private Status status;
    private double paymentAmount;
    private Remark remark;


    public PurchaseOrder(String poId, String prId, String raisedBy, String itemCode, int quantity, String supplierCode, String requiredDate, String requestedDate, Status status, double paymentAmount, Remark remark) {
        this.poId = poId;
        this.prId = prId;
        this.raisedBy = raisedBy;
        this.itemCode = itemCode;
        this.quantity = quantity;
        this.supplierCode = supplierCode;
        this.requiredDate = requiredDate;
        this.requestedDate = requestedDate;
        this.status = status;
        this.paymentAmount = paymentAmount;
        this.remark = remark;

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

    public String getRaisedBy() {
        return raisedBy;
    }

    public void setRaisedBy(String raisedBy) {
        this.raisedBy = raisedBy;
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

    public String getRequiredDate() {
        return requiredDate;
    }

    public void setRequiredDate(String requiredDate) {
        this.requiredDate = requiredDate;
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

    public double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public Remark getRemark() {
        return remark;
    }

    public void setRemark(Remark remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return poId + "," + prId + "," + raisedBy + "," + itemCode + "," + quantity + "," + supplierCode + "," + requiredDate + "," + requestedDate + "," + status + "," + paymentAmount + "," + remark;
    }

   
}
