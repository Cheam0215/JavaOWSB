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
    // File paths relative to the database package
    private static final String userFilePath = "/database/userFile.txt";
    private final String itemFilePath       = "database/itemFile.txt";
    private final String supplierFilePath   = "database/supplierFile.txt";
    private final String prFilePath         = "database/purchaseRequisitionFile.txt";
    private final String poFilePath         = "database/purchaseOrderFile.txt";
    private final String salesDataFilePath  = "database/salesDataFile.txt";
    private final String itemSupplyFilePath = "database/itemSupply.txt";
    
    
   
    // Login function
   public static UserRoles login(String username, String password) {
    try (BufferedReader reader = new BufferedReader(
            new InputStreamReader(FileManager.class.getResourceAsStream(userFilePath)))) {
        if (reader == null) {
            System.err.println("Resource not found: " + userFilePath);
            return null;
        }

        String line;
        while ((line = reader.readLine()) != null) {
            String[] data = line.split(",");
            if (data.length >= 4) {
                String fileUserID = String.valueOf(data[0]);
                String fileUsername = data[1].trim();
                String filePassword = data[2].trim();
                String fileRole = data[3].trim();

                String storedPassword = filePassword;
                try {
                    byte[] decodedBytes = Base64.getDecoder().decode(filePassword);
                    try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(decodedBytes))) {
                        storedPassword = (String) in.readObject();
                        System.out.println("storedPassword " + storedPassword);
                    }
                } catch (IOException | ClassNotFoundException | IllegalArgumentException e) {
                    System.out.println("Error");
                }

                // Compare username and password
                if (fileUsername.equals(username) && storedPassword.equals(password)) {
                    try {
                        Session loginSession = new Session();
                        loginSession.setUserID(fileUserID);
                        return UserRoles.valueOf(fileRole);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Invalid role in user file: " + fileRole);
                        return null;
                    }
                }
            }
        }
    } catch (IOException e) {
        System.err.println("Error reading user file: " + e.getMessage());
        return null;
    }

    return null; // No matching user found
}


    // Write an entity to a file, checking for duplicates
    public <T> boolean writeToFile(T entity, String filePath, 
                                   Function<T, String> idExtractor, 
                                   Function<T, String> toStringConverter) {
        // Read existing data to check for duplicates
        List<T> existingData = readFile(filePath, line -> null);
        Set<String> existingIds = new HashSet<>();
        for (T existing : existingData) {
            if (existing != null) {
                existingIds.add(idExtractor.apply(existing));
            }
        }

        String entityId = idExtractor.apply(entity);
        if (existingIds.contains(entityId)) {
            System.out.println("Duplicate ID found: " + entityId + ". Use updateToFile to modify.");
            return false;
        }

        // Write to file (append mode)
        try {
            File file = new File(getResourcePath(filePath));
            file.getParentFile().mkdirs(); // Create parent directories if they don't exist
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                writer.write(toStringConverter.apply(entity));
                writer.newLine();
                return true;
            }
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

        for (int i = 0; i < dataList.size(); i++) {
            if (idExtractor.apply(dataList.get(i)).equals(entityId)) {
                dataList.set(i, entity);
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Entity with ID " + entityId + " not found for update.");
            return false;
        }

        // Rewrite the entire file
        try {
            File file = new File(getResourcePath(filePath));
            file.getParentFile().mkdirs();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (T item : dataList) {
                    writer.write(toStringConverter.apply(item));
                    writer.newLine();
                }
                return true;
            }
        } catch (IOException e) {
            System.out.println("Error updating file " + filePath + ": " + e.getMessage());
            return false;
        }
    }

    // Read all entities from a file
    public <T> List<T> readFile(String filePath, Function<String, T> parser) {
        List<T> dataList = new ArrayList<>();
        try {
            File file = new File(getResourcePath(filePath));
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
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
            }
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

        // Rewrite the file
        try {
            File file = new File(getResourcePath(filePath));
            file.getParentFile().mkdirs();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (T item : dataList) {
                    writer.write(item.toString());
                    writer.newLine();
                }
                return true;
            }
        } catch (IOException e) {
            System.out.println("Error deleting from file " + filePath + ": " + e.getMessage());
            return false;
        }
    }

    // Helper method to get the absolute path for writing files
    private String getResourcePath(String filePath) {
        // Convert classpath resource path to file system path
        String basePath = new File("").getAbsolutePath();
        return basePath + File.separator + "src" + File.separator + filePath.replace("/", File.separator);
    }

    // Getter methods for file paths
    public String getUserFilePath() { return userFilePath; }
    public String getItemFilePath() { return itemFilePath; }
    public String getSupplierFilePath() { return supplierFilePath; }
    public String getPrFilePath() { return prFilePath; }
    public String getPoFilePath() { return poFilePath; }
    public String getSalesDataFilePath() { return salesDataFilePath; }
    public String getItemSupplyFilePath() {return itemSupplyFilePath;}  
}


