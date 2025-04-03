/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utility;

import java.io.*;
import java.util.*;
import java.util.function.Function;

/**
 *
 * @author Sheng Ting
 */
public class FileManager {
    private String userFilePath = "users.txt";
    private String itemFilePath = "items.txt";
    private String supplierFilePath = "suppliers.txt";
    private String prFilePath = "purchase_requisitions.txt"; /** Purchase Requisition file path */
    private String poFilePath = "purchase_orders.txt"; /** Purchase Order file path */

    // Write an entity to a file, checking for duplicates
    public <T> boolean writeToFile(T entity, String filePath, 
                                   Function<T, String> idExtractor, 
                                   Function<T, String> toStringConverter) {
        List<T> existingData = readFile(filePath, line -> null); // Load existing data
        Set<String> existingIds = new HashSet<>();
        for (T existing : existingData) {
            if (existing != null) {
                existingIds.add(idExtractor.apply(existing));
            }
        }

        String entityId = idExtractor.apply(entity);
        if (existingIds.contains(entityId)) {
            System.out.println("Duplicate ID found: " + entityId + ". Use updateToFile to modify.");
            return false; // Duplicate found
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(toStringConverter.apply(entity));
            writer.newLine();
            return true; // Successfully written
        } catch (IOException e) {
            System.out.println("Error writing to file " + filePath + ": " + e.getMessage());
            return false;
        }
    }

    // Update an existing entity in a file
    public <T> boolean updateToFile(T entity, String filePath, 
                                    Function<T, String> idExtractor, 
                                    Function<T, String> toStringConverter, 
                                    Function<String, T> parser) {
        List<T> dataList = readFile(filePath, parser);
        String entityId = idExtractor.apply(entity);
        boolean found = false;

        // Update the list in memory
        for (int i = 0; i < dataList.size(); i++) {
            if (idExtractor.apply(dataList.get(i)).equals(entityId)) {
                dataList.set(i, entity); // Replace old entry with updated entity
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Entity with ID " + entityId + " not found for update.");
            return false;
        }

        // Rewrite the entire file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (T item : dataList) {
                writer.write(toStringConverter.apply(item));
                writer.newLine();
            }
            return true; // Successfully updated
        } catch (IOException e) {
            System.out.println("Error updating file " + filePath + ": " + e.getMessage());
            return false;
        }
    }

    // Read all entities from a file
    public <T> List<T> readFile(String filePath, Function<String, T> parser) {
        List<T> dataList = new ArrayList<>();
        Set<String> ids = new HashSet<>(); // For duplicate checking during load

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    T entity = parser.apply(line);
                    if (entity != null) {
                        dataList.add(entity);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filePath + ". Starting fresh.");
        } catch (IOException e) {
            System.out.println("Error reading file " + filePath + ": " + e.getMessage());
        }
        return dataList;
    }

    // Delete an entity from a file
    public <T> boolean deleteFromFile(String entityId, String filePath, 
                                      Function<T, String> idExtractor, 
                                      Function<String, T> parser) {
        List<T> dataList = readFile(filePath, parser);
        boolean found = false;

        // Remove the entity from the list
        for (int i = 0; i < dataList.size(); i++) {
            if (idExtractor.apply(dataList.get(i)).equals(entityId)) {
                dataList.remove(i);
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Entity with ID " + entityId + " not found for deletion.");
            return false;
        }

        // Rewrite the file without the deleted entity
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (T item : dataList) {
                writer.write(item.toString()); // Assumes toString() is sufficient
                writer.newLine();
            }
            return true; // Successfully deleted
        } catch (IOException e) {
            System.out.println("Error deleting from file " + filePath + ": " + e.getMessage());
            return false;
        }
    }

    // Getter methods for file paths (optional, for flexibility)
    public String getUserFilePath() { return userFilePath; }
    public String getItemFilePath() { return itemFilePath; }
    public String getSupplierFilePath() { return supplierFilePath; }
    public String getPrFilePath() { return prFilePath; }
    public String getPoFilePath() { return poFilePath; }
}