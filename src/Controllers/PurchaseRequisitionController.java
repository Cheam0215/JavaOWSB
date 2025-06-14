/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

import Entities.PurchaseRequisition;
import Interface.PurchaseRequisitionServices;
import Utility.FileManager;
import Utility.Status;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sheng Ting
 */
public class PurchaseRequisitionController implements PurchaseRequisitionServices{
    
    private final FileManager fileManager;

    public PurchaseRequisitionController(FileManager fileManager) {
        this.fileManager = fileManager;
    }
    
    @Override
     public List<String[]> viewPurchaseRequisition() {
        List<PurchaseRequisition> prList = this.getAllPRs();

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
     
    public String addPurchaseRequisition(PurchaseRequisition pr) {
        try {
            String prFilePath = fileManager.getPrFilePath();
            boolean prAdded = fileManager.writeToFile(
                pr,
                prFilePath,
                PurchaseRequisition::getPrId, // Use prId as the unique key
                requisition -> String.format("%s,%s,%s,%d,%s,%s,%s",
                    requisition.getPrId(),
                    requisition.getItemCode(),
                    requisition.getRequestedBy(),
                    requisition.getQuantity(),
                    requisition.getRequiredDate(),
                    requisition.getRequestedDate(),
                    requisition.getStatus().toString())
            );

            if (!prAdded) {
                return "Error: Failed to add purchase requisition to prFile.txt. PR ID may already exist.";
            }

            return "Purchase requisition " + pr.getPrId() + " added successfully.";
        } catch (Exception e) {
            return "Error adding purchase requisition: " + e.getMessage();
        }
    }
    
    @Override
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
    
    @Override
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
    
    @Override
    public List<PurchaseRequisition> getAllPRs() {
        List<PurchaseRequisition> prList = fileManager.readFile(
            fileManager.getPrFilePath(),
            line -> {
                String[] data = line.split(",");
                if (data.length < 7) {
                    System.err.println("Invalid PR line: " + line);
                    return null;
                }
                try {
                    PurchaseRequisition pr = new PurchaseRequisition(
                        data[0], data[1], data[2],
                        Integer.parseInt(data[3]), data[4], data[5],
                        Status.valueOf(data[6])
                    );
                    System.out.println("Parsed PR: " + pr.getPrId());
                    return pr;
                } catch (Exception e) {
                    System.err.println("Error parsing PR line: " + line + " | Error: " + e.getMessage());
                    return null;
                }
            }
        );
        System.out.println("Total PRs read: " + prList.size());
        return prList;
    }
    
}
