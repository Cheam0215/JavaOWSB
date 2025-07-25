/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package OSWB;

import Controllers.ItemSupplyController;
import Controllers.PurchaseOrderController;
import Entities.PurchaseManager;
import Entities.User;
import Interface.ItemViewingServices;
import Interface.PurchaseRequisitionViewServices;
import Interface.SupplierViewingServices;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import Utility.Status;
import javax.swing.JFrame;

/**
 *
 * @author User
 */
public class PM_List_purchase_order extends javax.swing.JFrame {
    private final DefaultTableModel model = new DefaultTableModel();
    private final String columnName[]= {"Purchase Order ID","Purchase Requisition ID","Raised By","Item Code","Quantity","Supplier Code","Required Date","Requested Date","Status","Payment Amount","Remark"};
    private ItemViewingServices itemViewer;
    private PurchaseOrderController purchaseOrderController;  
    private PurchaseRequisitionViewServices purchaseRequisitionViewer;
    private ItemSupplyController itemSupplyController;
    private SupplierViewingServices supplierViewer;
    private User currentUser;
    private JFrame previousPage;

    /**
     * Creates new form PM_List_purchase_order
     * @param currentUser
     * @param previousPage
     * @param itemViewer
     * @param purchaseOrderController
     * @param purchaseRequisitionViewer
     * @param supplierViewer
     * @param itemSupplyController
     
     */
    public PM_List_purchase_order(User currentUser, JFrame previousPage, ItemViewingServices itemViewer, PurchaseOrderController purchaseOrderController, PurchaseRequisitionViewServices purchaseRequisitionViewer, SupplierViewingServices supplierViewer, ItemSupplyController itemSupplyController) {
        this.currentUser = currentUser;
        this.previousPage = previousPage;
        this.itemViewer = itemViewer;
        this.purchaseOrderController = purchaseOrderController;
        this.purchaseRequisitionViewer = purchaseRequisitionViewer;
        this.itemSupplyController = itemSupplyController;
        this.supplierViewer = supplierViewer;
        initComponents();
        setupTable();
        loadPO();
        edit();
        saveButton.setEnabled(false);
        cancelButton.setEnabled(false);
    }
    
    public PM_List_purchase_order(User currentUser, ItemViewingServices itemViewer, PurchaseOrderController purchaseOrderController, PurchaseRequisitionViewServices purchaseRequisitionViewer, SupplierViewingServices supplierViewer, ItemSupplyController itemSupplyController) {
        this.currentUser = currentUser;
        this.itemViewer = itemViewer;
        this.purchaseOrderController = purchaseOrderController;
        this.purchaseRequisitionViewer = purchaseRequisitionViewer;
        this.itemSupplyController = itemSupplyController;
        this.supplierViewer = supplierViewer;
        initComponents();
        setupTable();
        loadPO();
        edit();
        saveButton.setEnabled(false);
        cancelButton.setEnabled(false);
    }
    
    public void edit()
    {
        quantityTxtField1.setEditable(false);
        purchaseOrderTxtField1.setEditable(false);
        purchaseRequisitionIDTxtField1.setEditable(false);
        itemCodeTxtField1.setEditable(false);
        supplierIDTxtField2.setEditable(false);
        raisedByTxtField2.setEditable(false);
        statusTxtField2.setEditable(false);
        requestedDateTxtField3.setEditable(false);
        paymentAmountTxtField3.setEditable(false);
        requiredDateTxtField4.setEditable(false);
        remarksTxtField5.setEditable(false);
    }
    private void setupTable() {
        model.setColumnIdentifiers(columnName);
        purchaseOrderTable.setModel(model);
    }

