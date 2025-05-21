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
    
    @Override
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

    @Override
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

    @Override
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
}
