/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package OSWB;

import Entities.Administrator;
import Entities.User;
import Utility.UserRoles;
import java.util.List;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.table.DefaultTableModel;
import java.awt.Color; // For placeholder text color
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent; // For live search
import javax.swing.event.DocumentListener; // For live search

/**
 *
 * @author Sheng Ting
 */
public class ADMIN_USER extends javax.swing.JFrame {
    private final Administrator loggedInAdmin;
    private DefaultTableModel tableModel; 
    private final String[] columnNames = {"ID", "Username", "Role"}; 
    private final String SEARCH_PLACEHOLDER = "Search by Username or ID";

    /**
     * Creates new form ADMIN_USER
     * @param loggedInAdmin
     */    
     public ADMIN_USER(Administrator loggedInAdmin) {
        this.loggedInAdmin = loggedInAdmin;
        initComponents(); 
        setupTable();
        setupSearchAndFilter(); 
        loadUserTable(); 
        this.setTitle("User Management - Logged in as: " + loggedInAdmin.getUsername());
        
        SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
            userTable.requestFocusInWindow();
        }
    });
    }
     
     private void setupSearchAndFilter() {
        // Placeholder for searchField
        searchField.setText(SEARCH_PLACEHOLDER);
        searchField.setForeground(Color.GRAY);

        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals(SEARCH_PLACEHOLDER)) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setForeground(Color.GRAY);
                    searchField.setText(SEARCH_PLACEHOLDER);
                }
            }
        });

        // Live search listener for searchField
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { applyFilters(); }
            @Override
            public void removeUpdate(DocumentEvent e) { applyFilters(); }
            @Override
            public void changedUpdate(DocumentEvent e) { applyFilters(); } // For style changes, less common for plain text
        });

        // Populate filterRoles ComboBox
        filterRoles.addItem("All"); // Default "All" option
        for (UserRoles role : UserRoles.values()) {
            if (role != UserRoles.ADMINISTRATOR) { // Exclude ADMINISTRATOR
                filterRoles.addItem(role.name());
            }
        }
        filterRoles.setSelectedItem("All"); // Set default selection

        // Add action listener to filterRoles ComboBox
        // The filterRolesActionPerformed will call applyFilters()
    }
        
    
     private void setupTable() {
        tableModel = new DefaultTableModel(null, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make table cells non-editable
                return false;
            }
        };
        userTable.setModel(tableModel);
    }
     
    // New method to display a given list of users
    private void displayUsersInTable(List<User> usersToDisplay) {
        tableModel.setRowCount(0); // Clear existing rows
        for (User user : usersToDisplay) {
            // We assume usersToDisplay already excludes Admins if that's the general rule
            // or the filtering logic before calling this handles it.
            Object[] row = new Object[3];
            row[0] = user.getUserID();
            row[1] = user.getUsername();
            row[2] = user.getRole().name();
            tableModel.addRow(row);
        }
    }

    private void loadUserTable() {
        // Clear existing rows
        tableModel.setRowCount(0);

        List<User> users = loggedInAdmin.getAllUsers();

        for (User user : users) {
            // We only want to display non-admin users as per your requirement
            if (user.getRole() != UserRoles.ADMINISTRATOR) {
                Object[] row = new Object[3];
                row[0] = user.getUserID();
                row[1] = user.getUsername();
                row[2] = user.getRole().name(); // Or user.getRole().toString()
                tableModel.addRow(row);
            }
        }
    }
    
    private void applyFilters() {
        String searchText = searchField.getText().trim().toLowerCase();
        if (searchText.equals(SEARCH_PLACEHOLDER.toLowerCase())) {
            searchText = ""; // Treat placeholder as empty search
        }

        String selectedRoleString = (String) filterRoles.getSelectedItem();
        UserRoles selectedRole = null;
        if (selectedRoleString != null && !selectedRoleString.equals("All")) {
            try {
                selectedRole = UserRoles.valueOf(selectedRoleString);
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid role in filter: " + selectedRoleString);
                // Optionally, reset to "All" or show an error
            }
        }

        // Get all non-admin users once
        List<User> allNonAdminUsers = loggedInAdmin.getAllUsers().stream()
                                            .filter(user -> user.getRole() != UserRoles.ADMINISTRATOR)
                                            .collect(java.util.stream.Collectors.toList());

        // Apply filters
        List<User> filteredUsers = new java.util.ArrayList<>();
        for (User user : allNonAdminUsers) {
            boolean matchesSearch = true;
            if (!searchText.isEmpty()) {
                matchesSearch = user.getUsername().toLowerCase().contains(searchText) ||
                                user.getUserID().toLowerCase().contains(searchText);
            }

            boolean matchesRole = true;
            if (selectedRole != null) {
                matchesRole = user.getRole() == selectedRole;
            }

            if (matchesSearch && matchesRole) {
                filteredUsers.add(user);
            }
    }
        displayUsersInTable(filteredUsers); // Update table with filtered list
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel1 = new java.awt.Panel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        userTable = new javax.swing.JTable();
        updateButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        createButton = new javax.swing.JButton();
        panel2 = new java.awt.Panel();
        backButton = new javax.swing.JButton();
        searchField = new javax.swing.JTextField();
        filterRoles = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        panel1.setBackground(new java.awt.Color(153, 204, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Users");

        javax.swing.GroupLayout panel1Layout = new javax.swing.GroupLayout(panel1);
        panel1.setLayout(panel1Layout);
        panel1Layout.setHorizontalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 522, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(912, Short.MAX_VALUE))
        );
        panel1Layout.setVerticalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                .addContainerGap())
        );

        userTable.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(userTable);

        updateButton.setText("Update");
        updateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateButtonActionPerformed(evt);
            }
        });

        deleteButton.setText("Delete");
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        createButton.setText("Create new");
        createButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createButtonActionPerformed(evt);
            }
        });

        panel2.setBackground(new java.awt.Color(153, 204, 255));

        backButton.setText("Back");
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel2Layout = new javax.swing.GroupLayout(panel2);
        panel2.setLayout(panel2Layout);
        panel2Layout.setHorizontalGroup(
            panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel2Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(backButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panel2Layout.setVerticalGroup(
            panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel2Layout.createSequentialGroup()
                .addContainerGap(45, Short.MAX_VALUE)
                .addComponent(backButton)
                .addGap(28, 28, 28))
        );

        searchField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchFieldActionPerformed(evt);
            }
        });

        filterRoles.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {}));
        filterRoles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filterRolesActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Roles : ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(63, 63, 63)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(filterRoles, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1087, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(updateButton)
                                    .addComponent(deleteButton))
                                .addGap(124, 124, 124))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(createButton, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(104, 104, 104))))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(filterRoles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(updateButton)
                        .addGap(81, 81, 81)
                        .addComponent(deleteButton)
                        .addGap(79, 79, 79)
                        .addComponent(createButton)
                        .addGap(151, 151, 151))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(48, 48, 48)))
                .addComponent(panel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void updateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateButtonActionPerformed
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user from the table to update.", "No User Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String userIdToUpdate = (String) tableModel.getValueAt(selectedRow, 0);
        String currentUsername = (String) tableModel.getValueAt(selectedRow, 1);
        UserRoles currentRole = UserRoles.valueOf((String) tableModel.getValueAt(selectedRow, 2));

        // --- Let admin choose what to update ---
        String[] updateOptions = {"Username", "Password", "Role"};
        String chosenOption = (String) JOptionPane.showInputDialog(
                this,
                "What would you like to update for user: " + currentUsername + " (ID: " + userIdToUpdate + ")?",
                "Select Update Action",
                JOptionPane.QUESTION_MESSAGE,
                null,
                updateOptions,
                updateOptions[0] // Default selection
        );

        if (chosenOption == null) {
            return; // Admin cancelled
        }

        // Initialize variables to hold new values; null means no change intended for that field
        String newUsername = null;
        String newPassword = null;
        UserRoles newRole = null;
        boolean changeAttempted = false; // To track if any update was actually processed

        // --- Process based on chosen option ---
        switch (chosenOption) {
            case "Username" -> {
                String usernameInput = JOptionPane.showInputDialog(this, "Enter new username:", currentUsername);
                if (usernameInput != null && !usernameInput.trim().isEmpty() && !usernameInput.trim().equals(currentUsername)) {
                    newUsername = usernameInput.trim();
                    changeAttempted = true;
                } else if (usernameInput == null) { // Cancelled
                    JOptionPane.showMessageDialog(this, "Username update cancelled.", "Info", JOptionPane.INFORMATION_MESSAGE);
                    return;
                } else { // Entered same username or empty
                    JOptionPane.showMessageDialog(this, "No change to username.", "Info", JOptionPane.INFORMATION_MESSAGE);
                }
            }

            case "Password" -> {
                JPasswordField pf1 = new JPasswordField(20);
                JPasswordField pf2 = new JPasswordField(20);
                JLabel label1 = new JLabel("New Password:");
                JLabel label2 = new JLabel("Confirm New Password:");
                Box box = Box.createVerticalBox();
                box.add(label1); box.add(pf1);
                box.add(Box.createVerticalStrut(15));
                box.add(label2); box.add(pf2);

                int okCxl = JOptionPane.showConfirmDialog(this, box, "Set New Password for " + currentUsername, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (okCxl == JOptionPane.OK_OPTION) {
                    String pass1 = String.valueOf(pf1.getPassword());
                    String pass2 = String.valueOf(pf2.getPassword());
                    if (pass1.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "New password cannot be empty.", "Password Error", JOptionPane.ERROR_MESSAGE); return;
                    }
                    if (!pass1.equals(pass2)) {
                        JOptionPane.showMessageDialog(this, "Passwords do not match.", "Password Error", JOptionPane.ERROR_MESSAGE); return;
                    }
                    if (pass1.length() < 6) {
                        JOptionPane.showMessageDialog(this, "Password must be at least 6 characters long.", "Password Error", JOptionPane.ERROR_MESSAGE); return;
                    }
                    newPassword = pass1;
                    changeAttempted = true;
                } else {
                    JOptionPane.showMessageDialog(this, "Password update cancelled.", "Info", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            }

            case "Role" -> {
                String[] availableRoles = java.util.Arrays.stream(UserRoles.values())
                        .filter(r -> r != UserRoles.ADMINISTRATOR)
                        .map(Enum::name)
                        .toArray(String[]::new);
                String newRoleString = (String) JOptionPane.showInputDialog(this, "Select new role:",
                        "Update Role for " + currentUsername, JOptionPane.QUESTION_MESSAGE, null,
                        availableRoles, currentRole.name());
                if (newRoleString != null && !UserRoles.valueOf(newRoleString).equals(currentRole)) {
                    newRole = UserRoles.valueOf(newRoleString);
                    changeAttempted = true;
                } else if (newRoleString == null) { // Cancelled
                    JOptionPane.showMessageDialog(this, "Role update cancelled.", "Info", JOptionPane.INFORMATION_MESSAGE);
                    return;
                } else { // Selected same role
                    JOptionPane.showMessageDialog(this, "No change to role.", "Info", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }

        // If no change was actually attempted (e.g., user cancelled all individual prompts or entered same values)
        if (!changeAttempted) {
            // Message was already shown for individual cancellations or no changes.
            // If choosing "Username" but then cancelling the username input, this path won't be hit due to early returns.
            // This check is more for a scenario where an option is chosen but results in no effective change.
            // System.out.println("No effective changes to apply.");
            return;
        }

        // --- Call the update method in Administrator class ---
        String resultMessage = loggedInAdmin.updateUser(userIdToUpdate, newUsername, newPassword, newRole);

        if (resultMessage == null) {
            JOptionPane.showMessageDialog(this, "User ID: " + userIdToUpdate + " updated successfully.", "Update Successful", JOptionPane.INFORMATION_MESSAGE);
            loadUserTable(); // Refresh the table
        } else {
            JOptionPane.showMessageDialog(this, "Error updating user: " + resultMessage, "Update Failed", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_updateButtonActionPerformed

    private void createButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createButtonActionPerformed
        ADMIN_CREATE_USER adminCreate = new ADMIN_CREATE_USER(loggedInAdmin);
        this.dispose();
        adminCreate.setVisible(true);
        
    }//GEN-LAST:event_createButtonActionPerformed

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        ADMIN_DASHBOARD adminDashboard  = new ADMIN_DASHBOARD(loggedInAdmin);
        this.dispose();
        adminDashboard.setVisible(true);
        
    }//GEN-LAST:event_backButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
         int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user from the table to delete.", "No User Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String userIdToDelete = (String) tableModel.getValueAt(selectedRow, 0); // Assuming ID is in the first column

        // Confirmation dialog
        int confirmation = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete user ID: " + userIdToDelete + "?",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirmation == JOptionPane.YES_OPTION) {
            String resultMessage = loggedInAdmin.deleteUser(userIdToDelete);
            if (resultMessage == null) {
                JOptionPane.showMessageDialog(this, "User ID: " + userIdToDelete + " deleted successfully.", "Deletion Successful", JOptionPane.INFORMATION_MESSAGE);
                loadUserTable(); // Refresh the table
            } else {
                JOptionPane.showMessageDialog(this, "Error deleting user: " + resultMessage, "Deletion Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void searchFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchFieldActionPerformed
         applyFilters();
    }//GEN-LAST:event_searchFieldActionPerformed

    private void filterRolesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filterRolesActionPerformed
        applyFilters();
    }//GEN-LAST:event_filterRolesActionPerformed

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
            java.util.logging.Logger.getLogger(ADMIN_USER.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ADMIN_USER.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ADMIN_USER.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ADMIN_USER.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backButton;
    private javax.swing.JButton createButton;
    private javax.swing.JButton deleteButton;
    private javax.swing.JComboBox<String> filterRoles;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private java.awt.Panel panel1;
    private java.awt.Panel panel2;
    private javax.swing.JTextField searchField;
    private javax.swing.JButton updateButton;
    private javax.swing.JTable userTable;
    // End of variables declaration//GEN-END:variables
}
