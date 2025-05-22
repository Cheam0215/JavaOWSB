/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package OSWB;

import Controllers.ItemController;
import Controllers.PurchaseOrderController;
import Entities.PurchaseManager;
import Entities.User;
import Interface.ItemViewingServices;
import Interface.PurchaseRequisitionViewServices;
import Interface.SupplierViewingServices;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.JFrame;

/**
 *
 * @author User
 */
public class PM_List_requisition extends javax.swing.JFrame {
    private final DefaultTableModel model = new DefaultTableModel();
    private final String columnName[]= {"Purchase Requisition ID","Item Code","Requested By","Quantity","Required Date","Requested Date","Status"};
    private final User currentUser;
    private final JFrame previousPage;
    private final ItemViewingServices itemViewer;
    private final PurchaseOrderController purchaseOrderController;  
    private final PurchaseRequisitionViewServices purchaseRequisitionViewer;
    private final SupplierViewingServices supplierViewer;

     /**
     * Creates new form PM_Items
     * @param currentUser
     * @param previousPage
     * @param itemViewer
     * @param purchaseOrderController
     * @param purchaseRequisitionViewer
     * @param supplierViewer
     
     */
    public PM_List_requisition(User currentUser, JFrame previousPage, ItemViewingServices itemViewer, PurchaseOrderController purchaseOrderController, PurchaseRequisitionViewServices purchaseRequisitionViewer, SupplierViewingServices supplierViewer) {
        this.currentUser = currentUser;
        this.previousPage = previousPage;
        this.purchaseOrderController = purchaseOrderController;
        this.itemViewer = itemViewer;
        this.purchaseRequisitionViewer = purchaseRequisitionViewer;
        this.supplierViewer = supplierViewer;
        initComponents();
        setupTable();
        loadPR();
        loadSuppliers();
        edit();
        makePOButton2.setEnabled(true);
        saveButton1.setEnabled(false);
        cancelButton2.setEnabled(false);
        supplierComboBox1.setEnabled(false);
    }
    
    public void edit(){
        purchaseRequisitionIDTxtField.setEditable(false);
        quantityTxtField.setEditable(false);
        itemCodeTxtField.setEditable(false);
        requiredDateTxtField.setEditable(false);
        statusTxtField1.setEditable(false);
        requestedDatetxtField.setEditable(false);
        requestedByTxtField.setEditable(false);
        
    }
    
