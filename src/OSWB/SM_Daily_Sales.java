/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package OSWB;

import Controllers.SalesDataController;
import Entities.SalesData;
import Entities.SalesManager;
import Entities.User;
import Utility.FileManager;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
/**
 *
 * @author Edwin Chen
 */
public class SM_Daily_Sales extends javax.swing.JFrame {
    private final DefaultTableModel model = new DefaultTableModel();
    private final String columnName[] = {"Sales ID", "Item Code", "Quantity Sold", "Retail Price", "Date", "Total Amount"};
    private FileManager fileManager;
    private final Map<String, ItemDetails> itemDetailsMap = new HashMap<>();
    private boolean isEditing = false;
    private String editingSalesId;
    private String originalItemCode;
    private final User currentUser;
    private final SalesDataController salesDataController;
    private final JFrame previousScreen;


    private static class ItemDetails {
        int stockLevel;
        double retailPrice;

        ItemDetails(int stockLevel, double retailPrice) {
            this.stockLevel = stockLevel;
            this.retailPrice = retailPrice;
        }
    }
    /**
     * Creates new form SM_Daily_Sales
     * @param currentUser
     * @param salesDataController
     * @param previousScreen
     */
    public SM_Daily_Sales(User currentUser, SalesDataController salesDataController, JFrame previousScreen) {
        this.currentUser = currentUser;
        this.salesDataController = salesDataController;
        this.previousScreen = previousScreen;
        fileManager = new FileManager();
        initComponents();
        setupTable();
        loadSales();
        populateItemCodeComboBox();
        setDefaultValues();
        setupListeners();
        setupTableSelectionListener();
        editBtn.setEnabled(false); // Disable Edit button initially
        saveBtn.setEnabled(false); // Disable Save button initially
        
    }
    
    private void setupTable() {
        model.setColumnIdentifiers(columnName);
        jTable1.setModel(model);
    }
    
