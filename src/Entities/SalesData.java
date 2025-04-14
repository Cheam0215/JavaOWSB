/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entities;

/**
 *
 * @author Sheng Ting
 */
public class SalesData {
    private String salesId;
    private String itemCode;
    private int quantitySold;
    private double unitPrice;
    private String date;
    private double totalAmount;

    public SalesData(String salesId, String itemCode, int quantitySold, double unitPrice, String date) {
        this.salesId = salesId;
        this.itemCode = itemCode;
        this.quantitySold = quantitySold;
        this.unitPrice = unitPrice;
        this.date = date;
        this.totalAmount = quantitySold * unitPrice;
    }

    public String getSalesId() {
        return salesId;
    }

    public void setSalesId(String salesId) {
        this.salesId = salesId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

   

    @Override
    public String toString() {
        return salesId + "," + itemCode + "," + quantitySold + "," + unitPrice + "," + date + "," + totalAmount;
    }
}

    