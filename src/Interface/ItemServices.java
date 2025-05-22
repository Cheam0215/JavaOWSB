/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Interface;

import Entities.Item;

/**
 *
 * @author Sheng Ting
 */
public interface ItemServices extends ItemViewingServices{
    public String addItem(Item item, String supplierCode, double unitPrice);
    public String updateItem(Item updatedItem, String supplierCode, double unitPrice);
    public String deleteItem(String itemCode);
    public boolean updateItemStockLevel(String itemCode, int newStockLevel);
}
