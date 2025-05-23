/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package OSWB;

import Controllers.PurchaseOrderController;
import javax.swing.table.DefaultTableModel;
import Entities.User;
import Interface.ItemViewingServices;
import Interface.PurchaseRequisitionViewServices;
import Interface.SupplierViewingServices;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
/**
 *
 * @author User
 */
public class PM_Suppliers extends javax.swing.JFrame {
    private final DefaultTableModel model = new DefaultTableModel();
    private final String columnName[]= {"Supplier Code","Supplier Name","Contact Number","Address","Bank Account"};
    private final User currentUser;
    private final JFrame previousPage;
    private final ItemViewingServices itemViewer;
    private final PurchaseOrderController purchaseOrderController;  
    private final PurchaseRequisitionViewServices purchaseRequisitionViewer;
    private final SupplierViewingServices supplierViewer;

    /**
     * Creates new form PM_Suppliers
     * @param currentUser
     * @param previousPage
     * @param itemViewer
     * @param purchaseOrderController
     * @param supplierViewer
     * @param purchaseRequisitionViewer
     */
    public PM_Suppliers(User currentUser, JFrame previousPage, ItemViewingServices itemViewer, PurchaseOrderController purchaseOrderController, PurchaseRequisitionViewServices purchaseRequisitionViewer, SupplierViewingServices supplierViewer) {
        this.currentUser = currentUser;
        this.previousPage = previousPage;
        this.itemViewer = itemViewer;
        this.purchaseOrderController = purchaseOrderController;
        this.purchaseRequisitionViewer = purchaseRequisitionViewer;
        this.supplierViewer = supplierViewer;
        initComponents();
        setupTable();
        loadSuppliers();
        edit();
    }
    
    public void edit()
    {
        supplierCodeTxtField.setEditable(false);
        supplierNameTxtField.setEditable(false);
        contactNumberTxtField.setEditable(false);
        addressTxtField2.setEditable(false);
        bankAccountTxtField1.setEditable(false);
    }

    private void setupTable() {
        model.setColumnIdentifiers(columnName);
        supplierListTable.setModel(model);
    }
    
