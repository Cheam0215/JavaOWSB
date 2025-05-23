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
public interface InventoryManagerPOServices {
    public void rejectPurchaseOrder(String poId, Remark rejectionReason);
    public List<String[]> viewPurchaseOrder();
    public List<PurchaseOrder> getAllPOs();
    PurchaseOrder findApprovedPOByItemCode(String itemCode);
    PurchaseOrder getPOById(String poId);
}
