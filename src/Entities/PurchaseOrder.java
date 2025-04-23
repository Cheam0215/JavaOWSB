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
    private String raisedBy; 
    private String itemCode;      
    private int quantity;         
    private String supplierCode;  
    private String status;       
    private double paymentAmount;
    private String requestedDate;
    private String remark;
    private String requiredDate;

    public PurchaseOrder(String poId, String prId, String raisedBy, String itemCode, int quantity, String supplierCode, String status, double paymentAmount, String remark, String requestedDate, String requiredDate) {
        this.poId           = poId;
        this.prId           = prId;
        this.raisedBy       = raisedBy;
        this.itemCode       = itemCode;
        this.quantity       = quantity;
        this.supplierCode   = supplierCode;
        this.status         = status;
        this.paymentAmount  = paymentAmount;
        this.requestedDate  = requestedDate;
        this.remark         = remark;
        this.requiredDate   = requiredDate;
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

    public String getStatus(){
        return status;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public double getPaymentAmount(){
        return paymentAmount;
    }

    public void setPaymentAmount(double paymentAmount){
        this.paymentAmount = paymentAmount;
    }

    public String getRequestedDate(){
        return requestedDate;
    }

    public void setRequestedDate(String requestedDate){
        this.requestedDate = requestedDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRequiredDate(){
        return requestedDate;
    }

    public void setRequiredDate(String requiredDate){
        this.requiredDate = requiredDate;
    }

    @Override
    public String toString() {
        return poId + "," + prId + "," + itemCode + "," + quantity + "," + supplierCode + "," + status+ "," + paymentAmount + "," + requiredDate + "+" + requestedDate + "," + remark;
    }

   
}
