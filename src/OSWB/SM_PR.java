/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package OSWB;

import Controllers.PurchaseRequisitionController;
import Entities.PurchaseRequisition;
import Entities.SalesManager;
import Entities.User;
import Interface.PurchaseRequisitionServices;
import Utility.FileManager;
import Utility.Date;
import Utility.Status;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
/**
 *
 * @author Edwin Chen
 */
public class SM_PR extends javax.swing.JFrame {
    private final DefaultTableModel model = new DefaultTableModel();
    private final String columnName[]= {"Purchase Requisition ID","Item Code","Requested By","Quantity","Required Date","Requested Date","Status"};
    private FileManager fileManager;
    private boolean isEditing = false; // Track if we're in editing mode
    private String editingPrId = null; // Track the PR ID being edited
    private final User currentUser;
    private final PurchaseRequisitionServices purchaseRequisitionController;
    private final JFrame previousScreen;

    /**
     * Creates new form SM_PR
     * @param currentUser
     * @param purchaseRequisitionController
     * @param previousScreen
     */
    public SM_PR(User currentUser, PurchaseRequisitionController purchaseRequisitionController, JFrame previousScreen) {
        this.currentUser = currentUser;
        this.purchaseRequisitionController = purchaseRequisitionController;
        this.previousScreen = previousScreen;
        fileManager = new FileManager();
        initComponents();
        setupTable();
        loadPR();
        populateItemCodeComboBox(); 
        setDefaultValues(); 
        initializeFileManager();
        setupTableSelectionListener(); // Add listener to manage button states
        editBtn.setEnabled(false); // Disable Edit button initially
        saveBtn.setEnabled(false); // Disable Save button initially
        
    };
    
    private void setupTable() {
        model.setColumnIdentifiers(columnName);
        jTable1.setModel(model);
    }
    
    private void loadPR() {
        try {
            // Clear existing data from the table model
            model.setRowCount(0);

            List<String[]> PR = purchaseRequisitionController.viewPurchaseRequisition(); // Assuming viewItems returns List<String[]>
            if (PR.isEmpty()) {
                // Optional: Show a message if the overall item list is empty
                 JOptionPane.showMessageDialog(this, "There are no Purchase Requisition available.", "Load Purchase Requisition", JOptionPane.WARNING_MESSAGE);
            } else {
                for (String[] pr : PR) {
                    model.addRow(pr);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                                          "Error loading Purchase Requisition: " + e.getMessage(),
                                          "Load Purchase Requisition Error",
                                          JOptionPane.ERROR_MESSAGE);
            // Ensure table is empty if loading fails
            model.setRowCount(0);
        }
    }
     
    private void searchPR() {
        String searchTerm = jTextField1.getText().trim();
        
        // If search field is empty, reload all data
        if (searchTerm.isEmpty()) {
            loadPR();
            return;
        }
        
        try {
            // Clear current table data
            model.setRowCount(0);
            
            // Get all POs
            List<String[]> allPR = purchaseRequisitionController.viewPurchaseRequisition();
            boolean foundMatch = false;
            
            for (String[] pr : allPR) {
                if ((pr[0] != null && pr[0].toLowerCase().contains(searchTerm.toLowerCase())) ||
                    (pr[1] != null && pr[1].toLowerCase().contains(searchTerm.toLowerCase())) ||
                    (pr[4] != null && pr[4].toLowerCase().contains(searchTerm.toLowerCase())) ||
                    (pr[6] != null && pr[6].toLowerCase().contains(searchTerm.toLowerCase()))) {
                    model.addRow(pr);
                    foundMatch = true;
                }
            }
            
            // Show message if no results found
            if (!foundMatch) {
                JOptionPane.showMessageDialog(this, 
                    "No Purchase Requisiton found matching '" + searchTerm + "'", 
                    "Search Results", 
                    JOptionPane.INFORMATION_MESSAGE);
                // Reload all data after showing the message
                loadPR();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error searching Purchase Requistion: " + e.getMessage(), 
                "Search Error", 
                JOptionPane.ERROR_MESSAGE);
            loadPR();
        }
    }
    
    private void resetTable() {
        jTextField1.setText("");
        jLabel9.setText(generateNextPrID());
        jComboBox1.setSelectedIndex(-1);
        jSpinner1.setValue(0);
        jDateChooser1.setDate(new java.util.Date());
        isEditing = false;
        editingPrId = null;
        loadPR();
        addBtn.setEnabled(true);
        editBtn.setEnabled(jTable1.getSelectedRow() != -1);
        saveBtn.setEnabled(false);
        deleteBtn.setEnabled(jTable1.getSelectedRow() != -1);
    }
    
    private void populateItemCodeComboBox() {
        try {
            String filePath = new File("").getAbsolutePath() + File.separator + "src" + File.separator + fileManager.getItemFilePath().replace("/", File.separator);
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            jComboBox1.removeAllItems(); // Clear existing items
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length >= 1) {
                    jComboBox1.addItem(parts[0]); // Add item code to combo box
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading item codes: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setDefaultValues() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1); // Add 1 day to get tomorrow's date
        java.util.Date tomorrow = calendar.getTime();
        jLabel11.setText(currentUser.getUsername()); 
        jLabel12.setText("PENDING");
        Date today = Date.now();
        jLabel7.setText(today.toIsoString());
        jDateChooser1.setDate(tomorrow);
        jDateChooser1.setMinSelectableDate(tomorrow);
    }
    
    private String generateNextPrID() {
        String nextPrID = "PR001"; // Default if no PRs exist

        try {
            if (fileManager == null) {
                fileManager = new FileManager();
            }

            String filePath = fileManager.getPrFilePath();
            String absolutePath = new File("").getAbsolutePath() + File.separator + "src" + File.separator + filePath.replace("/", File.separator);
            File file = new File(absolutePath);

            if (!file.exists()) {
                return nextPrID;
            }

            List<String> lines = Files.readAllLines(Paths.get(absolutePath));
            int highestNumber = 0;

            for (String line : lines) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].startsWith("PR")) {
                    try {
                        String numPart = parts[0].substring(2);
                        int prNumber = Integer.parseInt(numPart);
                        if (prNumber > highestNumber) {
                            highestNumber = prNumber;
                        }
                    } catch (NumberFormatException e) {
                        // Skip invalid format
                    }
                }
            }