    private void loadSales() {
        try {
            // Clear existing data from the table model
            model.setRowCount(0);

            List<String[]> sales = salesDataController.viewSalesData(); // Assuming viewItems returns List<String[]>
            if (sales.isEmpty()) {
                // Optional: Show a message if the overall item list is empty
                 JOptionPane.showMessageDialog(this, "There are no Sales Data available.", "Load Sales Data", JOptionPane.WARNING_MESSAGE);
            } else {
                for (String[] item : sales) {
                    model.addRow(item);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                                          "Error loading Sales Data: " + e.getMessage(),
                                          "Load Sales Data Error",
                                          JOptionPane.ERROR_MESSAGE);
            // Ensure table is empty if loading fails
            model.setRowCount(0);
        }
    }
     
    private void searchSales() {
        String searchTerm = jTextField1.getText().trim();
        
        // If search field is empty, reload all data
        if (searchTerm.isEmpty()) {
            loadSales();
            return;
        }
        
        try {
            // Clear current table data
            model.setRowCount(0);
            
            // Get all POs
            List<String[]> allSales = salesDataController.viewSalesData();
            boolean foundMatch = false;
            
            for (String[] sales : allSales) {
                if ((sales[0] != null && sales[0].toLowerCase().contains(searchTerm.toLowerCase())) ||
                    (sales[1] != null && sales[1].toLowerCase().contains(searchTerm.toLowerCase())) ||
                    (sales[4] != null && sales[4].toLowerCase().contains(searchTerm.toLowerCase()))) {
                    model.addRow(sales);
                    foundMatch = true;
                }
            }
            
            // Show message if no results found
            if (!foundMatch) {
                JOptionPane.showMessageDialog(this, 
                    "No items found matching '" + searchTerm + "'", 
                    "Search Results", 
                    JOptionPane.INFORMATION_MESSAGE);
                // Reload all data after showing the message
                loadSales();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error searching items: " + e.getMessage(), 
                "Search Error", 
                JOptionPane.ERROR_MESSAGE);
            loadSales();
        }
    }
    
    private void resetTable() {
        jTextField1.setText("");
        loadSales();
        jLabel9.setText(generateNextSalesId());
        jComboBox1.setSelectedIndex(-1);
        jTextField3.setText("");
        jDateChooser1.setDate(new Date());
        jLabel11.setText("");
        jLabel13.setText("");
        isEditing = false;
        editingSalesId = null;
        originalItemCode = null;
        addBtn.setEnabled(true);
        editBtn.setEnabled(jTable1.getSelectedRow() != -1);
        saveBtn.setEnabled(false);
        deleteBtn.setEnabled(jTable1.getSelectedRow() != -1);
    }
    
    private void populateItemCodeComboBox() {
        try {
            String filePath = new File("").getAbsolutePath() + File.separator + "src" + File.separator + fileManager.getItemFilePath().replace("/", File.separator);
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            jComboBox1.removeAllItems();
            itemDetailsMap.clear();
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String itemCode = parts[0].trim();
                    int stockLevel = Integer.parseInt(parts[2].trim());
                    double retailPrice = Double.parseDouble(parts[3].trim());
                    jComboBox1.addItem(itemCode);
                    itemDetailsMap.put(itemCode, new ItemDetails(stockLevel, retailPrice));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading item codes: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setDefaultValues() {
        jLabel9.setText(generateNextSalesId());
        jDateChooser1.setDate(new Date());
        jDateChooser1.setMaxSelectableDate(new Date()); // Restrict to today or before
    }

    private String generateNextSalesId() {
        String nextSalesId = "SALE001";
        try {
            String filePath = fileManager.getSalesDataFilePath();
            String absolutePath = new File("").getAbsolutePath() + File.separator + "src" + File.separator + filePath.replace("/", File.separator);
            File file = new File(absolutePath);
            if (!file.exists()) {
                return nextSalesId;
            }
            List<String> lines = Files.readAllLines(Paths.get(absolutePath));
            int highestNumber = 0;
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].startsWith("SALE")) {
                    try {
                        String numPart = parts[0].substring(4);
                        int salesNumber = Integer.parseInt(numPart);
                        if (salesNumber > highestNumber) {
                            highestNumber = salesNumber;
                        }
                    } catch (NumberFormatException e) {
                    }
                }
            }
            int nextNumber = highestNumber + 1;
            nextSalesId = String.format("SALE%03d", nextNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nextSalesId;
    }

    private void setupListeners() {
        jComboBox1.addActionListener(e -> updateRetailPriceAndTotal());
        jTextField3.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateRetailPriceAndTotal(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateRetailPriceAndTotal(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateRetailPriceAndTotal(); }
        });
    }

    private void updateRetailPriceAndTotal() {
        String itemCode = (String) jComboBox1.getSelectedItem();
        String quantityStr = jTextField3.getText().trim();
        if (itemCode == null || itemCode.isEmpty() || quantityStr.isEmpty()) {
            jLabel11.setText("");
            jLabel13.setText("");
            return;
        }

        ItemDetails details = itemDetailsMap.get(itemCode);
        if (details == null) {
            jLabel11.setText("");
            jLabel13.setText("");
            return;
        }

        jLabel11.setText(String.format("%.2f", details.retailPrice));
        try {
            int quantity = Integer.parseInt(quantityStr);
            if (quantity <= 0 || quantity > details.stockLevel) {
                JOptionPane.showMessageDialog(this, "Quantity must be between 1 and " + details.stockLevel, "Invalid Quantity", JOptionPane.ERROR_MESSAGE);
                jTextField3.setText("");
                jLabel13.setText("");
                return;
            }
            double total = quantity * details.retailPrice;
            jLabel13.setText(String.format("%.2f", total));
        } catch (NumberFormatException e) {
            jLabel13.setText("");
        }
    }
    
    private boolean isItemCodeDateDuplicate(String itemCode, String date) {
        try {
            List<String[]> sales = salesDataController.viewSalesData();
            for (String[] sale : sales) {
                String existingItemCode = sale[1]; // Item Code
                String existingDate = sale[4]; // Date
                if (existingItemCode.equals(itemCode) && existingDate.equals(date)) {
                    return true;
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error checking duplicates: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
    
    private void setupTableSelectionListener() {
        jTable1.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                boolean rowSelected = jTable1.getSelectedRow() != -1;
                editBtn.setEnabled(rowSelected && !isEditing); // Enable Edit only if not editing
                saveBtn.setEnabled(isEditing); // Enable Save only when editing
                addBtn.setEnabled(!isEditing); // Disable Add when editing
                deleteBtn.setEnabled(rowSelected && !isEditing); // Enable Delete only if not editing
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        addBtn = new javax.swing.JButton();
        editBtn = new javax.swing.JButton();
        saveBtn = new javax.swing.JButton();
        deleteBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jTextField1 = new javax.swing.JTextField();
        searchBtn = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        resetBtn = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(51, 153, 255));

        jLabel1.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 1, 36)); // NOI18N
        jLabel1.setText("Daily Item Wise Sales ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(74, 74, 74)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(31, Short.MAX_VALUE))
        );

        jButton1.setText("Back");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        addBtn.setText("Add");
        addBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBtnActionPerformed(evt);
            }
        });

