/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package OSWB;

/**
 *
 * @author User
 */
public class PM_List_items extends javax.swing.JFrame {

    /**
     * Creates new form PM_Items
     */
    public PM_List_items() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFrame1 = new javax.swing.JFrame();
        jPanel1 = new javax.swing.JPanel();
        tittle = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        homeButton = new javax.swing.JButton();
        sideBarMenu = new javax.swing.JPanel();
        itemsListPageButton = new javax.swing.JButton();
        supplierPageButton = new javax.swing.JButton();
        purchaseOrderPageButton = new javax.swing.JButton();
        profilePageButton = new javax.swing.JButton();
        jFrame2 = new javax.swing.JFrame();
        jPanel2 = new javax.swing.JPanel();
        tittle1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        homeButton1 = new javax.swing.JButton();
        sideBarMenu1 = new javax.swing.JPanel();
        itemsListPageButton1 = new javax.swing.JButton();
        supplierPageButton1 = new javax.swing.JButton();
        purchaseOrderPageButton1 = new javax.swing.JButton();
        profilePageButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        tittle2 = new javax.swing.JPanel();
        pageName = new javax.swing.JLabel();
        homeButton2 = new javax.swing.JButton();
        itemsListTablePanel = new javax.swing.JPanel();
        backButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        itemsListTable = new javax.swing.JTable();
        sideBarMenu5 = new javax.swing.JPanel();
        itemsListPageButton5 = new javax.swing.JButton();
        supplierPageButton5 = new javax.swing.JButton();
        purchaseOrderPageButton5 = new javax.swing.JButton();

        jFrame1.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setToolTipText("");

        tittle.setBackground(new java.awt.Color(0, 102, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 48)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Purchase Manager Page");

        homeButton.setText("Home");
        homeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                homeButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tittleLayout = new javax.swing.GroupLayout(tittle);
        tittle.setLayout(tittleLayout);
        tittleLayout.setHorizontalGroup(
            tittleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tittleLayout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(homeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 70, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 669, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(214, 214, 214))
        );
        tittleLayout.setVerticalGroup(
            tittleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tittleLayout.createSequentialGroup()
                .addContainerGap(52, Short.MAX_VALUE)
                .addGroup(tittleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tittleLayout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tittleLayout.createSequentialGroup()
                        .addComponent(homeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(64, 64, 64))))
        );

        sideBarMenu.setBackground(new java.awt.Color(51, 51, 51));

