/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entities;

/**
 *
 * @author chieu
 */


import java.util.List;
import java.util.Map;


public class StockReportData {
    private final List<PurchaseOrder> stockInPOs;
    private final List<SalesData> stockOutSales;
    private final List<Item> inventoryItems;
    private final Map<String, Integer> stockInQty;
    private final Map<String, Double> stockInValue;
    private final Map<String, Integer> stockOutQty;
    private final Map<String, Double> stockOutValue;
    
    public StockReportData(
        List<PurchaseOrder> stockInPOs,
        List<SalesData> stockOutSales,
        List<Item> inventoryItems,
        Map<String, Integer> stockInQty,
        Map<String, Double> stockInValue,
        Map<String, Integer> stockOutQty,
        Map<String, Double> stockOutValue
    ) {
        this.stockInPOs = stockInPOs;
        this.stockOutSales = stockOutSales;
        this.inventoryItems = inventoryItems;
        this.stockInQty = stockInQty;
        this.stockInValue = stockInValue;
        this.stockOutQty = stockOutQty;
        this.stockOutValue = stockOutValue;
    }

    public List<PurchaseOrder> getStockInPOs() {
        return stockInPOs;
    }

    public List<SalesData> getStockOutSales() {
        return stockOutSales;
    }

    public List<Item> getInventoryItems() {
        return inventoryItems;
    }

    public Map<String, Integer> getStockInQty() {
        return stockInQty;
    }

    public Map<String, Double> getStockInValue() {
        return stockInValue;
    }

    public Map<String, Integer> getStockOutQty() {
        return stockOutQty;
    }

    public Map<String, Double> getStockOutValue() {
        return stockOutValue;
    }
   
}