            int nextNumber = highestNumber + 1;
            nextPrID = String.format("PR%03d", nextNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return nextPrID;
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
    
    private void initializeFileManager() {
        if (fileManager == null) {
            fileManager = new FileManager();
        }
        String nextPrID = generateNextPrID();
        jLabel9.setText(nextPrID);
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel10 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        addBtn = new javax.swing.JButton();
        searchBtn = new javax.swing.JButton();
        editBtn = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        saveBtn = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        deleteBtn = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jSpinner1 = new javax.swing.JSpinner();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel12 = new javax.swing.JLabel();
        resetBtn = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

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

        jLabel10.setText("Status : ");

        jLabel8.setText("Requested Date : ");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(51, 153, 255));

        jLabel1.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 1, 36)); // NOI18N
        jLabel1.setText("Purchase Requisition");

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

        searchBtn.setText("Search");
        searchBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchBtnActionPerformed(evt);
            }
        });

        editBtn.setText("Edit");
        editBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editBtnActionPerformed(evt);
            }
        });

        jLabel2.setText(" Purchase Requisition ID :");

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

        jLabel4.setText("Requested By : ");

        jLabel5.setText("Quantity: ");

        jLabel6.setText("Required Date : ");

        jLabel9.setText("jLabel9");

        jLabel11.setText("jLabel11");

        jLabel12.setText("jLabel12");

        resetBtn.setText("Reset");
        resetBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetBtnActionPerformed(evt);
            }
        });

        jLabel7.setText("jLabel7");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(48, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6)
                                    .addComponent(addBtn)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(42, 42, 42)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(editBtn)
                                        .addGap(267, 267, 267))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(34, 34, 34)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                    .addComponent(jDateChooser1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                        .addGap(108, 108, 108)
                                                        .addComponent(saveBtn)))
                                                .addGap(55, 55, 55)
                                                .addComponent(deleteBtn))
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE)
                                                .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jSpinner1, javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jComboBox1, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))))
                        .addGap(39, 39, 39)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27)
                                .addComponent(searchBtn)
                                .addGap(18, 18, 18)
                                .addComponent(resetBtn))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 887, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(32, 32, 32))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addGap(2, 2, 2)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(addBtn)
                            .addComponent(editBtn)
                            .addComponent(saveBtn)
                            .addComponent(deleteBtn))
                        .addGap(50, 50, 50)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel9))
                                .addGap(40, 40, 40)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel3)
                                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(47, 47, 47)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel11))
                                .addGap(47, 47, 47)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel5)
                                    .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(45, 45, 45)
                                .addComponent(jLabel6))
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(51, 51, 51)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(jLabel7))
                        .addGap(54, 54, 54)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(jLabel12)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(searchBtn)
                            .addComponent(resetBtn))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 558, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 48, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void addBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBtnActionPerformed
        String prId = generateNextPrID();
        jLabel9.setText(prId);
        String itemCode = (String) jComboBox1.getSelectedItem();
        String requestedBy = jLabel11.getText();
        int quantity = (Integer) jSpinner1.getValue();
        java.util.Date requiredUtilDate = jDateChooser1.getDate();
        if (requiredUtilDate == null) {
            JOptionPane.showMessageDialog(this, "Please specify a required date.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(requiredUtilDate);
        Date requiredDate = new Date(cal.get(java.util.Calendar.YEAR), 
                                    cal.get(java.util.Calendar.MONTH) + 1, 
                                    cal.get(java.util.Calendar.DAY_OF_MONTH));
        String requiredDateStr = requiredDate.toIsoString();
        String requestedDate = jLabel7.getText();
        Status status = Status.valueOf(jLabel12.getText());

        // Validate input
        if (itemCode == null || itemCode.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select an item code.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (quantity <= 0) {
            JOptionPane.showMessageDialog(this, "Quantity must be greater than 0.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Add the purchase requisition
        PurchaseRequisition purchaseRequisition = new PurchaseRequisition(prId, itemCode, requestedBy, quantity, requiredDateStr, requestedDate, status);        purchaseRequisitionController.addPurchaseRequisition(purchaseRequisition);
        JOptionPane.showMessageDialog(this, "Purchase Requisition added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        // Clear the form
        jLabel9.setText("");
        jComboBox1.setSelectedIndex(0);
        jSpinner1.setValue(0);
        Date today = new Date(2025, 5, 3);
        jDateChooser1.setDate(new java.util.Date());
        
        loadPR();
        resetTable();
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

    private void resetBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetBtnActionPerformed
        resetTable();
    }//GEN-LAST:event_resetBtnActionPerformed

    private void searchBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchBtnActionPerformed
        searchPR();
    }//GEN-LAST:event_searchBtnActionPerformed

    private void deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtnActionPerformed
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a Purchase Requisition to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String prId = (String) model.getValueAt(selectedRow, 0); // PR ID is in the first column

        // Show confirmation dialog
        int response = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete Purchase Requisition '" + prId + "'?",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        // Proceed only if the user clicks "Yes"
        if (response == JOptionPane.YES_OPTION) {
            purchaseRequisitionController.deletePurchaseRequisition(prId);
            JOptionPane.showMessageDialog(this, "Purchase Requisition deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadPR(); // Refresh the table
            resetTable();
        }
    }//GEN-LAST:event_deleteBtnActionPerformed

    private void editBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editBtnActionPerformed
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a Purchase Requisition to edit.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Get PR details from the selected row
        editingPrId = (String) model.getValueAt(selectedRow, 0); // PR ID
        String itemCode = (String) model.getValueAt(selectedRow, 1);
        String requestedBy = (String) model.getValueAt(selectedRow, 2);
        String quantity = (String) model.getValueAt(selectedRow, 3);
        String requiredDateStr = (String) model.getValueAt(selectedRow, 4);
        String requestedDate = (String) model.getValueAt(selectedRow, 5);
        String status = (String) model.getValueAt(selectedRow, 6);

        // Populate the UI fields
        jLabel9.setText(editingPrId); // PR ID (read-only)
        jComboBox1.setSelectedItem(itemCode); // Item Code (editable)
        jLabel11.setText(requestedBy); // Requested By (read-only)
        jSpinner1.setValue(Integer.parseInt(quantity)); // Quantity (editable)
        try {
            java.util.Date requiredDate = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(requiredDateStr);
            jDateChooser1.setDate(requiredDate); // Required Date (editable)
        } catch (java.text.ParseException e) {
            JOptionPane.showMessageDialog(this, "Error parsing required date: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        jLabel7.setText(requestedDate); // Requested Date (read-only)
        jLabel12.setText(status); // Status (read-only)

        // Enter editing mode
        isEditing = true;
        editBtn.setEnabled(false);
        saveBtn.setEnabled(true);
        addBtn.setEnabled(false);
        deleteBtn.setEnabled(false);
    }//GEN-LAST:event_editBtnActionPerformed

    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnActionPerformed
        if (!isEditing || editingPrId == null) {
            JOptionPane.showMessageDialog(this, "No Purchase Requisition is being edited.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Get updated values from the UI
        String itemCode = (String) jComboBox1.getSelectedItem();
        int quantity = (Integer) jSpinner1.getValue();
        java.util.Date requiredUtilDate = jDateChooser1.getDate();

        // Validate input
        if (itemCode == null || itemCode.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select an item code.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (quantity <= 0) {
            JOptionPane.showMessageDialog(this, "Quantity must be greater than 0.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (requiredUtilDate == null) {
            JOptionPane.showMessageDialog(this, "Please specify a required date.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Convert required date to string format
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(requiredUtilDate);
        Date requiredDate = new Date(cal.get(java.util.Calendar.YEAR), 
                                    cal.get(java.util.Calendar.MONTH) + 1, 
                                    cal.get(java.util.Calendar.DAY_OF_MONTH));
        String requiredDateStr = requiredDate.toIsoString();

        // Retrieve original values for uneditable fields
        String requestedBy = jLabel11.getText();
        String requestedDate = jLabel7.getText();
        Status status = Status.valueOf(jLabel12.getText());

        // Create updated PurchaseRequisition object
        PurchaseRequisition updatedPR = new PurchaseRequisition(editingPrId, itemCode, requestedBy, quantity, requiredDateStr, requestedDate, status);

        // Update the purchase requisition
        if (purchaseRequisitionController.updatePurchaseRequisition(updatedPR)) {
            JOptionPane.showMessageDialog(this, "Purchase Requisition updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadPR(); // Refresh the table
            resetTable(); // Reset the form
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update Purchase Requisition. Check console for details.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_saveBtnActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

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
            java.util.logging.Logger.getLogger(SM_PR.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SM_PR.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SM_PR.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SM_PR.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
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
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JButton resetBtn;
    private javax.swing.JButton saveBtn;
    private javax.swing.JButton searchBtn;
    // End of variables declaration//GEN-END:variables
}
