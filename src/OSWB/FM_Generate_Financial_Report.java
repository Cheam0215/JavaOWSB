/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package OSWB;

import Controllers.FinanceController;
import Entities.FinanceManager;

import Entities.PurchaseOrder;
import Entities.SalesData;
import Entities.User;
import Interface.PurchaseOrderViewServices;
import Interface.SalesDataViewingServices;
import Utility.Status;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
/**
 *
 * @author Maxcm
 */
public class FM_Generate_Financial_Report extends javax.swing.JFrame {

    private final User currentUser;
    private final PurchaseOrderViewServices purchaseOrderViewer;
    private final SalesDataViewingServices salesDataViewer;
    private final FinanceController financeController;
    private DefaultTableModel salesTableModel;
    private DefaultTableModel poTableModel;
    private final JFrame previousScreen;
    /**
     * Creates new form FM_Generate_Financial_Report
     * @param currentUser
     * @param purchaseOrderViewer
     * @param salesDataViewer
     * @param financeController
     * @param previousScreen
     */
    public FM_Generate_Financial_Report(User currentUser, PurchaseOrderViewServices purchaseOrderViewer, SalesDataViewingServices salesDataViewer, FinanceController financeController, JFrame previousScreen) {
        this.currentUser = currentUser;
        this.purchaseOrderViewer = purchaseOrderViewer;
        this.salesDataViewer = salesDataViewer;
        this.financeController = financeController;
        this.previousScreen = previousScreen;
        setupTables();
        initComponents();
        populateTables();
        
    }

    private void setupTables() {
        salesTableModel = new DefaultTableModel(
            new Object[][] {},
            new String[] {
                "Sales ID", "Item Code", "Retail Price", "Quantity Sold", "Total Amount", "Date"
            }) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        poTableModel = new DefaultTableModel(
            new Object[][] {},
            new String[] {
                "PO ID", "Item Code", "Quantity", "Supplier Code", "Status", 
                "Payment Amount", "Required Date", "Remark"
            }) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private void populateTables() {
        int selectedMonth = 4; 
        int year = 2025;

        salesTableModel.setRowCount(0);
        poTableModel.setRowCount(0);
        totalRevenueTxt.setText("");
        totalExpensesTxt.setText("");
        profitTxt.setText("");
        lossTxt.setText("");

        List<SalesData> salesList = salesDataViewer.getSalesData();
        List<PurchaseOrder> poList = purchaseOrderViewer.getAllPOs();

        double totalRevenue = 0;
        int salesRowCount = 0;
        for (SalesData sale : salesList) {
            if (sale != null && isInMonth(sale.getDate(), selectedMonth, year)) {
                salesTableModel.addRow(new Object[] {
                    sale.getSalesId(),
                    sale.getItemCode(),
                    sale.getRetailPrice(),
                    sale.getQuantitySold(),
                    sale.getTotalAmount(),
                    sale.getDate()
                });
                totalRevenue += sale.getTotalAmount();
                salesRowCount++;
            }
        }
        System.out.println("Initial sales table rows added: " + salesRowCount);

        double totalExpenses = 0;
        int poRowCount = 0;
        for (PurchaseOrder po : poList) {
            if (po != null && po.getStatus().equals(Status.PAID) && isInMonth(po.getRequiredDate(), selectedMonth, year)) {
                poTableModel.addRow(new Object[] {
                    po.getPoId(),
                    po.getItemCode(),
                    po.getQuantity(),
                    po.getSupplierCode(),
                    po.getStatus(),
                    po.getPaymentAmount(),
                    po.getRequiredDate(),
                    po.getRemark()
                });
                totalExpenses += po.getPaymentAmount();
                poRowCount++;
            }
        }
        System.out.println("Initial PO table rows added: " + poRowCount);

        totalRevenueTxt.setText(String.format("%.2f", totalRevenue));
        totalExpensesTxt.setText(String.format("%.2f", totalExpenses));

        double profitLoss = financeController.calculateProfitLoss(salesDataViewer.getSalesData(), purchaseOrderViewer.getAllPOs(), selectedMonth, year);
        if (profitLoss >= 0) {
            profitTxt.setText(String.format("%.2f", profitLoss));
            lossTxt.setText("0.00");
        } else {
            profitTxt.setText("0.00");
            lossTxt.setText(String.format("%.2f", Math.abs(profitLoss)));
        }

        salesTable.repaint();
        poTable.repaint();
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
        totalRevenueTxt = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        profitTxt = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        totalExpensesTxt = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        lossTxt = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        salesTable = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        poTable = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        genFinReportBtn = new javax.swing.JButton();
        finReportMonthChooser = new com.toedter.calendar.JMonthChooser();
        backBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        totalRevenueTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totalRevenueTxtActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 24)); // NOI18N
        jLabel2.setText("Financial Summary:");