    private void loadPO() {
        try {
            model.setRowCount(0);
            List<String[]> PO = purchaseOrderController.viewPurchaseOrder();
            if (PO.isEmpty()) {
                JOptionPane.showMessageDialog(this, "There are no Purchase Order yet.", "View Purchase Order", JOptionPane.WARNING_MESSAGE);
            } else {
                for (String[] po : PO) {
                    model.addRow(po);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading Purchase Orders: " + e.getMessage(), "Load Purchase Order Error", JOptionPane.ERROR_MESSAGE);
            model.setRowCount(0);
        }
    }
    
    private void searchPurchaseOrders() {
        String searchTerm = searchTxtField1.getText().trim();
        
        // If search field is empty, reload all data
        if (searchTerm.isEmpty()) {
            loadPO();
            return;
        }
        
        try {
            // Clear current table data
            model.setRowCount(0);
            
            // Get all POs
            List<String[]> allPOs = purchaseOrderController.viewPurchaseOrder();
            boolean foundMatch = false;
            
            for (String[] po : allPOs) {
                // Check if PO ID (index 0) or PR ID (index 1) contains the search term
                if ((po[0] != null && po[0].toLowerCase().contains(searchTerm.toLowerCase())) ||
                    (po[1] != null && po[1].toLowerCase().contains(searchTerm.toLowerCase()))) {
                    model.addRow(po);
                    foundMatch = true;
                }
            }
            
            // Show message if no results found
            if (!foundMatch) {
                JOptionPane.showMessageDialog(this, 
                    "No Purchase Orders found matching '" + searchTerm + "'", 
                    "Search Results", 
                    JOptionPane.INFORMATION_MESSAGE);
                // Reload all data after showing the message
                loadPO();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error searching Purchase Orders: " + e.getMessage(), 
                "Search Error", 
                JOptionPane.ERROR_MESSAGE);
            loadPO();
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

        tittle3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        homeButton3 = new javax.swing.JButton();
        sideBarMenu3 = new javax.swing.JPanel();
        itemsListPageButton3 = new javax.swing.JButton();
        supplierPageButton3 = new javax.swing.JButton();
        purchaseOrderPageButton3 = new javax.swing.JButton();
        profilePageButton3 = new javax.swing.JButton();
        saveButton2 = new javax.swing.JButton();
        itemNameLabel = new javax.swing.JLabel();
        stockLevelLabel = new javax.swing.JLabel();
        itemCodeTxtField = new javax.swing.JTextField();
        supplierIDTxtField = new javax.swing.JTextField();
        itemNameTxtField = new javax.swing.JTextField();
        stockLevelTxtField = new javax.swing.JTextField();
        searchTxtField = new javax.swing.JTextField();
        searchButton = new javax.swing.JButton();
        itemCodeLabel = new javax.swing.JLabel();
        supplierIDLabel = new javax.swing.JLabel();
        backButton = new javax.swing.JButton();
        tittle4 = new javax.swing.JPanel();
        pageName = new javax.swing.JLabel();
        logoutButton = new javax.swing.JButton();
        purchaseOrderListTablePanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        purchaseOrderTable = new javax.swing.JTable();
        editButton = new javax.swing.JButton();
        itemCodeLabel1 = new javax.swing.JLabel();
        quantityLabel = new javax.swing.JLabel();
        purchaseOrderTxtField1 = new javax.swing.JTextField();
        purchaseRequisitionIDTxtField1 = new javax.swing.JTextField();
        itemCodeTxtField1 = new javax.swing.JTextField();
        quantityTxtField1 = new javax.swing.JTextField();
        searchTxtField1 = new javax.swing.JTextField();
        searchButton1 = new javax.swing.JButton();
        purchaseOrderLabel = new javax.swing.JLabel();
        purchaseRequisitionIDLabel = new javax.swing.JLabel();
        supplierIDLabel2 = new javax.swing.JLabel();
        statusLabel = new javax.swing.JLabel();
        raisedbyLabel = new javax.swing.JLabel();
        supplierIDTxtField2 = new javax.swing.JTextField();
        raisedByTxtField2 = new javax.swing.JTextField();
        saveButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        statusTxtField2 = new javax.swing.JTextField();
        requestedDateLabel1 = new javax.swing.JLabel();
        requestedDateTxtField3 = new javax.swing.JTextField();
        paymentAmountLabel1 = new javax.swing.JLabel();
        paymentAmountTxtField3 = new javax.swing.JTextField();
        requiredDateLabel2 = new javax.swing.JLabel();
        requiredDateTxtField4 = new javax.swing.JTextField();
        remarksLabel3 = new javax.swing.JLabel();
        remarksTxtField5 = new javax.swing.JTextField();
        cancelButton = new javax.swing.JButton();
        backButton1 = new javax.swing.JButton();
        sideBarMenu5 = new javax.swing.JPanel();
        itemsListPageButton4 = new javax.swing.JButton();
        supplierPageButton4 = new javax.swing.JButton();
        purchaseOrderPageButton4 = new javax.swing.JButton();
        purchaseRequisitionPageButton = new javax.swing.JButton();

        tittle3.setBackground(new java.awt.Color(0, 102, 255));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 48)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Purchase Order Page");

        homeButton3.setText("Home");
        homeButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                homeButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tittle3Layout = new javax.swing.GroupLayout(tittle3);
        tittle3.setLayout(tittle3Layout);
        tittle3Layout.setHorizontalGroup(
            tittle3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tittle3Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(homeButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 669, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(214, 214, 214))
        );
        tittle3Layout.setVerticalGroup(
            tittle3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tittle3Layout.createSequentialGroup()
                .addContainerGap(52, Short.MAX_VALUE)
                .addGroup(tittle3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tittle3Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tittle3Layout.createSequentialGroup()
                        .addComponent(homeButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(64, 64, 64))))
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

        profilePageButton3.setText("Profile");
        profilePageButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                profilePageButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout sideBarMenu3Layout = new javax.swing.GroupLayout(sideBarMenu3);
        sideBarMenu3.setLayout(sideBarMenu3Layout);
        sideBarMenu3Layout.setHorizontalGroup(
            sideBarMenu3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sideBarMenu3Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(sideBarMenu3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(supplierPageButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(purchaseOrderPageButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(itemsListPageButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(profilePageButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(37, Short.MAX_VALUE))
        );
        sideBarMenu3Layout.setVerticalGroup(
            sideBarMenu3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sideBarMenu3Layout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addComponent(profilePageButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(103, 103, 103)
                .addComponent(itemsListPageButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(99, 99, 99)
                .addComponent(supplierPageButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 105, Short.MAX_VALUE)
                .addComponent(purchaseOrderPageButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(68, 68, 68))
        );

        saveButton2.setText("Save");
        saveButton2.setToolTipText("");

        itemNameLabel.setText("Item Name :");

        stockLevelLabel.setText("Stock Level :");

        itemCodeTxtField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemCodeTxtFieldActionPerformed(evt);
            }
        });

        supplierIDTxtField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supplierIDTxtFieldActionPerformed(evt);
            }
        });

        itemNameTxtField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemNameTxtFieldActionPerformed(evt);
            }
        });

