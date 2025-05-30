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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Edwin Chen
 */
public class SM_PR extends javax.swing.JFrame {
    private final DefaultTableModel model = new DefaultTableModel();
    private final String columnName[] = {"Purchase Requisition ID", "Item Code", "Item Name", "Stock Level", "Requested By", "Quantity", "Required Date", "Requested Date", "Status"};
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
    public SM_PR(User currentUser, PurchaseRequisitionController purchaseRequisitionController, JFrame previousScreen, String itemCode, String itemName, String stockLevel) {
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
        setupTableSelectionListener();
        editBtn.setEnabled(false);
        saveBtn.setEnabled(false);

        // Pre-fill the fields with the passed data
        if (itemCode != null && !itemCode.isEmpty()) {
            jComboBox1.setSelectedItem(itemCode); // Set the Item Code in the combo box
            jLabel14.setText(itemName != null ? itemName : "Unknown"); // Set the Item Name
            jLabel16.setText(stockLevel != null ? stockLevel : "0");   // Set the Stock Level
        }
    }
    
    public SM_PR(User currentUser, PurchaseRequisitionController purchaseRequisitionController, JFrame previousScreen) {
        this(currentUser, purchaseRequisitionController, previousScreen, null, null, null);
    }
    
    private void setupTable() {
        model.setColumnIdentifiers(columnName);
        jTable1.setModel(model);
        jTable1.setDefaultRenderer(Object.class, new CustomTableCellRenderer()); // Apply the custom renderer
    }
    
