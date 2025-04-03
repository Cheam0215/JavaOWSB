/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entities;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author Sheng Ting
 */
public class Supplier {
    private String supplierCode;
    private String supplierName;
    private List<String> itemIDs;
    
    public Supplier(String supplierCode, String supplierName) {
        this.supplierCode = supplierCode;
        this.supplierName = supplierName;
        this.itemIDs = new ArrayList<>();
    }

    // Getters
    public String getSupplierCode() {
        return supplierCode;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public List<String> getItemIds() {
        return new ArrayList<>(itemIDs); // Return a copy to protect encapsulation
    }

    // Setters
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    // Methods to manage itemIds
    public void addItemId(String itemId) {
        if (!itemIDs.contains(itemId)) {
            itemIDs.add(itemId);
        }
    }

    public void removeItemId(String itemId) {
        itemIDs.remove(itemId);
    }

    // For FileManager compatibility (toStringConverter)
    @Override
    public String toString() {
        String items = String.join(";", itemIDs); // Use semicolon to separate item IDs
        return supplierCode + "," + supplierName + "," + (items.isEmpty() ? "NONE" : items);
    }
    
}
