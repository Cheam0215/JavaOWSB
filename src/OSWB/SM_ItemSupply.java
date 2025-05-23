/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package OSWB;


import Controllers.ItemController;
import Controllers.ItemSupplyController;
import Controllers.SupplierController;
import Entities.ItemSupply;
import Entities.User;
import Interface.ItemSupplyServices;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
/**
 *
 * @author Edwin Chen
 */
public class SM_ItemSupply extends javax.swing.JFrame {
    private final DefaultTableModel model = new DefaultTableModel();
    private final String columnName[] = {"Item Code", "Supplier Code", "Item Name", "Unit Price"};
    private boolean isEditing = false;
    private String editingItemCode = null;
    private final ItemSupplyServices itemSupplyController;
    private final User currentUser;
    private final ItemController itemController;
    private final SupplierController supplierController;
    private final JFrame previousScreen;
    
    /**
     * Creates new form SM_ItemSupply
     * @param currentUser
     * @param itemController
     * @param supplierController
     * @param itemSupplyController
     * @param previousScreen
     
     */
    public SM_ItemSupply(User currentUser, ItemController itemController, SupplierController supplierController, ItemSupplyController itemSupplyController, JFrame previousScreen) {
        this.currentUser = currentUser;
        this.itemController = itemController;
        this.supplierController = supplierController;
        this.itemSupplyController = itemSupplyController;
        this.previousScreen = previousScreen;
        initComponents();
        setupTable();
        loadItemSupplies();
        setupButtonListeners();
        loadItemCodes();
        loadSupplierCodes();
        
    }
    
    private void setupTable() {
        model.setColumnIdentifiers(columnName);
        jTable1.setModel(model);
    }

    private void setupButtonListeners() {
        jTable1.getSelectionModel().addListSelectionListener(e -> {
            boolean rowSelected = jTable1.getSelectedRow() != -1;
            deleteBtn.setEnabled(rowSelected); // Delete
            editBtn.setEnabled(rowSelected && !isEditing); // Edit
            saveBtn.setEnabled(isEditing); // Save
            addBtn.setEnabled(!isEditing); // Add
        });
        jComboBox1.addActionListener(e -> updateItemName());
    }
    
