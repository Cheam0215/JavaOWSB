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
public interface InventoryServices {
    String receiveStockAndUpdateInventory(String itemCode, int quantityReceived, User performingUser);
    
}
