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
    private int stockLevel;     // Current stock quantity
    private double retailPrice; // Selling price

    // Constructor
    public Item(String itemCode, String itemName, int stockLevel, double retailPrice) {
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.stockLevel = stockLevel;
        this.retailPrice = retailPrice;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getStockLevel() {
        return stockLevel;
    }

    public void setStockLevel(int stockLevel) {
        this.stockLevel = stockLevel;
    }

    public double getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(double retailPrice) {
        this.retailPrice = retailPrice;
    }

    @Override
    public String toString() {
        return "Item{" + "itemCode=" + itemCode + ", itemName=" + itemName + ", stockLevel=" + stockLevel + ", retailPrice=" + retailPrice + '}';
    }

   
}