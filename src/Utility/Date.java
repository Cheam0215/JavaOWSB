/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utility;

/**
 *
 * @author Sheng Ting
 */
public class Date {
    
    private int year, month, day;

    public Date(int year, int month, int day) {
        this.year   = year;
        this.month  = month;
        this.day    = day;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
    
    @Override
    public String toString() {
        return month + "/" + day + "/" + year;
    }
}