        itemsListPageButton.setText("Items");
        itemsListPageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemsListPageButtonActionPerformed(evt);
            }
        });

        supplierPageButton.setText("Supplier");
        supplierPageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supplierPageButtonActionPerformed(evt);
            }
        });

        purchaseOrderPageButton.setText("Purchase Order");
        purchaseOrderPageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                purchaseOrderPageButtonActionPerformed(evt);
            }
        });

        profilePageButton.setText("Profile");
        profilePageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                profilePageButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout sideBarMenuLayout = new javax.swing.GroupLayout(sideBarMenu);
        sideBarMenu.setLayout(sideBarMenuLayout);
        sideBarMenuLayout.setHorizontalGroup(
            sideBarMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sideBarMenuLayout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(sideBarMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(supplierPageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(purchaseOrderPageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(itemsListPageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(profilePageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(37, Short.MAX_VALUE))
        );
        sideBarMenuLayout.setVerticalGroup(
            sideBarMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sideBarMenuLayout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addComponent(profilePageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(103, 103, 103)
                .addComponent(itemsListPageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(99, 99, 99)
                .addComponent(supplierPageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 105, Short.MAX_VALUE)
                .addComponent(purchaseOrderPageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(68, 68, 68))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tittle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(sideBarMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(tittle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sideBarMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jFrame2.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setToolTipText("");

        tittle1.setBackground(new java.awt.Color(0, 102, 255));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 48)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Purchase Manager Page");

        homeButton1.setText("Home");
        homeButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                homeButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tittle1Layout = new javax.swing.GroupLayout(tittle1);
        tittle1.setLayout(tittle1Layout);
        tittle1Layout.setHorizontalGroup(
            tittle1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tittle1Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(homeButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 70, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 669, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(214, 214, 214))
        );
        tittle1Layout.setVerticalGroup(
            tittle1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tittle1Layout.createSequentialGroup()
                .addContainerGap(52, Short.MAX_VALUE)
                .addGroup(tittle1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tittle1Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tittle1Layout.createSequentialGroup()
                        .addComponent(homeButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(64, 64, 64))))
        );

        sideBarMenu1.setBackground(new java.awt.Color(51, 51, 51));

        itemsListPageButton1.setText("Items");
        itemsListPageButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemsListPageButton1ActionPerformed(evt);
            }
        });

        supplierPageButton1.setText("Supplier");
        supplierPageButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supplierPageButton1ActionPerformed(evt);
            }
        });

        purchaseOrderPageButton1.setText("Purchase Order");
        purchaseOrderPageButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                purchaseOrderPageButton1ActionPerformed(evt);
            }
        });

        profilePageButton1.setText("Profile");
        profilePageButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                profilePageButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout sideBarMenu1Layout = new javax.swing.GroupLayout(sideBarMenu1);
        sideBarMenu1.setLayout(sideBarMenu1Layout);
        sideBarMenu1Layout.setHorizontalGroup(
            sideBarMenu1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sideBarMenu1Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(sideBarMenu1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(supplierPageButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(purchaseOrderPageButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(itemsListPageButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(profilePageButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(37, Short.MAX_VALUE))
        );
        sideBarMenu1Layout.setVerticalGroup(
            sideBarMenu1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sideBarMenu1Layout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addComponent(profilePageButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(103, 103, 103)
                .addComponent(itemsListPageButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(99, 99, 99)
                .addComponent(supplierPageButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 105, Short.MAX_VALUE)
                .addComponent(purchaseOrderPageButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(68, 68, 68))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tittle1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(sideBarMenu1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(tittle1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sideBarMenu1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jFrame2Layout = new javax.swing.GroupLayout(jFrame2.getContentPane());
        jFrame2.getContentPane().setLayout(jFrame2Layout);
        jFrame2Layout.setHorizontalGroup(
            jFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jFrame2Layout.setVerticalGroup(
            jFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tittle2.setBackground(new java.awt.Color(0, 102, 255));
        tittle2.setPreferredSize(new java.awt.Dimension(931, 186));

        pageName.setFont(new java.awt.Font("Segoe UI", 0, 48)); // NOI18N
        pageName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pageName.setText("Items List Page");

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
                .addGap(64, 64, 64)
                .addComponent(pageName, javax.swing.GroupLayout.PREFERRED_SIZE, 669, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        tittle2Layout.setVerticalGroup(
            tittle2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tittle2Layout.createSequentialGroup()
                .addContainerGap(49, Short.MAX_VALUE)
                .addGroup(tittle2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tittle2Layout.createSequentialGroup()
                        .addComponent(homeButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(64, 64, 64))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tittle2Layout.createSequentialGroup()
                        .addComponent(pageName, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(45, 45, 45))))
        );

        itemsListTablePanel.setBackground(new java.awt.Color(204, 204, 204));

        backButton.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        backButton.setText("<");
        backButton.setToolTipText("");
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });

        itemsListTable.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(itemsListTable);

        sideBarMenu5.setBackground(new java.awt.Color(51, 51, 51));

        itemsListPageButton5.setText("Items");
        itemsListPageButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemsListPageButton5ActionPerformed(evt);
            }
        });

        supplierPageButton5.setText("Supplier");
        supplierPageButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supplierPageButton5ActionPerformed(evt);
            }
        });

        purchaseOrderPageButton5.setText("Purchase Order");
        purchaseOrderPageButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                purchaseOrderPageButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout sideBarMenu5Layout = new javax.swing.GroupLayout(sideBarMenu5);
        sideBarMenu5.setLayout(sideBarMenu5Layout);
        sideBarMenu5Layout.setHorizontalGroup(
            sideBarMenu5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sideBarMenu5Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(sideBarMenu5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(supplierPageButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(purchaseOrderPageButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(itemsListPageButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(37, Short.MAX_VALUE))
        );
        sideBarMenu5Layout.setVerticalGroup(
            sideBarMenu5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sideBarMenu5Layout.createSequentialGroup()
                .addGap(96, 96, 96)
                .addComponent(itemsListPageButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 128, Short.MAX_VALUE)
                .addComponent(supplierPageButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(126, 126, 126)
                .addComponent(purchaseOrderPageButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(144, 144, 144))
        );

        javax.swing.GroupLayout itemsListTablePanelLayout = new javax.swing.GroupLayout(itemsListTablePanel);
        itemsListTablePanel.setLayout(itemsListTablePanelLayout);
        itemsListTablePanelLayout.setHorizontalGroup(
            itemsListTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(itemsListTablePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(sideBarMenu5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(itemsListTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(itemsListTablePanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(backButton, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(itemsListTablePanelLayout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 759, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(62, Short.MAX_VALUE))
        );
        itemsListTablePanelLayout.setVerticalGroup(
            itemsListTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(itemsListTablePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(backButton, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(sideBarMenu5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(itemsListTablePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(tittle2, javax.swing.GroupLayout.DEFAULT_SIZE, 1055, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tittle2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(itemsListTablePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void homeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_homeButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_homeButtonActionPerformed

    private void itemsListPageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemsListPageButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_itemsListPageButtonActionPerformed

    private void supplierPageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplierPageButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_supplierPageButtonActionPerformed

    private void purchaseOrderPageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_purchaseOrderPageButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_purchaseOrderPageButtonActionPerformed

    private void profilePageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_profilePageButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_profilePageButtonActionPerformed

    private void homeButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_homeButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_homeButton1ActionPerformed

    private void itemsListPageButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemsListPageButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_itemsListPageButton1ActionPerformed

    private void supplierPageButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplierPageButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_supplierPageButton1ActionPerformed

    private void purchaseOrderPageButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_purchaseOrderPageButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_purchaseOrderPageButton1ActionPerformed

    private void profilePageButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_profilePageButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_profilePageButton1ActionPerformed

    private void homeButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_homeButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_homeButton2ActionPerformed

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_backButtonActionPerformed

    private void itemsListPageButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemsListPageButton5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_itemsListPageButton5ActionPerformed

    private void supplierPageButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplierPageButton5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_supplierPageButton5ActionPerformed

    private void purchaseOrderPageButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_purchaseOrderPageButton5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_purchaseOrderPageButton5ActionPerformed

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
            java.util.logging.Logger.getLogger(PM_List_items.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PM_List_items.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PM_List_items.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PM_List_items.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PM_List_items().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backButton;
    private javax.swing.JButton homeButton;
    private javax.swing.JButton homeButton1;
    private javax.swing.JButton homeButton2;
    private javax.swing.JButton itemsListPageButton;
    private javax.swing.JButton itemsListPageButton1;
    private javax.swing.JButton itemsListPageButton5;
    private javax.swing.JTable itemsListTable;
    private javax.swing.JPanel itemsListTablePanel;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JFrame jFrame2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel pageName;
    private javax.swing.JButton profilePageButton;
    private javax.swing.JButton profilePageButton1;
    private javax.swing.JButton purchaseOrderPageButton;
    private javax.swing.JButton purchaseOrderPageButton1;
    private javax.swing.JButton purchaseOrderPageButton5;
    private javax.swing.JPanel sideBarMenu;
    private javax.swing.JPanel sideBarMenu1;
    private javax.swing.JPanel sideBarMenu5;
    private javax.swing.JButton supplierPageButton;
    private javax.swing.JButton supplierPageButton1;
    private javax.swing.JButton supplierPageButton5;
    private javax.swing.JPanel tittle;
    private javax.swing.JPanel tittle1;
    private javax.swing.JPanel tittle2;
    // End of variables declaration//GEN-END:variables
}
