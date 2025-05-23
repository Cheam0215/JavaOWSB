/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Interface;

import Entities.PurchaseRequisition;
import Utility.Status;

/**
 *
 * @author Sheng Ting
 */
public interface PurchaseRequisitionServices extends PurchaseRequisitionViewServices{
    public void addPurchaseRequisition(String prId, String itemCode, String requestedBy, int quantity, String requiredDate, String requestedDate, Status status);
    public boolean updatePurchaseRequisition(PurchaseRequisition updatedPR);
    public boolean deletePurchaseRequisition(String prId);
      
}
