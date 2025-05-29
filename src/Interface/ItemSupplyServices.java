/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Interface;

import Entities.ItemSupply;

/**
 *
 * @author Sheng Ting
 */
public interface ItemSupplyServices extends ItemSupplyViewingServices{
    public String addItemSupply(ItemSupply itemSupply);
    public String updateItemSupply(ItemSupply updatedItemSupply);
    public String deleteItemSupply(String itemCode, String supplierCode);
    
}
