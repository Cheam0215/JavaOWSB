/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Interface;

import Entities.Item;
import java.util.List;

/**
 *
 * @author Sheng Ting
 */
public interface ItemViewingServices {
     public List<String[]> viewItems();
     public List<Item> getAllItems();
     public Item getItemByCode(String itemCode);
     
}
