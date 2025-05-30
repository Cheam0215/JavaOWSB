/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package OSWB;

import Controllers.ItemController;
import Controllers.PurchaseRequisitionController;
import Entities.Item;
import Utility.FileManager;
import Entities.User;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JTable;
/**
 *
 * @author Edwin Chen
 */
public class SM_Item extends javax.swing.JFrame {
    private final DefaultTableModel model = new DefaultTableModel();
    private final User currentUser;
    private final ItemController itemController;
    private final String columnName[]= {"Item Code","Item Name","Stock Level","Retail Price"};
    private FileManager fileManager;
    private boolean isEditing = false; // Track if we're in editing mode
    private String editingItemCode = null; // Track the item code being edited
    private final JFrame previousScreen;
    private final PurchaseRequisitionController purchaseRequisitionController;


    
    public SM_Item(User currentUser, ItemController itemController, PurchaseRequisitionController purchaseRequisitionController,JFrame previousScreen) {
        this.currentUser = currentUser;
        this.itemController = itemController;
        this.purchaseRequisitionController = purchaseRequisitionController;
        fileManager = new FileManager();
        this.previousScreen = previousScreen;
        initComponents();
        initializeFileManager();
        setupTable();
        loadItems();
        setupDeleteButtonListener();
        setupTableSelectionListener(); 
        addButton.setEnabled(true);
        editBtn.setEnabled(false); 
        saveBtn.setEnabled(false); 
        
    }
    
