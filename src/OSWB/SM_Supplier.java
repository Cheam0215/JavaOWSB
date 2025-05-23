/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package OSWB;

import Controllers.SupplierController;
import Entities.Supplier;
import Entities.User;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
/**
 *
 * @author Edwin Chen
 */
public class SM_Supplier extends javax.swing.JFrame {
    private final DefaultTableModel model = new DefaultTableModel();
    private final String columnName[]= {"Supplier Code","Supplier Name","Contact Number","Address","Bank Account"};
    private boolean isEditing = false; // Track if we're in editing mode
    private String editingSupplierCode = null; // Track the supplier code being edited
    private final User currentUser;
    private final SupplierController supplierController;
    private final JFrame previousScreen;
    
    /**
     * Creates new form SM_Supplier
     * @param currentUser
     * @param supplierController
     * @param previousScreen
     */
    public SM_Supplier(User currentUser, SupplierController supplierController, JFrame previousScreen) {
        this.currentUser = currentUser;
        this.supplierController = supplierController;
        this.previousScreen = previousScreen;
        initComponents();
        setupTable();
        loadSuppliers();
        setDefaultValues();
        setupTableSelectionListener();
        addbtn.setEnabled(true);
        editBtn.setEnabled(false); // Disable Edit button initially
        saveBtn.setEnabled(false); // Disable Save button initially
        deleteBtn.setEnabled(false); // Disable Delete button initially

        
    }
    
    private void setupTable() {
        model.setColumnIdentifiers(columnName);
        jTable1.setModel(model);
    }
    
