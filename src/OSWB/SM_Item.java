/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package OSWB;

import Entities.Item;
import Utility.FileManager;
import Entities.SalesManager;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import javax.swing.JOptionPane;
/**
 *
 * @author Edwin Chen
 */
public class SM_Item extends javax.swing.JFrame {
    private DefaultTableModel model = new DefaultTableModel();
    private String columnName[]= {"Item Code","Item Name","Supplier Code","Stock Level","Unit Price","Retail Price"};
    private SalesManager salesManager;
    private FileManager fileManager;
    private boolean isEditing = false; // Track if we're in editing mode
    private String editingItemCode = null; // Track the item code being edited
    
    public SM_Item() {
        salesManager = new SalesManager("SM001", "salesmanager", "password");
        fileManager = new FileManager();
        initComponents();
        initializeFileManager();
        setupTable();
        loadItems();
        setupDeleteButtonListener();
        setupTableSelectionListener(); // Add this to enable/disable Edit and Save buttons
        editBtn.setEnabled(false); // Disable Edit button initially
        saveBtn.setEnabled(false); // Disable Save button initially
    }
    
    private void setupTable() {
        model.setColumnIdentifiers(columnName);
        jTable1.setModel(model);
    }
    
    private void setupDeleteButtonListener() {
            jTable1.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            @Override
            public void valueChanged(javax.swing.event.ListSelectionEvent e) {
                deleteBtn.setEnabled(jTable1.getSelectedRow() != -1);
            }
        });
    }
    
    private void setupTableSelectionListener() {
        jTable1.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            @Override
            public void valueChanged(javax.swing.event.ListSelectionEvent e) {
                boolean rowSelected = jTable1.getSelectedRow() != -1;
                deleteBtn.setEnabled(rowSelected);
                editBtn.setEnabled(rowSelected && !isEditing); // Enable Edit only if not already editing
                saveBtn.setEnabled(isEditing); // Enable Save only when editing
                addButton.setEnabled(!isEditing); // Disable Add when editing
            }
        });
    }
    
    private void loadItems() {
        try {
            model.setRowCount(0);
            List<String[]> items = salesManager.viewItems();
            if (items.isEmpty()) {
                JOptionPane.showMessageDialog(this, "There are no items available.", "Load Items", JOptionPane.WARNING_MESSAGE);
            } else {
                for (String[] item : items) {
                    model.addRow(item);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading items: " + e.getMessage(), "Load Items Error", JOptionPane.ERROR_MESSAGE);
            model.setRowCount(0);
        }
        jTable1.clearSelection(); // Clear selection after loading
        isEditing = false; // Reset editing state
        editingItemCode = null;
        editBtn.setEnabled(false);
        saveBtn.setEnabled(false);
        addButton.setEnabled(true);
    }
     
    private void searchItems() {
        String searchTerm = jTextField1.getText().trim();
        
        // If search field is empty, reload all data
        if (searchTerm.isEmpty()) {
            loadItems();
            return;
        }
        
        try {
            // Clear current table data
            model.setRowCount(0);
            
            // Get all POs
            List<String[]> allItems = salesManager.viewItems();
            boolean foundMatch = false;
            
            for (String[] item : allItems) {
                if ((item[0] != null && item[0].toLowerCase().contains(searchTerm.toLowerCase())) ||
                    (item[1] != null && item[1].toLowerCase().contains(searchTerm.toLowerCase()))) {
                    model.addRow(item);
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
                loadItems();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error searching items: " + e.getMessage(), 
                "Search Error", 
                JOptionPane.ERROR_MESSAGE);
            loadItems();
        }
    }
    
    private void resetTable() {
        jTextField1.setText("");
        jTextField3.setText("");
        jTextField4.setText("");
        jTextField2.setText("");
        jComboBox1.setSelectedIndex(-1);
        jLabel10.setText(generateNextItemCode());
        jLabel11.setText("0");
        isEditing = false;
        editingItemCode = null;
        loadItems();
    }
    
    public boolean addItem(Item item) {
        // We should directly use the salesManager's addItem method which already has file handling implemented
        return salesManager.addItem(item);
    }

    // This method should be called in the constructor or in a separate init method
    private void initializeFileManager() {
        // Initialize fileManager
        if (fileManager == null) {
            fileManager = new FileManager();
        }

        // Load the next item code and display it in jLabel10
        String nextItemCode = generateNextItemCode();
        jLabel10.setText(nextItemCode);

        // Set default stock level to 0
        jLabel11.setText("0");

        // Initialize and populate supplier combobox
        loadSupplierCodes();
    }
    
    private void loadSupplierCodes() {
        try {
            jComboBox1.removeAllItems();

            if (fileManager == null) {
                fileManager = new FileManager();
            }

            // Use the FileManager's readFile method instead of directly accessing the file
            List<String> suppliers = fileManager.readFile(
                fileManager.getSupplierFilePath(), 
                line -> line  // Simple identity function to keep the lines as strings
            );

            if (suppliers.isEmpty()) {
                jComboBox1.addItem("No suppliers found");
                return;
            }

            for (String line : suppliers) {
                String[] parts = line.split(",");
                if (parts.length >= 1) {
                    jComboBox1.addItem(parts[0]);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading suppliers: " + e.getMessage(), 
                                         "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private String generateNextItemCode() {
        String nextItemCode = "ITEM001"; // Default if no items exist

        try {
            if (fileManager == null) {
                fileManager = new FileManager();
            }

            // Use the same path resolution as FileManager's writeToFile
            String filePath = fileManager.getItemFilePath();
            String absolutePath = new File("").getAbsolutePath() + File.separator + "src" + File.separator + filePath.replace("/", File.separator);
            File file = new File(absolutePath);

            if (!file.exists()) {
                return nextItemCode;
            }

            List<String> lines = Files.readAllLines(Paths.get(absolutePath));
            int highestNumber = 0;

            for (String line : lines) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].startsWith("ITEM")) {
                    try {
                        String numPart = parts[0].substring(4);
                        int itemNumber = Integer.parseInt(numPart);
                        if (itemNumber > highestNumber) {
                            highestNumber = itemNumber;
                        }
                    } catch (NumberFormatException e) {
                        // Skip invalid format
                    }
                }
            }

            int nextNumber = highestNumber + 1;
            nextItemCode = String.format("ITEM%03d", nextNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return nextItemCode;
    }

     
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel7 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        addButton = new javax.swing.JButton();
        SearchBtn = new javax.swing.JButton();
        editBtn = new javax.swing.JButton();
        saveBtn = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        deleteBtn = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        ResetBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel7.setText("Unit Price : ");

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

        jLabel8.setText("Retail Price : ");

        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(51, 153, 255));

        jLabel1.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 1, 36)); // NOI18N
        jLabel1.setText("Items");

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

        addButton.setText("Add");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        SearchBtn.setText("Search");
        SearchBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SearchBtnActionPerformed(evt);
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

        jLabel3.setText("Item Code :");

        deleteBtn.setText("Delete");
        deleteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteBtnActionPerformed(evt);
            }
        });

        jLabel4.setText("Item Name : ");

        jLabel5.setText("Supplier Code : ");

        jLabel6.setText("Stock Level :");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel2.setText("RM");

        jLabel9.setText("RM");

        jLabel10.setText("jLabel10");

        jLabel11.setText("jLabel11");

        ResetBtn.setText("Reset");
        ResetBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ResetBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addButton)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel8)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(34, 34, 34)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGap(33, 33, 33)
                                .addComponent(editBtn)))
                        .addGap(42, 42, 42)
                        .addComponent(saveBtn)
                        .addGap(39, 39, 39)
                        .addComponent(deleteBtn))
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 684, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(SearchBtn)
                                .addGap(18, 18, 18)
                                .addComponent(ResetBtn)
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
                    .addComponent(addButton)
                    .addComponent(editBtn)
                    .addComponent(saveBtn)
                    .addComponent(deleteBtn)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(SearchBtn)
                    .addComponent(ResetBtn))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 558, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(19, 19, 19))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel10))
                        .addGap(47, 47, 47)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(42, 42, 42)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(45, 45, 45)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel11))
                        .addGap(45, 45, 45)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(jLabel2)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(41, 41, 41)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        SM_Main smMain = new SM_Main();
        smMain.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        String itemName = jTextField3.getText().trim();
        String supplierCode = jComboBox1.getSelectedItem().toString();
        String unitPriceText = jTextField4.getText().trim();
        String retailPriceText = jTextField2.getText().trim();

        if (itemName.isEmpty() || unitPriceText.isEmpty() || retailPriceText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double unitPrice = Double.parseDouble(unitPriceText);
            double retailPrice = Double.parseDouble(retailPriceText);

            Item newItem = new Item(generateNextItemCode(), itemName, supplierCode, 0, unitPrice, retailPrice);

            // Debug: Log the file path
            String filePath = fileManager.getItemFilePath();
            String absolutePath = new File("").getAbsolutePath() + File.separator + "src" + File.separator + filePath.replace("/", File.separator);
            System.out.println("Attempting to write to: " + absolutePath);

            if (addItem(newItem)) {
                JOptionPane.showMessageDialog(this, "Item added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                jLabel10.setText(generateNextItemCode());
                jLabel11.setText("0");
                jTextField3.setText("");
                jTextField4.setText("");
                jTextField2.setText("");
                loadItems();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add item. Check console for details.", "Error", JOptionPane.ERROR_MESSAGE);
                System.out.println("Failed to add item. Item details: " + newItem.toString());
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid prices.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_addButtonActionPerformed

    private void SearchBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SearchBtnActionPerformed
        searchItems();
    }//GEN-LAST:event_SearchBtnActionPerformed

    private void ResetBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ResetBtnActionPerformed
        resetTable();
    }//GEN-LAST:event_ResetBtnActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtnActionPerformed
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Get the item code from the selected row (column 0)
        String itemCode = jTable1.getValueAt(selectedRow, 0).toString();

        // Confirm deletion
        int dialogResult = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete item " + itemCode + "?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (dialogResult != JOptionPane.YES_OPTION) {
            return;
        }

        // Delete the item using SalesManager
        if (salesManager.deleteItem(itemCode)) {
            JOptionPane.showMessageDialog(this, "Item deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadItems(); // Reload the table
            jLabel10.setText(generateNextItemCode()); // Update item code
            jLabel11.setText("0");
            jTextField3.setText("");
            jTextField4.setText("");
            jTextField2.setText("");
            
        } else {
            JOptionPane.showMessageDialog(this, "Failed to delete item " + itemCode + ". Check console for details.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_deleteBtnActionPerformed

    private void editBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editBtnActionPerformed
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item to edit.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Get item details from the selected row
        editingItemCode = jTable1.getValueAt(selectedRow, 0).toString();
        String itemName = jTable1.getValueAt(selectedRow, 1).toString();
        String supplierCode = jTable1.getValueAt(selectedRow, 2).toString();
        String stockLevel = jTable1.getValueAt(selectedRow, 3).toString();
        String unitPrice = jTable1.getValueAt(selectedRow, 4).toString();
        String retailPrice = jTable1.getValueAt(selectedRow, 5).toString();

        // Populate the UI fields
        jLabel10.setText(editingItemCode);
        jLabel11.setText(stockLevel);
        jTextField3.setText(itemName);
        jComboBox1.setSelectedItem(supplierCode);
        jTextField4.setText(unitPrice);
        jTextField2.setText(retailPrice);

        // Enter editing mode
        isEditing = true;
        editBtn.setEnabled(false);
        saveBtn.setEnabled(true);
        addButton.setEnabled(false);
    }//GEN-LAST:event_editBtnActionPerformed

    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnActionPerformed
        if (!isEditing || editingItemCode == null) {
            JOptionPane.showMessageDialog(this, "No item is being edited.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Get updated values from the UI
        String itemName = jTextField3.getText().trim();
        String supplierCode = jComboBox1.getSelectedItem().toString();
        String unitPriceText = jTextField4.getText().trim();
        String retailPriceText = jTextField2.getText().trim();

        // Validate input
        if (itemName.isEmpty() || unitPriceText.isEmpty() || retailPriceText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double unitPrice = Double.parseDouble(unitPriceText);
            double retailPrice = Double.parseDouble(retailPriceText);
            int stockLevel = Integer.parseInt(jLabel11.getText());

            // Create updated Item object
            Item updatedItem = new Item(editingItemCode, itemName, supplierCode, stockLevel, unitPrice, retailPrice);

            // Update the item using SalesManager
            if (salesManager.updateItem(updatedItem)) {
                JOptionPane.showMessageDialog(this, "Item updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadItems(); // Reload the table
                // Reset fields
                jLabel10.setText(generateNextItemCode());
                jLabel11.setText("0");
                jTextField3.setText("");
                jTextField4.setText("");
                jTextField2.setText("");
                jComboBox1.setSelectedIndex(-1); // Clear selection
                isEditing = false;
                editingItemCode = null;
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update item. Check console for details.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid prices.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_saveBtnActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SM_Item.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SM_Item().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ResetBtn;
    private javax.swing.JButton SearchBtn;
    private javax.swing.JButton addButton;
    private javax.swing.JButton deleteBtn;
    private javax.swing.JButton editBtn;
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JButton saveBtn;
    // End of variables declaration//GEN-END:variables
}
