package Entities;

import Utility.FileManager;
import Utility.Remark;
import Utility.Status;
import Utility.UserRoles;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Sheng Ting
 */
public class PurchaseManager extends User{
    private final FileManager fileManager;
    
    public PurchaseManager(String userId, String username, String password) {
        super(userId, username, password, UserRoles.PURCHASE_MANAGER);
        this.fileManager = new FileManager();
    }
    
    public List<String[]> viewItems() {
        List<Item> itemList = fileManager.readFile(
            fileManager.getItemFilePath(),
            line -> {
                String[] data = line.split(",");
                // Ensure the line has the expected number of fields
                if (data.length < 4) {
                    System.out.println("Invalid item data: " + line);
                    return null; // Skip invalid lines
                }
                try {
                    return new Item(
                        data[0], // itemCode
                        data[1], // itemName
                        Integer.parseInt(data[2]), // stockLevel
                        Double.parseDouble(data[3]) // retailPrice
                    );
                } catch (NumberFormatException e) {
                    System.out.println("Error parsing item data: " + line + " | " + e.getMessage());
                    return null; // Skip lines with parsing errors
                }
            }
        );

        // Filter out null entries (invalid lines)
        List<String[]> result = new ArrayList<>();
        for (Item item : itemList) {
            if (item == null) continue; // Skip invalid items
            String[] row = new String[]{
                item.getItemCode(),
                item.getItemName(),
                String.valueOf(item.getStockLevel()),
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
                    Integer.parseInt(data[2]), // contactNumber is in position 3
                    data[3], // address is in position 4
                    data[4]); // bankAccount is in position 5

                // Add the itemCode to the supplier's list of item codes

                return supplier;
            }
        );

        // Convert the list of Supplier objects to List<String[]> for the table
        List<String[]> result = new ArrayList<>();
        for (Supplier supplier : supplierList) {
            String[] row = new String[]{
                supplier.getSupplierCode(),
                supplier.getSupplierName(),
                String.valueOf(supplier.getContactNumber()),
                supplier.getAddress(),
                supplier.getBankAccount()
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
                    data[5],                   // requestedDate
                    Status.valueOf(data[6])    // status - assuming Status is an enum
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
    
    public String generatePurchaseOrder() {
        return "";
    }
    
    public boolean editPurchaseOrder() {
        return true;
    }
    
    public boolean deletePurchaseOrder() {
        return true;
    }
    
    public void displayMenu() {
        
    };
    
    
}
