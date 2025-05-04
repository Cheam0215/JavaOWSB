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
    private double retailPrice;
    private String date;
    private double totalAmount;

    public SalesData(String salesId, String itemCode, int quantitySold, double retailPrice, String date, double totalAmount) {
        this.salesId = salesId;
        this.itemCode = itemCode;
        this.quantitySold = quantitySold;
        this.retailPrice = retailPrice;
        this.date = date;
        this.totalAmount = totalAmount;
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

    public double getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(double retailPrice) {
        this.retailPrice = retailPrice;
    }

   

    @Override
    public String toString() {
        return salesId + "," + itemCode + "," + quantitySold + "," + retailPrice + "," + date + "," + totalAmount;
    }
}

    