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
    private List<String> itemCode;
    private int contactNumber;
    private String address;
    private String bankAccount;
    
    
    public Supplier(String supplierCode, String supplierName, int contactNumber, String address, String bankAccount) {
        this.supplierCode = supplierCode;
        this.supplierName = supplierName;
        this.itemCode = new ArrayList<>();
        this.contactNumber = contactNumber;
        this.address = address;
        this.bankAccount = bankAccount;

    }
    
    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public List<String> getItemIds() {
        return new ArrayList<>(itemCode); // Return a copy to protect encapsulation
    }

    public int getContactNumber() {
        return contactNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public void setContactNumber(int contactNumber) {
        this.contactNumber = contactNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // Methods to manage itemIds
    public void addItemId(String itemId) {
        if (!itemCode.contains(itemId)) {
            itemCode.add(itemId);
        }
    }

    public void removeItemId(String itemId) {
        itemCode.remove(itemId);
    }

    // For FileManager compatibility (toStringConverter)
    @Override
    public String toString() {
        String items = String.join(";", itemCode); // Use semicolon to separate item IDs
        return supplierCode + "," + supplierName + "," + (items.isEmpty() ? "NONE" : items + "," + contactNumber + "," + address);
        
    }
    
    public List<String> getItemCodes() {
        return itemCode;
    }

    public void setItemCodes(List<String> itemCodes) {
        this.itemCode = itemCodes;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;

    }
    
    
    
}
