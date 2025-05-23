/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Interface;

import Entities.PurchaseOrder;
import java.util.List;

/**
 *
 * @author Sheng Ting
 */
public interface PurchaseOrderViewServices {
    public List<String[]> viewPurchaseOrder();
    public List<PurchaseOrder> getAllPOs();
    PurchaseOrder findApprovedPOByItemCode(String itemCode);
    PurchaseOrder getPOById(String poId);
    
}
