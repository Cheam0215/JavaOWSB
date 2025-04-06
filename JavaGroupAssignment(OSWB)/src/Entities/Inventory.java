/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entities;

import java.util.List;

/**
 *
 * @author Sheng Ting
 */
public class Inventory {
    private static final int LOW_STOCK_THRESHOLD = 100; // Low stock threshold
    
    public List<Item> viewItems () {
        return null;
    }
    
    public boolean updateStock() {
        return true;
    }
    
    public boolean checkLowStock() {
        return true;
    }
    
    public String generateStockReport () {
        return null;
    }
}
