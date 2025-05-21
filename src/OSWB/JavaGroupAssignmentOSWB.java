/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package OSWB;

import Controllers.ItemController;
import Controllers.ItemSupplyController;
import Controllers.PurchaseOrderController;
import Controllers.PurchaseRequisitionController;
import Controllers.SalesDataController;
import Controllers.SupplierController;
import Utility.FileManager;

/**
 *
 * @author Sheng Ting
 */
public class JavaGroupAssignmentOSWB {
    public static void main(String[] args) {
        FileManager fileManager = new FileManager();
        ItemController itemController                                   = new ItemController(fileManager);
        ItemSupplyController itemSupplyController                       = new ItemSupplyController(fileManager);
        PurchaseOrderController purchaseOrderController                 = new PurchaseOrderController(fileManager);
        PurchaseRequisitionController purchaseRequisitionController     = new PurchaseRequisitionController(fileManager);
        SalesDataController salesDataController                         = new SalesDataController(fileManager);
        SupplierController supplierController                           = new SupplierController(fileManager);
        
        Login login = new Login(itemController, itemSupplyController, purchaseOrderController, purchaseRequisitionController, salesDataController,supplierController);
        login.setVisible(true);
    }
    
}
