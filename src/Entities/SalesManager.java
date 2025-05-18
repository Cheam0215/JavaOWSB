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
    
    public String validateSupplierCode(String supplierCode) {
        try {
            List<Supplier> supplierList = fileManager.readFile(
                fileManager.getSupplierFilePath(),
                line -> {
                    String[] data = line.split(",");
                    try {
                        if (data.length >= 5) { // Ensure all fields are present
                            return new Supplier(
                                data[0],          // supplierCode
                                data[1],          // supplierName
                                Integer.parseInt(data[2]), // contactNumber
                                data[3],          // address
                                Integer.parseInt(data[4])          // bankAccount
                            );
                        }
                        return null; // Skip invalid lines
                    } catch (Exception e) {
                        System.out.println("Error parsing supplier data: " + line + " | " + e.getMessage());
                        return null;
                    }
                }
            );

            for (Supplier supplier : supplierList) {
                if (supplier != null && supplier.getSupplierCode().equals(supplierCode)) {
                    return "Success";
                }
            }
            return "Error: Invalid supplier code " + supplierCode + ".";
        } catch (Exception e) {
            return "Error validating supplier code: " + e.getMessage();
        }
    }

    public String addItem(Item item, String supplierCode, double unitPrice) {
        try {
            // Step 1: Validate supplierCode
            String validationResult = validateSupplierCode(supplierCode);
            if (!validationResult.equals("Success")) {
                return validationResult;
            }

            // Step 2: Save to itemFile.txt with duplicate check
            String itemFilePath = fileManager.getItemFilePath();
            boolean itemSaved = fileManager.writeToFile(
                item,
                itemFilePath,
                Item::getItemCode,
                i -> String.format("%s,%s,%d,%.1f",
                    i.getItemCode(),
                    i.getItemName(),
                    i.getStockLevel(),
                    i.getRetailPrice()).replaceAll("\\.0$", ".0") // Ensure 50.0, not 50
            );

            if (!itemSaved) {
                return "Error: Failed to save item to itemFile.txt. Item may already exist.";
            }

            // Step 3: Save to itemSupply.txt with duplicate check
            String supplyFilePath = fileManager.getItemSupplyFilePath();
            ItemSupply itemSupply = new ItemSupply(item.getItemCode(), supplierCode, item.getItemName(), unitPrice);
            boolean supplySaved = fileManager.writeToFile(
                itemSupply,
                supplyFilePath,
                supply -> supply.getItemCode() + "," + supply.getSupplierCode(),
                supply -> String.format("%s,%s,%s,%.1f",
                    supply.getItemCode(),
                    supply.getSupplierCode(),
                    supply.getItemName(),
                    supply.getUnitPrice()).replaceAll("\\.0$", ".0")
            );

            if (!supplySaved) {
                return "Error: Failed to save item supply to itemSupply.txt. Item-supplier pair may already exist.";
            }

            return "Item " + item.getItemCode() + " added successfully.";
        } catch (Exception e) {
            return "Error adding item: " + e.getMessage();
        }
    }

    public String updateItem(Item updatedItem, String supplierCode, double unitPrice) {
        try {
            // Step 1: Validate supplierCode
            String validationResult = validateSupplierCode(supplierCode);
            if (!validationResult.equals("Success")) {
                return validationResult;
            }

            // Step 2: Update itemFile.txt
            String itemFilePath = fileManager.getItemFilePath();
            boolean itemUpdated = fileManager.updateToFile(
                updatedItem,
                itemFilePath,
                Item::getItemCode,
                item -> String.format("%s,%s,%d,%.1f",
                    item.getItemCode(),
                    item.getItemName(),
                    item.getStockLevel(),
                    item.getRetailPrice()).replaceAll("\\.0$", ".0"),
                line -> {
                    String[] data = line.split(",");
                    try {
                        if (data.length >= 4) {
                            return new Item(data[0], data[1], Integer.parseInt(data[2]), Double.parseDouble(data[3]));
                        }
                        return null;
                    } catch (Exception e) {
                        System.out.println("Error parsing item data: " + line + " | " + e.getMessage());
                        return null;
                    }
                }
            );

            if (!itemUpdated) {
                return "Error: Item with code " + updatedItem.getItemCode() + " not found in itemFile.txt.";
            }

            // Step 3: Check if itemCode,supplierCode pair exists in itemSupply.txt
            String supplyFilePath = fileManager.getItemSupplyFilePath();
            ItemSupply updatedSupply = new ItemSupply(updatedItem.getItemCode(), supplierCode, updatedItem.getItemName(), unitPrice);
            boolean supplyExists = false;

            List<ItemSupply> supplyList = fileManager.readFile(
                supplyFilePath,
                line -> {
                    String[] data = line.split(",");
                    try {
                        if (data.length >= 4) {
                            return new ItemSupply(data[0], data[1], data[2], Double.parseDouble(data[3]));
                        }
                        return null;
                    } catch (Exception e) {
                        System.out.println("Error parsing item supply data: " + line + " | " + e.getMessage());
                        return null;
                    }
                }
            );

            for (ItemSupply supply : supplyList) {
                if (supply != null && supply.getItemCode().equals(updatedItem.getItemCode()) && supply.getSupplierCode().equals(supplierCode)) {
                    supplyExists = true;
                    break;
                }
            }

            // Step 4: Update or add to itemSupply.txt
            if (supplyExists) {
                boolean supplyUpdated = fileManager.updateToFile(
                    updatedSupply,
                    supplyFilePath,
                    supply -> supply.getItemCode() + "," + supply.getSupplierCode(),
                    supply -> String.format("%s,%s,%s,%.1f",
                        supply.getItemCode(),
                        supply.getSupplierCode(),
                        supply.getItemName(),
                        supply.getUnitPrice()).replaceAll("\\.0$", ".0"),
                    line -> {
                        String[] data = line.split(",");
                        try {
                            if (data.length >= 4) {
                                return new ItemSupply(data[0], data[1], data[2], Double.parseDouble(data[3]));
                            }
                            return null;
                        } catch (Exception e) {
                            System.out.println("Error parsing item supply data: " + line + " | " + e.getMessage());
                            return null;
                        }
                    }
                );

                if (!supplyUpdated) {
                    return "Error: Failed to update item supply in itemSupply.txt.";
                }
            } else {
                boolean supplyAdded = fileManager.writeToFile(
                    updatedSupply,
                    supplyFilePath,
                    supply -> supply.getItemCode() + "," + supply.getSupplierCode(),
                    supply -> String.format("%s,%s,%s,%.1f",
                        supply.getItemCode(),
                        supply.getSupplierCode(),
                        supply.getItemName(),
                        supply.getUnitPrice()).replaceAll("\\.0$", ".0")
                );

                if (!supplyAdded) {
                    return "Error: Failed to add new item supply to itemSupply.txt. Item-supplier pair may already exist.";
                }
            }

            return "Item " + updatedItem.getItemCode() + " updated successfully.";
        } catch (Exception e) {
            return "Error updating item: " + e.getMessage();
        }
    }

    public String deleteItem(String itemCode) {
        try {
            // Step 1: Delete from itemFile.txt
            String itemFilePath = fileManager.getItemFilePath();
            boolean itemDeleted = fileManager.deleteFromFile(
                itemCode,
                itemFilePath,
                Item::getItemCode,
                line -> {
                    String[] data = line.split(",");
                    try {
                        if (data.length >= 4) {
                            return new Item(data[0], data[1], Integer.parseInt(data[2]), Double.parseDouble(data[3]));
                        }
                        return null;
                    } catch (Exception e) {
                        System.out.println("Error parsing item data: " + line + " | " + e.getMessage());
                        return null;
                    }
                }
            );

            if (!itemDeleted) {
                return "Error: Item with code " + itemCode + " not found in itemFile.txt.";
            }

            // Step 2: Delete from itemSupply.txt (remove all entries with matching itemCode)
            String supplyFilePath = fileManager.getItemSupplyFilePath();
            boolean supplyFound = false;

            while (true) {
                boolean deleted = fileManager.deleteFromFile(
                    itemCode,
                    supplyFilePath,
                    supply -> supply.getItemCode(),
                    line -> {
                        String[] data = line.split(",");
                        try {
                            if (data.length >= 4) {
                                return new ItemSupply(data[0], data[1], data[2], Double.parseDouble(data[3]));
                            }
                            return null;
                        } catch (Exception e) {
                            System.out.println("Error parsing item supply data: " + line + " | " + e.getMessage());
                            return null;
                        }
                    }
                );

                if (deleted) {
                    supplyFound = true;
                } else {
                    break;
                }
            }

            if (!supplyFound) {
                return "Error: Item with code " + itemCode + " not found in itemSupply.txt.";
            }

            return "Item " + itemCode + " deleted successfully.";
        } catch (Exception e) {
            return "Error deleting item: " + e.getMessage();
        }
    }
    
    public String addItemSupply(ItemSupply itemSupply) {
        try {
            // Step 1: Validate supplierCode
            String validationResult = validateSupplierCode(itemSupply.getSupplierCode());
            if (!validationResult.equals("Success")) {
                return validationResult;
            }

            // Step 2: Write to itemSupply.txt using custom format
            String supplyFilePath = fileManager.getItemSupplyFilePath();
            boolean supplyAdded = fileManager.writeToFile(
                itemSupply,
                supplyFilePath,
                supply -> supply.getItemCode() + "," + supply.getSupplierCode(),
                supply -> String.format("%s,%s,%s,%.1f",
                    supply.getItemCode(),
                    supply.getSupplierCode(),
                    supply.getItemName(),
                    supply.getUnitPrice()).replaceAll("\\.0$", ".0")
            );

            if (!supplyAdded) {
                return "Error: Failed to add item supply to itemSupply.txt. Item-supplier pair may already exist.";
            }

            return "Item supply for item " + itemSupply.getItemCode() + " and supplier " + itemSupply.getSupplierCode() + " added successfully.";
        } catch (Exception e) {
            return "Error adding item supply: " + e.getMessage();
        }
    }

    public String updateItemSupply(ItemSupply updatedItemSupply) {
        try {
            // Step 1: Validate supplierCode
            String validationResult = validateSupplierCode(updatedItemSupply.getSupplierCode());
            if (!validationResult.equals("Success")) {
                return validationResult;
            }

            // Step 2: Update itemSupply.txt using updateToFile
            String supplyFilePath = fileManager.getItemSupplyFilePath();
            boolean supplyUpdated = fileManager.updateToFile(
                updatedItemSupply,
                supplyFilePath,
                supply -> supply.getItemCode() + "," + supply.getSupplierCode(),
                supply -> String.format("%s,%s,%s,%.1f",
                    supply.getItemCode(),
                    supply.getSupplierCode(),
                    supply.getItemName(),
                    supply.getUnitPrice()).replaceAll("\\.0$", ".0"),
                line -> {
                    String[] data = line.split(",");
                    try {
                        if (data.length >= 4) {
                            return new ItemSupply(data[0], data[1], data[2], Double.parseDouble(data[3]));
                        }
                        return null;
                    } catch (Exception e) {
                        System.out.println("Error parsing item supply data: " + line + " | " + e.getMessage());
                        return null;
                    }
                }
            );

            if (!supplyUpdated) {
                return "Error: Item supply with code " + updatedItemSupply.getItemCode() + " and supplier " + updatedItemSupply.getSupplierCode() + " not found in itemSupply.txt.";
            }

            return "Item supply for item " + updatedItemSupply.getItemCode() + " and supplier " + updatedItemSupply.getSupplierCode() + " updated successfully.";
        } catch (Exception e) {
            return "Error updating item supply: " + e.getMessage();
        }
    }

    public String deleteItemSupply(String itemCode) {
        try {
            // Step 1: Delete from itemSupply.txt (remove all entries with matching itemCode)
            String supplyFilePath = fileManager.getItemSupplyFilePath();
            boolean supplyFound = false;

            while (true) {
                boolean deleted = fileManager.deleteFromFile(
                    itemCode,
                    supplyFilePath,
                    supply -> supply.getItemCode(),
                    line -> {
                        String[] data = line.split(",");
                        try {
                            if (data.length >= 4) {
                                return new ItemSupply(data[0], data[1], data[2], Double.parseDouble(data[3]));
                            }
                            return null;
                        } catch (Exception e) {
                            System.out.println("Error parsing item supply data: " + line + " | " + e.getMessage());
                            return null;
                        }
                    }
                );

                if (deleted) {
                    supplyFound = true;
                } else {
                    break;
                }
            }

            if (!supplyFound) {
                return "Error: Item supply with code " + itemCode + " not found in itemSupply.txt.";
            }

            return "Item supply for item " + itemCode + " deleted successfully.";
        } catch (Exception e) {
            return "Error deleting item supply: " + e.getMessage();
        }
    }


    // Supplier Management
    public boolean addSupplier(Supplier supplier) {
        String filePath = fileManager.getSupplierFilePath();

        return fileManager.writeToFile(
            supplier,
            filePath,
            Supplier::getSupplierCode,
            s -> String.format("%s,%s,%d,%s,%s",
                s.getSupplierCode(),
                s.getSupplierName(),
                s.getContactNumber(),
                s.getAddress(),
                s.getBankAccount())
        );
    }

    public boolean updateSupplier(Supplier updatedSupplier) {
        String filePath = fileManager.getSupplierFilePath();

        return fileManager.updateToFile(
            updatedSupplier,
            filePath,
            Supplier::getSupplierCode,
            s -> String.format("%s,%s,%d,%s,%d",
                s.getSupplierCode(),
                s.getSupplierName(),
                s.getContactNumber(),
                s.getAddress(),
                s.getBankAccount()),
            line -> {
                String[] parts = line.split(",");
                if (parts.length != 5) return null;
                return new Supplier(parts[0], parts[1], Integer.parseInt(parts[2]), parts[3], Integer.parseInt(parts[4]));
            }
        );
    }

    public boolean deleteSupplier(String supplierCode) {
        String filePath = fileManager.getSupplierFilePath();

        return fileManager.deleteFromFile(
            supplierCode,
            filePath,
            Supplier::getSupplierCode,
            line -> {
                String[] parts = line.split(",");
                if (parts.length != 5) return null;
                try {
                    return new Supplier(
                        parts[0],                         // supplierCode
                        parts[1],                         // supplierName
                        Integer.parseInt(parts[2]),       // contactNumber
                        parts[3],                         // address
                        Integer.parseInt(parts[4])       // bankAccount
                    );
                } catch (NumberFormatException e) {
                    return null; // skip malformed line
                }
            }
        );
    }

    public void addSalesData(String salesId, String itemCode, int quantitySold, double retailPrice, String date, double totalAmount) {
        try {
            String filePath = fileManager.getSalesDataFilePath();
            SalesData newSales = new SalesData(salesId, itemCode, quantitySold, retailPrice, date, totalAmount);

            // Append new sales data using FileManager's writeToFile
            boolean success = fileManager.writeToFile(
                newSales,
                filePath,
                sales -> sales.getSalesId(), // Key function for duplicate check
                sales -> String.format("%s,%s,%d,%.2f,%s,%.2f",
                    sales.getSalesId(),
                    sales.getItemCode(),
                    sales.getQuantitySold(),
                    sales.getRetailPrice(),
                    sales.getDate(),
                    sales.getTotalAmount())
            );

            if (!success) {
                System.out.println("Failed to add sales data for Sales ID: " + salesId);
                return;
            }

            // Update stock level in itemFile.txt
            String itemFilePath = fileManager.getItemFilePath();
            List<Item> itemList = fileManager.readFile(
                itemFilePath,
                line -> {
                    String[] parts = line.split(",");
                    try {
                        if (parts.length >= 4) {
                            return new Item(parts[0], parts[1], Integer.parseInt(parts[2]), Double.parseDouble(parts[3]));
                        }
                        return null;
                    } catch (Exception e) {
                        System.out.println("Error parsing item data: " + line + " | " + e.getMessage());
                        return null;
                    }
                }
            );

            Item targetItem = itemList.stream()
                .filter(item -> item != null && item.getItemCode().equals(itemCode))
                .findFirst()
                .orElse(null);

            if (targetItem != null) {
                int newStock = targetItem.getStockLevel() - quantitySold;
                targetItem.setStockLevel(newStock);

                fileManager.updateToFile(
                    targetItem,
                    itemFilePath,
                    item -> item.getItemCode(), // Key function
                    item -> String.format("%s,%s,%d,%.2f",
                        item.getItemCode(),
                        item.getItemName(),
                        item.getStockLevel(),
                        item.getRetailPrice()),
                    line -> {
                        String[] parts = line.split(",");
                        try {
                            if (parts.length >= 4) {
                                return new Item(parts[0], parts[1], Integer.parseInt(parts[2]), Double.parseDouble(parts[3]));
                            }
                            return null;
                        } catch (Exception e) {
                            System.out.println("Error parsing item data: " + line + " | " + e.getMessage());
                            return null;
                        }
                    }
                );
            } else {
                System.out.println("Item with code " + itemCode + " not found for stock update.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error adding sales data: " + e.getMessage());
        }
    }

    public boolean updateSalesData(SalesData updatedSales) {
        try {
            String filePath = fileManager.getSalesDataFilePath();

            // Read the original sales data before updating
            List<SalesData> salesList = fileManager.readFile(
                filePath,
                line -> {
                    String[] parts = line.split(",");
                    try {
                        if (parts.length >= 6) {
                            return new SalesData(
                                parts[0], parts[1], Integer.parseInt(parts[2]),
                                Double.parseDouble(parts[3]), parts[4], Double.parseDouble(parts[5])
                            );
                        }
                        return null;
                    } catch (Exception e) {
                        System.out.println("Error parsing sales data: " + line + " | " + e.getMessage());
                        return null;
                    }
                }
            );

            SalesData originalSales = salesList.stream()
                .filter(sales -> sales != null && sales.getSalesId().equals(updatedSales.getSalesId()))
                .findFirst()
                .orElse(null);

            if (originalSales == null) {
                System.out.println("Sales Data with ID " + updatedSales.getSalesId() + " not found.");
                return false;
            }

            // Update sales data using FileManager
            boolean updated = fileManager.updateToFile(
                updatedSales,
                filePath,
                sales -> sales.getSalesId(), // Key function
                sales -> String.format("%s,%s,%d,%.2f,%s,%.2f",
                    sales.getSalesId(),
                    sales.getItemCode(),
                    sales.getQuantitySold(),
                    sales.getRetailPrice(),
                    sales.getDate(),
                    sales.getTotalAmount()),
                line -> {
                    String[] parts = line.split(",");
                    try {
                        if (parts.length >= 6) {
                            return new SalesData(
                                parts[0], parts[1], Integer.parseInt(parts[2]),
                                Double.parseDouble(parts[3]), parts[4], Double.parseDouble(parts[5])
                            );
                        }
                        return null;
                    } catch (Exception e) {
                        System.out.println("Error parsing sales data: " + line + " | " + e.getMessage());
                        return null;
                    }
                }
            );

            if (!updated) {
                System.out.println("Sales Data with ID " + updatedSales.getSalesId() + " not found.");
                return false;
            }

            // Update stock level in itemFile.txt based on quantity change
            int quantityChange = originalSales.getQuantitySold() - updatedSales.getQuantitySold();
            String itemFilePath = fileManager.getItemFilePath();
            List<Item> itemList = fileManager.readFile(
                itemFilePath,
                line -> {
                    String[] parts = line.split(",");
                    try {
                        if (parts.length >= 4) {
                            return new Item(parts[0], parts[1], Integer.parseInt(parts[2]), Double.parseDouble(parts[3]));
                        }
                        return null;
                    } catch (Exception e) {
                        System.out.println("Error parsing item data: " + line + " | " + e.getMessage());
                        return null;
                    }
                }
            );

            Item targetItem = itemList.stream()
                .filter(item -> item != null && item.getItemCode().equals(updatedSales.getItemCode()))
                .findFirst()
                .orElse(null);

            if (targetItem != null) {
                int newStock = targetItem.getStockLevel() + quantityChange;
                targetItem.setStockLevel(newStock);

                fileManager.updateToFile(
                    targetItem,
                    itemFilePath,
                    item -> item.getItemCode(), // Key function
                    item -> String.format("%s,%s,%d,%.2f",
                        item.getItemCode(),
                        item.getItemName(),
                        item.getStockLevel(),
                        item.getRetailPrice()),
                    line -> {
                        String[] parts = line.split(",");
                        try {
                            if (parts.length >= 4) {
                                return new Item(parts[0], parts[1], Integer.parseInt(parts[2]), Double.parseDouble(parts[3]));
                            }
                            return null;
                        } catch (Exception e) {
                            System.out.println("Error parsing item data: " + line + " | " + e.getMessage());
                            return null;
                        }
                    }
                );
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error updating Sales Data: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteSalesData(String salesId) {
        try {
            String filePath = fileManager.getSalesDataFilePath();

            // First, retrieve the SalesData to be deleted to get quantitySold and itemCode
            List<SalesData> salesList = fileManager.readFile(
                filePath,
                line -> {
                    String[] parts = line.split(",");
                    try {
                        if (parts.length >= 6) {
                            return new SalesData(
                                parts[0], parts[1], Integer.parseInt(parts[2]),
                                Double.parseDouble(parts[3]), parts[4], Double.parseDouble(parts[5])
                            );
                        }
                        return null;
                    } catch (Exception e) {
                        System.out.println("Error parsing sales data: " + line + " | " + e.getMessage());
                        return null;
                    }
                }
            );

            SalesData deletedSales = salesList.stream()
                .filter(sales -> sales != null && sales.getSalesId().equals(salesId))
                .findFirst()
                .orElse(null);

            if (deletedSales == null) {
                System.out.println("Sales ID " + salesId + " not found.");
                return false;
            }

            // Delete sales data using FileManager (remove the extra formatter argument)
            boolean deleted = fileManager.deleteFromFile(
                salesId,
                filePath,
                sales -> sales.getSalesId(), // Key function
                line -> {
                    String[] parts = line.split(",");
                    try {
                        if (parts.length >= 6) {
                            return new SalesData(
                                parts[0], parts[1], Integer.parseInt(parts[2]),
                                Double.parseDouble(parts[3]), parts[4], Double.parseDouble(parts[5])
                            );
                        }
                        return null;
                    } catch (Exception e) {
                        System.out.println("Error parsing sales data: " + line + " | " + e.getMessage());
                        return null;
                    }
                }
            );

            if (!deleted) {
                System.out.println("Sales ID " + salesId + " not found.");
                return false;
            }

            // Update stock level in itemFile.txt by adding back the original quantity
            String itemFilePath = fileManager.getItemFilePath();
            List<Item> itemList = fileManager.readFile(
                itemFilePath,
                line -> {
                    String[] parts = line.split(",");
                    try {
                        if (parts.length >= 4) {
                            return new Item(parts[0], parts[1], Integer.parseInt(parts[2]), Double.parseDouble(parts[3]));
                        }
                        return null;
                    } catch (Exception e) {
                        System.out.println("Error parsing item data: " + line + " | " + e.getMessage());
                        return null;
                    }
                }
            );

            Item targetItem = itemList.stream()
                .filter(item -> item != null && item.getItemCode().equals(deletedSales.getItemCode()))
                .findFirst()
                .orElse(null);

            if (targetItem != null) {
                int newStock = targetItem.getStockLevel() + deletedSales.getQuantitySold();
                targetItem.setStockLevel(newStock);

                fileManager.updateToFile(
                    targetItem,
                    itemFilePath,
                    item -> item.getItemCode(), // Key function
                    item -> String.format("%s,%s,%d,%.2f",
                        item.getItemCode(),
                        item.getItemName(),
                        item.getStockLevel(),
                        item.getRetailPrice()),
                    line -> {
                        String[] parts = line.split(",");
                        try {
                            if (parts.length >= 4) {
                                return new Item(parts[0], parts[1], Integer.parseInt(parts[2]), Double.parseDouble(parts[3]));
                            }
                            return null;
                        } catch (Exception e) {
                            System.out.println("Error parsing item data: " + line + " | " + e.getMessage());
                            return null;
                        }
                    }
                );
            }

            return true;
        } catch (Exception e) {
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
                Supplier supplier = new Supplier(
                    data[0],               // supplierCode
                    data[1],               // supplierName
                    Integer.parseInt(data[2]),  // contactNumber
                    data[3],               // address
                    Integer.parseInt(data[4])   // bankAccount
                );
                return supplier;
            }
        );

        List<String[]> result = new ArrayList<>();
        for (Supplier supplier : supplierList) {
            String[] row = new String[]{
                supplier.getSupplierCode(),
                supplier.getSupplierName(),
                String.valueOf(supplier.getContactNumber()),
                supplier.getAddress(),
                String.valueOf(supplier.getBankAccount())
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