    private void setupTable() {
        model.setColumnIdentifiers(columnName);
        requisitionTable.setModel(model);
    }
    private void loadSuppliers() {
        try {
            List<String[]> suppliers = supplierViewer.viewSuppliers();
            String[] supplierCodes = suppliers.stream().map(row -> row[0]).toArray(String[]::new);
            supplierComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(supplierCodes));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading suppliers: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadPR() {
        try {
            // Clear existing data from the table model
            model.setRowCount(0);

            List<String[]> PR = purchaseRequisitionViewer.viewPurchaseRequisition(); // Assuming viewItems returns List<String[]>
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
        String searchTerm = searchTxtField.getText().trim();
        
        // If search field is empty, reload all data
        if (searchTerm.isEmpty()) {
            loadPR();
            return;
        }
        
        try {
            // Clear current table data
            model.setRowCount(0);
            
            // Get all POs
            List<String[]> allPR = purchaseRequisitionViewer.viewPurchaseRequisition();
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        saveButton = new javax.swing.JButton();
        tittle4 = new javax.swing.JPanel();
        pageName = new javax.swing.JLabel();
        requisitionTablePanel = new javax.swing.JPanel();
        backButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        requisitionTable = new javax.swing.JTable();
        quantityLabel = new javax.swing.JLabel();
        requiredDateLabel = new javax.swing.JLabel();
        purchaseRequisitionIDTxtField = new javax.swing.JTextField();
        itemCodeTxtField = new javax.swing.JTextField();
        quantityTxtField = new javax.swing.JTextField();
        requiredDateTxtField = new javax.swing.JTextField();
        searchTxtField = new javax.swing.JTextField();
        searchButton = new javax.swing.JButton();
        purchaseRequisitionIDLabel = new javax.swing.JLabel();
        itemCodeLabel = new javax.swing.JLabel();
        supplierLabel1 = new javax.swing.JLabel();
        requestedDateLabel1 = new javax.swing.JLabel();
        requestedDatetxtField = new javax.swing.JTextField();
        statusLabel1 = new javax.swing.JLabel();
        statusTxtField1 = new javax.swing.JTextField();
        supplierComboBox1 = new javax.swing.JComboBox<>();
        requestedByLabel2 = new javax.swing.JLabel();
        requestedByTxtField = new javax.swing.JTextField();
        saveButton1 = new javax.swing.JButton();
        makePOButton2 = new javax.swing.JButton();
        cancelButton2 = new javax.swing.JButton();
        sideBarMenu3 = new javax.swing.JPanel();
        itemsListPageButton3 = new javax.swing.JButton();
        supplierPageButton3 = new javax.swing.JButton();
        purchaseOrderPageButton3 = new javax.swing.JButton();

        saveButton.setText("Save");
        saveButton.setToolTipText("");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        tittle4.setBackground(new java.awt.Color(0, 102, 255));
        tittle4.setPreferredSize(new java.awt.Dimension(931, 186));

        pageName.setFont(new java.awt.Font("Segoe UI", 0, 48)); // NOI18N
        pageName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pageName.setText("Requisition List Page");

        javax.swing.GroupLayout tittle4Layout = new javax.swing.GroupLayout(tittle4);
        tittle4.setLayout(tittle4Layout);
        tittle4Layout.setHorizontalGroup(
            tittle4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tittle4Layout.createSequentialGroup()
                .addGap(211, 211, 211)
                .addComponent(pageName, javax.swing.GroupLayout.PREFERRED_SIZE, 669, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(157, Short.MAX_VALUE))
        );
        tittle4Layout.setVerticalGroup(
            tittle4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tittle4Layout.createSequentialGroup()
                .addContainerGap(48, Short.MAX_VALUE)
                .addComponent(pageName, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46))
        );

        requisitionTablePanel.setBackground(new java.awt.Color(204, 204, 204));

        backButton.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        backButton.setText("<");
        backButton.setToolTipText("");
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });

        requisitionTable.setModel(new javax.swing.table.DefaultTableModel(
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
        requisitionTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                requisitionTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(requisitionTable);

        quantityLabel.setText("Quantity :");

        requiredDateLabel.setText("Required Date :");

        purchaseRequisitionIDTxtField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                purchaseRequisitionIDTxtFieldActionPerformed(evt);
            }
        });

