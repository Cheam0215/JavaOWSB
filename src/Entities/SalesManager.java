/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
        try {
            String filePath = fileManager.getItemFilePath();
            String absolutePath = new File("").getAbsolutePath() + File.separator + "src" + File.separator + filePath.replace("/", File.separator);

            File file = new File(absolutePath);
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            // Check if the file exists and doesn't end with a newline
            if (file.exists()) {
                try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
                    long length = raf.length();
                    if (length > 0) {
                        raf.seek(length - 1);
                        byte lastByte = raf.readByte();
                        if (lastByte != '\n') {
                            raf.writeBytes(System.lineSeparator()); // Add a platform-specific newline
                        }
                    }
                }
            }

            // Append the new item
            try (FileWriter fw = new FileWriter(absolutePath, true);
                 BufferedWriter bw = new BufferedWriter(fw)) {
                String itemData = String.format("%s,%s,%s,%d,%.1f,%.1f",
                        item.getItemCode(),
                        item.getItemName(),                        
                        item.getStockLevel(),
                        item.getRetailPrice());
                bw.write(itemData);
                bw.newLine(); // Use BufferedWriter's newLine() for platform-specific newline
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateItem(Item updatedItem) {
        try {
            String filePath = fileManager.getItemFilePath();
            String absolutePath = new File("").getAbsolutePath() + File.separator + "src" + File.separator + filePath.replace("/", File.separator);

            File file = new File(absolutePath);
            if (!file.exists()) {
                System.out.println("Item file does not exist: " + absolutePath);
                return false;
            }

            // Read all lines from the file
            List<String> lines = Files.readAllLines(Paths.get(absolutePath));
            List<String> updatedLines = new ArrayList<>();

            // Update the item with the matching itemCode
            boolean found = false;
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].equals(updatedItem.getItemCode())) {
                    found = true;
                    // Format the updated item data
                    String updatedLine = String.format("%s,%s,%s,%d,%.1f,%.1f",
                            updatedItem.getItemCode(),
                            updatedItem.getItemName(),
                            updatedItem.getStockLevel(),
                            updatedItem.getRetailPrice());
                    updatedLines.add(updatedLine);
                } else {
                    updatedLines.add(line);
                }
            }

            if (!found) {
                System.out.println("Item with code " + updatedItem.getItemCode() + " not found.");
                return false;
            }

            // Rewrite the file with the updated lines
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(absolutePath))) {
                for (String line : updatedLines) {
                    bw.write(line);
                    bw.newLine();
                }
            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error updating item: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteItem(String itemCode) {
        try {
            String filePath = fileManager.getItemFilePath();
            String absolutePath = new File("").getAbsolutePath() + File.separator + "src" + File.separator + filePath.replace("/", File.separator);

            File file = new File(absolutePath);
            if (!file.exists()) {
                System.out.println("Item file does not exist: " + absolutePath);
                return false;
            }

            // Read all lines from the file
            List<String> lines = Files.readAllLines(Paths.get(absolutePath));
            List<String> updatedLines = new ArrayList<>();

            // Filter out the item with the matching itemCode
            boolean found = false;
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].equals(itemCode)) {
                    found = true;
                    continue; 
                }
                updatedLines.add(line);
            }

            if (!found) {
                System.out.println("Item with code " + itemCode + " not found.");
                return false;
            }

            // Rewrite the file with the updated lines
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(absolutePath))) {
                for (String line : updatedLines) {
                    bw.write(line);
                    bw.newLine();
                }
            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error deleting item: " + e.getMessage());
            return false;
        }
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

    public void addPurchaseRequisition(String prId, String itemCode, String requestedBy, int quantity, String requiredDate, String requestedDate, Status status) {
        try {
            String filePath = fileManager.getPrFilePath();
            String absolutePath = new File("").getAbsolutePath() + File.separator + "src" + File.separator + filePath.replace("/", File.separator);

            // Append the new purchase requisition
            try (FileWriter fw = new FileWriter(absolutePath, true);
                 BufferedWriter bw = new BufferedWriter(fw)) {
                String prData = String.format("%s,%s,%s,%d,%s,%s,%s",
                    prId,
                    itemCode,
                    requestedBy,
                    quantity,
                    requiredDate,
                    requestedDate,
                    status.toString());
                bw.write(prData);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error adding purchase requisition: " + e.getMessage());
        }
    }
    
    public boolean deletePurchaseRequisition(String prId) {
        try {
            String filePath = fileManager.getPrFilePath();
            String absolutePath = new File("").getAbsolutePath() + File.separator + "src" + File.separator + filePath.replace("/", File.separator);

            // Read all lines from the file
            List<String> lines = Files.readAllLines(Paths.get(absolutePath));
            List<String> updatedLines = new ArrayList<>();

            // Filter out the purchase requisition with the matching prId
            boolean found = false;
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].equals(prId)) {
                    found = true;
                    continue; 
                }
                updatedLines.add(line);
            }

            if (!found) {
                System.out.println("Purchase Requisition with ID " + prId + " not found.");
                return false;
            }

            // Rewrite the file with the updated lines
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(absolutePath))) {
                for (String line : updatedLines) {
                    bw.write(line);
                    bw.newLine();
                }
            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error deleting purchase requisition: " + e.getMessage());
            return false;
        }
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

    public void displayMenu() {
        
    };
    
}