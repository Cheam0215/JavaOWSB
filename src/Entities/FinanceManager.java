/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entities;
import Utility.UserRoles;

public class FinanceManager extends User {

    public FinanceManager(String userId, String username, String password) {
        super(userId, username, password, UserRoles.FINANCE_MANAGER);
    }
    
    public static class InventoryItem {
        private final String itemCode;
        private final String itemName;
        private final int stockLevel;
        private final String supplierCode;
        private final double unitPrice;
        private final double retailPrice;

        public InventoryItem(String itemCode, String itemName, int stockLevel,
                             String supplierCode, double unitPrice, double retailPrice) {
            this.itemCode = itemCode;
            this.itemName = itemName;
            this.stockLevel = stockLevel;
            this.supplierCode = supplierCode;
            this.unitPrice = unitPrice;
            this.retailPrice = retailPrice;
        }

        public String getItemCode() {
            return itemCode;
        }

        public String getItemName() {
            return itemName;
        }

        public int getStockLevel() {
            return stockLevel;
        }

        public String getSupplierCode() {
            return supplierCode;
        }

        public double getUnitPrice() {
            return unitPrice;
        }

        public double getRetailPrice() {
            return retailPrice;
        }
    }

    
}