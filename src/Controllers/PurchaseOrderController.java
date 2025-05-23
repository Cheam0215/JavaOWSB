/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

import Entities.ItemSupply;
import Entities.PurchaseOrder;
import Entities.PurchaseRequisition;
import Entities.User;
import Interface.FinanceManagerPOServices;
import Interface.InventoryManagerPOServices;
import Interface.PurchaseOrderServices;
import Utility.FileManager;
import Utility.Remark;
import Utility.Status;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 * @author Sheng Ting
 */
public class PurchaseOrderController implements PurchaseOrderServices, InventoryManagerPOServices, FinanceManagerPOServices{
    
    private final FileManager fileManager;

    public PurchaseOrderController(FileManager fileManager) {
        this.fileManager = fileManager;
    }
    
    @Override
    public List<String[]> viewPurchaseOrder() {
        List<PurchaseOrder> poList = this.getAllPOs();

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
    
    @Override
    public String generatePurchaseOrder(String prId, String supplierCode, User performingUser) {
        try {
            
            // Step 1: Find the PurchaseRequisition by prId
            List<PurchaseRequisition> prList = fileManager.readFile(
                fileManager.getPrFilePath(),
                line -> {
                    String[] data = line.split(",");
                    try {
                        return new PurchaseRequisition(
                            data[0], data[1], data[2], Integer.parseInt(data[3]),
                            data[4], data[5], Status.valueOf(data[6])
                        );
                    } catch (Exception e) {
                        System.out.println("Error parsing PR data: " + line + " | " + e.getMessage());
                        return null;
                    }
                }
            );

            PurchaseRequisition targetPr = null;
            for (PurchaseRequisition pr : prList) {
                if (pr != null && pr.getPrId().equals(prId)) {
                    targetPr = pr;
                    break;
                }
            }

            if (targetPr == null) {
                return "Error: Purchase Requisition with ID " + prId + " not found.";
            }

            if (targetPr.getStatus() == Status.APPROVED) {
                return "Error: Purchase Requisition already approved.";
            }

            // Step 2: Update PurchaseRequisition status to APPROVED
            targetPr.setStatus(Status.APPROVED);
            boolean prUpdated = fileManager.updateToFile(
                targetPr,
                fileManager.getPrFilePath(),
                pr -> pr.getPrId(),
                pr -> String.join(",", pr.getPrId(), pr.getItemCode(), pr.getRequestedBy(),
                    String.valueOf(pr.getQuantity()), pr.getRequiredDate(), pr.getRequestedDate(),
                    pr.getStatus().toString()),
                line -> {
                    String[] data = line.split(",");
                    try {
                        return new PurchaseRequisition(
                            data[0], data[1], data[2], Integer.parseInt(data[3]),
                            data[4], data[5], Status.valueOf(data[6])
                        );
                    } catch (Exception e) {
                        System.out.println("Error parsing PR data: " + line + " | " + e.getMessage());
                        return null;
                    }
                }
            );

            if (!prUpdated) {
                return "Error: Failed to update Purchase Requisition status.";
            }

            // Step 3: Generate next poId by scanning existing PurchaseOrders
            List<PurchaseOrder> poList = this.getAllPOs();

            int maxPoNumber = 0;
            for (PurchaseOrder po : poList) {
                if (po == null) continue;
                String poId = po.getPoId();
                if (poId.startsWith("PO")) {
                    try {
                        int number = Integer.parseInt(poId.substring(2));
                        maxPoNumber = Math.max(maxPoNumber, number);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid poId format: " + poId + " | Skipping...");
                    }
                }
            }

            // Generate next poId (e.g., PO003 for maxPoNumber = 2)
            String poId = String.format("PO%03d", maxPoNumber + 1);

            // Step 4: Create a new PurchaseOrder
            PurchaseOrder newPo = new PurchaseOrder(
                poId,                          // poId
                prId,                          // prId
                performingUser.getUserID(),    // raisedBy (from Session)
                targetPr.getItemCode(),        // itemCode
                targetPr.getQuantity(),        // quantity
                supplierCode,                  // supplierCode
                targetPr.getRequiredDate(),    // requiredDate
                targetPr.getRequestedDate(),   // requestedDate
                Status.PENDING,                // status
                0.0,                           // paymentAmount (default to 0.0)
                Remark.NULL                    // remark
            );

            // Step 5: Save the new PurchaseOrder
            boolean poSaved = fileManager.writeToFile(
                newPo,
                fileManager.getPoFilePath(),
                po -> po.getPoId(),
                po -> String.join(",", po.getPoId(), po.getPrId(), po.getRaisedBy(),
                    po.getItemCode(), String.valueOf(po.getQuantity()), po.getSupplierCode(),
                    po.getRequiredDate(), po.getRequestedDate(), po.getStatus().toString(),
                    String.valueOf(po.getPaymentAmount()), po.getRemark().toString())
            );

            if (!poSaved) {
                return "Error: Failed to save Purchase Order.";
            }

            return "Purchase Order " + poId + " created successfully.";
        } catch (Exception e) {
            return "Error generating Purchase Order: " + e.getMessage();
        }
    }
    
    @Override
    public boolean editPurchaseOrder(String poId, int newQuantity , double new_payment_amount) {
        try {
            // Validate poId
            if (poId == null || poId.trim().isEmpty()) {
                System.out.println("Error: Invalid Purchase Order ID.");
                return false;
            }

            // Validate quantity
            if (newQuantity <= 0) {
                System.out.println("Error: Quantity must be positive.");
                return false;
            }

            // Read all PurchaseOrders
            List<PurchaseOrder> poList = this.getAllPOs();

            // Find the target PurchaseOrder
            PurchaseOrder targetPo = null;
            for (PurchaseOrder po : poList) {
                if (po != null && po.getPoId().equals(poId)) {
                    targetPo = po;
                    break;
                }
            }

            if (targetPo == null) {
                System.out.println("Error: Purchase Order with ID " + poId + " not found.");
                return false;
            }

            // Check if status is PENDING
            if (targetPo.getStatus() != Status.PENDING) {
                System.out.println("Error: Purchase Order " + poId + " cannot be edited. Status is " + targetPo.getStatus() + ", must be PENDING.");
                return false;
            }

            // Update quantity
            targetPo.setQuantity(newQuantity);
            targetPo.setPaymentAmount(new_payment_amount);
            // Update the file
            boolean updated = fileManager.updateToFile(
                targetPo,
                fileManager.getPoFilePath(),
                po -> po.getPoId(),
                PurchaseOrder::toString,
                PurchaseOrder::fromDataString
            );

            if (!updated) {
                System.out.println("Error: Failed to update Purchase Order " + poId + " in file.");
                return false;
            }

            System.out.println("Purchase Order " + poId + " updated successfully with new quantity: " + newQuantity);
            return true;
        } catch (Exception e) {
            System.out.println("Error updating Purchase Order: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean deletePurchaseOrder(String poId) {
        try {
            // Validate poId
            if (poId == null || poId.trim().isEmpty()) {
                System.out.println("Error: Invalid Purchase Order ID.");
                return false;
            }

            // Read all PurchaseOrders
            List<PurchaseOrder> poList = this.getAllPOs();

            // Find the target PurchaseOrder
            PurchaseOrder targetPo = null;
            for (PurchaseOrder po : poList) {
                if (po != null && po.getPoId().equals(poId)) {
                    targetPo = po;
                    break;
                }
            }

            if (targetPo == null) {
                System.out.println("Error: Purchase Order with ID " + poId + " not found.");
                return false;
            }

            // Check if status is PENDING
            if (targetPo.getStatus() != Status.PENDING) {
                System.out.println("Error: Purchase Order " + poId + " cannot be deleted. Status is " + targetPo.getStatus() + ", must be PENDING.");
                return false;
            }

            // Update status and remark
            targetPo.setStatus(Status.DELETED);
            targetPo.setRemark(Remark.DELETED_BY_PURCHASE_MANAGER);

            // Update the file
            boolean updated = fileManager.updateToFile(
                targetPo,
                fileManager.getPoFilePath(),
                po -> po.getPoId(),
                PurchaseOrder::toString,
                PurchaseOrder::fromDataString
            );

            if (!updated) {
                System.out.println("Error: Failed to update Purchase Order " + poId + " in file.");
                return false;
            }

            System.out.println("Purchase Order " + poId + " deleted successfully.");
            return true;
        } catch (Exception e) {
            System.out.println("Error deleting Purchase Order: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public PurchaseOrder findApprovedPOByItemCode(String itemCode) {
        if (itemCode == null || itemCode.trim().isEmpty()) {
            // Or throw new IllegalArgumentException("Item code cannot be null or empty.");
            return null;
        }

        List<PurchaseOrder> allPOs = getAllPOs(); // Helper method to read all POs
        
        return allPOs.stream()
                .filter(po -> po.getItemCode().equals(itemCode) && po.getStatus() == Status.APPROVED)
                .findFirst() // Get the first one that matches (assuming one item per approved PO for simplicity here)
                .orElse(null);
    }

    @Override
    public boolean updatePurchaseOrderStatus(String poId, Status newStatus) {
        if (poId == null || poId.trim().isEmpty() || newStatus == null) {
            System.err.println("Error: PO ID or new status is null/empty in updatePurchaseOrderStatus.");
            return false;
        }

        List<PurchaseOrder> allPOs = getAllPOs();
        Optional<PurchaseOrder> poToUpdateOpt = allPOs.stream()
                .filter(po -> po.getPoId().equals(poId))
                .findFirst();

        if (poToUpdateOpt.isPresent()) {
            PurchaseOrder poToUpdate = poToUpdateOpt.get();
            poToUpdate.setStatus(newStatus); // Update the status in the object

            return fileManager.updateToFile(
                    poToUpdate,
                    fileManager.getPoFilePath(),
                    PurchaseOrder::getPoId,       // idExtractor
                    PurchaseOrder::toString,  // toStringConverter (e.g. po -> po.toCSV())
                    PurchaseOrder::fromDataString // parser (e.g. line -> PurchaseOrder.fromCSV(line))
            );
        } else {
            System.err.println("Purchase Order with ID '" + poId + "' not found for status update.");
            return false; // PO not found
        }
    }

    @Override
    public PurchaseOrder getPOById(String poId) {
        if (poId == null || poId.trim().isEmpty()) {
            return null;
        }
        List<PurchaseOrder> allPOs = getAllPOs();
        return allPOs.stream()
                .filter(po -> po.getPoId().equals(poId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<PurchaseOrder> getAllPOs() {
        // Ensure PurchaseOrder.fromDataString method is robust (handles nulls, exceptions)
        return fileManager.readFile(fileManager.getPoFilePath(), PurchaseOrder::fromDataString)
                .stream()
                .filter(Objects::nonNull) // Filter out any nulls from parsing errors
                .collect(Collectors.toList());
    }
    
    @Override
    public void rejectPurchaseOrder(String poId, Remark rejectionReason) {
        List<PurchaseOrder> poList = this.getAllPOs();

        boolean poFound = false;
        PurchaseOrder matchedPO = null;

        for (PurchaseOrder po : poList) {
             if (po.getPoId().equals(poId) ) {
                poFound = true;
                matchedPO = po;
                break;
            }
        }

        if (!poFound) {
            throw new IllegalArgumentException("No valid APPROVED PO found for PO ID: " + poId);
        }

        try {
            matchedPO.setStatus(Status.REJECTED); // Use Status enum
            matchedPO.setRemark(rejectionReason);

            boolean poUpdated = fileManager.updateToFile(
                matchedPO,
                fileManager.getPoFilePath(),
                PurchaseOrder::getPoId,
                PurchaseOrder::toString,
                PurchaseOrder::fromDataString
            );

            if (!poUpdated) {
                throw new RuntimeException("Failed to update PO file with rejection");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to reject PO: " + e.getMessage(), e);
        }
    
    }
    
    @Override
    public String approvePurchaseOrder(String poId, int newQuantity, String newSupplierCode) throws IllegalArgumentException {
        if (poId == null || poId.trim().isEmpty()) {
            throw new IllegalArgumentException("PO ID cannot be empty");
        }
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }

        List<PurchaseOrder> poList = this.getAllPOs();

        for (PurchaseOrder po : poList) {
            if (po != null && po.getPoId().equals(poId)) {
                if (!po.getStatus().equals(Status.PENDING)) {
                    return "Purchase Order " + poId + " is already " + po.getStatus();
                }

                if (newSupplierCode != null && !newSupplierCode.isEmpty() && !newSupplierCode.equals(po.getSupplierCode())) {
                    List<ItemSupply> itemSupplies = fileManager.readFile(
                        fileManager.getItemSupplyFilePath(),
                        line -> {
                            String[] data = line.split(",");
                            if (data.length < 4) return null;
                            try {
                                return new ItemSupply(data[0], data[1], data[2], Double.parseDouble(data[3]));
                            } catch (Exception e) {
                                return null;
                            }
                        }
                    );
                    boolean validSupplier = false;
                    for (ItemSupply supply : itemSupplies) {
                        if (supply != null && 
                            supply.getItemCode().equals(po.getItemCode()) && 
                            supply.getSupplierCode().equals(newSupplierCode)) {
                            validSupplier = true;
                            break;
                        }
                    }
                    if (!validSupplier) {
                        return "Supplier " + newSupplierCode + " does not supply item " + po.getItemCode();
                    }
                    po.setSupplierCode(newSupplierCode);
                }

                if (newQuantity > 0) {
                    po.setQuantity(newQuantity);
                }

                po.setStatus(Status.APPROVED);
                boolean success = fileManager.updateToFile(
                    po, fileManager.getPoFilePath(),
                    PurchaseOrder::getPoId, PurchaseOrder::toString,
                    line -> {
                        String[] data = line.split(",");
                        if (data.length < 11) return null;
                        try {
                            return new PurchaseOrder(
                                data[0], data[1], data[2], data[3],
                                Integer.parseInt(data[4]), data[5], data[6], data[7],
                                Status.valueOf(data[8]), Double.parseDouble(data[9]),
                                Remark.valueOf(data[10])
                            );
                        } catch (Exception e) {
                            return null;
                        }
                    }
                );
                return success ? "Purchase Order " + poId + " approved successfully" : "Failed to update Purchase Order " + poId;
            }
        }
        return "Purchase Order " + poId + " not found";
    }
    
    @Override
    public String payPurchaseOrder(String poId) throws IllegalArgumentException {
        if (poId == null || poId.trim().isEmpty()) {
            throw new IllegalArgumentException("PO ID cannot be empty");
        }

        List<PurchaseOrder> poList = this.getAllPOs();

        for (PurchaseOrder po : poList) {
            if (po != null && po.getPoId().equals(poId)) {
                if (!po.getStatus().equals(Status.RECEIVED)) {
                    return "Only RECEIVED Purchase Order can be selected.";
                }

                po.setStatus(Status.PAID);
                boolean success = fileManager.updateToFile(
                    po, fileManager.getPoFilePath(),
                    PurchaseOrder::getPoId, 
                    PurchaseOrder::toString,
                    line -> {
                        String[] data = line.split(",");
                        if (data.length < 11) return null;
                        try {
                            return new PurchaseOrder(
                                data[0], data[1], data[2], data[3],
                                Integer.parseInt(data[4]), data[5], data[6], data[7],
                                Status.valueOf(data[8]), Double.parseDouble(data[9]),
                                Remark.valueOf(data[10])
                            );
                        } catch (Exception e) {
                            return null;
                        }
                    }
                );
                return success ? "Payment for Purchase Order: " + poId + " was successful." : "Failed to process payment for Purchase Order " + poId;
            }
        }
        return "Purchase Order " + poId + " not found";
    }

}
