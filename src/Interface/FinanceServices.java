/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Interface;

import Entities.PurchaseOrder;
import Entities.SalesData;
import java.util.List;

/**
 *
 * @author Sheng Ting
 */
public interface FinanceServices {
    public String generateFinancialReport(List<SalesData> salesList, List<PurchaseOrder> poList);
    public double calculateProfitLoss(List<SalesData> salesList, List<PurchaseOrder> poList, int month, int year);
}