        jLabel3.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 18)); // NOI18N
        jLabel3.setText("Total Revenue:");

        jLabel6.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 18)); // NOI18N
        jLabel6.setText("Profit:");

        profitTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                profitTxtActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 18)); // NOI18N
        jLabel4.setText("Loss:");

        totalExpensesTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totalExpensesTxtActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 18)); // NOI18N
        jLabel5.setText("Total Expenses:");

        lossTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lossTxtActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(totalRevenueTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(totalExpensesTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel4)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(lossTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel6)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(profitTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(totalRevenueTxt)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totalExpensesTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 65, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(profitTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lossTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addContainerGap())
        );

        salesTable.setModel(salesTableModel);
        jScrollPane1.setViewportView(salesTable);

        jLabel7.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 24)); // NOI18N
        jLabel7.setText("Sales Details:");

        poTable.setModel(poTableModel);
        jScrollPane2.setViewportView(poTable);

        jLabel8.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 24)); // NOI18N
        jLabel8.setText("Purchase Order Details:");

        jLabel1.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 36)); // NOI18N
        jLabel1.setText("Financial Report");

        genFinReportBtn.setText("Generate");
        genFinReportBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                genFinReportBtnActionPerformed(evt);
            }
        });

        backBtn.setText("Back");
        backBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1255, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8)
                            .addComponent(jLabel1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(finReportMonthChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(genFinReportBtn)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(backBtn)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(jLabel7))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(genFinReportBtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(finReportMonthChooser, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(backBtn)
                .addContainerGap(37, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void totalRevenueTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totalRevenueTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_totalRevenueTxtActionPerformed

    private void lossTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lossTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lossTxtActionPerformed

    private void profitTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_profitTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_profitTxtActionPerformed

    private void totalExpensesTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totalExpensesTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_totalExpensesTxtActionPerformed

    private void genFinReportBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_genFinReportBtnActionPerformed
        int selectedMonth = finReportMonthChooser.getMonth(); 
        int year = 2025; 

        salesTableModel.setRowCount(0);
        poTableModel.setRowCount(0);
        totalRevenueTxt.setText("");
        totalExpensesTxt.setText("");
        profitTxt.setText("");
        lossTxt.setText("");

        List<SalesData> salesList = salesDataViewer.getSalesData();
        List<PurchaseOrder> poList = purchaseOrderViewer.getAllPOs();

        double totalRevenue = 0;
        int salesRowCount = 0;
        for (SalesData sale : salesList) {
            if (sale != null && isInMonth(sale.getDate(), selectedMonth, year)) {
                salesTableModel.addRow(new Object[] {
                    sale.getSalesId(),
                    sale.getItemCode(),
                    sale.getRetailPrice(),
                    sale.getQuantitySold(),
                    sale.getTotalAmount(),
                    sale.getDate()
                });
                totalRevenue += sale.getTotalAmount();
                salesRowCount++;
            }
        }
        System.out.println("Sales table rows added: " + salesRowCount);

        double totalExpenses = 0;
        int poRowCount = 0;
        for (PurchaseOrder po : poList) {
            if (po != null && po.getStatus().equals(Status.PAID) && isInMonth(po.getRequiredDate(), selectedMonth, year)) {
                poTableModel.addRow(new Object[] {
                    po.getPoId(),
                    po.getItemCode(),
                    po.getQuantity(),
                    po.getSupplierCode(),
                    po.getStatus(),
                    po.getPaymentAmount(),
                    po.getRequiredDate(),
                    po.getRemark()
                });
                totalExpenses += po.getPaymentAmount();
                poRowCount++;
            }
        }
        System.out.println("PO table rows added: " + poRowCount);

        totalRevenueTxt.setText(String.format("%.2f", totalRevenue));
        totalExpensesTxt.setText(String.format("%.2f", totalExpenses));

        double profitLoss = financeController.calculateProfitLoss(salesDataViewer.getSalesData(), purchaseOrderViewer.getAllPOs(), selectedMonth, year);
        if (profitLoss >= 0) {
            profitTxt.setText(String.format("%.2f", profitLoss));
            lossTxt.setText("0.00");
        } else {
            profitTxt.setText("0.00");
            lossTxt.setText(String.format("%.2f", Math.abs(profitLoss)));
        }

        salesTable.repaint();
        poTable.repaint();
    }//GEN-LAST:event_genFinReportBtnActionPerformed

    private void backBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backBtnActionPerformed
        if (this.previousScreen != null) {
            this.previousScreen.setVisible(true); // Just make the existing one visible
        } else {
            // Fallback or error: Should not happen if previousScreen is always passed
            JOptionPane.showMessageDialog(this, "Error: Previous screen reference lost.", "Navigation Error", JOptionPane.ERROR_MESSAGE);
            // Optionally, recreate Login if truly lost
            // new Login().setVisible(true);
        }
        this.dispose();
    }//GEN-LAST:event_backBtnActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
      
    }
    
    private boolean isInMonth(String date, int month, int year) {
        try {
            String[] parts = date.contains("-") ? date.split("-") : null;
            if (parts == null || parts.length < 3) return false;

            int dateMonth, dateYear;
            if (parts[0].length() == 4) { 
                dateYear = Integer.parseInt(parts[0]);
                dateMonth = Integer.parseInt(parts[1]);
            } else { 
                dateYear = Integer.parseInt(parts[2]);
                dateMonth = Integer.parseInt(parts[1]);
            }

            return dateMonth == (month + 1) && dateYear == year;
        } catch (Exception e) {
            System.err.println("Error parsing date: " + date);
            return false;
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backBtn;
    private com.toedter.calendar.JMonthChooser finReportMonthChooser;
    private javax.swing.JButton genFinReportBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField lossTxt;
    private javax.swing.JTable poTable;
    private javax.swing.JTextField profitTxt;
    private javax.swing.JTable salesTable;
    private javax.swing.JTextField totalExpensesTxt;
    private javax.swing.JTextField totalRevenueTxt;
    // End of variables declaration//GEN-END:variables
}
