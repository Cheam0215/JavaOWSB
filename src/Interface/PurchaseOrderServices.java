/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Interface;

import Entities.User;
import Utility.Status;

/**
 *
 * @author Sheng Ting
 */
public interface PurchaseOrderServices extends PurchaseOrderViewServices{
    public String generatePurchaseOrder(String prId, String supplierCode, User performingUser);
    public boolean editPurchaseOrder(String poId, int newQuantity , String itemCode, String supplierCode);
    public boolean deletePurchaseOrder(String poId);
    boolean updatePurchaseOrderStatus(String poId, Status newStatus);
}
