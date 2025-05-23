/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Interface;

import Entities.Item;
import Entities.PurchaseOrder;
import Entities.SalesData;
import Entities.StockReportData;
import Entities.User;
import java.util.List;
import java.util.Map;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Sheng Ting
 */
public interface InventoryServices {
    String receiveStockAndUpdateInventory(String itemCode, int quantityReceived, User performingUser);
    public StockReportData displayStockReport(List<PurchaseOrder> poList, List<SalesData> salesList, List<Item> itemList, String startDate, String endDate);
    public Map<String, Integer> calculateStockReport(List<Item> itemList, List<PurchaseOrder> poList, List<SalesData> salesList, String startDate, String endDate, DefaultTableModel tableModel, JTable jTable1);     
    
}
