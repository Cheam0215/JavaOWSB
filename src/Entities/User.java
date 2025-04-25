/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entities;

import Utility.UserRoles;
import Utility.Session;
import Utility.FileManager;
import java.util.List;



/**
 *
 * @author Sheng Ting
 */
public abstract class User {
    private String userID;
    private String username;
    private String password;
    private UserRoles role; // Role (e.g., "ADMINISTRATOR", "SALES_MANAGER", "PURCHASE_MANAGER", "INVENTORY_MANAGER", "FINANCE_MANAGER")
    
    public User(String userID, String username, String password, UserRoles role) {
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

    public UserRoles getRole() {
        return role;
    }

    public void setRole(UserRoles role) {
        this.role = role;
    }

    
    
    public static boolean login(String userID, String password) {
        FileManager fileManager = new FileManager();
        
        if (userID == null || userID.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return false;
        }
        List<User> userList = fileManager.readFile(
            fileManager.getUserFilePath(),
            User::parse
        );
        System.out.println(userList.stream());
        return userList.stream()
            .anyMatch(user -> user.getUserID().equals(userID) && user.getPassword().equals(password));
        }

       public boolean logout () {
           Session logout = new Session();
           logout.setUserID("");
           return true;
    }
    
    public abstract void displayMenu();
    
     public static User parse(String line) {
        String[] data = line.split(",");
        if (data.length < 4) {
            return null;
        }
        try {
            UserRoles role = UserRoles.valueOf(data[3]);
            return switch (role) {
                case ADMINISTRATOR -> new Administrator(data[0], data[1], data[2]);
                case SALES_MANAGER -> new SalesManager(data[0], data[1], data[2]);
                case PURCHASE_MANAGER -> new PurchaseManager(data[0], data[1], data[2]);
                case INVENTORY_MANAGER -> new InventoryManager(data[0], data[1], data[2]);
                case FINANCE_MANAGER -> new FinanceManager(data[0], data[1], data[2]);
                default -> null;
            };
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid role in user data: " + line);
            return null;
        }
    }
    
    // For FileManager compatibility
    @Override
    public String toString() {
        return "User{" + "userID=" + userID + ", username=" + username + ", password=" + password + ", role=" + role + '}';
    }
    
    
}
