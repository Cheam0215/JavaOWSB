/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entities;

import Utility.FileManager;
import Utility.UserRoles;
import java.util.List;


/**
 *
 * @author Sheng Ting
 */
public class SalesManager extends User {
    private FileManager fileManager;

    // Constructor
    public SalesManager(String userId, String username, String password) {
        super(userId, username, password, UserRoles.SALES_MANAGER);
        this.fileManager = new FileManager();
    }

    // Item Management
    public boolean addItem(Item item) {
        return fileManager.writeToFile(
            item, fileManager.getItemFilePath(),
            Item::getItemCode, Item::toString
        );
    }

    public boolean updateItem(Item updatedItem) {
        return fileManager.updateToFile(
            updatedItem, fileManager.getItemFilePath(),
            Item::getItemCode, Item::toString,
            line -> {
                String[] data = line.split(",");
                return new Item(data[0], data[1], data[2], Integer.parseInt(data[3]), Double.parseDouble(data[4]), Double.parseDouble(data[5]));
            }
        );
    }

    public boolean deleteItem(String itemCode) {
        return fileManager.deleteFromFile(
            itemCode, fileManager.getItemFilePath(),
            Item::getItemCode,
            line -> {
                String[] data = line.split(",");
                return new Item(data[0], data[1], data[2], Integer.parseInt(data[3]), Double.parseDouble(data[4]), Double.parseDouble(data[5]));
            }
        );
    }

    // Supplier Management
    public boolean addSupplier(Supplier supplier) {
        return fileManager.writeToFile(
            supplier, fileManager.getSupplierFilePath(),
            Supplier::getSupplierCode, Supplier::toString
        );
    }

    public boolean updateSupplier(Supplier updatedSupplier) {
//        return fileManager.updateToFile(
//            updatedSupplier, fileManager.getSupplierFilePath(),
//            Supplier::getSupplierCode, Supplier::toString,
//            line -> {
//                String[] data = line.split(",");
//                Supplier s = new Supplier(data[0], data[1]);
//                if (!data[2].equals("NONE")) {
//                    String[] items = data[2].split(";");
//                    for (String itemId : items) s.addItemId(itemId);
//                }
//                return s;
//            }
//        );
            return true;
    }

    public boolean deleteSupplier(String supplierCode) {
//        return fileManager.deleteFromFile(
//            supplierCode, fileManager.getSupplierFilePath(),
//            Supplier::getSupplierCode,
//            line -> {
//                String[] data = line.split(",");
//                Supplier s = new Supplier(data[0], data[1]);
//                if (!data[2].equals("NONE")) {
//                    String[] items = data[2].split(";");
//                    for (String itemId : items) s.addItemId(itemId);
//                }
//                return s;
//            }
//        );

        return true;
    }

    // Placeholder for Sales Data and PR methods (expand as needed)
    public void enterSalesData(String itemCode, int quantitySold, String date) {
        System.out.println("Entering sales data: " + itemCode + ", " + quantitySold + ", " + date);
        // Add logic to save sales data and update stock
    }

    public void createPurchaseRequisition(String prId, String itemCode, int quantity, String requiredDate) {
        System.out.println("Creating PR: " + prId + ", " + itemCode + ", " + quantity + ", " + requiredDate);
        // Add logic to save PR to file
    }

    public List<String> viewPurchaseRequisition() {
        System.out.println("Viewing all PRs...");
        return List.of();
    }

    public List<String> viewPurchaseOrder() {
        System.out.println("Viewing all POs...");
        return List.of(); 
    }
}