        editBtn.setText("Edit");
        editBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editBtnActionPerformed(evt);
            }
        });

        saveBtn.setText("Save");
        saveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveBtnActionPerformed(evt);
            }
        });

        deleteBtn.setText("Delete");
        deleteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteBtnActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        searchBtn.setText("Search");
        searchBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchBtnActionPerformed(evt);
            }
        });

        jLabel2.setText(" Sales ID :");

        jLabel3.setText("Item Code :");

        jLabel4.setText("Quantity Sold :");

        jLabel6.setText("Retail Price :");

        jLabel7.setText("Date :");

        jLabel8.setText("Total Amount :");

        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel9.setText("jLabel9");

        resetBtn.setText("Reset");
        resetBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetBtnActionPerformed(evt);
            }
        });

        jLabel11.setText("0");

        jLabel12.setText("RM");

        jLabel13.setText("0");

        jLabel14.setText("RM");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addBtn)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel4)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(34, 34, 34)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(editBtn)
                        .addGap(41, 41, 41)
                        .addComponent(saveBtn)
                        .addGap(39, 39, 39)
                        .addComponent(deleteBtn))
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 720, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(searchBtn)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(resetBtn)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addGap(15, 15, 15))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addBtn)
                    .addComponent(editBtn)
                    .addComponent(saveBtn)
                    .addComponent(deleteBtn)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchBtn)
                    .addComponent(resetBtn))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel9))
                        .addGap(40, 40, 40)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(42, 42, 42)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(43, 43, 43)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel11)
                            .addComponent(jLabel14))
                        .addGap(40, 40, 40)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel7)
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(51, 51, 51)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(jLabel12)
                            .addComponent(jLabel13))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 558, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void addBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBtnActionPerformed
        String salesId = jLabel9.getText();
        String itemCode = (String) jComboBox1.getSelectedItem();
        String quantityStr = jTextField3.getText().trim();
        String retailPriceStr = jLabel11.getText();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(jDateChooser1.getDate());
        String totalAmountStr = jLabel13.getText();

        if (itemCode == null || itemCode.isEmpty() || quantityStr.isEmpty() || retailPriceStr.isEmpty() || totalAmountStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Check for duplicate itemCode on the same date
        if (isItemCodeDateDuplicate(itemCode, date)) {
            JOptionPane.showMessageDialog(this, "Sales data for Item Code " + itemCode + " on " + date + " already exists.", "Duplicate Entry", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int quantitySold = Integer.parseInt(quantityStr);
            double retailPrice = Double.parseDouble(retailPriceStr);
            double totalAmount = Double.parseDouble(totalAmountStr);

            ItemDetails details = itemDetailsMap.get(itemCode);
            if (quantitySold > details.stockLevel) {
                JOptionPane.showMessageDialog(this, "Quantity exceeds available stock (" + details.stockLevel + ").", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            salesDataController.addSalesData(salesId, itemCode, quantitySold, retailPrice, date, totalAmount);
            JOptionPane.showMessageDialog(this, "Sales data added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            resetTable();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid number format.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_addBtnActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (this.previousScreen != null) {
            this.previousScreen.setVisible(true); // Just make the existing one visible
        } else {
            // Fallback or error: Should not happen if previousScreen is always passed
            JOptionPane.showMessageDialog(this, "Error: Previous screen reference lost.", "Navigation Error", JOptionPane.ERROR_MESSAGE);
            // Optionally, recreate Login if truly lost
            // new Login().setVisible(true);
        }
        this.dispose();

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void searchBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchBtnActionPerformed
        searchSales();
    }//GEN-LAST:event_searchBtnActionPerformed

    private void resetBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetBtnActionPerformed
        resetTable();
    }//GEN-LAST:event_resetBtnActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtnActionPerformed
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String salesId = (String) model.getValueAt(selectedRow, 0); // Sales ID is in column 0
        boolean success = salesDataController.deleteSalesData(salesId);

        if (success) {
            JOptionPane.showMessageDialog(this, "Sales data with ID " + salesId + " deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadSales(); 
            resetTable();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to delete sales data with ID " + salesId + ".", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_deleteBtnActionPerformed

    private void editBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editBtnActionPerformed
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to edit.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        isEditing = true;
        editingSalesId = (String) model.getValueAt(selectedRow, 0); // Sales ID
        originalItemCode = (String) model.getValueAt(selectedRow, 1); // Original Item Code
        jLabel9.setText(editingSalesId);
        jComboBox1.setSelectedItem(originalItemCode);
        jTextField3.setText(model.getValueAt(selectedRow, 2).toString()); // Quantity Sold
        jLabel11.setText(model.getValueAt(selectedRow, 3).toString()); // Retail Price
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            jDateChooser1.setDate(sdf.parse((String) model.getValueAt(selectedRow, 4))); // Date
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error parsing date: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            jDateChooser1.setDate(new Date());
        }
        jLabel13.setText(model.getValueAt(selectedRow, 5).toString()); // Total Amount
        updateRetailPriceAndTotal(); // Sync retail price and total
        
        isEditing = true;
        editBtn.setEnabled(false);
        saveBtn.setEnabled(true);
        addBtn.setEnabled(false);
        deleteBtn.setEnabled(false);
    }//GEN-LAST:event_editBtnActionPerformed

    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnActionPerformed
       if (!isEditing) {
            JOptionPane.showMessageDialog(this, "Please click 'Edit' first to modify a record.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String salesId = jLabel9.getText();
        String itemCode = (String) jComboBox1.getSelectedItem();
        String quantityStr = jTextField3.getText().trim();
        String retailPriceStr = jLabel11.getText();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(jDateChooser1.getDate());
        String totalAmountStr = jLabel13.getText();

        if (itemCode == null || itemCode.isEmpty() || quantityStr.isEmpty() || retailPriceStr.isEmpty() || totalAmountStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!itemCode.equals(originalItemCode) && isItemCodeDateDuplicate(itemCode, date)) {
            JOptionPane.showMessageDialog(this, "Sales data for Item Code " + itemCode + " on " + date + " already exists.", "Duplicate Entry", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int quantitySold = Integer.parseInt(quantityStr);
            double retailPrice = Double.parseDouble(retailPriceStr);
            double totalAmount = Double.parseDouble(totalAmountStr);

            ItemDetails details = itemDetailsMap.get(itemCode);
            if (quantitySold > details.stockLevel) {
                JOptionPane.showMessageDialog(this, "Quantity exceeds available stock (" + details.stockLevel + ").", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            SalesData updatedSales = new SalesData(salesId, itemCode, quantitySold, retailPrice, date, totalAmount);
            boolean success = salesDataController.updateSalesData(updatedSales);

            if (success) {
                JOptionPane.showMessageDialog(this, "Sales data with ID " + salesId + " updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                resetTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update sales data with ID " + salesId + ".", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid number format.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_saveBtnActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SM_Daily_Sales.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SM_Daily_Sales.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SM_Daily_Sales.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SM_Daily_Sales.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addBtn;
    private javax.swing.JButton deleteBtn;
    private javax.swing.JButton editBtn;
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox<String> jComboBox1;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JButton resetBtn;
    private javax.swing.JButton saveBtn;
    private javax.swing.JButton searchBtn;
    // End of variables declaration//GEN-END:variables
}