    private void loadSuppliers() {
        try {
            // Clear existing data from the table model
            model.setRowCount(0);

            List<String[]> items = supplierViewer.viewSuppliers(); // Assuming viewItems returns List<String[]>
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
        String searchTerm = searchTxtField.getText().trim();
        
        // If search field is empty, reload all data
        if (searchTerm.isEmpty()) {
            loadSuppliers();
            return;
        }
        
        try {
            // Clear current table data
            model.setRowCount(0);
            
            // Get all POs
            List<String[]> allSuppliers = supplierViewer.viewSuppliers();
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
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tittle2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        homeButton2 = new javax.swing.JButton();
        sideBarMenu2 = new javax.swing.JPanel();
        itemsListPageButton2 = new javax.swing.JButton();
        supplierPageButton2 = new javax.swing.JButton();
        purchaseOrderPageButton2 = new javax.swing.JButton();
        profilePageButton2 = new javax.swing.JButton();
        tittle3 = new javax.swing.JPanel();
        pageName = new javax.swing.JLabel();
        supplierListTablePanel = new javax.swing.JPanel();
        backButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        supplierListTable = new javax.swing.JTable();
        contactNumberLabel = new javax.swing.JLabel();
        supplierCodeTxtField = new javax.swing.JTextField();
        supplierNameTxtField = new javax.swing.JTextField();
        contactNumberTxtField = new javax.swing.JTextField();
        searchTxtField = new javax.swing.JTextField();
        searchButton = new javax.swing.JButton();
        supplierCodeLabel = new javax.swing.JLabel();
        supplierNameLabel = new javax.swing.JLabel();
        addressLabel1 = new javax.swing.JLabel();
        bankAccountLabel2 = new javax.swing.JLabel();
        bankAccountTxtField1 = new javax.swing.JTextField();
        addressTxtField2 = new javax.swing.JTextField();
        sideBarMenu5 = new javax.swing.JPanel();
        itemsListPageButton4 = new javax.swing.JButton();
        supplierPageButton4 = new javax.swing.JButton();
        purchaseOrderPageButton4 = new javax.swing.JButton();
        purchaseRequisitionPageButton = new javax.swing.JButton();

        tittle2.setBackground(new java.awt.Color(0, 102, 255));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 48)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Purchase Manager Page");

        homeButton2.setText("Home");
        homeButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                homeButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tittle2Layout = new javax.swing.GroupLayout(tittle2);
        tittle2.setLayout(tittle2Layout);
        tittle2Layout.setHorizontalGroup(
            tittle2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tittle2Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(homeButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 669, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(214, 214, 214))
        );
        tittle2Layout.setVerticalGroup(
            tittle2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tittle2Layout.createSequentialGroup()
                .addContainerGap(52, Short.MAX_VALUE)
                .addGroup(tittle2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tittle2Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tittle2Layout.createSequentialGroup()
                        .addComponent(homeButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(64, 64, 64))))
        );

        sideBarMenu2.setBackground(new java.awt.Color(51, 51, 51));

        itemsListPageButton2.setText("Items");
        itemsListPageButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemsListPageButton2ActionPerformed(evt);
            }
        });

        supplierPageButton2.setText("Supplier");
        supplierPageButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supplierPageButton2ActionPerformed(evt);
            }
        });

        purchaseOrderPageButton2.setText("Purchase Order");
        purchaseOrderPageButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                purchaseOrderPageButton2ActionPerformed(evt);
            }
        });

        profilePageButton2.setText("Profile");
        profilePageButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                profilePageButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout sideBarMenu2Layout = new javax.swing.GroupLayout(sideBarMenu2);
        sideBarMenu2.setLayout(sideBarMenu2Layout);
        sideBarMenu2Layout.setHorizontalGroup(
            sideBarMenu2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sideBarMenu2Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(sideBarMenu2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(supplierPageButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(purchaseOrderPageButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(itemsListPageButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(profilePageButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(37, Short.MAX_VALUE))
        );
        sideBarMenu2Layout.setVerticalGroup(
            sideBarMenu2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sideBarMenu2Layout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addComponent(profilePageButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(103, 103, 103)
                .addComponent(itemsListPageButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(99, 99, 99)
                .addComponent(supplierPageButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 105, Short.MAX_VALUE)
                .addComponent(purchaseOrderPageButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(68, 68, 68))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tittle3.setBackground(new java.awt.Color(0, 102, 255));

        pageName.setFont(new java.awt.Font("Segoe UI", 0, 48)); // NOI18N
        pageName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pageName.setText("Supplier List Page");

        javax.swing.GroupLayout tittle3Layout = new javax.swing.GroupLayout(tittle3);
        tittle3.setLayout(tittle3Layout);
        tittle3Layout.setHorizontalGroup(
            tittle3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tittle3Layout.createSequentialGroup()
                .addGap(217, 217, 217)
                .addComponent(pageName, javax.swing.GroupLayout.PREFERRED_SIZE, 669, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        tittle3Layout.setVerticalGroup(
            tittle3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tittle3Layout.createSequentialGroup()
                .addContainerGap(49, Short.MAX_VALUE)
                .addComponent(pageName, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45))
        );

        supplierListTablePanel.setBackground(new java.awt.Color(204, 204, 204));

        backButton.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        backButton.setText("<");
        backButton.setToolTipText("");
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });

        supplierListTable.setModel(new javax.swing.table.DefaultTableModel(
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
        supplierListTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                supplierListTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(supplierListTable);

        contactNumberLabel.setText("Contact Number :");

        supplierCodeTxtField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supplierCodeTxtFieldActionPerformed(evt);
            }
        });

        supplierNameTxtField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supplierNameTxtFieldActionPerformed(evt);
            }
        });

        contactNumberTxtField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                contactNumberTxtFieldActionPerformed(evt);
            }
        });

        searchButton.setText("Search");
        searchButton.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

        supplierCodeLabel.setText("Supplier Code :");

        supplierNameLabel.setText("Supplier Name :");

        addressLabel1.setText("Address :");

        bankAccountLabel2.setText("Bank Account :");

        bankAccountTxtField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bankAccountTxtField1ActionPerformed(evt);
            }
        });

        addressTxtField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addressTxtField2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout supplierListTablePanelLayout = new javax.swing.GroupLayout(supplierListTablePanel);
        supplierListTablePanel.setLayout(supplierListTablePanelLayout);
        supplierListTablePanelLayout.setHorizontalGroup(
            supplierListTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(supplierListTablePanelLayout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(supplierListTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(supplierListTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(supplierListTablePanelLayout.createSequentialGroup()
                            .addGap(223, 223, 223)
                            .addComponent(searchTxtField, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(searchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(supplierListTablePanelLayout.createSequentialGroup()
                            .addGroup(supplierListTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(supplierListTablePanelLayout.createSequentialGroup()
                                    .addComponent(supplierNameLabel)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(supplierNameTxtField, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(supplierListTablePanelLayout.createSequentialGroup()
                                    .addComponent(supplierCodeLabel)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(supplierCodeTxtField, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGap(18, 18, 18)
                            .addGroup(supplierListTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(supplierListTablePanelLayout.createSequentialGroup()
                                    .addComponent(contactNumberLabel)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(contactNumberTxtField, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE))
                                .addGroup(supplierListTablePanelLayout.createSequentialGroup()
                                    .addGroup(supplierListTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(addressLabel1)
                                        .addComponent(bankAccountLabel2))
                                    .addGap(21, 21, 21)
                                    .addGroup(supplierListTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(bankAccountTxtField1)
                                        .addComponent(addressTxtField2))))))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 759, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(55, 55, 55))
            .addGroup(supplierListTablePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(backButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        supplierListTablePanelLayout.setVerticalGroup(
            supplierListTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(supplierListTablePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(backButton, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(supplierListTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchTxtField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchButton))
                .addGap(32, 32, 32)
                .addGroup(supplierListTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(supplierCodeLabel)
                    .addComponent(contactNumberLabel)
                    .addComponent(supplierCodeTxtField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(contactNumberTxtField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(supplierListTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(supplierNameLabel)
                    .addComponent(supplierNameTxtField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addressLabel1)
                    .addComponent(addressTxtField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(supplierListTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bankAccountLabel2)
                    .addComponent(bankAccountTxtField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(50, Short.MAX_VALUE))
        );

        sideBarMenu5.setBackground(new java.awt.Color(51, 51, 51));

        itemsListPageButton4.setText("Items");
        itemsListPageButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemsListPageButton4ActionPerformed(evt);
            }
        });

        supplierPageButton4.setText("Supplier");
        supplierPageButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supplierPageButton4ActionPerformed(evt);
            }
        });

        purchaseOrderPageButton4.setText("Purchase Order");
        purchaseOrderPageButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                purchaseOrderPageButton4ActionPerformed(evt);
            }
        });

        purchaseRequisitionPageButton.setText("Purchase Requisition");
        purchaseRequisitionPageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                purchaseRequisitionPageButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout sideBarMenu5Layout = new javax.swing.GroupLayout(sideBarMenu5);
        sideBarMenu5.setLayout(sideBarMenu5Layout);
        sideBarMenu5Layout.setHorizontalGroup(
            sideBarMenu5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sideBarMenu5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(sideBarMenu5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(purchaseRequisitionPageButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(itemsListPageButton4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(supplierPageButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(purchaseOrderPageButton4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        sideBarMenu5Layout.setVerticalGroup(
            sideBarMenu5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sideBarMenu5Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(itemsListPageButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(110, 110, 110)
                .addComponent(supplierPageButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(purchaseRequisitionPageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(107, 107, 107)
                .addComponent(purchaseOrderPageButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(72, 72, 72))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(tittle3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(sideBarMenu5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(supplierListTablePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tittle3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(supplierListTablePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(sideBarMenu5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void homeButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_homeButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_homeButton2ActionPerformed

    private void itemsListPageButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemsListPageButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_itemsListPageButton2ActionPerformed

    private void supplierPageButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplierPageButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_supplierPageButton2ActionPerformed

    private void purchaseOrderPageButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_purchaseOrderPageButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_purchaseOrderPageButton2ActionPerformed

    private void profilePageButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_profilePageButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_profilePageButton2ActionPerformed

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

    private void supplierCodeTxtFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplierCodeTxtFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_supplierCodeTxtFieldActionPerformed

    private void supplierNameTxtFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplierNameTxtFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_supplierNameTxtFieldActionPerformed

    private void contactNumberTxtFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_contactNumberTxtFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_contactNumberTxtFieldActionPerformed

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
    searchSuppliers();        // TODO add your handling code here:
    }//GEN-LAST:event_searchButtonActionPerformed

    private void bankAccountTxtField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bankAccountTxtField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bankAccountTxtField1ActionPerformed

    private void addressTxtField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addressTxtField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addressTxtField2ActionPerformed

    private void supplierListTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_supplierListTableMouseClicked
        int selectedRow = supplierListTable.getSelectedRow();

    if (selectedRow >= 0) {
        // Get values from each column
        String supplierCode = supplierListTable.getValueAt(selectedRow, 0).toString();
        String supplierName = supplierListTable.getValueAt(selectedRow, 1).toString();
        String contactNumber = supplierListTable.getValueAt(selectedRow, 2).toString();
        String address = supplierListTable.getValueAt(selectedRow, 3).toString();
        String bankAccount = supplierListTable.getValueAt(selectedRow, 4).toString();

        // Set values to text fields
        supplierCodeTxtField.setText(supplierCode);
        supplierNameTxtField.setText(supplierName);
        contactNumberTxtField.setText(contactNumber);
        addressTxtField2.setText(address);
        bankAccountTxtField1.setText(bankAccount);
    }     // TODO add your handling code here:
    }//GEN-LAST:event_supplierListTableMouseClicked

    private void itemsListPageButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemsListPageButton4ActionPerformed
        new PM_List_items(currentUser,this, itemViewer, purchaseOrderController, purchaseRequisitionViewer, supplierViewer).setVisible(true);
        this.dispose();         // TODO add your handling code here:
    }//GEN-LAST:event_itemsListPageButton4ActionPerformed

    private void supplierPageButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplierPageButton4ActionPerformed
        new PM_Suppliers(currentUser,this, itemViewer, purchaseOrderController, purchaseRequisitionViewer, supplierViewer).setVisible(true);
        this.dispose(); // TODO add your handling code here:
    }//GEN-LAST:event_supplierPageButton4ActionPerformed

    private void purchaseOrderPageButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_purchaseOrderPageButton4ActionPerformed
        new PM_List_purchase_order(currentUser,this, itemViewer, purchaseOrderController, purchaseRequisitionViewer, supplierViewer).setVisible(true);
        this.dispose(); // TODO add your handling code here:
    }//GEN-LAST:event_purchaseOrderPageButton4ActionPerformed

    private void purchaseRequisitionPageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_purchaseRequisitionPageButtonActionPerformed
        new PM_List_requisition(currentUser,this, itemViewer, purchaseOrderController, purchaseRequisitionViewer, supplierViewer).setVisible(true);
        this.dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_purchaseRequisitionPageButtonActionPerformed

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
            java.util.logging.Logger.getLogger(PM_Suppliers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PM_Suppliers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PM_Suppliers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PM_Suppliers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                PurchaseManager supplier = new PurchaseManager("","","");
//                new PM_Suppliers(supplier,null).setVisible(true);
//            }
//        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel addressLabel1;
    private javax.swing.JTextField addressTxtField2;
    private javax.swing.JButton backButton;
    private javax.swing.JLabel bankAccountLabel2;
    private javax.swing.JTextField bankAccountTxtField1;
    private javax.swing.JLabel contactNumberLabel;
    private javax.swing.JTextField contactNumberTxtField;
    private javax.swing.JButton homeButton2;
    private javax.swing.JButton itemsListPageButton2;
    private javax.swing.JButton itemsListPageButton4;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel pageName;
    private javax.swing.JButton profilePageButton2;
    private javax.swing.JButton purchaseOrderPageButton2;
    private javax.swing.JButton purchaseOrderPageButton4;
    private javax.swing.JButton purchaseRequisitionPageButton;
    private javax.swing.JButton searchButton;
    private javax.swing.JTextField searchTxtField;
    private javax.swing.JPanel sideBarMenu2;
    private javax.swing.JPanel sideBarMenu5;
    private javax.swing.JLabel supplierCodeLabel;
    private javax.swing.JTextField supplierCodeTxtField;
    private javax.swing.JTable supplierListTable;
    private javax.swing.JPanel supplierListTablePanel;
    private javax.swing.JLabel supplierNameLabel;
    private javax.swing.JTextField supplierNameTxtField;
    private javax.swing.JButton supplierPageButton2;
    private javax.swing.JButton supplierPageButton4;
    private javax.swing.JPanel tittle2;
    private javax.swing.JPanel tittle3;
    // End of variables declaration//GEN-END:variables
}
