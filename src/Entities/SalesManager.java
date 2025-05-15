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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
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
    public boolean validateSupplierCode(String supplierCode) {
        try {
            String filePath = fileManager.getSupplierFilePath();
            String absolutePath = new File("").getAbsolutePath() + File.separator + "src" + File.separator + filePath.replace("/", File.separator);
            List<String> lines = Files.readAllLines(Paths.get(absolutePath));
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].equals(supplierCode)) {
                    return true;
                }
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error validating supplier code: " + e.getMessage());
            return false;
        }
    }

    public void addItem(Item item, String supplierCode, double unitPrice) {
        try {
            // Validate supplierCode
            if (!validateSupplierCode(supplierCode)) {
                System.out.println("Invalid supplier code: " + supplierCode);
                return;
            }

            // Add to itemFile.txt
            String itemFilePath = fileManager.getItemFilePath();
            String itemAbsolutePath = new File("").getAbsolutePath() + File.separator + "src" + File.separator + itemFilePath.replace("/", File.separator);
            try (FileWriter fw = new FileWriter(itemAbsolutePath, true);
                 BufferedWriter bw = new BufferedWriter(fw)) {
                String itemData = String.format("%s,%s,%d,%.2f",
                    item.getItemCode(),
                    item.getItemName(),
                    item.getStockLevel(),
                    item.getRetailPrice());
                bw.write(itemData);
                bw.newLine();
            }

            // Add to itemSupply.txt
            String supplyFilePath = fileManager.getItemSupplyFilePath();
            String supplyAbsolutePath = new File("").getAbsolutePath() + File.separator + "src" + File.separator + supplyFilePath.replace("/", File.separator);
            try (FileWriter fw = new FileWriter(supplyAbsolutePath, true);
                 BufferedWriter bw = new BufferedWriter(fw)) {
                String supplyData = String.format("%s,%s,%s,%.2f",
                    item.getItemCode(),
                    supplierCode,
                    item.getItemName(),
                    unitPrice);
                bw.write(supplyData);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error adding item: " + e.getMessage());
        }
    }

    public boolean updateItem(Item updatedItem, String supplierCode, double unitPrice) {
        try {
            // Validate supplierCode
            if (!validateSupplierCode(supplierCode)) {
                System.out.println("Invalid supplier code: " + supplierCode);
                return false;
            }

            // Update itemFile.txt
            String itemFilePath = fileManager.getItemFilePath();
            String itemAbsolutePath = new File("").getAbsolutePath() + File.separator + "src" + File.separator + itemFilePath.replace("/", File.separator);
            List<String> itemLines = Files.readAllLines(Paths.get(itemAbsolutePath));
            List<String> updatedItemLines = new ArrayList<>();
            boolean itemFound = false;

            for (String line : itemLines) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].equals(updatedItem.getItemCode())) {
                    itemFound = true;
                    String updatedLine = String.format("%s,%s,%d,%.2f",
                            updatedItem.getItemCode(),
                            updatedItem.getItemName(),
                            updatedItem.getStockLevel(),
                            updatedItem.getRetailPrice());
                    updatedItemLines.add(updatedLine);
                } else {
                    updatedItemLines.add(line);
                }
            }

            if (!itemFound) {
                System.out.println("Item with code " + updatedItem.getItemCode() + " not found in itemFile.txt.");
                return false;
            }

            // Rewrite itemFile.txt
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(itemAbsolutePath))) {
                for (String line : updatedItemLines) {
                    bw.write(line);
                    bw.newLine();
                }
            }

            // Update itemSupply.txt
            String supplyFilePath = fileManager.getItemSupplyFilePath();
            String supplyAbsolutePath = new File("").getAbsolutePath() + File.separator + "src" + File.separator + supplyFilePath.replace("/", File.separator);
            List<String> supplyLines = Files.readAllLines(Paths.get(supplyAbsolutePath));
            List<String> updatedSupplyLines = new ArrayList<>();
            boolean supplyFound = false;

            for (String line : supplyLines) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].equals(updatedItem.getItemCode())) {
                    supplyFound = true;
                    String updatedLine = String.format("%s,%s,%s,%.2f",
                            updatedItem.getItemCode(),
                            supplierCode,
                            updatedItem.getItemName(),
                            unitPrice);
                    updatedSupplyLines.add(updatedLine);
                } else {
                    updatedSupplyLines.add(line);
                }
            }

            if (!supplyFound) {
                System.out.println("Item with code " + updatedItem.getItemCode() + " not found in itemSupply.txt.");
                return false;
            }

            // Rewrite itemSupply.txt
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(supplyAbsolutePath))) {
                for (String line : updatedSupplyLines) {
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
            // Delete from itemFile.txt
            String itemFilePath = fileManager.getItemFilePath();
            String itemAbsolutePath = new File("").getAbsolutePath() + File.separator + "src" + File.separator + itemFilePath.replace("/", File.separator);
            List<String> itemLines = Files.readAllLines(Paths.get(itemAbsolutePath));
            List<String> updatedItemLines = new ArrayList<>();
            boolean itemFound = false;

            for (String line : itemLines) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].equals(itemCode)) {
                    itemFound = true;
                    continue;
                }
                updatedItemLines.add(line);
            }

            if (!itemFound) {
                System.out.println("Item with code " + itemCode + " not found in itemFile.txt.");
                return false;
            }

            // Rewrite itemFile.txt
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(itemAbsolutePath))) {
                for (String line : updatedItemLines) {
                    bw.write(line);
                    bw.newLine();
                }
            }

            // Delete from itemSupply.txt
            String supplyFilePath = fileManager.getItemSupplyFilePath();
            String supplyAbsolutePath = new File("").getAbsolutePath() + File.separator + "src" + File.separator + supplyFilePath.replace("/", File.separator);
            List<String> supplyLines = Files.readAllLines(Paths.get(supplyAbsolutePath));
            List<String> updatedSupplyLines = new ArrayList<>();
            boolean supplyFound = false;

            for (String line : supplyLines) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].equals(itemCode)) {
                    supplyFound = true;
                    continue;
                }
                updatedSupplyLines.add(line);
            }

            if (!supplyFound) {
                System.out.println("Item with code " + itemCode + " not found in itemSupply.txt.");
                return false;
            }

            // Rewrite itemSupply.txt
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(supplyAbsolutePath))) {
                for (String line : updatedSupplyLines) {
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
    
    public void addItemSupply(ItemSupply itemSupply) {
        try {
            // Validate supplierCode
            if (!validateSupplierCode(itemSupply.getSupplierCode())) {
                System.out.println("Invalid supplier code: " + itemSupply.getSupplierCode());
                return;
            }

            // Add to itemSupply.txt
            String supplyFilePath = fileManager.getItemSupplyFilePath();
            String supplyAbsolutePath = new File("").getAbsolutePath() + File.separator + "src" + File.separator + supplyFilePath.replace("/", File.separator);
            try (FileWriter fw = new FileWriter(supplyAbsolutePath, true);
                 BufferedWriter bw = new BufferedWriter(fw)) {
                String supplyData = String.format("%s,%s,%s,%.2f",
                    itemSupply.getItemCode(),
                    itemSupply.getSupplierCode(),
                    itemSupply.getItemName(),
                    itemSupply.getUnitPrice());
                bw.write(supplyData);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error adding item supply: " + e.getMessage());
        }
    }

    public boolean updateItemSupply(ItemSupply updatedItemSupply) {
        try {
            // Validate supplierCode
            if (!validateSupplierCode(updatedItemSupply.getSupplierCode())) {
                System.out.println("Invalid supplier code: " + updatedItemSupply.getSupplierCode());
                return false;
            }

            // Update itemSupply.txt
            String supplyFilePath = fileManager.getItemSupplyFilePath();
            String supplyAbsolutePath = new File("").getAbsolutePath() + File.separator + "src" + File.separator + supplyFilePath.replace("/", File.separator);
            List<String> supplyLines = Files.readAllLines(Paths.get(supplyAbsolutePath));
            List<String> updatedSupplyLines = new ArrayList<>();
            boolean supplyFound = false;

            for (String line : supplyLines) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].equals(updatedItemSupply.getItemCode())) {
                    supplyFound = true;
                    String updatedLine = String.format("%s,%s,%s,%.2f",
                            updatedItemSupply.getItemCode(),
                            updatedItemSupply.getSupplierCode(),
                            updatedItemSupply.getItemName(),
                            updatedItemSupply.getUnitPrice());
                    updatedSupplyLines.add(updatedLine);
                } else {
                    updatedSupplyLines.add(line);
                }
            }

            if (!supplyFound) {
                System.out.println("Item supply with code " + updatedItemSupply.getItemCode() + " not found in itemSupply.txt.");
                return false;
            }

            // Rewrite itemSupply.txt
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(supplyAbsolutePath))) {
                for (String line : updatedSupplyLines) {
                    bw.write(line);
                    bw.newLine();
                }
            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error updating item supply: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteItemSupply(String itemCode) {
        try {
            // Delete from itemSupply.txt
            String supplyFilePath = fileManager.getItemSupplyFilePath();
            String supplyAbsolutePath = new File("").getAbsolutePath() + File.separator + "src" + File.separator + supplyFilePath.replace("/", File.separator);
            List<String> supplyLines = Files.readAllLines(Paths.get(supplyAbsolutePath));
            List<String> updatedSupplyLines = new ArrayList<>();
            boolean supplyFound = false;

            for (String line : supplyLines) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].equals(itemCode)) {
                    supplyFound = true;
                    continue;
                }
                updatedSupplyLines.add(line);
            }

            if (!supplyFound) {
                System.out.println("Item supply with code " + itemCode + " not found in itemSupply.txt.");
                return false;
            }

            // Rewrite itemSupply.txt
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(supplyAbsolutePath))) {
                for (String line : updatedSupplyLines) {
                    bw.write(line);
                    bw.newLine();
                }
            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error deleting item supply: " + e.getMessage());
            return false;
        }
    }
    

    // Supplier Management
    public void addSupplier(Supplier supplier) {
        try {
            String filePath = fileManager.getSupplierFilePath();
            String absolutePath = new File("").getAbsolutePath() + File.separator + "src" + File.separator + filePath.replace("/", File.separator);

            // Append the new supplier data
            try (FileWriter fw = new FileWriter(absolutePath, true);
                 BufferedWriter bw = new BufferedWriter(fw)) {
                String supplierData = String.format("%s,%s,%d,%s,%s",
                    supplier.getSupplierCode(),
                    supplier.getSupplierName(),
                    supplier.getContactNumber(),
                    supplier.getAddress(),
                    supplier.getBankAccount());
                bw.write(supplierData);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error adding supplier: " + e.getMessage());
        }
    }

    public boolean updateSupplier(Supplier updatedSupplier) {
        try {
            String filePath = fileManager.getSupplierFilePath();
            String absolutePath = new File("").getAbsolutePath() + File.separator + "src" + File.separator + filePath.replace("/", File.separator);

            // Read all lines from the file
            List<String> lines = Files.readAllLines(Paths.get(absolutePath));
            List<String> updatedLines = new ArrayList<>();

            // Update the supplier data with the matching supplierCode
            boolean found = false;
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].equals(updatedSupplier.getSupplierCode())) {
                    found = true;
                    // Format the updated supplier data
                    String updatedLine = String.format("%s,%s,%d,%s,%s",
                            updatedSupplier.getSupplierCode(),
                            updatedSupplier.getSupplierName(),
                            updatedSupplier.getContactNumber(),
                            updatedSupplier.getAddress(),
                            updatedSupplier.getBankAccount());
                    updatedLines.add(updatedLine);
                } else {
                    updatedLines.add(line);
                }
            }

            if (!found) {
                System.out.println("Supplier with code " + updatedSupplier.getSupplierCode() + " not found.");
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
            System.out.println("Error updating supplier: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteSupplier(String supplierCode) {
        try {
            String filePath = fileManager.getSupplierFilePath();
            String absolutePath = new File("").getAbsolutePath() + File.separator + "src" + File.separator + filePath.replace("/", File.separator);

            // Read all lines from the file
            List<String> lines = Files.readAllLines(Paths.get(absolutePath));
            List<String> updatedLines = new ArrayList<>();

            // Filter out the line with the matching supplierCode
            boolean found = false;
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].equals(supplierCode)) {
                    found = true;
                    continue; // Skip this line (delete it)
                }
                updatedLines.add(line);
            }

            if (!found) {
                System.out.println("Supplier with code " + supplierCode + " not found.");
                return false;
            }

            // Write the updated lines back to the file
            try (FileWriter fw = new FileWriter(absolutePath, false);
                 BufferedWriter bw = new BufferedWriter(fw)) {
                for (String line : updatedLines) {
                    bw.write(line);
                    bw.newLine();
                }
            }

            return true;
        } catch (IOException e) {
            System.out.println("Error deleting supplier: " + e.getMessage());
            return false;
        }
    }

    public void addSalesData(String salesId, String itemCode, int quantitySold, double retailPrice, String date, double totalAmount) {
        try {
            String filePath = fileManager.getSalesDataFilePath();
            String absolutePath = new File("").getAbsolutePath() + File.separator + "src" + File.separator + filePath.replace("/", File.separator);

            // Append the new sales data
            try (FileWriter fw = new FileWriter(absolutePath, true);
                 BufferedWriter bw = new BufferedWriter(fw)) {
                String salesData = String.format("%s,%s,%d,%.2f,%s,%.2f",
                    salesId,
                    itemCode,
                    quantitySold,
                    retailPrice,
                    date,
                    totalAmount);
                bw.write(salesData);
                bw.newLine();
            }

            // Update stock level in itemFile.txt
            String itemFilePath = fileManager.getItemFilePath();
            String itemAbsolutePath = new File("").getAbsolutePath() + File.separator + "src" + File.separator + itemFilePath.replace("/", File.separator);
            List<String> itemLines = Files.readAllLines(Paths.get(itemAbsolutePath));
            List<String> updatedItemLines = new ArrayList<>();

            for (String line : itemLines) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length > 2 && parts[0].equals(itemCode)) {
                    int currentStock = Integer.parseInt(parts[2].trim());
                    int newStock = currentStock - quantitySold;
                    String updatedLine = String.format("%s,%s,%d,%s", parts[0], parts[1], newStock, String.join(",", Arrays.copyOfRange(parts, 3, parts.length)));
                    updatedItemLines.add(updatedLine);
                } else {
                    updatedItemLines.add(line);
                }
            }

            // Rewrite itemFile.txt with updated stock
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(itemAbsolutePath))) {
                for (String line : updatedItemLines) {
                    bw.write(line);
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error adding sales data: " + e.getMessage());
        }
    }

    public boolean updateSalesData(SalesData updatedSales) {
        try {
            String filePath = fileManager.getSalesDataFilePath();
            String absolutePath = new File("").getAbsolutePath() + File.separator + "src" + File.separator + filePath.replace("/", File.separator);

            // Read all lines from the file
            List<String> lines = Files.readAllLines(Paths.get(absolutePath));
            List<String> updatedLines = new ArrayList<>();
            int originalQuantity = 0;

            // Find the original quantity to calculate the difference
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].equals(updatedSales.getSalesId())) {
                    originalQuantity = Integer.parseInt(parts[2].trim());
                    break;
                }
            }

            // Update the sales data with the matching salesId
            boolean found = false;
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].equals(updatedSales.getSalesId())) {
                    found = true;
                    // Format the updated sales data
                    String updatedLine = String.format("%s,%s,%d,%.2f,%s,%.2f",
                            updatedSales.getSalesId(),
                            updatedSales.getItemCode(),
                            updatedSales.getQuantitySold(),
                            updatedSales.getRetailPrice(),
                            updatedSales.getDate(),
                            updatedSales.getTotalAmount());
                    updatedLines.add(updatedLine);
                } else {
                    updatedLines.add(line);
                }
            }

            if (!found) {
                System.out.println("Sales Data with ID " + updatedSales.getSalesId() + " not found.");
                return false;
            }

            // Rewrite the file with the updated lines
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(absolutePath))) {
                for (String line : updatedLines) {
                    bw.write(line);
                    bw.newLine();
                }
            }

            // Update stock level in itemFile.txt based on quantity change
            String itemFilePath = fileManager.getItemFilePath();
            String itemAbsolutePath = new File("").getAbsolutePath() + File.separator + "src" + File.separator + itemFilePath.replace("/", File.separator);
            List<String> itemLines = Files.readAllLines(Paths.get(itemAbsolutePath));
            List<String> updatedItemLines = new ArrayList<>();

            int quantityChange = originalQuantity - updatedSales.getQuantitySold();
            for (String line : itemLines) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length > 2 && parts[0].equals(updatedSales.getItemCode())) {
                    int currentStock = Integer.parseInt(parts[2].trim());
                    int newStock = currentStock + quantityChange; // Add the difference back to stock
                    String updatedLine = String.format("%s,%s,%d,%s", parts[0], parts[1], newStock, String.join(",", Arrays.copyOfRange(parts, 3, parts.length)));
                    updatedItemLines.add(updatedLine);
                } else {
                    updatedItemLines.add(line);
                }
            }

            // Rewrite itemFile.txt with updated stock
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(itemAbsolutePath))) {
                for (String line : updatedItemLines) {
                    bw.write(line);
                    bw.newLine();
                }
            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error updating Sales Data: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteSalesData(String salesId) {
        try {
            String filePath = fileManager.getSalesDataFilePath();
            String absolutePath = new File("").getAbsolutePath() + File.separator + "src" + File.separator + filePath.replace("/", File.separator);

            // Read all lines from the file
            List<String> lines = Files.readAllLines(Paths.get(absolutePath));
            List<String> updatedLines = new ArrayList<>();
            String itemCode = null;
            int originalQuantity = 0;

            // Find the itemCode and original quantity to revert stock
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].equals(salesId)) {
                    itemCode = parts[1];
                    originalQuantity = Integer.parseInt(parts[2].trim());
                    continue; // Skip this line (delete it)
                }
                updatedLines.add(line);
            }

            if (itemCode == null) {
                System.out.println("Sales ID " + salesId + " not found.");
                return false;
            }

            // Write the updated lines back to the file
            try (FileWriter fw = new FileWriter(absolutePath, false);
                 BufferedWriter bw = new BufferedWriter(fw)) {
                for (String line : updatedLines) {
                    bw.write(line);
                    bw.newLine();
                }
            }

            // Update stock level in itemFile.txt by adding back the original quantity
            String itemFilePath = fileManager.getItemFilePath();
            String itemAbsolutePath = new File("").getAbsolutePath() + File.separator + "src" + File.separator + itemFilePath.replace("/", File.separator);
            List<String> itemLines = Files.readAllLines(Paths.get(itemAbsolutePath));
            List<String> updatedItemLines = new ArrayList<>();

            for (String line : itemLines) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length > 2 && parts[0].equals(itemCode)) {
                    int currentStock = Integer.parseInt(parts[2].trim());
                    int newStock = currentStock + originalQuantity;
                    String updatedLine = String.format("%s,%s,%d,%s", parts[0], parts[1], newStock, String.join(",", Arrays.copyOfRange(parts, 3, parts.length)));
                    updatedItemLines.add(updatedLine);
                } else {
                    updatedItemLines.add(line);
                }
            }

            // Rewrite itemFile.txt with updated stock
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(itemAbsolutePath))) {
                for (String line : updatedItemLines) {
                    bw.write(line);
                    bw.newLine();
                }
            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error deleting sales data: " + e.getMessage());
            return false;
        }
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
    
    public boolean updatePurchaseRequisition(PurchaseRequisition updatedPR) {
        try {
            String filePath = fileManager.getPrFilePath();
            String absolutePath = new File("").getAbsolutePath() + File.separator + "src" + File.separator + filePath.replace("/", File.separator);

            // Read all lines from the file
            List<String> lines = Files.readAllLines(Paths.get(absolutePath));
            List<String> updatedLines = new ArrayList<>();

            // Update the purchase requisition with the matching prId
            boolean found = false;
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].equals(updatedPR.getPrId())) {
                    found = true;
                    // Format the updated purchase requisition data
                    String updatedLine = String.format("%s,%s,%s,%d,%s,%s,%s",
                            updatedPR.getPrId(),
                            updatedPR.getItemCode(),
                            updatedPR.getRequestedBy(),
                            updatedPR.getQuantity(),
                            updatedPR.getRequiredDate(),
                            updatedPR.getRequestedDate(),
                            updatedPR.getStatus());
                    updatedLines.add(updatedLine);
                } else {
                    updatedLines.add(line);
                }
            }

            if (!found) {
                System.out.println("Purchase Requisition with ID " + updatedPR.getPrId() + " not found.");
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
            System.out.println("Error updating Purchase Requisition: " + e.getMessage());
            return false;
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
    
    public List<String[]> viewItemSupplies() {
        List<ItemSupply> itemSupplyList = fileManager.readFile(
            fileManager.getItemSupplyFilePath(),
            line -> {
                String[] data = line.split(",");
                if (data.length < 4) {
                    System.out.println("Invalid item supply data: " + line);
                    return null;
                }
                try {
                    return new ItemSupply(
                        data[0], // itemCode
                        data[1], // supplierCode
                        data[2], // itemName
                        Double.parseDouble(data[3]) // unitPrice
                    );
                } catch (NumberFormatException e) {
                    System.out.println("Error parsing item supply data: " + line + " | " + e.getMessage());
                    return null;
                }
            }
        );

        // Convert the list of ItemSupply objects to List<String[]> for the table
        List<String[]> result = new ArrayList<>();
        for (ItemSupply itemSupply : itemSupplyList) {
            if (itemSupply == null) continue;
            String[] row = new String[]{
                itemSupply.getItemCode(),
                itemSupply.getSupplierCode(),
                itemSupply.getItemName(),
                String.valueOf(itemSupply.getUnitPrice())
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
                    Double.parseDouble(data[3]), // retailPrice
                    data[4],                     // date
                    Double.parseDouble(data[5])  // totalAmount
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