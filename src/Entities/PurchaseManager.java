package Entities;

import Utility.UserRoles;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Sheng Ting
 */
public class PurchaseManager extends User{
    
    public PurchaseManager(String userId, String username, String password) {
        super(userId, username, password, UserRoles.PURCHASE_MANAGER);
    }
}