        itemCodeTxtField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemCodeTxtFieldActionPerformed(evt);
            }
        });

        quantityTxtField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quantityTxtFieldActionPerformed(evt);
            }
        });

        requiredDateTxtField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                requiredDateTxtFieldActionPerformed(evt);
            }
        });

        searchButton.setText("Search");
        searchButton.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

        purchaseRequisitionIDLabel.setText("Purchase Requisition ID :");

        itemCodeLabel.setText("Item Code :");

        supplierLabel1.setText("Supplier :");

        requestedDateLabel1.setText("Requested Date :");

        requestedDatetxtField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                requestedDatetxtFieldActionPerformed(evt);
            }
        });

        statusLabel1.setText("Status :");

        statusTxtField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statusTxtField1ActionPerformed(evt);
            }
        });

        supplierComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        supplierComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supplierComboBox1ActionPerformed(evt);
            }
        });

        requestedByLabel2.setText("Requested by :");

        requestedByTxtField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                requestedByTxtFieldActionPerformed(evt);
            }
        });

        saveButton1.setText("Save");
        saveButton1.setToolTipText("");
        saveButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButton1ActionPerformed(evt);
            }
        });

        makePOButton2.setText("Make PO");
        makePOButton2.setToolTipText("");
        makePOButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                makePOButton2ActionPerformed(evt);
            }
        });

        cancelButton2.setText("Cancel");
        cancelButton2.setToolTipText("");
        cancelButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout requisitionTablePanelLayout = new javax.swing.GroupLayout(requisitionTablePanel);
        requisitionTablePanel.setLayout(requisitionTablePanelLayout);
        requisitionTablePanelLayout.setHorizontalGroup(
            requisitionTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, requisitionTablePanelLayout.createSequentialGroup()
                .addGroup(requisitionTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, requisitionTablePanelLayout.createSequentialGroup()
                        .addGap(259, 259, 259)
                        .addComponent(searchTxtField, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(searchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 130, Short.MAX_VALUE))
                    .addGroup(requisitionTablePanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(saveButton1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cancelButton2)
                .addGap(41, 41, 41))
            .addGroup(requisitionTablePanelLayout.createSequentialGroup()
                .addGroup(requisitionTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(requisitionTablePanelLayout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addGroup(requisitionTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 759, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(requisitionTablePanelLayout.createSequentialGroup()
                                .addGroup(requisitionTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(purchaseRequisitionIDLabel)
                                    .addComponent(statusLabel1)
                                    .addComponent(itemCodeLabel)
                                    .addComponent(supplierLabel1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(requisitionTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(requisitionTablePanelLayout.createSequentialGroup()
                                        .addComponent(supplierComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(18, 18, 18)
                                        .addComponent(requestedByLabel2)
                                        .addGap(18, 18, 18)
                                        .addComponent(requestedByTxtField, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(requisitionTablePanelLayout.createSequentialGroup()
                                        .addGroup(requisitionTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(statusTxtField1)
                                            .addComponent(itemCodeTxtField)
                                            .addComponent(purchaseRequisitionIDTxtField, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE))
                                        .addGap(18, 18, 18)
                                        .addGroup(requisitionTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(requestedDateLabel1)
                                            .addComponent(requiredDateLabel)
                                            .addComponent(quantityLabel))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(requisitionTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(quantityTxtField)
                                            .addComponent(requiredDateTxtField)
                                            .addComponent(requestedDatetxtField)))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, requisitionTablePanelLayout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(makePOButton2))))))
                    .addGroup(requisitionTablePanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(backButton)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        requisitionTablePanelLayout.setVerticalGroup(
            requisitionTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(requisitionTablePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(backButton, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addGroup(requisitionTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchTxtField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchButton)
                    .addComponent(makePOButton2))
                .addGap(18, 18, 18)
                .addGroup(requisitionTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(purchaseRequisitionIDLabel)
                    .addComponent(quantityLabel)
                    .addComponent(purchaseRequisitionIDTxtField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(quantityTxtField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(requisitionTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(itemCodeLabel)
                    .addComponent(requiredDateLabel)
                    .addComponent(itemCodeTxtField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(requiredDateTxtField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(requisitionTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(requestedDateLabel1)
                    .addComponent(requestedDatetxtField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(statusLabel1)
                    .addComponent(statusTxtField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(requisitionTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(supplierComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(requestedByLabel2)
                    .addComponent(requestedByTxtField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(supplierLabel1))
                .addGap(18, 18, 18)
                .addGroup(requisitionTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveButton1)
                    .addComponent(cancelButton2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        sideBarMenu3.setBackground(new java.awt.Color(51, 51, 51));

        itemsListPageButton3.setText("Items");
        itemsListPageButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemsListPageButton3ActionPerformed(evt);
            }
        });

        supplierPageButton3.setText("Supplier");
        supplierPageButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supplierPageButton3ActionPerformed(evt);
            }
        });

        purchaseOrderPageButton3.setText("Purchase Order");
        purchaseOrderPageButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                purchaseOrderPageButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout sideBarMenu3Layout = new javax.swing.GroupLayout(sideBarMenu3);
        sideBarMenu3.setLayout(sideBarMenu3Layout);
        sideBarMenu3Layout.setHorizontalGroup(
            sideBarMenu3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, sideBarMenu3Layout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addGroup(sideBarMenu3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(purchaseOrderPageButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(itemsListPageButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(supplierPageButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        sideBarMenu3Layout.setVerticalGroup(
            sideBarMenu3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sideBarMenu3Layout.createSequentialGroup()
                .addGap(92, 92, 92)
                .addComponent(itemsListPageButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 127, Short.MAX_VALUE)
                .addComponent(supplierPageButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(126, 126, 126)
                .addComponent(purchaseOrderPageButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(148, 148, 148))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tittle4, javax.swing.GroupLayout.DEFAULT_SIZE, 1041, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(sideBarMenu3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(requisitionTablePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tittle4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(requisitionTablePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(sideBarMenu3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        if (previousPage != null) {
            previousPage.setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, 
                "No previous page available.", 
                "Navigation Error", 
                JOptionPane.INFORMATION_MESSAGE);
        }        // TODO add your handling code here:
    }//GEN-LAST:event_backButtonActionPerformed

    private void itemsListPageButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemsListPageButton3ActionPerformed
        new PM_List_items(currentUser,this, itemViewer, purchaseOrderController, purchaseRequisitionViewer, supplierViewer).setVisible(true);
        this.dispose();         // TODO add your handling code here:
    }//GEN-LAST:event_itemsListPageButton3ActionPerformed

    private void supplierPageButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplierPageButton3ActionPerformed
        new PM_Suppliers(currentUser,this, itemViewer, purchaseOrderController, purchaseRequisitionViewer, supplierViewer).setVisible(true);
        this.dispose(); // TODO add your handling code here:
    }//GEN-LAST:event_supplierPageButton3ActionPerformed

    private void purchaseOrderPageButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_purchaseOrderPageButton3ActionPerformed
        new PM_List_purchase_order(currentUser,this, itemViewer, purchaseOrderController, purchaseRequisitionViewer, supplierViewer).setVisible(true);
        this.dispose(); // TODO add your handling code here:
    }//GEN-LAST:event_purchaseOrderPageButton3ActionPerformed

    private void purchaseRequisitionIDTxtFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_purchaseRequisitionIDTxtFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_purchaseRequisitionIDTxtFieldActionPerformed

    private void requestedDatetxtFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_requestedDatetxtFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_requestedDatetxtFieldActionPerformed

    private void statusTxtField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statusTxtField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_statusTxtField1ActionPerformed

    private void quantityTxtFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quantityTxtFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_quantityTxtFieldActionPerformed

    private void requiredDateTxtFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_requiredDateTxtFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_requiredDateTxtFieldActionPerformed

    private void itemCodeTxtFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemCodeTxtFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_itemCodeTxtFieldActionPerformed

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
    searchPR();    // TODO add your handling code here:
    }//GEN-LAST:event_searchButtonActionPerformed

    private void requisitionTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_requisitionTableMouseClicked
        int selectedRow = requisitionTable.getSelectedRow();

    if (selectedRow >= 0) {
        // Get values from each column
        String purchaseRequisitionID = requisitionTable.getValueAt(selectedRow, 0).toString();
        String itemCode = requisitionTable.getValueAt(selectedRow, 1).toString();
        String requestedBy = requisitionTable.getValueAt(selectedRow, 2).toString();
        String quantity = requisitionTable.getValueAt(selectedRow, 3).toString();
        String requiredDate = requisitionTable.getValueAt(selectedRow, 4).toString();
        String requestedDate = requisitionTable.getValueAt(selectedRow, 5).toString();
        String status = requisitionTable.getValueAt(selectedRow, 6).toString();

        // Set values to text fields
        purchaseRequisitionIDTxtField.setText(purchaseRequisitionID);
        itemCodeTxtField.setText(itemCode);
        requestedByTxtField.setText(requestedBy);
        quantityTxtField.setText(quantity);
        requiredDateTxtField.setText(requiredDate);
        requestedDatetxtField.setText(requestedDate);
        statusTxtField1.setText(status);
    }     // TODO add your handling code her        // TODO add your handling code here:
    }//GEN-LAST:event_requisitionTableMouseClicked

    private void requestedByTxtFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_requestedByTxtFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_requestedByTxtFieldActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_saveButtonActionPerformed

    private void makePOButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_makePOButton2ActionPerformed
        supplierComboBox1.setEnabled(true);
        saveButton1.setEnabled(true);
        cancelButton2.setEnabled(true);
        makePOButton2.setEnabled(false);
                // TODO add your handling code here:
    }//GEN-LAST:event_makePOButton2ActionPerformed

    private void supplierComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplierComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_supplierComboBox1ActionPerformed

    private void saveButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButton1ActionPerformed
        int selectedRow = requisitionTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a Purchase Requisition.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String prId = requisitionTable.getValueAt(selectedRow, 0).toString();
        String itemCode = requisitionTable.getValueAt(selectedRow, 1).toString();
        String quantity = requisitionTable.getValueAt(selectedRow, 3).toString();
        String requiredDate = requisitionTable.getValueAt(selectedRow, 4).toString();
        String status = requisitionTable.getValueAt(selectedRow, 6).toString();
        String supplierCode = (String) supplierComboBox1.getSelectedItem();
        if (supplierCode == null || supplierCode.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a supplier.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Show confirmation dialog
        String confirmationMessage = String.format(
                "Are you sure you want to generate a Purchase Order for:\n" +
                        "Purchase Requisition ID: %s\n" +
                        "Item Code: %s\n" +
                        "Quantity: %s\n" +
                        "Required Date: %s\n" +
                        "Current Status: %s\n" +
                        "Supplier Code: %s",
                prId, itemCode, quantity, requiredDate, status, supplierCode
        );
        int response = JOptionPane.showConfirmDialog(
                this,
                confirmationMessage,
                "Confirm Purchase Order Creation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (response != JOptionPane.YES_OPTION) {
            // User canceled, exit the method
            return;
        }

        // Proceed with generating the PurchaseOrder
        String result = purchaseOrderController.generatePurchaseOrder(prId, supplierCode, currentUser);
        if (result.startsWith("Error")) {
            JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, result, "Success", JOptionPane.INFORMATION_MESSAGE);
            loadPR(); // Refresh table to reflect updated PR status
        }     // TODO add your handling code here:
    }//GEN-LAST:event_saveButton1ActionPerformed

    private void cancelButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButton2ActionPerformed
        int selectedRow = requisitionTable.getSelectedRow();
        if (selectedRow >= 0) {
            // Restore text fields to the selected row's values
            purchaseRequisitionIDTxtField.setText(requisitionTable.getValueAt(selectedRow, 0).toString());
            itemCodeTxtField.setText(requisitionTable.getValueAt(selectedRow, 1).toString());
            requestedByTxtField.setText(requisitionTable.getValueAt(selectedRow, 2).toString());
            quantityTxtField.setText(requisitionTable.getValueAt(selectedRow, 3).toString());
            requiredDateTxtField.setText(requisitionTable.getValueAt(selectedRow, 4).toString());
            requestedDatetxtField.setText(requisitionTable.getValueAt(selectedRow, 5).toString());
            statusTxtField1.setText(requisitionTable.getValueAt(selectedRow, 6).toString());
            
        } else {
            // Clear all text fields if no row is selected
            purchaseRequisitionIDTxtField.setText("");
            itemCodeTxtField.setText("");
            requestedByTxtField.setText("");
            quantityTxtField.setText("");
            requiredDateTxtField.setText("");
            requestedDatetxtField.setText("");
            statusTxtField1.setText("");
        }

        // Disable all text fields
        edit();
        // Reset button states
        saveButton1.setEnabled(false);
        makePOButton2.setEnabled(true);
        supplierComboBox1.setEnabled(false);// TODO add your handling code here:
        cancelButton2.setEnabled(false);
    }//GEN-LAST:event_cancelButton2ActionPerformed

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
            java.util.logging.Logger.getLogger(PM_List_requisition.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PM_List_requisition.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PM_List_requisition.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PM_List_requisition.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                PurchaseManager pr = new PurchaseManager("", "", "");
//                new PM_List_requisition(pr,null).setVisible(true);
//            }
//        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backButton;
    private javax.swing.JButton cancelButton2;
    private javax.swing.JLabel itemCodeLabel;
    private javax.swing.JTextField itemCodeTxtField;
    private javax.swing.JButton itemsListPageButton3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton makePOButton2;
    private javax.swing.JLabel pageName;
    private javax.swing.JButton purchaseOrderPageButton3;
    private javax.swing.JLabel purchaseRequisitionIDLabel;
    private javax.swing.JTextField purchaseRequisitionIDTxtField;
    private javax.swing.JLabel quantityLabel;
    private javax.swing.JTextField quantityTxtField;
    private javax.swing.JLabel requestedByLabel2;
    private javax.swing.JTextField requestedByTxtField;
    private javax.swing.JLabel requestedDateLabel1;
    private javax.swing.JTextField requestedDatetxtField;
    private javax.swing.JLabel requiredDateLabel;
    private javax.swing.JTextField requiredDateTxtField;
    private javax.swing.JTable requisitionTable;
    private javax.swing.JPanel requisitionTablePanel;
    private javax.swing.JButton saveButton;
    private javax.swing.JButton saveButton1;
    private javax.swing.JButton searchButton;
    private javax.swing.JTextField searchTxtField;
    private javax.swing.JPanel sideBarMenu3;
    private javax.swing.JLabel statusLabel1;
    private javax.swing.JTextField statusTxtField1;
    private javax.swing.JComboBox<String> supplierComboBox1;
    private javax.swing.JLabel supplierLabel1;
    private javax.swing.JButton supplierPageButton3;
    private javax.swing.JPanel tittle4;
    // End of variables declaration//GEN-END:variables
}
