/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Interface;

import Entities.User;

/**
 *
 * @author Sheng Ting
 */
public interface PurchaseOrderServices extends PurchaseOrderViewServices{
    public String generatePurchaseOrder(String prId, String supplierCode, User performingUser);
    public boolean editPurchaseOrder(String poId, int newQuantity , double new_payment_amount);
    public boolean deletePurchaseOrder(String poId);
}
