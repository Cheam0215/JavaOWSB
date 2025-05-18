/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entities;

/**
 *
 * @author Edwin Chen
 */
public class ItemSupply {
    private String itemCode;
    private String supplierCode;
    private String itemName;
    private double unitPrice;

    public ItemSupply(String itemCode, String supplierCode, String itemName, double unitPrice) {
        this.itemCode = itemCode;
        this.supplierCode = supplierCode;
        this.itemName = itemName;
        this.unitPrice = unitPrice;
    }
    
    

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Override
    public String toString() {
        return itemCode + "," + supplierCode + "," + itemName + "," + unitPrice;
    }



    
    
}
