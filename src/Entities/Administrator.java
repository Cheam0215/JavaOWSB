package Entities;

import Utility.FileManager;
import Utility.UserRoles; // Assuming UserRoles is in Utility package
// Remove static imports for UserRoles if you use UserRoles.ADMINISTRATOR etc.

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Sheng Ting
 */
public class Administrator extends User {
    private final FileManager fileManager;

    public Administrator(String userId, String username, String password) {
        super(userId, username, password, UserRoles.ADMINISTRATOR);
        this.fileManager = new FileManager(); // Or inject if you prefer
    }

    // Constructor if needed for creating an instance without an initial password (e.g., for operations)
    public Administrator(String userId, String username) {
        super(userId, username, "123456", UserRoles.ADMINISTRATOR);
        this.fileManager = new FileManager();
    }


    /**
     * Registers a new user in the system.
     * Performs validation, checks for duplicates, serializes password, and saves the user.
     *
     * @param newUsername The username for the new user.
     * @param newPassword The plain text password for the new user.
     * @param role        The role for the new user.
     * @return null if registration is successful, or an error message string if it fails.
     */
     /**
     *
     * @param newUserObject The User object containing details for the new user.
     *                     The password in this object should be plain text.
     * @return null if registration is successful, or an error message string if it fails.
     */
    public String registerUser(User newUserObject) {
        if (newUserObject == null) {
            return "User object cannot be null.";
        }

        String newUsername = newUserObject.getUsername();
        String plainTextPassword = newUserObject.getPassword(); // Assuming getPassword() on newUserObject returns plain text
        UserRoles role = newUserObject.getRole();

        // --- Business Logic Level Input Validation ---
        if (newUsername == null || newUsername.trim().isEmpty()) {
            return "Username cannot be empty.";
        }
        if (plainTextPassword == null || plainTextPassword.length() < 6) {
            return "Password must be at least 6 characters long.";
        }
        if (role == null) {
            return "A role must be specified for the new user.";
        }
        if (role == UserRoles.ADMINISTRATOR) {
            return "Cannot create an Administrator role through this function. Please select a different role.";
        }
        // --- End Business Logic Level Input Validation ---

        // Read existing users to check for duplicate username
        List<User> existingUsers = fileManager.readFile(
            fileManager.getUserFilePath(),
            line -> { // Parser logic to create User objects from file lines
                String[] data = line.split(",", -1);
                if (data.length < 4) {
                    System.err.println("Skipping invalid user data (not enough fields): " + line);
                    return null;
                }
                String storedPasswordFromFile = data[2]; // This is the storable (e.g., Base64) password
                // For comparison or creating User objects for the list, you might deserialize.
                // However, for the existingUsers list, the primary goal is just to check username.
                // The password field here for the User constructor can be the storable one.
                try {
                    return new User(
                        data[0], // id
                        data[1], // username
                        storedPasswordFromFile, // password (storable form)
                        UserRoles.valueOf(data[3]) // role
                    );
                } catch (IllegalArgumentException e) {
                    System.err.println("Error parsing user data: " + line + " | " + e.getMessage());
                    return null;
                }
            }
        ).stream().filter(Objects::nonNull).collect(Collectors.toList());

        // Check for duplicate username
        for (User existingUser : existingUsers) {
            if (existingUser.getUsername().equalsIgnoreCase(newUsername)) {
                return "Username '" + newUsername + "' already exists. Please choose a different username.";
            }
        }

        // "Serialize" the plainTextPassword from newUserObject to its storable format
        String passwordToStoreInFile;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream out = new ObjectOutputStream(baos)) {
            out.writeObject(plainTextPassword); // Serialize the plain text password
            out.flush();
            passwordToStoreInFile = Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (IOException e) {
            System.err.println("Error serializing password: " + e.getMessage());
            return "Error processing password. User not created.";
        }

        // Generate a new ID (The ID from newUserObject might be ignored or used as a suggestion)
        // It's safer to generate a new unique ID here to avoid clashes if the UI pre-populates an ID.
        int maxId = 0;
        for (User user : existingUsers) {
            try {
                int currentId = Integer.parseInt(user.getUserID());
                if (currentId > maxId) {
                    maxId = currentId;
                }
            } catch (NumberFormatException e) {
                System.err.println("Warning: Non-numeric user ID found: " + user.getUserID());
            }
        }
        String finalUserID = String.format("%03d", maxId + 1);
        // If you trust the ID from newUserObject and it's guaranteed unique by UI (less safe):
        // String finalUserID = newUserObject.getUserID();

        // Create the final user object to be saved, ensuring it has the storable password
        // and the correct role-specific type.
        User userToSave;
        switch (role) {
            case FINANCE_MANAGER -> userToSave = new FinanceManager(finalUserID, newUsername, passwordToStoreInFile);
            case INVENTORY_MANAGER -> userToSave = new InventoryManager(finalUserID, newUsername, passwordToStoreInFile);
            case PURCHASE_MANAGER -> // If PurchaseManager constructor needs more (like injected services), this needs adjustment.
                // For now, assuming a simple constructor for entity creation.
                userToSave = new PurchaseManager(finalUserID, newUsername, passwordToStoreInFile);
            case SALES_MANAGER -> userToSave = new SalesManager(finalUserID, newUsername, passwordToStoreInFile);
            default -> {
                return "Internal error: Invalid role specified after validation.";
            }
        }

        // Write the userToSave object to the file
        boolean success = fileManager.writeToFile(
            userToSave,
            fileManager.getUserFilePath(),
            User::getUserID,
            user -> user.getUserID() + "," + user.getUsername() + "," + user.getPassword() + "," + user.getRole().name()
        );

        if (success) {
            return null; // Success
        } else {
            return "Failed to save user data to file.";
        }
    }
    