    private void updateItemName() {
        String itemCode = (String) jComboBox1.getSelectedItem();
        if (itemCode != null && !itemCode.equals("No items found")) {
            try {
                List<String[]> items = itemController.viewItems();
                for (String[] item : items) {
                    if (item[0].equals(itemCode)) {
                        jLabel6.setText(item[1]); // Set Item Name from viewItems
                        return;
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error loading item name: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        jLabel6.setText("");
    }

    private void loadItemSupplies() {
        try {
            model.setRowCount(0);
            List<String[]> itemSupplies = itemSupplyController.viewItemSupplies();
            if (itemSupplies.isEmpty()) {
                JOptionPane.showMessageDialog(this, "There are no item supplies available.", "Load Item Supplies", JOptionPane.WARNING_MESSAGE);
            } else {
                for (String[] itemSupply : itemSupplies) {
                    model.addRow(itemSupply);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading item supplies: " + e.getMessage(), "Load Item Supplies Error", JOptionPane.ERROR_MESSAGE);
            model.setRowCount(0);
        }
        jTable1.clearSelection();
        isEditing = false;
        editingItemCode = null;
        updateButtonStates();
    }

    private void loadItemCodes() {
        jComboBox1.removeAllItems();
        try {
            List<String[]> items = itemController.viewItems();
            for (String[] item : items) {
                jComboBox1.addItem(item[0]); // Item Code
            }
            if (jComboBox1.getItemCount() == 0) {
                jComboBox1.addItem("No items found");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading item codes: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            jComboBox1.addItem("No items found");
        }
        updateItemName();
    }

    private void loadSupplierCodes() {
        jComboBox2.removeAllItems();
        try {
            List<String[]> suppliers = supplierController.viewSuppliers();
            for (String[] supplier : suppliers) {
                jComboBox2.addItem(supplier[0]); // Supplier Code
            }
            if (jComboBox2.getItemCount() == 0) {
                jComboBox2.addItem("No suppliers found");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading supplier codes: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            jComboBox2.addItem("No suppliers found");
        }
    }
    
    private boolean isDuplicateItemSupply(String itemCode, String supplierCode) {
        try {
            List<String[]> itemSupplies = itemSupplyController.viewItemSupplies();
            for (String[] itemSupply : itemSupplies) {
                if (itemSupply[0].equals(itemCode) && itemSupply[1].equals(supplierCode)) {
                    return true; // Duplicate found
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error checking duplicates: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    private void searchItemSupplies() {
        String searchTerm = jTextField2.getText().trim();
        if (searchTerm.isEmpty()) {
            loadItemSupplies();
            return;
        }

        try {
            model.setRowCount(0);
            List<String[]> itemSupplies = itemSupplyController.viewItemSupplies();
            boolean foundMatch = false;
            for (String[] itemSupply : itemSupplies) {
                if (itemSupply[0].toLowerCase().contains(searchTerm.toLowerCase()) ||
                    itemSupply[1].toLowerCase().contains(searchTerm.toLowerCase()) ||
                    itemSupply[2].toLowerCase().contains(searchTerm.toLowerCase())) {
                    model.addRow(itemSupply);
                    foundMatch = true;
                }
            }
            if (!foundMatch) {
                JOptionPane.showMessageDialog(this, "No item supplies found matching '" + searchTerm + "'", "Search Results", JOptionPane.INFORMATION_MESSAGE);
                loadItemSupplies();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error searching item supplies: " + e.getMessage(), "Search Error", JOptionPane.ERROR_MESSAGE);
            loadItemSupplies();
        }
    }

    private void resetTable() {
        jTextField2.setText("");
        jComboBox1.setSelectedIndex(0);
        jComboBox2.setSelectedIndex(0);
        jLabel6.setText("");
        jTextField1.setText("");
        isEditing = false;
        editingItemCode = null;
        loadItemSupplies();
        updateButtonStates();
    }

    private void updateButtonStates() {
        addBtn.setEnabled(!isEditing);
        editBtn.setEnabled(jTable1.getSelectedRow() != -1 && !isEditing);
        saveBtn.setEnabled(isEditing);
        deleteBtn.setEnabled(jTable1.getSelectedRow() != -1);
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
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        addBtn = new javax.swing.JButton();
        editBtn = new javax.swing.JButton();
        saveBtn = new javax.swing.JButton();
        deleteBtn = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton5 = new javax.swing.JButton();
        jTextField2 = new javax.swing.JTextField();
        searchBtn = new javax.swing.JButton();
        resetBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(51, 153, 255));

        jLabel1.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 1, 36)); // NOI18N
        jLabel1.setText("Item Source");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jLabel2.setText("Item Code :");

        jLabel3.setText("Supplier Code :");

        jLabel4.setText("Item Name :");

        jLabel5.setText("Unit Price : ");

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

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel6.setText("jLabel6");

        jLabel7.setText("RM");

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

        jButton5.setText("Back");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        searchBtn.setText("Search");
        searchBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchBtnActionPerformed(evt);
            }
        });

        resetBtn.setText("Reset");
        resetBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(31, 31, 31)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(232, 232, 232))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(addBtn)
                                .addGap(32, 32, 32)
                                .addComponent(editBtn)
                                .addGap(42, 42, 42)
                                .addComponent(saveBtn)
                                .addGap(44, 44, 44)
                                .addComponent(deleteBtn))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(39, 39, 39)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(searchBtn)
                        .addGap(18, 18, 18)
                        .addComponent(resetBtn)
                        .addGap(239, 239, 239)
                        .addComponent(jButton5)
                        .addContainerGap(30, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 706, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addBtn)
                    .addComponent(saveBtn)
                    .addComponent(deleteBtn)
                    .addComponent(editBtn)
                    .addComponent(jButton5)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchBtn)
                    .addComponent(resetBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 469, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(50, 50, 50)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(51, 51, 51)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel6))
                        .addGap(50, 50, 50)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jLabel7)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(36, 36, 36))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        if (this.previousScreen != null) {
            this.previousScreen.setVisible(true); // Just make the existing one visible
        } else {
            // Fallback or error: Should not happen if previousScreen is always passed
            JOptionPane.showMessageDialog(this, "Error: Previous screen reference lost.", "Navigation Error", JOptionPane.ERROR_MESSAGE);
            // Optionally, recreate Login if truly lost
            // new Login().setVisible(true);
        }
        this.dispose();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void addBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBtnActionPerformed
        String itemCode = (String) jComboBox1.getSelectedItem();
        String supplierCode = (String) jComboBox2.getSelectedItem();
        String itemName = jLabel6.getText().trim();
        String unitPriceStr = jTextField1.getText().trim();

        if (itemCode == null || itemCode.equals("No items found") || supplierCode == null || supplierCode.equals("No suppliers found") ||
            itemName.isEmpty() || unitPriceStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields and select valid options.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (isDuplicateItemSupply(itemCode, supplierCode)) {
            JOptionPane.showMessageDialog(this, "This item code and supplier code combination already exists.", "Duplicate Error", JOptionPane.ERROR_MESSAGE);
            return;
        }


        try {
            double unitPrice = Double.parseDouble(unitPriceStr);
            ItemSupply itemSupply = new ItemSupply(itemCode, supplierCode, itemName, unitPrice);
            itemSupplyController.addItemSupply(itemSupply);
            JOptionPane.showMessageDialog(this, "Item supply added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            resetTable();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Unit price must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_addBtnActionPerformed

    private void editBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editBtnActionPerformed
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item supply to edit.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        isEditing = true;
        editingItemCode = (String) model.getValueAt(selectedRow, 0); // Item Code
        jComboBox1.setSelectedItem(editingItemCode);
        jComboBox2.setSelectedItem(model.getValueAt(selectedRow, 1)); // Supplier Code
        jLabel6.setText((String) model.getValueAt(selectedRow, 2)); // Item Name
        jTextField1.setText(model.getValueAt(selectedRow, 3).toString()); // Unit Price
        updateButtonStates();
    }//GEN-LAST:event_editBtnActionPerformed

    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnActionPerformed
        if (!isEditing || editingItemCode == null) {
            JOptionPane.showMessageDialog(this, "No item supply is being edited.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String itemCode = (String) jComboBox1.getSelectedItem();
        String supplierCode = (String) jComboBox2.getSelectedItem();
        String itemName = jLabel6.getText().trim();
        String unitPriceStr = jTextField1.getText().trim();

        if (itemCode == null || itemCode.equals("No items found") || supplierCode == null || supplierCode.equals("No suppliers found") ||
            itemName.isEmpty() || unitPriceStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields and select valid options.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate table row selection for duplicate check
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to verify the update.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String currentSupplierCode = (String) model.getValueAt(selectedRow, 1); // Assuming column 1 is supplierCode
        if (!supplierCode.equals(currentSupplierCode) && isDuplicateItemSupply(itemCode, supplierCode)) {
            JOptionPane.showMessageDialog(this, "This item code and supplier code combination already exists.", "Duplicate Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double unitPrice = Double.parseDouble(unitPriceStr);
            ItemSupply updatedItemSupply = new ItemSupply(itemCode, supplierCode, itemName, unitPrice);

            // Assuming updateItemSupply returns a string
            String result = itemSupplyController.updateItemSupply(updatedItemSupply);
            if (result.startsWith("Item supply ") && result.endsWith(" updated successfully.")) {
                JOptionPane.showMessageDialog(this, "Item supply updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                resetTable();
                isEditing = false; // Reset editing state
                editingItemCode = null; // Reset editing item code
                resetTable();
            } else {
                JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Unit price must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_saveBtnActionPerformed

    private void deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtnActionPerformed
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item supply to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String itemCode = (String) model.getValueAt(selectedRow, 0);
        int dialogResult = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete item supply " + itemCode + "?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (dialogResult != JOptionPane.YES_OPTION) {
            return;
        }

        String result = itemSupplyController.deleteItemSupply(itemCode); // Capture the string result
        if (result.startsWith("Item supply for item ") && result.endsWith(" deleted successfully.")) {
            JOptionPane.showMessageDialog(this, "Item supply deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadItemSupplies(); 
        } else {
            JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_deleteBtnActionPerformed

    private void searchBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchBtnActionPerformed
        searchItemSupplies();
    }//GEN-LAST:event_searchBtnActionPerformed

    private void resetBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetBtnActionPerformed
        resetTable();
    }//GEN-LAST:event_resetBtnActionPerformed

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
            java.util.logging.Logger.getLogger(SM_ItemSupply.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SM_ItemSupply.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SM_ItemSupply.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SM_ItemSupply.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
       
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addBtn;
    private javax.swing.JButton deleteBtn;
    private javax.swing.JButton editBtn;
    private javax.swing.JButton jButton5;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JButton resetBtn;
    private javax.swing.JButton saveBtn;
    private javax.swing.JButton searchBtn;
    // End of variables declaration//GEN-END:variables
}