    private class CustomTableCellRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, 
                                                      boolean hasFocus, int row, int column) {
            Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            // Get the stock level from the Stock Level column (column index 3)
            Object stockLevelObj = table.getValueAt(row, 3); // Column 3 is Stock Level
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
    
    private Map<String, String[]> getItemDetailsMap() {
        Map<String, String[]> itemDetailsMap = new HashMap<>();
        try {
            String filePath = new File("").getAbsolutePath() + File.separator + "src" + File.separator + fileManager.getItemFilePath().replace("/", File.separator);
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length >= 3 && parts[0] != null) { // Assuming ItemCode, ItemName, StockLevel
                    itemDetailsMap.put(parts[0], new String[]{parts[1], parts[2]}); // Map ItemCode to [ItemName, StockLevel]
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading item details: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return itemDetailsMap;
    }
    
    private void loadPR() {
        try {
            // Clear existing data from the table model
            model.setRowCount(0);

            List<String[]> PR = purchaseRequisitionController.viewPurchaseRequisition(); // Assuming viewItems returns List<String[]>
            Map<String, String[]> itemDetailsMap = getItemDetailsMap();

            if (PR.isEmpty()) {
                JOptionPane.showMessageDialog(this, "There are no Purchase Requisition available.", "Load Purchase Requisition", JOptionPane.WARNING_MESSAGE);
            } else {
                for (String[] pr : PR) {
                    String itemCode = pr[1];
                    String[] itemDetails = itemDetailsMap.getOrDefault(itemCode, new String[]{"Unknown", "0"});
                    String itemName = itemDetails[0];
                    String stockLevel = itemDetails[1];
                    String[] rowData = new String[]{
                        pr[0], // Purchase Requisition ID
                        pr[1], // Item Code
                        itemName, // Item Name
                        stockLevel, // Stock Level
                        pr[2], // Requested By
                        pr[3], // Quantity
                        pr[4], // Required Date
                        pr[5], // Requested Date
                        pr[6]  // Status
                    };
                    model.addRow(rowData);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                                          "Error loading Purchase Requisition: " + e.getMessage(),
                                          "Load Purchase Requisition Error",
                                          JOptionPane.ERROR_MESSAGE);
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
            model.setRowCount(0);
            List<String[]> allPR = purchaseRequisitionController.viewPurchaseRequisition();
            Map<String, String[]> itemDetailsMap = getItemDetailsMap();
            boolean foundMatch = false;
            
            for (String[] pr : allPR) {
                String itemCode = pr[1];
                String[] itemDetails = itemDetailsMap.getOrDefault(itemCode, new String[]{"Unknown", "0"});
                String itemName = itemDetails[0];
                String stockLevel = itemDetails[1];
                if ((pr[0] != null && pr[0].toLowerCase().contains(searchTerm.toLowerCase())) ||
                    (pr[1] != null && pr[1].toLowerCase().contains(searchTerm.toLowerCase())) ||
                    (itemName.toLowerCase().contains(searchTerm.toLowerCase())) ||
                    (stockLevel.toLowerCase().contains(searchTerm.toLowerCase())) ||
                    (pr[4] != null && pr[4].toLowerCase().contains(searchTerm.toLowerCase())) ||
                    (pr[6] != null && pr[6].toLowerCase().contains(searchTerm.toLowerCase()))) {
                    String[] rowData = new String[]{
                        pr[0], // Purchase Requisition ID
                        pr[1], // Item Code
                        itemName, // Item Name
                        stockLevel, // Stock Level
                        pr[2], // Requested By
                        pr[3], // Quantity
                        pr[4], // Required Date
                        pr[5], // Requested Date
                        pr[6]  // Status
                    };
                    model.addRow(rowData);
                    foundMatch = true;
                }
            }
            
            if (!foundMatch) {
                JOptionPane.showMessageDialog(this, 
                    "No Purchase Requisition found matching '" + searchTerm + "'", 
                    "Search Results", 
                    JOptionPane.INFORMATION_MESSAGE);
                loadPR();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error searching Purchase Requisition: " + e.getMessage(), 
                "Search Error", 
                JOptionPane.ERROR_MESSAGE);
            loadPR();
        }
    }
    
    private void resetTable() {
        jTextField1.setText("");
        jLabel9.setText(generateNextPrID());
        jComboBox1.setSelectedIndex(-1); // This will trigger jComboBox1ActionPerformed to clear jLabel14 and jLabel16
        jTextField2.setText("");
        jDateChooser1.setDate(new java.util.Date());
        isEditing = false;
        editingPrId = null;
        loadPR();
        addBtn.setEnabled(true);
        editBtn.setEnabled(jTable1.getSelectedRow() != -1);
        saveBtn.setEnabled(false);
        deleteBtn.setEnabled(jTable1.getSelectedRow() != -1);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1); // Add 1 day to get tomorrow's date
        java.util.Date tomorrow = calendar.getTime();
        jDateChooser1.setDate(tomorrow);
        jDateChooser1.setMinSelectableDate(tomorrow);
    }
    
    private int getStockLevel(String itemCode) {
        try {
            String filePath = new File("").getAbsolutePath() + File.separator + "src" + File.separator + fileManager.getItemFilePath().replace("/", File.separator);
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length >= 3 && parts[0].equals(itemCode)) { // Assuming stockLevel is the 3rd field (index 2)
                    return Integer.parseInt(parts[2].trim()); // Parse stock level
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading stock levels: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid stock level format for item " + itemCode, "Error", JOptionPane.ERROR_MESSAGE);
        }
        return -1; // Return -1 if not found or error occurs
    }

    private class ItemCodeRenderer extends JLabel implements ListCellRenderer<String> {
        public ItemCodeRenderer() {
            setOpaque(true); // Make the renderer opaque to show background color
        }

        public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {
            setText(value); // Set the text to the item code

            // Default colors
            Color background = Color.WHITE;
            Color foreground = Color.BLACK;

            // Check stock level if value is not null
            if (value != null) {
                int stockLevel = getStockLevel(value);
                if (stockLevel >= 0 && stockLevel < 100) {
                    background = Color.RED; // Red background for stock < 100
                    foreground = Color.BLACK; // White text for better contrast
                }
            }

            // Apply selected colors if the item is selected
            if (isSelected) {
                background = list.getSelectionBackground();
                foreground = list.getSelectionForeground();
            }

            setBackground(background);
            setForeground(foreground);
            return this;
        }
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
            // Set the custom renderer
            jComboBox1.setRenderer(new ItemCodeRenderer());
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
        jLabel14.setText(""); // Clear Item Name initially
        jLabel16.setText(""); // Clear Stock Level initially
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

        jFrame1 = new javax.swing.JFrame();
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
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel12 = new javax.swing.JLabel();
        resetBtn = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        FindLowStock = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

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

        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jLabel13.setText("Item Name :");

        jLabel14.setText("jLabel14");

        jLabel15.setText("Stock Level : ");

        jLabel16.setText("jLabel16");

        FindLowStock.setText("Find Low Stock");
        FindLowStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FindLowStockActionPerformed(evt);
            }
        });

        jPanel8.setBackground(new java.awt.Color(255, 51, 51));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel17.setText("Low Stock");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel2)
                                    .addComponent(addBtn))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(editBtn))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(34, 34, 34)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jComboBox1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(108, 108, 108)
                                                .addComponent(saveBtn)
                                                .addGap(55, 55, 55)
                                                .addComponent(deleteBtn))
                                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addComponent(jLabel8)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
                                .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(0, 13, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(42, 42, 42)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27)
                                .addComponent(searchBtn)
                                .addGap(18, 18, 18)
                                .addComponent(resetBtn)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel17)
                                .addGap(18, 18, 18)
                                .addComponent(FindLowStock))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 887, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(32, 32, 32))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(addBtn)
                                .addComponent(editBtn)
                                .addComponent(saveBtn)
                                .addComponent(deleteBtn))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(searchBtn)
                                .addComponent(resetBtn))))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17)
                            .addComponent(FindLowStock))))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel9))
                        .addGap(40, 40, 40)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(39, 39, 39)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(jLabel14))
                        .addGap(35, 35, 35)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel15)
                                    .addComponent(jLabel16))
                                .addGap(41, 41, 41)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel11))
                                .addGap(47, 47, 47)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel5)
                                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(45, 45, 45)
                                .addComponent(jLabel6))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(181, 181, 181)
                                .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(51, 51, 51)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(jLabel7))
                        .addGap(49, 49, 49)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(jLabel12))
                        .addGap(76, 76, 76))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 558, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37))))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void addBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBtnActionPerformed
        try {
            String prId = jLabel9.getText(); // Already generated, no need to regenerate
            String itemCode = (String) jComboBox1.getSelectedItem();
            String requestedBy = jLabel11.getText();
            // Parse quantity from text field
            int quantity = Integer.parseInt(jTextField2.getText().trim());
            java.util.Date requiredUtilDate = jDateChooser1.getDate();

            // Validate required date
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
            PurchaseRequisition purchaseRequisition = new PurchaseRequisition(prId, itemCode, requestedBy, quantity, requiredDateStr, requestedDate, status);
            String result = purchaseRequisitionController.addPurchaseRequisition(purchaseRequisition);
            if (result.startsWith("Purchase requisition") && result.endsWith("added successfully.")) {
                JOptionPane.showMessageDialog(this, "Purchase Requisition added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                resetTable(); // This will handle clearing the form and refreshing the table
                loadPR(); // Refresh the table
            } else {
                JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid number format. Please enter a valid number for quantity.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Invalid date or status format: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error adding Purchase Requisition: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
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
        String requestedBy = (String) model.getValueAt(selectedRow, 4);
        String quantity = (String) model.getValueAt(selectedRow, 5);
        String requiredDateStr = (String) model.getValueAt(selectedRow, 6);
        String requestedDate = (String) model.getValueAt(selectedRow, 7);
        String status = (String) model.getValueAt(selectedRow, 8);

        // Populate the UI fields
        jLabel9.setText(editingPrId); // PR ID (read-only)
        jComboBox1.setSelectedItem(itemCode); // Item Code (editable)
        jLabel11.setText(requestedBy); // Requested By (read-only)
        jTextField2.setText(quantity); // Quantity (editable)
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
        try {
            if (!isEditing || editingPrId == null) {
                JOptionPane.showMessageDialog(this, "No Purchase Requisition is being edited.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Get updated values from the UI
            String itemCode = (String) jComboBox1.getSelectedItem();
            // Parse quantity from text field
            int quantity = Integer.parseInt(jTextField2.getText().trim());
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
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid number format. Please enter a valid number for quantity.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Invalid date or status format: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating Purchase Requisition: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        resetTable();
    }//GEN-LAST:event_saveBtnActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        String selectedItemCode = (String) jComboBox1.getSelectedItem();
        if (selectedItemCode != null) {
            Map<String, String[]> itemDetailsMap = getItemDetailsMap();
            String[] itemDetails = itemDetailsMap.getOrDefault(selectedItemCode, new String[]{"Unknown", "0"});
            jLabel14.setText(itemDetails[0]); // Set Item Name
            jLabel16.setText(itemDetails[1]); // Set Stock Level
        } else {
            jLabel14.setText(""); // Clear Item Name if no selection
            jLabel16.setText(""); // Clear Stock Level if no selection
        }
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void FindLowStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FindLowStockActionPerformed
        try {
            model.setRowCount(0); // Clear the table
            List<String[]> allPR = purchaseRequisitionController.viewPurchaseRequisition();
            Map<String, String[]> itemDetailsMap = getItemDetailsMap();
            boolean foundLowStock = false;

            for (String[] pr : allPR) {
                String itemCode = pr[1]; // Item Code is the second column (index 1)
                String[] itemDetails = itemDetailsMap.getOrDefault(itemCode, new String[]{"Unknown", "0"});
                int stockLevel = Integer.parseInt(itemDetails[1].trim()); // Stock level from itemDetails[1]
                if (stockLevel < 100) {
                    String itemName = itemDetails[0];
                    String[] rowData = new String[]{
                        pr[0], // Purchase Requisition ID
                        pr[1], // Item Code
                        itemName, // Item Name
                        itemDetails[1], // Stock Level
                        pr[2], // Requested By
                        pr[3], // Quantity
                        pr[4], // Required Date
                        pr[5], // Requested Date
                        pr[6]  // Status
                    };
                    model.addRow(rowData);
                    foundLowStock = true;
                }
            }

            if (!foundLowStock) {
                JOptionPane.showMessageDialog(this,
                    "No purchase requisitions with stock level below 100 found.",
                    "Low Stock Search",
                    JOptionPane.INFORMATION_MESSAGE);
                loadPR(); // Reload all purchase requisitions if none found
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error searching low stock purchase requisitions: " + e.getMessage(),
                "Search Error",
                JOptionPane.ERROR_MESSAGE);
            loadPR(); // Reload all purchase requisitions on error
        }
    }//GEN-LAST:event_FindLowStockActionPerformed

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
    private javax.swing.JButton FindLowStock;
    private javax.swing.JButton addBtn;
    private javax.swing.JButton deleteBtn;
    private javax.swing.JButton editBtn;
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox<String> jComboBox1;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
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
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JButton resetBtn;
    private javax.swing.JButton saveBtn;
    private javax.swing.JButton searchBtn;
    // End of variables declaration//GEN-END:variables
}