    /**
     * Retrieves a list of all users from the system, excluding the currently logged-in administrator.
     *
     * @return A List of User objects.
     */
    public List<User> getAllUsers() {
        List<User> allUsers = fileManager.readFile(
            fileManager.getUserFilePath(),
            line -> {
                String[] data = line.split(",", -1);
                if (data.length < 4) {
                    System.err.println("Skipping invalid user data for table (not enough fields): " + line);
                    return null;
                }
                // Password handling for display - generally, you DON'T want to load actual passwords
                // into memory for just displaying a user list. For this table, we might not even need it.
                // Let's assume the User constructor can handle a password field, even if it's just for consistency.
                String storedPasswordFromFile = data[2];
                String actualPasswordForUserObject = storedPasswordFromFile;

                if (isBase64(storedPasswordFromFile)) {
                    try {
                        byte[] decodedBytes = Base64.getDecoder().decode(storedPasswordFromFile);
                        try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(decodedBytes))) {
                            actualPasswordForUserObject = (String) in.readObject();
                        }
                    } catch (IOException | ClassNotFoundException | IllegalArgumentException e) {
                        // Error deserializing, use raw
                    }
                }

                try {
                    // Create a generic User object for the list.
                    // If you have specific User subclasses (FinanceManager etc.) and want to instantiate them,
                    // you'd need more complex logic here based on data[3] (role)
                    UserRoles role = UserRoles.valueOf(data[3]);
                    // For simplicity, we'll create base User objects.
                    // The actual specific type (FinanceManager etc.) is less critical for just listing.
                    // Pass the passwordToStoreInFile if that's what your User constructor expects
                    return new User(data[0], data[1], actualPasswordForUserObject, role);

                } catch (IllegalArgumentException e) {
                    System.err.println("Error parsing user role for table: " + data[3] + " in line: " + line + " | " + e.getMessage());
                    return null;
                }
            }
        );
        // Filter out nulls and the admin him/herself from the list if desired
        return allUsers.stream()
                       .filter(Objects::nonNull)
                       // .filter(user -> !user.getUserID().equals(this.getUserID())) // Optional: exclude current admin
                       .collect(Collectors.toList());
    }
    
     /**
     * Deletes a user from the system.
     *
     * @param userIdToDelete The ID of the user to delete.
     * @return null if deletion is successful, or an error message string if it fails.
     */
    public String deleteUser(String userIdToDelete) {
        if (userIdToDelete == null || userIdToDelete.trim().isEmpty()) {
            return "User ID to delete cannot be empty.";
        }

        // Prevent admin from deleting themselves or another admin through this general method
        User userToDeleteDetails = getUserById(userIdToDelete); // Need a helper method for this
        if (userToDeleteDetails == null) {
            return "User with ID '" + userIdToDelete + "' not found.";
        }
        if (userToDeleteDetails.getRole() == UserRoles.ADMINISTRATOR) {
            return "Administrators cannot be deleted through this function.";
        }
        if (userIdToDelete.equals(this.getUserID())) {
             return "You cannot delete your own account.";
        }


        // The parser for readFile when deleting only needs to extract the ID to find the user.
        // However, your FileManager.deleteFromFile needs the full object parser to read all users first.
        boolean success = fileManager.deleteFromFile(
            userIdToDelete,
            fileManager.getUserFilePath(),
            User::getUserID, // Extracts ID from a User object
            line -> { // Parser to reconstruct User objects from file lines
                String[] data = line.split(",", -1);
                if (data.length < 4) return null;
                // Simplified parsing for delete context, password not strictly needed
                // but constructor might require it.
                 String storedPasswordFromFile = data[2];
                String actualPasswordForUserObject = storedPasswordFromFile;

                if (isBase64(storedPasswordFromFile)) {
                    try {
                        byte[] decodedBytes = Base64.getDecoder().decode(storedPasswordFromFile);
                        try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(decodedBytes))) {
                            actualPasswordForUserObject = (String) in.readObject();
                        }
                    } catch (IOException | ClassNotFoundException | IllegalArgumentException e) {
                        // Error deserializing, use raw
                    }
                }
                try {
                    return new User(data[0], data[1], actualPasswordForUserObject, UserRoles.valueOf(data[3]));
                } catch (IllegalArgumentException e) {
                    return null;
                }
            }
        );

        if (success) {
            return null; // Deletion successful
        } else {
            // The FileManager.deleteFromFile prints its own more specific error,
            // but we can return a general one too.
            return "Failed to delete user with ID '" + userIdToDelete + "'. Check console for details.";
        }
    }

    // Helper method to get a user by ID (useful for delete/update validation)
    public User getUserById(String userId) {
        List<User> users = getAllUsers(); // Uses the existing method
        for (User user : users) {
            if (user.getUserID().equals(userId)) {
                return user;
            }
        }
        return null;
    }
    
    
   
    public String updateUser(String userIdToUpdate, User updatedUserDetails) {
        if (userIdToUpdate == null || userIdToUpdate.trim().isEmpty()) {
            return "User ID to update cannot be empty.";
        }
        if (updatedUserDetails == null) {
            return "Updated user details cannot be null.";
        }

        User existingUser = getUserById(userIdToUpdate); // Fetches user with current *storable* password
        if (existingUser == null) {
            return "User with ID '" + userIdToUpdate + "' not found.";
        }

        if (existingUser.getRole() == UserRoles.ADMINISTRATOR) {
            return "Administrator accounts cannot be modified through this function.";
        }

        // --- Determine final values based on updatedUserDetails and existingUser ---
        String finalUsername = existingUser.getUsername();
        // Check if a new username is provided and is different
        if (updatedUserDetails.getUsername() != null &&
            !updatedUserDetails.getUsername().trim().isEmpty() &&
            !updatedUserDetails.getUsername().trim().equalsIgnoreCase(existingUser.getUsername())) {

            String proposedNewUsername = updatedUserDetails.getUsername().trim();
            // Validate new username length (if you have such rules)
            // Check for duplicate username against OTHER users
            List<User> allUsers = getAllUsers();
            for (User u : allUsers) {
                if (!u.getUserID().equals(userIdToUpdate) && u.getUsername().equalsIgnoreCase(proposedNewUsername)) {
                    return "New username '" + proposedNewUsername + "' is already taken by another user.";
                }
            }
            finalUsername = proposedNewUsername;
        }

        UserRoles finalRole = existingUser.getRole();
        // Check if a new role is provided and is different
        if (updatedUserDetails.getRole() != null && updatedUserDetails.getRole() != existingUser.getRole()) {
            if (updatedUserDetails.getRole() == UserRoles.ADMINISTRATOR) {
                return "Cannot change role to Administrator through this function.";
            }
            finalRole = updatedUserDetails.getRole();
        }

        String finalStorablePassword = existingUser.getPassword(); // Keep existing password by default
        // Check if a new plain text password is provided in updatedUserDetails
        // (getPassword() on updatedUserDetails should return plain text for new password)
        if (updatedUserDetails.getPassword() != null && !updatedUserDetails.getPassword().isEmpty()) {
            String plainTextNewPassword = updatedUserDetails.getPassword();
            if (plainTextNewPassword.length() < 6) {
                return "New password must be at least 6 characters long.";
            }
            // Process the new plain text password to its storable format
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                 ObjectOutputStream out = new ObjectOutputStream(baos)) {
                out.writeObject(plainTextNewPassword);
                out.flush();
                finalStorablePassword = Base64.getEncoder().encodeToString(baos.toByteArray());
            } catch (IOException e) {
                System.err.println("Error serializing new password during update: " + e.getMessage());
                return "Error processing new password. User not updated.";
            }
        }

        // --- Reconstruct the User object to be saved with all final values ---
        User userToSave;
        // Use userIdToUpdate (the original ID), finalUsername, finalStorablePassword, finalRole
        switch (finalRole) {
            case FINANCE_MANAGER:
                userToSave = new FinanceManager(userIdToUpdate, finalUsername, finalStorablePassword);
                break;
            case INVENTORY_MANAGER:
                userToSave = new InventoryManager(userIdToUpdate, finalUsername, finalStorablePassword);
                break;
            case PURCHASE_MANAGER:
                // If PurchaseManager constructor requires injected services for its *entity state*,
                // this becomes complex. Usually, for saving, you use a simpler constructor.
                // If the services are only for *operations*, they aren't part of the saved entity state.
                userToSave = new PurchaseManager(userIdToUpdate, finalUsername, finalStorablePassword);
                break;
            case SALES_MANAGER:
                userToSave = new SalesManager(userIdToUpdate, finalUsername, finalStorablePassword);
                break;
            default:
                return "Internal error: Invalid role encountered during user update.";
        }

        // --- Write to file using your existing updateToFile function ---
        boolean success = fileManager.updateToFile(
            userToSave, // The fully reconstructed object with final values
            fileManager.getUserFilePath(),
            User::getUserID,
            user -> user.getUserID() + "," + user.getUsername() + "," + user.getPassword() + "," + user.getRole().name(),
            line -> { // Parser to reconstruct User objects for the list in updateToFile
                String[] data = line.split(",", -1);
                if (data.length < 4) return null;
                try {
                    // When parsing for updateToFile's internal list, use the storable password from file
                    return new User(data[0], data[1], data[2], UserRoles.valueOf(data[3]));
                } catch (IllegalArgumentException e) {
                    return null;
                }
            }
        );

        if (success) {
            return null; // Update successful
        } else {
            // updateToFile prints its own message, but we can return a more general one
            return "Failed to update user with ID '" + userIdToUpdate + "'.";
        }
    }
    
    

    // Helper method to check if a string is likely Base64
    // This is a basic check and might not be foolproof for all Base64 strings.
    private boolean isBase64(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        // Basic Base64 character set check and padding check
        // Regex from: https://stackoverflow.com/a/7632778/1258019
        return str.matches("^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=)?$");
    }

    // ... other methods in Administrator class (updateUser, deleteUser, etc.)
}