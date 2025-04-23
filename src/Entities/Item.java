/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entities;

/**
 *
 * @author Sheng Ting
 */
public class Item {
    private String itemCode;    // Unique identifier for the item
    private String itemName;    // Name of the item
    private String supplierCode;  // ID of the supplier providing this item
    private int stockLevel;     // Current stock quantity
    private double unitPrice;
    private double retailPrice;

    // Constructor
    public Item(String itemCode, String itemName, String supplierId, int stockLevel, double unitPrice, double retailPrice) {
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.supplierCode = supplierId;
        this.stockLevel = stockLevel;
        this.unitPrice = unitPrice;
        this.retailPrice = retailPrice;
    }

    // Getters
    public String getItemCode() {
        return itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public int getStockLevel() {
        return stockLevel;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public double getRetailPrice() {
        return retailPrice;
    }

    // Setters
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public void setStockLevel(int stockLevel) {
        this.stockLevel = stockLevel;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setRetailPrice(double retailPrice) {
        this.retailPrice = retailPrice;
    }

    // For FileManager compatibility (toStringConverter)
    @Override
    public String toString() {
        return itemCode + "," + itemName + "," + supplierCode + "," + stockLevel + "," + unitPrice + "," + retailPrice;
    }
}