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
    private int unitPriwe;

    public ItemSupply(String itemCode, String supplierCode, String itemName, int unitPriwe) {
        this.itemCode = itemCode;
        this.supplierCode = supplierCode;
        this.itemName = itemName;
        this.unitPriwe = unitPriwe;
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

    public int getUnitPriwe() {
        return unitPriwe;
    }

    public void setUnitPriwe(int unitPriwe) {
        this.unitPriwe = unitPriwe;
    }

    @Override
    public String toString() {
        return "ItemSupply{" + "itemCode=" + itemCode + ", supplierCode=" + supplierCode + ", itemName=" + itemName + ", unitPriwe=" + unitPriwe + '}';
    }
    
    
}
