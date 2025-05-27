/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Interface;

import Entities.PurchaseOrder;
import Utility.Remark;
import java.util.List;

/**
 *
 * @author Sheng Ting
 */
public interface FinanceManagerPOServices {
    public List<PurchaseOrder> getAllPOs();
    public String approvePurchaseOrder(String poId, int newQuantity, String newSupplierCode, Remark approveReason);
    public void rejectPurchaseOrder(String poId, Remark rejectionReason);
    public String payPurchaseOrder(String poId);
}