    private void loadSuppliers() {
        try {
            // Clear existing data from the table model
            model.setRowCount(0);

            List<String[]> items = supplierController.viewSuppliers(); // Assuming viewItems returns List<String[]>
            if (items.isEmpty()) {
                // Optional: Show a message if the overall item list is empty
                 JOptionPane.showMessageDialog(this, "There are no suppliers available.", "Load Suppliers", JOptionPane.WARNING_MESSAGE);
            } else {
                for (String[] item : items) {
                    model.addRow(item);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                                          "Error loading suppliers: " + e.getMessage(),
                                          "Load Suppliers Error",
                                          JOptionPane.ERROR_MESSAGE);
            // Ensure table is empty if loading fails
            model.setRowCount(0);
        }
    }
    
    private void searchSuppliers() {
        String searchTerm = jTextField1.getText().trim();
        
        // If search field is empty, reload all data
        if (searchTerm.isEmpty()) {
            loadSuppliers();
            return;
        }
        
        try {
            // Clear current table data
            model.setRowCount(0);
            
            // Get all POs
            List<String[]> allSuppliers = supplierController.viewSuppliers();
            boolean foundMatch = false;
            
            for (String[] suppliers : allSuppliers) {
                if ((suppliers[0] != null && suppliers[0].toLowerCase().contains(searchTerm.toLowerCase())) ||
                    (suppliers[1] != null && suppliers[1].toLowerCase().contains(searchTerm.toLowerCase())) ||
                    (suppliers[2] != null && suppliers[2].toLowerCase().contains(searchTerm.toLowerCase()))) {
                    model.addRow(suppliers);
                    foundMatch = true;
                }
            }
            
            // Show message if no results found
            if (!foundMatch) {
                JOptionPane.showMessageDialog(this, 
                    "No Suppliers found matching '" + searchTerm + "'", 
                    "Search Results", 
                    JOptionPane.INFORMATION_MESSAGE);
                // Reload all data after showing the message
                loadSuppliers();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error searching Suppliers: " + e.getMessage(), 
                "Search Error", 
                JOptionPane.ERROR_MESSAGE);
            loadSuppliers();
        }
    }
    
    private void resetTable() {
        jTextField1.setText("");
        jLabel8.setText(generateNextSupplierCode());
        jTextField2.setText("");
        jTextField3.setText("");
        jTextField4.setText("");
        jTextField5.setText("");
        isEditing = false;
        editingSupplierCode = null;
        loadSuppliers();
        addbtn.setEnabled(true);
        editBtn.setEnabled(jTable1.getSelectedRow() != -1);
        saveBtn.setEnabled(false);
        deleteBtn.setEnabled(jTable1.getSelectedRow() != -1);
    }
    
    private void setDefaultValues() {
        jLabel8.setText(generateNextSupplierCode());
    }
    
    private String generateNextSupplierCode() {
        String nextSupplierCode = "SUP001"; // Default if no suppliers exist
        try {
            List<String[]> suppliers = supplierController.viewSuppliers();
            int highestNumber = 0;
            for (String[] supplier : suppliers) {
                if (supplier[0] != null && supplier[0].startsWith("SUP")) {
                    try {
                        String numPart = supplier[0].substring(3);
                        int supplierNumber = Integer.parseInt(numPart);
                        if (supplierNumber > highestNumber) {
                            highestNumber = supplierNumber;
                        }
                    } catch (NumberFormatException e) {
                    }
                }
            }
            int nextNumber = highestNumber + 1;
            nextSupplierCode = String.format("SUP%03d", nextNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nextSupplierCode;
    }

    private void setupTableSelectionListener() {
        jTable1.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                boolean rowSelected = jTable1.getSelectedRow() != -1;
                editBtn.setEnabled(rowSelected && !isEditing);
                saveBtn.setEnabled(isEditing);
                addbtn.setEnabled(!isEditing);
                deleteBtn.setEnabled(rowSelected && !isEditing);
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

        jLabel6 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        addbtn = new javax.swing.JButton();
        SearchBtn = new javax.swing.JButton();
        jTextField3 = new javax.swing.JTextField();
        editBtn = new javax.swing.JButton();
        saveBtn = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        deleteBtn = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jTextField2 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        resetBtn = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel6.setText("Contact Number :");

        addbtn.setText("Add");
        addbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addbtnActionPerformed(evt);
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

        jLabel3.setText("Supplier Code :");

        deleteBtn.setText("Delete");
        deleteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteBtnActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(51, 153, 255));

        jLabel1.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 1, 36)); // NOI18N
        jLabel1.setText("Suppliers");

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

        jLabel4.setText("Supplier Name : ");

        jLabel7.setText("Address :");

        jButton1.setText("Back");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
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

        resetBtn.setText("Reset");
        resetBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetBtnActionPerformed(evt);
            }
        });

        jLabel8.setText("jLabel8");

        jLabel2.setText("Bank Account :");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(addbtn)
                        .addGap(53, 53, 53)
                        .addComponent(editBtn)
                        .addGap(41, 41, 41)
                        .addComponent(saveBtn)
                        .addGap(39, 39, 39)
                        .addComponent(deleteBtn))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(34, 34, 34)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(34, 34, 34)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 646, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(SearchBtn)
                        .addGap(18, 18, 18)
                        .addComponent(resetBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1)))
                .addContainerGap(27, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addbtn)
                    .addComponent(editBtn)
                    .addComponent(saveBtn)
                    .addComponent(deleteBtn)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(SearchBtn)
                    .addComponent(resetBtn)
                    .addComponent(jButton1))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 558, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel8))
                        .addGap(47, 47, 47)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(45, 45, 45)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(40, 40, 40)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(42, 42, 42)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(46, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

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

    private void SearchBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SearchBtnActionPerformed
        searchSuppliers();
    }//GEN-LAST:event_SearchBtnActionPerformed

    private void resetBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetBtnActionPerformed
        resetTable();
    }//GEN-LAST:event_resetBtnActionPerformed

    private void addbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addbtnActionPerformed
        String supplierCode = jLabel8.getText();
        String supplierName = jTextField2.getText().trim();
        String contactNumberStr = jTextField3.getText().trim();
        String address = jTextField4.getText().trim();
        String bankAccountStr = jTextField5.getText().trim();

        if (supplierName.isEmpty() || contactNumberStr.isEmpty() || address.isEmpty() || bankAccountStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int contactNumber = Integer.parseInt(contactNumberStr);
            int bankAccount = Integer.parseInt(bankAccountStr);
            Supplier supplier = new Supplier(supplierCode, supplierName, contactNumber, address, bankAccount);
            supplierController.addSupplier(supplier);
            JOptionPane.showMessageDialog(this, "Supplier added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            resetTable();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Contact number and bank account must be a valid integer.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_addbtnActionPerformed

    private void editBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editBtnActionPerformed
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a supplier to edit.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        isEditing = true;
        editingSupplierCode = (String) model.getValueAt(selectedRow, 0); // Supplier Code
        jLabel8.setText(editingSupplierCode);
        jTextField2.setText((String) model.getValueAt(selectedRow, 1)); // Supplier Name
        jTextField3.setText((String) model.getValueAt(selectedRow, 2)); // Contact Number
        jTextField4.setText((String) model.getValueAt(selectedRow, 3)); // Address
        jTextField5.setText((String) model.getValueAt(selectedRow, 4)); // Bank Account

        isEditing = true;
        addbtn.setEnabled(false);
        editBtn.setEnabled(false);
        saveBtn.setEnabled(true);
        deleteBtn.setEnabled(false);
    }//GEN-LAST:event_editBtnActionPerformed

    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnActionPerformed
        if (!isEditing || editingSupplierCode == null) {
            JOptionPane.showMessageDialog(this, "No supplier is being edited.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String supplierCode = jLabel8.getText();
        String supplierName = jTextField2.getText().trim();
        String contactNumberStr = jTextField3.getText().trim();
        String address = jTextField4.getText().trim();
        String bankAccountStr = jTextField5.getText().trim();

        if (supplierName.isEmpty() || contactNumberStr.isEmpty() || address.isEmpty() || bankAccountStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int contactNumber = Integer.parseInt(contactNumberStr);
            int bankAccount = Integer.parseInt(bankAccountStr); 
            Supplier updatedSupplier = new Supplier(supplierCode, supplierName, contactNumber, address, bankAccount);
            if (supplierController.updateSupplier(updatedSupplier)) {
                JOptionPane.showMessageDialog(this, "Supplier updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                resetTable();
                isEditing = false; // Reset editing state
                editingSupplierCode = null; // Reset editing supplier code
                resetTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update supplier.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Contact number and bank account must be valid integers.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_saveBtnActionPerformed

    private void deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtnActionPerformed
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a supplier to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String supplierCode = (String) model.getValueAt(selectedRow, 0); // Supplier Code
        if (supplierController.deleteSupplier(supplierCode)) {
            JOptionPane.showMessageDialog(this, "Supplier deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            resetTable();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to delete supplier.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_deleteBtnActionPerformed

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
            java.util.logging.Logger.getLogger(SM_Supplier.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SM_Supplier.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SM_Supplier.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SM_Supplier.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton SearchBtn;
    private javax.swing.JButton addbtn;
    private javax.swing.JButton deleteBtn;
    private javax.swing.JButton editBtn;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JButton resetBtn;
    private javax.swing.JButton saveBtn;
    // End of variables declaration//GEN-END:variables
}
