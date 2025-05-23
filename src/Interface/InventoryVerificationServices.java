/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Interface;

import Entities.FinanceManager;
import java.util.List;

/**
 *
 * @author Sheng Ting
 */
public interface InventoryVerificationServices {
     public List<FinanceManager.InventoryItem> verifyInventoryUpdate();
}
