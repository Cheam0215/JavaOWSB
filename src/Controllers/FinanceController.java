/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

import Entities.PurchaseOrder;
import Entities.SalesData;
import Utility.FileManager;
import Utility.Status;
import java.util.List;

/**
 *
 * @author Sheng Ting
 */
public class FinanceController {
    
    private final FileManager fileManager;

    public FinanceController(FileManager fileManager) {
        this.fileManager = fileManager;
    }
    
    public double calculateProfitLoss(List<SalesData> salesList, List<PurchaseOrder> poList, int month, int year) {
      
        double totalRevenue = 0;
        for (SalesData sale : salesList) {
            if (sale != null && isInMonth(sale.getDate(), month, year)) {
                totalRevenue += sale.getTotalAmount();
            }
        }

        double totalExpenses = 0;
        for (PurchaseOrder po : poList) {
            if (po != null && po.getStatus().equals(Status.PAID) && isInMonth(po.getRequiredDate(), month, year)) {
                totalExpenses += po.getPaymentAmount();
            }
        }

        return totalRevenue - totalExpenses;
    }

    private boolean isInMonth(String date, int month, int year) {
        try {
            String[] parts = date.contains("-") ? date.split("-") : null;
            if (parts == null || parts.length < 3) return false;

            int dateMonth, dateYear;
            if (parts[0].length() == 4) {
                dateYear = Integer.parseInt(parts[0]);
                dateMonth = Integer.parseInt(parts[1]);
            } else {
                dateYear = Integer.parseInt(parts[2]);
                dateMonth = Integer.parseInt(parts[1]);
            }

            return dateMonth == (month + 1) && dateYear == year;
        } catch (Exception e) {
            System.err.println("Error parsing date: " + date);
            return false;
        }
    }

    public String generateFinancialReport(List<SalesData> salesList, List<PurchaseOrder> poList) {
        double totalRevenue = 0;
        for (SalesData sale : salesList) {
            if (sale != null) {
                totalRevenue += sale.getTotalAmount();
            }
        }

        double totalExpenses = 0;
        for (PurchaseOrder po : poList) {
            if (po != null && po.getStatus().equals(Status.PAID)) {
                totalExpenses += po.getPaymentAmount();
            }
        }

        double profitLoss = totalRevenue - totalExpenses;

        StringBuilder report = new StringBuilder("Financial Report\n");
        report.append("========================================\n\n");

        report.append("Financial Summary:\n");
        report.append(String.format("Total Revenue: RM%.2f\n", totalRevenue));
        report.append(String.format("Total Expenses (Paid POs): RM%.2f\n", totalExpenses));
        report.append(String.format("Profit/Loss: RM%.2f\n", profitLoss));
        report.append("----------------------------------------\n\n");

        report.append("Sales Details:\n");
        report.append("Sales ID | Item Code | Quantity Sold | Retail Price | Date | Total Amount\n");
        report.append("-------------------------------------------------------------------\n");
        for (SalesData sale : salesList) {
            if (sale != null) {
                report.append(String.format("%s | %s | %d | RM%.2f | %s | RM%.2f\n",
                    sale.getSalesId(), sale.getItemCode(), sale.getQuantitySold(),
                    sale.getRetailPrice(), sale.getDate(), sale.getTotalAmount()));
            }
        }
        report.append("----------------------------------------\n\n");

        report.append("Purchase Order Details:\n");
        report.append("PO ID | Item Code | Quantity | Supplier | Status | Payment Amount | Date\n");
        report.append("---------------------------------------------------------------------------\n");
        for (PurchaseOrder po : poList) {
            if (po != null) {
                report.append(String.format("%s | %s | %d | %s | %s | RM%.2f | %s\n",
                    po.getPoId(), po.getItemCode(), po.getQuantity(),
                    po.getSupplierCode(), po.getStatus(), po.getPaymentAmount(), po.getRequestedDate()));
            }
        }

        return report.toString();
    }
    
}
