/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Interface;

import Entities.ItemSupply;
import java.util.List;

/**
 *
 * @author Sheng Ting
 */
public interface ItemSupplyViewingServices {
    public List<String[]> viewItemSupplies();
    public List<ItemSupply> getAllItemSupply();
}