    private void setupTable() {
        model.setColumnIdentifiers(columnName);
        jTable1.setModel(model);
        jTable1.setDefaultRenderer(Object.class, new CustomTableCellRenderer());
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
                CreatePR.setEnabled(rowSelected); // Enable Create Purchase Requisition only if a row is selected
            }
        });
    }
    
    private class CustomTableCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, 
                                                      boolean hasFocus, int row, int column) {
            Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            // Get the stock level from the Stock Level column (column index 2)
            Object stockLevelObj = table.getValueAt(row, 2); // Column 2 is Stock Level
            int stockLevel = 0;
            try {
                stockLevel = Integer.parseInt(stockLevelObj.toString());
            } catch (NumberFormatException e) {
                System.out.println("Error parsing stock level: " + stockLevelObj);
            }

            // Set red background if stock level is less than 100
            if (stockLevel < 100) {
                cell.setBackground(Color.RED);
                cell.setForeground(Color.WHITE); // Make text white for contrast
            } else {
                cell.setBackground(table.getBackground()); // Reset to default background
                cell.setForeground(table.getForeground()); // Reset to default foreground
            }

            // Handle selection color
            if (isSelected) {
                cell.setBackground(table.getSelectionBackground());
                cell.setForeground(table.getSelectionForeground());
            }

            return cell;
        }
    }
    
    
    
    private void loadItems() {
        try {
            model.setRowCount(0);
            List<String[]> items = itemController.viewItems();
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

        if (searchTerm.isEmpty()) {
            loadItems();
            return;
        }
        
        try {
            model.setRowCount(0);
            List<String[]> allItems = itemController.viewItems();
            boolean foundMatch = false;
            for (String[] item : allItems) {
                if ((item[0] != null && item[0].toLowerCase().contains(searchTerm.toLowerCase())) ||
                    (item[1] != null && item[1].toLowerCase().contains(searchTerm.toLowerCase()))) {
                    model.addRow(item);
                    foundMatch = true;
                }
            }

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
        jLabel2.setText(generateNextItemCode());
        jTextField3.setText("");
        jTextField5.setText("");
        jTextField2.setText("");
        jTextField4.setText("");
        isEditing = false;
        editingItemCode = null;
        loadItems();
        addButton.setEnabled(true);
        editBtn.setEnabled(jTable1.getSelectedRow() != -1);
        saveBtn.setEnabled(false);
        deleteBtn.setEnabled(jTable1.getSelectedRow() != -1);
    }
    
    private void initializeFileManager() {
        if (fileManager == null) {
            fileManager = new FileManager();
        }
        String nextItemCode = generateNextItemCode();
        jLabel2.setText(nextItemCode);
        loadSupplierCodes();
    }
    
    private void loadSupplierCodes() {
        try {
            jComboBox1.removeAllItems();
            
            if (fileManager == null) {
                fileManager = new FileManager();
            }
            
            List<String> suppliers = fileManager.readFile(
                fileManager.getSupplierFilePath(), 
                line -> line
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
        String nextItemCode = "ITEM001";
        try {
            if (fileManager == null) {
                fileManager = new FileManager();
            }
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
    
    private String[] getItemSupplyDetails(String itemCode) {
        try {
            String filePath = fileManager.getItemSupplyFilePath();
            String absolutePath = new File("").getAbsolutePath() + File.separator + "src" + File.separator + filePath.replace("/", File.separator);
            List<String> lines = Files.readAllLines(Paths.get(absolutePath));
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length >= 4 && parts[0].equals(itemCode)) {
                    return new String[]{parts[1], parts[3]}; // [supplierCode, unitPrice]
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error fetching item supply details: " + e.getMessage(), 
                                         "Error", JOptionPane.ERROR_MESSAGE);
        }
        return new String[]{"SUP001", "0"}; // Default values if not found
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
        jLabel9 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        ResetBtn = new javax.swing.JButton();
        jTextField5 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        CreatePR = new javax.swing.JButton();

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
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
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
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jLabel9.setText("RM");

        ResetBtn.setText("Reset");
        ResetBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ResetBtnActionPerformed(evt);
            }
        });

        jLabel2.setText("jLabel2");

        jLabel7.setText("Unit Price :");

        jLabel10.setText("RM");

        jButton2.setText("Find Low Stock");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(255, 51, 51));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel11.setText("Low Stock");

        CreatePR.setText("Create Purchase Requisition");
        CreatePR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CreatePRActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(addButton)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel7))
                                .addGap(34, 34, 34)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(13, 13, 13)
                                        .addComponent(editBtn)
                                        .addGap(50, 50, 50)
                                        .addComponent(saveBtn)
                                        .addGap(51, 51, 51)
                                        .addComponent(deleteBtn))
                                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(32, 32, 32))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(CreatePR)
                                .addGap(41, 41, 41)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(SearchBtn)
                                .addGap(18, 18, 18)
                                .addComponent(ResetBtn)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 139, Short.MAX_VALUE)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel11)
                                .addGap(18, 18, 18)
                                .addComponent(jButton2)))))
                .addGap(50, 50, 50))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(addButton)
                            .addComponent(editBtn)
                            .addComponent(saveBtn)
                            .addComponent(deleteBtn))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(45, 45, 45)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel2))
                                .addGap(47, 47, 47)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel4)
                                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(42, 42, 42)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel5)
                                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(40, 40, 40)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel6)
                                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(47, 47, 47)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel9)
                                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(48, 48, 48)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel10)
                                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(CreatePR))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(SearchBtn)
                                .addComponent(ResetBtn))
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11)
                            .addComponent(jButton2))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 558, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(48, Short.MAX_VALUE))
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

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        String itemCode = jLabel2.getText();
        String itemName = jTextField3.getText().trim();
        String stockLevelStr = jTextField5.getText().trim();
        String retailPriceStr = jTextField2.getText().trim();
        String unitPriceStr = jTextField4.getText().trim();
        String supplierCode = (String) jComboBox1.getSelectedItem();

        if (itemName.isEmpty() || stockLevelStr.isEmpty() || retailPriceStr.isEmpty() || 
            unitPriceStr.isEmpty() || supplierCode == null || supplierCode.equals("No suppliers found")) {
            JOptionPane.showMessageDialog(this, "Please fill all fields and select a valid supplier.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int stockLevel = Integer.parseInt(stockLevelStr);
            double retailPrice = Double.parseDouble(retailPriceStr);
            double unitPrice = Double.parseDouble(unitPriceStr);

            // Validate positive values
            if (stockLevel <= 0) {
                JOptionPane.showMessageDialog(this, "Stock level must be a positive integer.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (retailPrice <= 0) {
                JOptionPane.showMessageDialog(this, "Retail price must be a positive number.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (unitPrice <= 0) {
                JOptionPane.showMessageDialog(this, "Unit price must be a positive number.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (retailPrice <= unitPrice) {
                JOptionPane.showMessageDialog(this, "Retail price must be larger than unit price.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }   

            Item item = new Item(itemCode, itemName, stockLevel, retailPrice);
            itemController.addItem(item, supplierCode, unitPrice); // Use unitPrice from jTextField4
            JOptionPane.showMessageDialog(this, "Item added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            resetTable();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Stock level must be a positive integer, unit price and retail price must be positive numbers.", "Error", JOptionPane.ERROR_MESSAGE);
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

        String itemCode = jTable1.getValueAt(selectedRow, 0).toString();
        int dialogResult = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete item " + itemCode + "?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (dialogResult != JOptionPane.YES_OPTION) {
            return;
        }

        String result = itemController.deleteItem(itemCode); // Capture the string result
        if (result.startsWith("Item ") && result.endsWith(" deleted successfully.")) {
            JOptionPane.showMessageDialog(this, "Item deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadItems(); // Refresh the table
            jTextField3.setText(""); // Clear item name field (assuming jTextField3 is for item name)
            jTextField2.setText(""); // Clear stock level field (assuming jTextField2 is for stock)
            jTextField4.setText(""); // Clear unit price field
            resetTable();
        } else {
            JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_deleteBtnActionPerformed

    private void editBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editBtnActionPerformed
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item to edit.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        isEditing = true;
        editingItemCode = (String) model.getValueAt(selectedRow, 0); // Item Code
        jLabel2.setText(editingItemCode);
        jTextField3.setText((String) model.getValueAt(selectedRow, 1)); // Item Name
        jTextField5.setText(model.getValueAt(selectedRow, 2).toString()); // Stock Level
        jTextField2.setText(model.getValueAt(selectedRow, 3).toString()); // Retail Price
        
        // Fetch supplierCode and unitPrice from itemSupply.txt
        String[] supplyDetails = getItemSupplyDetails(editingItemCode);
        jComboBox1.setSelectedItem(supplyDetails[0]); // Supplier Code
        jTextField4.setText(supplyDetails[1]); // Unit Price

        addButton.setEnabled(false);
        editBtn.setEnabled(false);
        saveBtn.setEnabled(true);
        deleteBtn.setEnabled(false);
    }//GEN-LAST:event_editBtnActionPerformed

    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnActionPerformed
        if (!isEditing || editingItemCode == null) {
            JOptionPane.showMessageDialog(this, "No item is being edited.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String itemCode = jLabel2.getText();
        String itemName = jTextField3.getText().trim();
        String stockLevelStr = jTextField5.getText().trim();
        String retailPriceStr = jTextField2.getText().trim();
        String unitPriceStr = jTextField4.getText().trim();
        String supplierCode = (String) jComboBox1.getSelectedItem();

        if (itemName.isEmpty() || stockLevelStr.isEmpty() || retailPriceStr.isEmpty() || 
            unitPriceStr.isEmpty() || supplierCode == null || supplierCode.equals("No suppliers found")) {
            JOptionPane.showMessageDialog(this, "Please fill all fields and select a valid supplier.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int stockLevel = Integer.parseInt(stockLevelStr);
            double retailPrice = Double.parseDouble(retailPriceStr);
            double unitPrice = Double.parseDouble(unitPriceStr); 

            // Validate positive values
            if (stockLevel <= 0) {
                JOptionPane.showMessageDialog(this, "Stock level must be a positive integer.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (retailPrice <= 0) {
                JOptionPane.showMessageDialog(this, "Retail price must be a positive number.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (unitPrice <= 0) {
                JOptionPane.showMessageDialog(this, "Unit price must be a positive number.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (retailPrice <= unitPrice) {
                JOptionPane.showMessageDialog(this, "Retail price must be larger than unit price.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Item updatedItem = new Item(itemCode, itemName, stockLevel, retailPrice);

            // Assuming updateItem returns a string indicating success or error
            String result = itemController.updateItem(updatedItem, supplierCode, unitPrice);
            if (result.startsWith("Item ") && result.endsWith(" updated successfully.")) {
                JOptionPane.showMessageDialog(this, "Item updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                resetTable();
                isEditing = false;
                editingItemCode = null; // Reset editing state
                resetTable();
            } else {
                JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Stock level must be a positive integer, and retail price and unit price must be positive numbers.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_saveBtnActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            model.setRowCount(0); // Clear the table
            List<String[]> allItems = itemController.viewItems();
            boolean foundLowStock = false;

            for (String[] item : allItems) {
                // Assuming item[2] is the stock level
                int stockLevel = Integer.parseInt(item[2]);
                if (stockLevel < 100) {
                    model.addRow(item);
                    foundLowStock = true;
                }
            }

            if (!foundLowStock) {
                JOptionPane.showMessageDialog(this, 
                    "No items with stock level below 100 found.", 
                    "Low Stock Search", 
                    JOptionPane.INFORMATION_MESSAGE);
                // Reload all items if no low stock items are found
                loadItems();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error searching low stock items: " + e.getMessage(), 
                "Search Error", 
                JOptionPane.ERROR_MESSAGE);
            loadItems();
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void CreatePRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CreatePRActionPerformed
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item to create a purchase requisition.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Get the data from the selected row
        String itemCode = (String) model.getValueAt(selectedRow, 0); // Item Code (column 0)
        String itemName = (String) model.getValueAt(selectedRow, 1); // Item Name (column 1)
        String stockLevel = model.getValueAt(selectedRow, 2).toString(); // Stock Level (column 2)

        // Open the SM_PR page and pass the selected item's details, reusing the existing purchaseRequisitionController
        SM_PR smPrPage = new SM_PR(currentUser, purchaseRequisitionController, this, itemCode, itemName, stockLevel);
        smPrPage.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_CreatePRActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
               
     
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CreatePR;
    private javax.swing.JButton ResetBtn;
    private javax.swing.JButton SearchBtn;
    private javax.swing.JButton addButton;
    private javax.swing.JButton deleteBtn;
    private javax.swing.JButton editBtn;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
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
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JButton saveBtn;
    // End of variables declaration//GEN-END:variables
}
