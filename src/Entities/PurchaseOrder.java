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
    
    /**
     * Creates a PurchaseOrder object from a CSV string line.
     * @param line The CSV string line from the data file.
     * @return A PurchaseOrder object, or null if the line is invalid or cannot be parsed.
     */
    public static PurchaseOrder fromDataString(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }
        String[] data = line.split(",", -1); // Use -1 to keep trailing empty strings

        // Expecting 11 fields based on your constructor and toString
        if (data.length < 11) {
            System.err.println("Invalid data line for PurchaseOrder (not enough fields): " + line);
            return null;
        }

        try {
            String poId = data[0].trim();
            String prId = data[1].trim();
            String raisedBy = data[2].trim();
            String itemCode = data[3].trim();
            int quantity = Integer.parseInt(data[4].trim());
            String supplierCode = data[5].trim();
            String requiredDate = data[6].trim();
            String requestedDate = data[7].trim();
            Status status = Status.valueOf(data[8].trim().toUpperCase()); // Be robust with enum parsing
            double paymentAmount = Double.parseDouble(data[9].trim());
            Remark remark = Remark.valueOf(data[10].trim().toUpperCase()); // Be robust

            return new PurchaseOrder(poId, prId, raisedBy, itemCode, quantity, supplierCode,
                                     requiredDate, requestedDate, status, paymentAmount, remark);
        } catch (NumberFormatException e) {
            System.err.println("Error parsing number in PurchaseOrder line: " + line + " | " + e.getMessage());
            return null;
        } catch (IllegalArgumentException e) {
            System.err.println("Error parsing enum (Status or Remark) in PurchaseOrder line: " + line + " | " + e.getMessage());
            return null;
        } catch (Exception e) { // Catch any other unexpected errors during parsing
            System.err.println("Unexpected error parsing PurchaseOrder line: " + line + " | " + e.getMessage());
            e.printStackTrace(); // Good to see the stack trace for unexpected issues
            return null;
        }
    }

    // Optional: Consider implementing equals() and hashCode() if you store these in Sets or use them as Map keys.
    // Based on poId for example.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PurchaseOrder that = (PurchaseOrder) o;
        return poId.equals(that.poId);
    }

    @Override
    public int hashCode() {
        return poId.hashCode();
    }

   
}