        stockLevelTxtField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stockLevelTxtFieldActionPerformed(evt);
            }
        });

        searchButton.setText("Search");
        searchButton.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        itemCodeLabel.setText("Item Code :");

        supplierIDLabel.setText("Supplier ID :");

        backButton.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        backButton.setText("<");
        backButton.setToolTipText("");
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tittle4.setBackground(new java.awt.Color(0, 102, 255));
        tittle4.setPreferredSize(new java.awt.Dimension(931, 186));

        pageName.setFont(new java.awt.Font("Segoe UI", 0, 48)); // NOI18N
        pageName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pageName.setText("Purchase Order Page");

        logoutButton.setText("Log out");
        logoutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tittle4Layout = new javax.swing.GroupLayout(tittle4);
        tittle4.setLayout(tittle4Layout);
        tittle4Layout.setHorizontalGroup(
            tittle4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tittle4Layout.createSequentialGroup()
                .addContainerGap(300, Short.MAX_VALUE)
                .addGroup(tittle4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tittle4Layout.createSequentialGroup()
                        .addComponent(logoutButton)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tittle4Layout.createSequentialGroup()
                        .addComponent(pageName, javax.swing.GroupLayout.PREFERRED_SIZE, 669, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(266, 266, 266))))
        );
        tittle4Layout.setVerticalGroup(
            tittle4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tittle4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(logoutButton)
                .addGap(29, 29, 29)
                .addComponent(pageName, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(36, Short.MAX_VALUE))
        );

        purchaseOrderListTablePanel.setBackground(new java.awt.Color(204, 204, 204));

        purchaseOrderTable.setModel(new javax.swing.table.DefaultTableModel(
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
        purchaseOrderTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                purchaseOrderTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(purchaseOrderTable);

        editButton.setText("Edit");
        editButton.setToolTipText("");
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButtonActionPerformed(evt);
            }
        });

        itemCodeLabel1.setText("Item Code :");

        quantityLabel.setText("Quantity :");

        purchaseOrderTxtField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                purchaseOrderTxtField1ActionPerformed(evt);
            }
        });

        purchaseRequisitionIDTxtField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                purchaseRequisitionIDTxtField1ActionPerformed(evt);
            }
        });

        itemCodeTxtField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemCodeTxtField1ActionPerformed(evt);
            }
        });

        quantityTxtField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quantityTxtField1ActionPerformed(evt);
            }
        });

        searchTxtField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchTxtField1ActionPerformed(evt);
            }
        });

        searchButton1.setText("Search");
        searchButton1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        searchButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButton1ActionPerformed(evt);
            }
        });

        purchaseOrderLabel.setText("Purchase Order ID :");

        purchaseRequisitionIDLabel.setText("Purchase Requisition ID :");

        supplierIDLabel2.setText("Supplier Code :");

        statusLabel.setText("Status :");

        raisedbyLabel.setText("Raised by :");

        supplierIDTxtField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supplierIDTxtField2ActionPerformed(evt);
            }
        });

        raisedByTxtField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                raisedByTxtField2ActionPerformed(evt);
            }
        });

        saveButton.setText("Save");
        saveButton.setToolTipText("");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        deleteButton.setText("Delete");
        deleteButton.setToolTipText("");
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        statusTxtField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statusTxtField2ActionPerformed(evt);
            }
        });

        requestedDateLabel1.setText("Requested Date :");

        requestedDateTxtField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                requestedDateTxtField3ActionPerformed(evt);
            }
        });

        paymentAmountLabel1.setText("Payment Amount :");

        paymentAmountTxtField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                paymentAmountTxtField3ActionPerformed(evt);
            }
        });

        requiredDateLabel2.setText("Required Date :");

        requiredDateTxtField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                requiredDateTxtField4ActionPerformed(evt);
            }
        });

        remarksLabel3.setText("Remarks :");

        remarksTxtField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                remarksTxtField5ActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.setToolTipText("");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        backButton1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        backButton1.setText("<");
        backButton1.setToolTipText("");
        backButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout purchaseOrderListTablePanelLayout = new javax.swing.GroupLayout(purchaseOrderListTablePanel);
        purchaseOrderListTablePanel.setLayout(purchaseOrderListTablePanelLayout);
        purchaseOrderListTablePanelLayout.setHorizontalGroup(
            purchaseOrderListTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(purchaseOrderListTablePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(purchaseOrderListTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(purchaseOrderListTablePanelLayout.createSequentialGroup()
                        .addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(editButton, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(purchaseOrderListTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(purchaseOrderListTablePanelLayout.createSequentialGroup()
                                .addGroup(purchaseOrderListTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(purchaseOrderListTablePanelLayout.createSequentialGroup()
                                        .addGap(451, 451, 451)
                                        .addComponent(searchButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(purchaseOrderListTablePanelLayout.createSequentialGroup()
                                        .addGap(136, 136, 136)
                                        .addComponent(searchTxtField1, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(purchaseOrderListTablePanelLayout.createSequentialGroup()
                                .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(88, 88, 88))))
                    .addGroup(purchaseOrderListTablePanelLayout.createSequentialGroup()
                        .addGroup(purchaseOrderListTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(purchaseOrderListTablePanelLayout.createSequentialGroup()
                                .addComponent(backButton1)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jScrollPane2))
                        .addContainerGap())))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, purchaseOrderListTablePanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(purchaseOrderListTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(purchaseOrderListTablePanelLayout.createSequentialGroup()
                        .addComponent(purchaseRequisitionIDLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(purchaseRequisitionIDTxtField1, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(purchaseOrderListTablePanelLayout.createSequentialGroup()
                        .addComponent(purchaseOrderLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(purchaseOrderTxtField1, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, purchaseOrderListTablePanelLayout.createSequentialGroup()
                        .addGroup(purchaseOrderListTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(supplierIDLabel2)
                            .addComponent(raisedbyLabel)
                            .addComponent(requestedDateLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(purchaseOrderListTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(raisedByTxtField2, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(supplierIDTxtField2, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(requestedDateTxtField3, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGroup(purchaseOrderListTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(purchaseOrderListTablePanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(purchaseOrderListTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(quantityLabel)
                            .addComponent(statusLabel)
                            .addComponent(itemCodeLabel1))
                        .addGap(45, 45, 45)
                        .addGroup(purchaseOrderListTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(itemCodeTxtField1, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(purchaseOrderListTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(statusTxtField2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(paymentAmountTxtField3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(quantityTxtField1, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(purchaseOrderListTablePanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(purchaseOrderListTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(requiredDateLabel2)
                            .addComponent(remarksLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(purchaseOrderListTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(requiredDateTxtField4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(remarksTxtField5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(purchaseOrderListTablePanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(paymentAmountLabel1)))
                .addGap(175, 175, 175))
        );
        purchaseOrderListTablePanelLayout.setVerticalGroup(
            purchaseOrderListTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(purchaseOrderListTablePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(backButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(purchaseOrderListTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchTxtField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchButton1))
                .addGap(42, 42, 42)
                .addGroup(purchaseOrderListTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(purchaseOrderLabel)
                    .addComponent(itemCodeLabel1)
                    .addComponent(purchaseOrderTxtField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(itemCodeTxtField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(purchaseOrderListTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(purchaseRequisitionIDLabel)
                    .addComponent(quantityLabel)
                    .addComponent(purchaseRequisitionIDTxtField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(quantityTxtField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(purchaseOrderListTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(supplierIDLabel2)
                    .addComponent(statusLabel)
                    .addComponent(supplierIDTxtField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(statusTxtField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(purchaseOrderListTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(raisedbyLabel)
                    .addComponent(raisedByTxtField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(paymentAmountLabel1)
                    .addComponent(paymentAmountTxtField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(purchaseOrderListTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(requestedDateLabel1)
                    .addComponent(requestedDateTxtField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(requiredDateLabel2)
                    .addComponent(requiredDateTxtField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(purchaseOrderListTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(remarksLabel3)
                    .addComponent(remarksTxtField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(purchaseOrderListTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editButton, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
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
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(tittle4, javax.swing.GroupLayout.PREFERRED_SIZE, 1235, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(sideBarMenu5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(purchaseOrderListTablePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tittle4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(purchaseOrderListTablePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(sideBarMenu5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void homeButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_homeButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_homeButton3ActionPerformed

    private void itemsListPageButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemsListPageButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_itemsListPageButton3ActionPerformed

    private void supplierPageButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplierPageButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_supplierPageButton3ActionPerformed

    private void purchaseOrderPageButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_purchaseOrderPageButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_purchaseOrderPageButton3ActionPerformed

    private void profilePageButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_profilePageButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_profilePageButton3ActionPerformed

    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
        int selectedRow = purchaseOrderTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a Purchase Order to edit.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String status = purchaseOrderTable.getValueAt(selectedRow, 8).toString();
        if (!status.equals(Status.PENDING.toString())) {
            JOptionPane.showMessageDialog(this,
                "Cannot edit Purchase Order. Only PENDING Purchase Orders can be edited. Current status: " + status + ".",
                "Invalid Status",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Enable only quantity field for editing
        quantityTxtField1.setEditable(true);
        paymentAmountTxtField3.setEditable(false);
        // Disable all other fields
        purchaseOrderTxtField1.setEditable(false);
        purchaseRequisitionIDTxtField1.setEditable(false);
        itemCodeTxtField1.setEditable(false);
        supplierIDTxtField2.setEditable(false);
        raisedByTxtField2.setEditable(false);
        statusTxtField2.setEditable(false);
        requestedDateTxtField3.setEditable(false);
        requiredDateTxtField4.setEditable(false);
        remarksTxtField5.setEditable(false);
        // Enable Save button and disable Delete button
        saveButton.setEnabled(true);
        deleteButton.setEnabled(false);
        editButton.setEnabled(false);
        cancelButton.setEnabled(true);
        // TODO add your handling code here:
    }//GEN-LAST:event_editButtonActionPerformed

    private void itemCodeTxtFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemCodeTxtFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_itemCodeTxtFieldActionPerformed

    private void supplierIDTxtFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplierIDTxtFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_supplierIDTxtFieldActionPerformed

    private void itemNameTxtFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemNameTxtFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_itemNameTxtFieldActionPerformed

    private void stockLevelTxtFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stockLevelTxtFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_stockLevelTxtFieldActionPerformed

    private void purchaseOrderTxtField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_purchaseOrderTxtField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_purchaseOrderTxtField1ActionPerformed

    private void purchaseRequisitionIDTxtField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_purchaseRequisitionIDTxtField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_purchaseRequisitionIDTxtField1ActionPerformed

    private void itemCodeTxtField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemCodeTxtField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_itemCodeTxtField1ActionPerformed

    private void quantityTxtField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quantityTxtField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_quantityTxtField1ActionPerformed

    private void supplierIDTxtField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplierIDTxtField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_supplierIDTxtField2ActionPerformed

    private void raisedByTxtField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_raisedByTxtField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_raisedByTxtField2ActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        int selectedRow = purchaseOrderTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a Purchase Order to save.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String poId = purchaseOrderTxtField1.getText().trim();
        String quantityStr = quantityTxtField1.getText().trim();
        String itemCode = itemCodeTxtField1.getText().trim();
        String supplierCode = supplierIDTxtField2.getText().trim();

        // Validate quantity
        int quantity;
        
        try {
            quantity = Integer.parseInt(quantityStr);
            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Quantity must be a positive integer.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid quantity format. Please enter a valid number.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Confirm the update
        String confirmationMessage = String.format(
            "Are you sure you want to update the following Purchase Order?\n" +
            poId, quantity
        );
        int response = JOptionPane.showConfirmDialog(
            this,
            confirmationMessage,
            "Confirm Purchase Order Update",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (response != JOptionPane.YES_OPTION) {
            return; 
        }

        // Update the Purchase Order
        try {
            boolean updated = purchaseOrderController.editPurchaseOrder(poId, quantity , itemCode, supplierCode);
            if (updated) {
                JOptionPane.showMessageDialog(this, "Purchase Order " + poId + " updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                List<String[]> allPOs = purchaseOrderController.viewPurchaseOrder();
                
                double new_payment_amount =0.0;
                for (String[] po : allPOs) {
                // Check if PO ID (index 0) or PR ID (index 1) contains the search term
                    if (po[0].equals(poId)){
                        new_payment_amount = Double.parseDouble(po[9]);
                        
                        break;
                    }
                }
                
                model.setValueAt(String.valueOf(quantity), selectedRow, 4);
                model.setValueAt(String.valueOf(new_payment_amount), selectedRow, 9);
                paymentAmountTxtField3.setText(String.valueOf(new_payment_amount));
                // Disable editing
                quantityTxtField1.setEditable(false);
                
                saveButton.setEnabled(false);
                deleteButton.setEnabled(true);
                editButton.setEnabled(true);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed to update Purchase Order " + poId + ". Check console logs for details.",
                    "Update Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error updating Purchase Order " + poId + ": " + e.getMessage(),
                "Update Exception",
                JOptionPane.ERROR_MESSAGE);
        }   // TODO add your handling code here:
    }//GEN-LAST:event_saveButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        int selectedRow = purchaseOrderTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a Purchase Order to delete.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String poId = purchaseOrderTable.getValueAt(selectedRow, 0).toString();
        String itemCode = purchaseOrderTable.getValueAt(selectedRow, 3).toString();
        String status = purchaseOrderTable.getValueAt(selectedRow, 8).toString();

        // Check if status is PENDING
        if (!status.equals(Status.PENDING.toString())) {
            JOptionPane.showMessageDialog(
                this,
                "Cannot delete Purchase Order " + poId + ". Only PENDING Purchase Orders can be deleted. Current status: " + status + ".",
                "Invalid Status",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Show confirmation dialog
        String confirmationMessage = String.format(
            "Are you sure you want to delete the following Purchase Order?\n" +
            "Purchase Order ID: %s\n" +
            "Item Code: %s\n" +
            "Current Status: %s",
            poId, itemCode, status
        );
        int response = JOptionPane.showConfirmDialog(
            this,
            confirmationMessage,
            "Confirm Purchase Order Deletion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (response != JOptionPane.YES_OPTION) {
            return;
        }

        
        try {
            boolean deleted = purchaseOrderController.deletePurchaseOrder(poId);
            if (deleted) {
                JOptionPane.showMessageDialog(this, "Purchase Order " + poId + " deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadPO(); // Refresh table
                // Update text fields if the deleted row was selected
                if (purchaseOrderTable.getSelectedRow() == selectedRow) {
                    purchaseOrderTxtField1.setText("");
                    purchaseRequisitionIDTxtField1.setText("");
                    raisedByTxtField2.setText("");
                    itemCodeTxtField1.setText("");
                    quantityTxtField1.setText("");
                    supplierIDTxtField2.setText("");
                    requiredDateTxtField4.setText("");
                    requestedDateTxtField3.setText("");
                    statusTxtField2.setText("");
                    paymentAmountTxtField3.setText("");
                    remarksTxtField5.setText("");
                }
  
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed to delete Purchase Order " + poId + ". Check console logs for details.",
                    "Deletion Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error deleting Purchase Order " + poId + ": " + e.getMessage(),
                "Deletion Exception",
                JOptionPane.ERROR_MESSAGE);
        }
//make a confirmation than delete        // TODO add your handling code here:
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void searchTxtField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchTxtField1ActionPerformed
//make a grey enter item code        // TODO add your handling code here:
    }//GEN-LAST:event_searchTxtField1ActionPerformed

    private void statusTxtField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statusTxtField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_statusTxtField2ActionPerformed

    private void requestedDateTxtField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_requestedDateTxtField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_requestedDateTxtField3ActionPerformed

    private void paymentAmountTxtField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_paymentAmountTxtField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_paymentAmountTxtField3ActionPerformed

    private void requiredDateTxtField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_requiredDateTxtField4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_requiredDateTxtField4ActionPerformed

    private void purchaseOrderTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_purchaseOrderTableMouseClicked
        int selectedRow = purchaseOrderTable.getSelectedRow();

        if (selectedRow >= 0) {
            purchaseOrderTxtField1.setText(purchaseOrderTable.getValueAt(selectedRow, 0).toString());
            purchaseRequisitionIDTxtField1.setText(purchaseOrderTable.getValueAt(selectedRow, 1).toString());
            raisedByTxtField2.setText(purchaseOrderTable.getValueAt(selectedRow, 2).toString());
            itemCodeTxtField1.setText(purchaseOrderTable.getValueAt(selectedRow, 3).toString());
            quantityTxtField1.setText(purchaseOrderTable.getValueAt(selectedRow, 4).toString());
            supplierIDTxtField2.setText(purchaseOrderTable.getValueAt(selectedRow, 5).toString());
            requiredDateTxtField4.setText(purchaseOrderTable.getValueAt(selectedRow, 6).toString());
            requestedDateTxtField3.setText(purchaseOrderTable.getValueAt(selectedRow, 7).toString());
            statusTxtField2.setText(purchaseOrderTable.getValueAt(selectedRow, 8).toString());
            paymentAmountTxtField3.setText(purchaseOrderTable.getValueAt(selectedRow, 9).toString());
            remarksTxtField5.setText(purchaseOrderTable.getValueAt(selectedRow, 10).toString());
        }      // TODO add your handling code here:
    }//GEN-LAST:event_purchaseOrderTableMouseClicked

    private void remarksTxtField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_remarksTxtField5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_remarksTxtField5ActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        int selectedRow = purchaseOrderTable.getSelectedRow();
        if (selectedRow >= 0) {
            // Restore text fields to the selected row's values
            purchaseOrderTxtField1.setText(purchaseOrderTable.getValueAt(selectedRow, 0).toString());
            purchaseRequisitionIDTxtField1.setText(purchaseOrderTable.getValueAt(selectedRow, 1).toString());
            raisedByTxtField2.setText(purchaseOrderTable.getValueAt(selectedRow, 2).toString());
            itemCodeTxtField1.setText(purchaseOrderTable.getValueAt(selectedRow, 3).toString());
            quantityTxtField1.setText(purchaseOrderTable.getValueAt(selectedRow, 4).toString());
            supplierIDTxtField2.setText(purchaseOrderTable.getValueAt(selectedRow, 5).toString());
            requiredDateTxtField4.setText(purchaseOrderTable.getValueAt(selectedRow, 6).toString());
            requestedDateTxtField3.setText(purchaseOrderTable.getValueAt(selectedRow, 7).toString());
            statusTxtField2.setText(purchaseOrderTable.getValueAt(selectedRow, 8).toString());
            paymentAmountTxtField3.setText(purchaseOrderTable.getValueAt(selectedRow, 9).toString());
            remarksTxtField5.setText(purchaseOrderTable.getValueAt(selectedRow, 10).toString());
        } else {
            // Clear all text fields if no row is selected
            purchaseOrderTxtField1.setText("");
            purchaseRequisitionIDTxtField1.setText("");
            raisedByTxtField2.setText("");
            itemCodeTxtField1.setText("");
            quantityTxtField1.setText("");
            supplierIDTxtField2.setText("");
            requiredDateTxtField4.setText("");
            requestedDateTxtField3.setText("");
            statusTxtField2.setText("");
            paymentAmountTxtField3.setText("");
            remarksTxtField5.setText("");
        }

        // Disable all text fields
        edit();
        // Reset button states
        saveButton.setEnabled(false);
        deleteButton.setEnabled(true);
        editButton.setEnabled(true);
        cancelButton.setEnabled(false);// TODO add your handling code here:
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_backButtonActionPerformed

    private void backButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButton1ActionPerformed
        if (previousPage != null) {
            previousPage.setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, 
                "No previous page available.", 
                "Navigation Error", 
                JOptionPane.INFORMATION_MESSAGE);
        }        // TODO add your handling code here:
    }//GEN-LAST:event_backButton1ActionPerformed

    private void logoutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutButtonActionPerformed
        currentUser.logout();
        Login login = new Login();
        this.dispose();
        login.setVisible(true);
        
    }//GEN-LAST:event_logoutButtonActionPerformed

    private void itemsListPageButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemsListPageButton4ActionPerformed
        new PM_List_items(currentUser, this, itemViewer, purchaseOrderController, purchaseRequisitionViewer, supplierViewer, itemSupplyController).setVisible(true);
        this.dispose();       // TODO add your handling code here:
    }//GEN-LAST:event_itemsListPageButton4ActionPerformed

    private void supplierPageButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplierPageButton4ActionPerformed
        new PM_Suppliers(currentUser, this, itemViewer, purchaseOrderController, purchaseRequisitionViewer, supplierViewer, itemSupplyController).setVisible(true);
        this.dispose(); // TODO add your handling code here:
    }//GEN-LAST:event_supplierPageButton4ActionPerformed

    private void purchaseOrderPageButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_purchaseOrderPageButton4ActionPerformed
        new PM_List_purchase_order(currentUser,this, itemViewer, purchaseOrderController, purchaseRequisitionViewer, supplierViewer, itemSupplyController).setVisible(true);
        this.dispose(); // TODO add your handling code here:
    }//GEN-LAST:event_purchaseOrderPageButton4ActionPerformed

    private void purchaseRequisitionPageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_purchaseRequisitionPageButtonActionPerformed
        new PM_List_requisition(currentUser,this, itemViewer, purchaseOrderController, purchaseRequisitionViewer, supplierViewer, itemSupplyController).setVisible(true);
        this.dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_purchaseRequisitionPageButtonActionPerformed

    private void searchButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButton1ActionPerformed
      searchPurchaseOrders();// TODO add your handling code here:
    }//GEN-LAST:event_searchButton1ActionPerformed
    
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
            java.util.logging.Logger.getLogger(PM_List_purchase_order.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PM_List_purchase_order.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PM_List_purchase_order.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PM_List_purchase_order.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                PurchaseManager po = new PurchaseManager("", "", "");
//                new PM_List_purchase_order(po,null).setVisible(true);
//            }
//        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backButton;
    private javax.swing.JButton backButton1;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton editButton;
    private javax.swing.JButton homeButton3;
    private javax.swing.JLabel itemCodeLabel;
    private javax.swing.JLabel itemCodeLabel1;
    private javax.swing.JTextField itemCodeTxtField;
    private javax.swing.JTextField itemCodeTxtField1;
    private javax.swing.JLabel itemNameLabel;
    private javax.swing.JTextField itemNameTxtField;
    private javax.swing.JButton itemsListPageButton3;
    private javax.swing.JButton itemsListPageButton4;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton logoutButton;
    private javax.swing.JLabel pageName;
    private javax.swing.JLabel paymentAmountLabel1;
    private javax.swing.JTextField paymentAmountTxtField3;
    private javax.swing.JButton profilePageButton3;
    private javax.swing.JLabel purchaseOrderLabel;
    private javax.swing.JPanel purchaseOrderListTablePanel;
    private javax.swing.JButton purchaseOrderPageButton3;
    private javax.swing.JButton purchaseOrderPageButton4;
    private javax.swing.JTable purchaseOrderTable;
    private javax.swing.JTextField purchaseOrderTxtField1;
    private javax.swing.JLabel purchaseRequisitionIDLabel;
    private javax.swing.JTextField purchaseRequisitionIDTxtField1;
    private javax.swing.JButton purchaseRequisitionPageButton;
    private javax.swing.JLabel quantityLabel;
    private javax.swing.JTextField quantityTxtField1;
    private javax.swing.JTextField raisedByTxtField2;
    private javax.swing.JLabel raisedbyLabel;
    private javax.swing.JLabel remarksLabel3;
    private javax.swing.JTextField remarksTxtField5;
    private javax.swing.JLabel requestedDateLabel1;
    private javax.swing.JTextField requestedDateTxtField3;
    private javax.swing.JLabel requiredDateLabel2;
    private javax.swing.JTextField requiredDateTxtField4;
    private javax.swing.JButton saveButton;
    private javax.swing.JButton saveButton2;
    private javax.swing.JButton searchButton;
    private javax.swing.JButton searchButton1;
    private javax.swing.JTextField searchTxtField;
    private javax.swing.JTextField searchTxtField1;
    private javax.swing.JPanel sideBarMenu3;
    private javax.swing.JPanel sideBarMenu5;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JTextField statusTxtField2;
    private javax.swing.JLabel stockLevelLabel;
    private javax.swing.JTextField stockLevelTxtField;
    private javax.swing.JLabel supplierIDLabel;
    private javax.swing.JLabel supplierIDLabel2;
    private javax.swing.JTextField supplierIDTxtField;
    private javax.swing.JTextField supplierIDTxtField2;
    private javax.swing.JButton supplierPageButton3;
    private javax.swing.JButton supplierPageButton4;
    private javax.swing.JPanel tittle3;
    private javax.swing.JPanel tittle4;
    // End of variables declaration//GEN-END:variables
}
