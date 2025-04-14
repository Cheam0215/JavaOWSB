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
    private String supplierId;  // ID of the supplier providing this item
    private int stockLevel;     // Current stock quantity

    // Constructor
    public Item(String itemCode, String itemName, String supplierId, int stockLevel) {
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.supplierId = supplierId;
        this.stockLevel = stockLevel;
    }

    // Getters
    public String getItemCode() {
        return itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public int getStockLevel() {
        return stockLevel;
    }

    // Setters
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public void setStockLevel(int stockLevel) {
        this.stockLevel = stockLevel;
    }

    // For FileManager compatibility (toStringConverter)
    @Override
    public String toString() {
        return itemCode + "," + itemName + "," + supplierId + "," + stockLevel;
    }
}