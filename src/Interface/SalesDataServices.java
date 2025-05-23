/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Interface;

import Entities.SalesData;

/**
 *
 * @author Sheng Ting
 */
public interface SalesDataServices extends SalesDataViewingServices{
     public void addSalesData(String salesId, String itemCode, int quantitySold, double retailPrice, String date, double totalAmount);
     public boolean updateSalesData(SalesData updatedSales);
     public boolean deleteSalesData(String salesId);
}
