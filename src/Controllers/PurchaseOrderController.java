/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

import Entities.PurchaseOrder;
import Entities.PurchaseRequisition;
import Entities.User;
import Interface.PurchaseOrderServices;
import Utility.FileManager;
import Utility.Remark;
import Utility.Status;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sheng Ting
 */
public class PurchaseOrderController implements PurchaseOrderServices{
    
    private final FileManager fileManager;

    public PurchaseOrderController(FileManager fileManager) {
        this.fileManager = fileManager;
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
    
    public String generatePurchaseOrder(String prId, String supplierCode, User performingUser) {
        try {
             //Validate session userID
//            if (session.getUserID() == null || session.getUserID().isEmpty()) {
//                return "Error: No user logged in. Please log in to generate a Purchase Order.";
//            }

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
            List<PurchaseOrder> poList = fileManager.readFile(
                fileManager.getPoFilePath(),
                line -> {
                    String[] data = line.split(",");
                    try {
                        return new PurchaseOrder(
                            data[0], data[1], data[2], data[3],
                            Integer.parseInt(data[4]), data[5], data[6], data[7],
                            Status.valueOf(data[8]), Double.parseDouble(data[9]),
                            Remark.valueOf(data[10])
                        );
                    } catch (Exception e) {
                        System.out.println("Error parsing PO data: " + line + " | " + e.getMessage());
                        return null;
                    }
                }
            );

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
    
    public boolean editPurchaseOrder(String poId, int newQuantity , double new_payment_amount) {
        try {
            // Validate session
//            if (session.getUserID() == null || session.getUserID().isEmpty()) {
//                System.out.println("Error: No user logged in.");
//                return false;
//            }

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
            List<PurchaseOrder> poList = fileManager.readFile(
                fileManager.getPoFilePath(),
                line -> {
                    String[] data = line.split(",");
                    try {
                        return new PurchaseOrder(
                            data[0], data[1], data[2], data[3],
                            Integer.parseInt(data[4]), data[5], data[6], data[7],
                            Status.valueOf(data[8]), Double.parseDouble(data[9]),
                            Remark.valueOf(data[10])
                        );
                    } catch (Exception e) {
                        System.out.println("Error parsing PO data: " + line + " | " + e.getMessage());
                        return null;
                    }
                }
            );

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
                po -> String.join(",", po.getPoId(), po.getPrId(), po.getRaisedBy(),
                    po.getItemCode(), String.valueOf(po.getQuantity()), po.getSupplierCode(),
                    po.getRequiredDate(), po.getRequestedDate(), po.getStatus().toString(),
                    String.valueOf(po.getPaymentAmount()), po.getRemark().toString()),
                line -> {
                    String[] data = line.split(",");
                    try {
                        return new PurchaseOrder(
                            data[0], data[1], data[2], data[3],
                            Integer.parseInt(data[4]), data[5], data[6], data[7],
                            Status.valueOf(data[8]), Double.parseDouble(data[9]),
                            Remark.valueOf(data[10])
                        );
                    } catch (Exception e) {
                        System.out.println("Error parsing PO data: " + line + " | " + e.getMessage());
                        return null;
                    }
                }
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
    
    public boolean deletePurchaseOrder(String poId) {
        try {
            // Validate session
//            if (session.getUserID() == null || session.getUserID().isEmpty()) {
//                System.out.println("Error: No user logged in.");
//                return false;
//            }

            // Validate poId
            if (poId == null || poId.trim().isEmpty()) {
                System.out.println("Error: Invalid Purchase Order ID.");
                return false;
            }

            // Read all PurchaseOrders
            List<PurchaseOrder> poList = fileManager.readFile(
                fileManager.getPoFilePath(),
                line -> {
                    String[] data = line.split(",");
                    try {
                        return new PurchaseOrder(
                            data[0], data[1], data[2], data[3],
                            Integer.parseInt(data[4]), data[5], data[6], data[7],
                            Status.valueOf(data[8]), Double.parseDouble(data[9]),
                            Remark.valueOf(data[10])
                        );
                    } catch (Exception e) {
                        System.out.println("Error parsing PO data: " + line + " | " + e.getMessage());
                        return null;
                    }
                }
            );

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
                po -> String.join(",", po.getPoId(), po.getPrId(), po.getRaisedBy(),
                    po.getItemCode(), String.valueOf(po.getQuantity()), po.getSupplierCode(),
                    po.getRequiredDate(), po.getRequestedDate(), po.getStatus().toString(),
                    String.valueOf(po.getPaymentAmount()), po.getRemark().toString()),
                line -> {
                    String[] data = line.split(",");
                    try {
                        return new PurchaseOrder(
                            data[0], data[1], data[2], data[3],
                            Integer.parseInt(data[4]), data[5], data[6], data[7],
                            Status.valueOf(data[8]), Double.parseDouble(data[9]),
                            Remark.valueOf(data[10])
                        );
                    } catch (Exception e) {
                        System.out.println("Error parsing PO data: " + line + " | " + e.getMessage());
                        return null;
                    }
                }
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
}
