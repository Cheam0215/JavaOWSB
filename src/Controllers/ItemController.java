/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

import Entities.Item;
import Entities.ItemSupply;
import Entities.Supplier;
import Interface.ItemServices;
import Utility.FileManager;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sheng Ting
 */
public class ItemController implements ItemServices{
    
    private final FileManager fileManager;

    public ItemController(FileManager fileManager) {
        this.fileManager = fileManager;
    }
    
    @Override
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
    
    @Override
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

    @Override
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

    @Override
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
}
