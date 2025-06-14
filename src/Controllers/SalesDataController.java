/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

import Entities.Item;
import Entities.SalesData;
import Interface.SalesDataServices;
import Utility.FileManager;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sheng Ting
 */
public class SalesDataController implements SalesDataServices{
    
    private final FileManager fileManager;

    public SalesDataController(FileManager fileManager) {
        this.fileManager = fileManager;
    }
    @Override
    public List<SalesData> getSalesData() {
        List<SalesData> salesList = fileManager.readFile(
            fileManager.getSalesDataFilePath(),
            line -> {
                String[] data = line.split(",");
                if (data.length < 6) {
                    System.err.println("Invalid sales data: " + line);
                    return null;
                }
                try {
                    SalesData sale = new SalesData(
                        data[0], data[1],
                        Integer.parseInt(data[2]),
                        Double.parseDouble(data[3]),
                        data[4],
                        Double.parseDouble(data[5])
                    );
                    System.out.println("Parsed Sales: " + sale.getSalesId());
                    return sale;
                } catch (Exception e) {
                    System.err.println("Error parsing sales data: " + line + " | Error: " + e.getMessage());
                    return null;
                }
            }
        );
        System.out.println("Total Sales read: " + salesList.size());
        return salesList;
    }
    
    @Override
    public List<String[]> viewSalesData() {
        List<SalesData> salesList = this.getSalesData();

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
    
    public String addSalesData(SalesData salesData) {
        try {
            // Step 1: Define file paths
            String salesFilePath = fileManager.getSalesDataFilePath();
            String itemFilePath = fileManager.getItemFilePath();

            // Step 2: Write to salesDataFile.txt
            boolean salesAdded = fileManager.writeToFile(
                salesData,
                salesFilePath,
                SalesData::getSalesId, // Key function for duplicate check
                sales -> String.format("%s,%s,%d,%.2f,%s,%.2f",
                    sales.getSalesId(),
                    sales.getItemCode(),
                    sales.getQuantitySold(),
                    sales.getRetailPrice(),
                    sales.getDate(),
                    sales.getTotalAmount())
            );

            if (!salesAdded) {
                System.out.println("Sales write failed for salesId: " + salesData.getSalesId());
                return "Error: Failed to add sales data to salesDataFile.txt. Sales ID may already exist or file access issue.";
            }
            System.out.println("Sales data written successfully for salesId: " + salesData.getSalesId());

            // Step 3: Read and update item stock
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
                .filter(item -> item != null && item.getItemCode().equals(salesData.getItemCode()))
                .findFirst()
                .orElse(null);

            if (targetItem != null) {
                int newStock = targetItem.getStockLevel() - salesData.getQuantitySold();
                if (newStock < 0) {
                    return "Error: Insufficient stock for item " + salesData.getItemCode() + ". Current stock: " + targetItem.getStockLevel();
                }
                targetItem.setStockLevel(newStock);

                System.out.println("Attempting to update item stock for: " + itemFilePath);
                boolean itemUpdated = fileManager.updateToFile(
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

                if (!itemUpdated) {
                    System.out.println("Item stock update failed for itemCode: " + salesData.getItemCode());
                    return "Error: Failed to update stock level for item " + salesData.getItemCode() + ". File update issue.";
                }
                System.out.println("Item stock updated successfully for itemCode: " + salesData.getItemCode());
            } else {
                return "Error: Item with code " + salesData.getItemCode() + " not found for stock update.";
            }

            return "Sales data " + salesData.getSalesId() + " added successfully and stock updated.";
        } catch (Exception e) {
            System.out.println("Exception during sales data addition: " + e.getMessage());
            return "Error adding sales data: " + e.getMessage() + ". Please check file paths or permissions.";
        }
    }

    @Override
    public boolean updateSalesData(SalesData updatedSales) {
        try {
            String filePath = fileManager.getSalesDataFilePath();

            // Read the original sales data before updating
            List<SalesData> salesList = this.getSalesData();

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

    @Override
    public boolean deleteSalesData(String salesId) {
        try {

            // First, retrieve the SalesData to be deleted to get quantitySold and itemCode
            List<SalesData> salesList = this.getSalesData();
            

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
                fileManager.getSalesDataFilePath(),
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
    
    
}
