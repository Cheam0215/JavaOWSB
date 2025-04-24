package Entities;

import Utility.FileManager;
import Utility.UserRoles;
import Utility.Remark;
import Utility.Status;
import java.util.List;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Sheng Ting
 */
public class PurchaseManager extends User{
    private final FileManager fileManager;
    
    public PurchaseManager(String userId, String username, String password) {
        super(userId, username, password, UserRoles.PURCHASE_MANAGER);
        this.fileManager = new FileManager();
    }
    
    public String viewItems() {
        List<Item> itemsList = fileManager.readFile(
            fileManager.getPoFilePath(),
            line -> {
                String[] data = line.split(",");
                return new Item(data[0], data[1], data[2], Integer.parseInt(data[3]), Double.parseDouble(data[4]), Double.parseDouble(data[5]));
            }
        );

        StringBuilder view = new StringBuilder("Item List:\n");
        view.append("Item Code|Item Name|Supplier ID|Stock Level\n");
        view.append("-------------------------------------------\n");

        for (Item item : itemsList) {
            view.append(String.format("%s | %s | %s | %d \n",
                item.getItemCode(), item.getItemName(), item.getSupplierCode(), 
                item.getStockLevel()));
        }
        return view.toString();
    }
    
    public String viewSuppliers() {
        List<Supplier> supplierList = fileManager.readFile(
            fileManager.getPoFilePath(),
            line -> {
                String[] data = line.split(",");
                return new Supplier(data[0], data[1], Integer.parseInt(data[2]), data[3], data[4]);
            }//2 is list may got problem
        );

        StringBuilder view = new StringBuilder("Item List:\n");
        view.append("Supplier Code|Supplier Name\n");
        view.append("-------------------------------------------\n");

        for (Supplier sup : supplierList) {
            view.append(String.format("%s | %s \n",
                sup.getSupplierCode(), sup.getSupplierName(), sup.getItemIds()));
        }
        return view.toString();
    }
    
    public String viewPurchaseRequisition() {
        List<PurchaseRequisition> prList = fileManager.readFile(
            fileManager.getPrFilePath(),
            line -> {
                String[] data = line.split(",");
                return new PurchaseRequisition(data[0], data[1], data[2], Integer.parseInt(data[3]), data[4], data[5],data[6], Status.valueOf(data[7]));
            }
        );

        StringBuilder view = new StringBuilder("Purchase Requisitions:\n");
        view.append("PR ID | Item Code | Quantity | Required Date | Supplier\n");
        view.append("-------------------------------------------------\n");

        for (PurchaseRequisition pr : prList) {
            view.append(String.format("%s | %s | %d | %s | %s\n",
                pr.getPrId(), pr.getItemCode(), pr.getQuantity(),
                pr.getRequiredDate(), pr.getSupplierCode()));
        }
        return view.toString();
    }
    
    public String viewPurchaseOrder() {
         List<PurchaseOrder> poList = fileManager.readFile(
            fileManager.getPoFilePath(),
            line -> {
                String[] data = line.split(",");
                return new PurchaseOrder(data[0], data[1], data[2], data[3],
                    Integer.parseInt(data[4]), data[5], data[6], data[7],Status.valueOf(data[8]),
                    Double.parseDouble(data[9]),Remark.valueOf(data[10]));

            }
        );
         
        StringBuilder view = new StringBuilder("Purchase Order:\n");
        view.append("Purchase Order ID | Purchase Requisition ID | Item Code | Quantity | Supplier ID | Status | Payment Amount\n");
        view.append("-------------------------------------------------\n");

        for (PurchaseOrder po : poList){
            view.append(String.format("%s | %s | %s | %d | %s | %s | RM%.2f\n",
                po.getPoId(), po.getPrId(), po.getItemCode(),
                po.getQuantity(), po.getSupplierCode(),po.getStatus(), po.getPaymentAmount()));   
        }
        return view.toString();
    }
    
    public String generatePurchaseOrder() {
        return "";
    }
    
    public boolean editPurchaseOrder() {
        return true;
    }
    
    public boolean deletePurchaseOrder() {
        return true;
    }
    
    
    
    
}
