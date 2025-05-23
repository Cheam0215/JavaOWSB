/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Interface;

import Entities.SalesData;
import java.util.List;

/**
 *
 * @author Sheng Ting
 */
public interface SalesDataViewingServices {
    public List<String[]> viewSalesData();
    public List<SalesData> getSalesData();
}
