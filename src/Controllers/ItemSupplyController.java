/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

import Entities.ItemSupply;
import Entities.Supplier;
import Interface.ItemSupplyServices;
import Utility.FileManager;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sheng Ting
 */
public class ItemSupplyController implements ItemSupplyServices{
    
    private final FileManager fileManager;

    public ItemSupplyController(FileManager fileManager) {
        this.fileManager = fileManager;
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
    
    @Override
    public List<ItemSupply> getAllItemSupply(){
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
         
         return itemSupplyList;
    }
    
    @Override
    public List<String[]> viewItemSupplies() {
        List<ItemSupply> itemSupplyList = this.getAllItemSupply();

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
    
    @Override
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
    
    @Override
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

    
    public String deleteItemSupply(String itemCode, String supplierCode) {
        try {
            // Step 1: Delete from itemSupply.txt (remove entry with matching itemCode and supplierCode)
            String supplyFilePath = fileManager.getItemSupplyFilePath();
            String uniqueKey = itemCode + "," + supplierCode; // Combine itemCode and supplierCode as the key

            boolean deleted = fileManager.deleteFromFile(
                uniqueKey,
                supplyFilePath,
                supply -> supply.getItemCode() + "," + supply.getSupplierCode(), // Key function using both fields
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

            if (!deleted) {
                return "Error: Item supply with code " + itemCode + " and supplier " + supplierCode + " not found in itemSupply.txt.";
            }

            return "Item supply for item " + itemCode + " and supplier " + supplierCode + " deleted successfully.";
        } catch (Exception e) {
            return "Error deleting item supply: " + e.getMessage();
        }
    }
}
