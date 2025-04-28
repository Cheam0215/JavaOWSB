/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entities;

import Utility.FileManager;
import Utility.Remark;
import Utility.Status;
import Utility.UserRoles;
import java.util.ArrayList;
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
    
    public List<String[]> viewItems() {
        List<Item> itemList = fileManager.readFile(
            fileManager.getItemFilePath(),
            line -> {
                String[] data = line.split(",");
                return new Item(data[0], data[1], data[2], 
                    Integer.parseInt(data[3]), 
                    Double.parseDouble(data[4]), 
                    Double.parseDouble(data[5]));
            }
        );

        // Convert the list of Item objects to List<String[]> for the table
        List<String[]> result = new ArrayList<>();
        for (Item item : itemList) {
            String[] row = new String[]{
                item.getItemCode(),
                item.getItemName(),
                item.getSupplierCode(),
                String.valueOf(item.getStockLevel()),
                String.valueOf(item.getUnitPrice()),
                String.valueOf(item.getRetailPrice())
            };
            result.add(row);
        }
        return result;
    }
    
    public List<String[]> viewSuppliers() {
        List<Supplier> supplierList = fileManager.readFile(
            fileManager.getSupplierFilePath(),
            line -> {
                String[] data = line.split(",");
                // Create a supplier with the correct parameter order
                // The format seems to be: supplierCode, supplierName, itemCode, contactNumber, address, bankAccount
                Supplier supplier = new Supplier(data[0], data[1], 
                    Integer.parseInt(data[3]), // contactNumber is in position 3
                    data[4], // address is in position 4
                    data[5]); // bankAccount is in position 5

                // Add the itemCode to the supplier's list of item codes
                supplier.getItemCodes().add(data[2]);

                return supplier;
            }
        );

        // Convert the list of Supplier objects to List<String[]> for the table
        List<String[]> result = new ArrayList<>();
        for (Supplier supplier : supplierList) {
            String[] row = new String[]{
                supplier.getSupplierCode(),
                supplier.getSupplierName(),
                String.join(", ", supplier.getItemCodes()), // Format the list of item codes
                String.valueOf(supplier.getContactNumber()),
                supplier.getAddress(),
                supplier.getBankAccount()
            };
            result.add(row);
        }
        return result;
    }
    
    public List<String[]> viewSalesData() {
        List<SalesData> salesList = fileManager.readFile(
            fileManager.getSalesDataFilePath(),
            line -> {
                String[] data = line.split(",");
                return new SalesData(
                    data[0],                     // salesId
                    data[1],                     // itemCode
                    Integer.parseInt(data[2]),   // quantitySold
                    Double.parseDouble(data[3]), // unitPrice
                    Double.parseDouble(data[4]), // retailPrice
                    data[5],                     // date
                    Double.parseDouble(data[6])  // totalAmount
                );
            }
        );

        // Convert the list of SalesData objects to List<String[]> for the table
        List<String[]> result = new ArrayList<>();
        for (SalesData sale : salesList) {
            String[] row = new String[]{
                sale.getSalesId(),
                sale.getItemCode(),
                String.valueOf(sale.getQuantitySold()),
                String.valueOf(sale.getUnitPrice()),
                String.valueOf(sale.getRetailPrice()),
                sale.getDate(),
                String.valueOf(sale.getTotalAmount())
            };
            result.add(row);
        }
        return result;
    }
    

    public List<String[]> viewPurchaseRequisition() {
        List<PurchaseRequisition> prList = fileManager.readFile(
            fileManager.getPrFilePath(),
            line -> {
                String[] data = line.split(",");
                return new PurchaseRequisition(
                    data[0],                   // prId
                    data[1],                   // itemCode
                    data[2],                   // requestedBy
                    Integer.parseInt(data[3]), // quantity
                    data[4],                   // requiredDate
                    data[5],                   // supplierCode
                    data[6],                   // requestedDate
                    Status.valueOf(data[7])    // status - assuming Status is an enum
                );
            }
        );

        // Convert the list of PurchaseRequisition objects to List<String[]> for the table
        List<String[]> result = new ArrayList<>();
        for (PurchaseRequisition pr : prList) {
            String[] row = new String[]{
                pr.getPrId(),
                pr.getItemCode(),
                pr.getRequestedBy(),
                String.valueOf(pr.getQuantity()),
                pr.getRequiredDate(),
                pr.getSupplierCode(),
                pr.getRequestedDate(),
                pr.getStatus().toString()
            };
            result.add(row);
        }
        return result;
    }

    public List<String[]> viewPurchaseOrder() {
        List<PurchaseOrder> poList = fileManager.readFile(
            fileManager.getPoFilePath(),
            line -> {
                String[] data = line.split(",");
                return new PurchaseOrder(data[0], data[1], data[2], data[3],
                    Integer.parseInt(data[4]), data[5], data[6], data[7], Status.valueOf(data[8]),
                    Double.parseDouble(data[9]), Remark.valueOf(data[10]));
            }
        );

        // Convert the list of PurchaseOrder objects to List<String[]> for the table
        List<String[]> result = new ArrayList<>();
        for (PurchaseOrder po : poList) {
            String[] row = new String[]{
                po.getPoId(),
                po.getPrId(),
                po.getRaisedBy(),
                po.getItemCode(),
                String.valueOf(po.getQuantity()),
                po.getSupplierCode(),
                po.getRequiredDate(),
                po.getRequestedDate(),
                po.getStatus().toString(),
                String.valueOf(po.getPaymentAmount()),
                po.getRemark().toString()
            };
            result.add(row);
        }
        return result;
    }

    public void displayMenu() {
        
    };
    
}