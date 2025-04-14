/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entities;

/**
 *
 * @author Sheng Ting
 */
public class User {
    private String userID;
    private String username;
    private String password;
    private String role; // Role (e.g., "ADMIN", "SALES_MANAGER", "PURCHASE_MANAGER", "INVENTORY_MANAGER", "FINANCE_MANAGER")
    
    public User (){
        
    }

    public User(String userID, String username, String password, String role) {
        this.userID     = userID;
        this.username   = username;
        this.password   = password;
        this.role       = role;
    }
    
    

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    
    public boolean login () {
        return true;
    }
    
    public boolean logout () {
        return true;
    }
    // For FileManager compatibility
    @Override
    public String toString() {
        return userID + "," + username + "," + password + "," + role;
    }
    
}
