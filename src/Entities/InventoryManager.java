/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entities;
import Utility.UserRoles;


/**
 *
 * @author Sheng Ting
 */
public class InventoryManager extends User{
    
    public InventoryManager(String userId, String username, String password) {
        super(userId, username, password, UserRoles.INVENTORY_MANAGER);
    }
    
    }
