/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entities;

import java.util.List;

import Utility.FileManager;

/**
 *
 * @author Sheng Ting
 */
public class Inventory {
    private static final int LOW_STOCK_THRESHOLD = 100; // Low stock threshold
    private FileManager fileManager = new FileManager();

    public List<Item> viewItems() {
        return fileManager.readFile(
            fileManager.getItemFilePath(),
            line -> {
                String[] data = line.split(",");
                return new Item(data[0], data[1], data[2], Integer.parseInt(data[3]), Double.parseDouble(data[4]), Double.parseDouble(data[5]));
            }
        );
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
