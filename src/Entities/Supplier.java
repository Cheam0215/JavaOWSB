/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entities;



/**
 *
 * @author Sheng Ting
 */
public class Supplier {
    private String supplierCode;
    private String supplierName;
    private int contactNumber;
    private String address;
    private int bankAccount;
    
    
    public Supplier(String supplierCode, String supplierName, int contactNumber, String address, int bankAccount) {
        this.supplierCode = supplierCode;
        this.supplierName = supplierName;
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

    @Override
    public String toString() {
        return supplierCode + "," + supplierName + "," + contactNumber + "," + address + "," + bankAccount;
    }
    

    

    public int getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(int bankAccount) {
        this.bankAccount = bankAccount;

    }
    
    
    
}
