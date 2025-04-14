/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package OSWB;

/**
 *
 * @author User
 */
public class PM_List_requisition extends javax.swing.JFrame {

    /**
     * Creates new form PM_List_requisition
     */
    public PM_List_requisition() {
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

        tittle4 = new javax.swing.JPanel();
        pageName = new javax.swing.JLabel();
        homeButton4 = new javax.swing.JButton();
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
        supplierCodeLabel1 = new javax.swing.JLabel();
        supplierCodeTxtField1 = new javax.swing.JTextField();
        sideBarMenu3 = new javax.swing.JPanel();
        itemsListPageButton3 = new javax.swing.JButton();
        supplierPageButton3 = new javax.swing.JButton();
        purchaseOrderPageButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tittle4.setBackground(new java.awt.Color(0, 102, 255));
        tittle4.setPreferredSize(new java.awt.Dimension(931, 186));

        pageName.setFont(new java.awt.Font("Segoe UI", 0, 48)); // NOI18N
        pageName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pageName.setText("Requisition List Page");

        homeButton4.setText("Home");
        homeButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                homeButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tittle4Layout = new javax.swing.GroupLayout(tittle4);
        tittle4.setLayout(tittle4Layout);
        tittle4Layout.setHorizontalGroup(
            tittle4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tittle4Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(homeButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(57, 57, 57)
                .addComponent(pageName, javax.swing.GroupLayout.PREFERRED_SIZE, 669, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(157, Short.MAX_VALUE))
        );
        tittle4Layout.setVerticalGroup(
            tittle4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tittle4Layout.createSequentialGroup()
                .addContainerGap(48, Short.MAX_VALUE)
                .addGroup(tittle4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tittle4Layout.createSequentialGroup()
                        .addComponent(homeButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(64, 64, 64))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tittle4Layout.createSequentialGroup()
                        .addComponent(pageName, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(46, 46, 46))))
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

        purchaseRequisitionIDLabel.setText("Purchase Requisition ID :");

        itemCodeLabel.setText("Item Code :");

        supplierCodeLabel1.setText("Supplier Code :");

        supplierCodeTxtField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supplierCodeTxtField1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout requisitionTablePanelLayout = new javax.swing.GroupLayout(requisitionTablePanel);
        requisitionTablePanel.setLayout(requisitionTablePanelLayout);
        requisitionTablePanelLayout.setHorizontalGroup(
            requisitionTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(requisitionTablePanelLayout.createSequentialGroup()
                .addGroup(requisitionTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(requisitionTablePanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(backButton, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(requisitionTablePanelLayout.createSequentialGroup()
                        .addGap(259, 259, 259)
                        .addComponent(searchTxtField, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(searchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(requisitionTablePanelLayout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addGroup(requisitionTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(requisitionTablePanelLayout.createSequentialGroup()
                                .addGroup(requisitionTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(requisitionTablePanelLayout.createSequentialGroup()
                                        .addComponent(purchaseRequisitionIDLabel)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(purchaseRequisitionIDTxtField, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, requisitionTablePanelLayout.createSequentialGroup()
                                        .addGroup(requisitionTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(itemCodeLabel)
                                            .addComponent(supplierCodeLabel1))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(requisitionTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(supplierCodeTxtField1)
                                            .addComponent(itemCodeTxtField, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE))))
                                .addGap(18, 18, 18)
                                .addGroup(requisitionTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(requisitionTablePanelLayout.createSequentialGroup()
                                        .addComponent(requiredDateLabel)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(requiredDateTxtField))
                                    .addGroup(requisitionTablePanelLayout.createSequentialGroup()
                                        .addComponent(quantityLabel)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(quantityTxtField, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 759, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        requisitionTablePanelLayout.setVerticalGroup(
            requisitionTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(requisitionTablePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(backButton, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(requisitionTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchTxtField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchButton))
                .addGap(32, 32, 32)
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
                    .addComponent(supplierCodeLabel1)
                    .addComponent(supplierCodeTxtField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
            .addGroup(sideBarMenu3Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(sideBarMenu3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(supplierPageButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(purchaseOrderPageButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(itemsListPageButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(37, Short.MAX_VALUE))
        );
        sideBarMenu3Layout.setVerticalGroup(
            sideBarMenu3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sideBarMenu3Layout.createSequentialGroup()
                .addGap(96, 96, 96)
                .addComponent(itemsListPageButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 127, Short.MAX_VALUE)
                .addComponent(supplierPageButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(126, 126, 126)
                .addComponent(purchaseOrderPageButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(144, 144, 144))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tittle4, javax.swing.GroupLayout.DEFAULT_SIZE, 1037, Short.MAX_VALUE)
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
    }// </editor-fold>//GEN-END:initComponents

    private void homeButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_homeButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_homeButton4ActionPerformed

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_backButtonActionPerformed

    private void itemsListPageButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemsListPageButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_itemsListPageButton3ActionPerformed

    private void supplierPageButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplierPageButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_supplierPageButton3ActionPerformed

    private void purchaseOrderPageButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_purchaseOrderPageButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_purchaseOrderPageButton3ActionPerformed

    private void purchaseRequisitionIDTxtFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_purchaseRequisitionIDTxtFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_purchaseRequisitionIDTxtFieldActionPerformed

    private void itemCodeTxtFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemCodeTxtFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_itemCodeTxtFieldActionPerformed

    private void quantityTxtFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quantityTxtFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_quantityTxtFieldActionPerformed

    private void requiredDateTxtFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_requiredDateTxtFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_requiredDateTxtFieldActionPerformed

    private void supplierCodeTxtField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplierCodeTxtField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_supplierCodeTxtField1ActionPerformed

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
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PM_List_requisition().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backButton;
    private javax.swing.JButton homeButton4;
    private javax.swing.JLabel itemCodeLabel;
    private javax.swing.JTextField itemCodeTxtField;
    private javax.swing.JButton itemsListPageButton3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel pageName;
    private javax.swing.JButton purchaseOrderPageButton3;
    private javax.swing.JLabel purchaseRequisitionIDLabel;
    private javax.swing.JTextField purchaseRequisitionIDTxtField;
    private javax.swing.JLabel quantityLabel;
    private javax.swing.JTextField quantityTxtField;
    private javax.swing.JLabel requiredDateLabel;
    private javax.swing.JTextField requiredDateTxtField;
    private javax.swing.JTable requisitionTable;
    private javax.swing.JPanel requisitionTablePanel;
    private javax.swing.JButton searchButton;
    private javax.swing.JTextField searchTxtField;
    private javax.swing.JPanel sideBarMenu3;
    private javax.swing.JLabel supplierCodeLabel1;
    private javax.swing.JTextField supplierCodeTxtField1;
    private javax.swing.JButton supplierPageButton3;
    private javax.swing.JPanel tittle4;
    // End of variables declaration//GEN-END:variables
}
