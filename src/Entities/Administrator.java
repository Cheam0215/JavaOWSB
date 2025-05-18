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
import java.util.Optional;
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
        super(userId, username, "INTERNAL_ADMIN_PLACEHOLDER_PASS", UserRoles.ADMINISTRATOR);
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
    public String registerUser(String newUsername, String newPassword, UserRoles role) {
        // --- Business Logic Level Input Validation ---
        // These checks are important even if the UI does some validation,
        // as this method is the ultimate authority for business rules.
        if (newUsername == null || newUsername.trim().isEmpty()) {
            return "Username cannot be empty.";
        }
        if (newPassword == null || newPassword.length() < 6) { // Ensure this matches UI or is stricter
            return "Password must be at least 6 characters long.";
        }
        if (role == null) {
            return "A role must be specified for the new user.";
        }
        if (role == UserRoles.ADMINISTRATOR) {
            // Prevent creating another admin through this specific method,
            // Admin creation might have a special process or be disabled.
            return "Cannot create an Administrator role through this function. Please select a different role.";
        }
        // --- End Business Logic Level Input Validation ---


        // Read existing users to check for duplicate username
        List<User> existingUsers = fileManager.readFile(
            fileManager.getUserFilePath(), // Make sure getUserFilePath() is correct
            line -> {
                String[] data = line.split(",", -1); // Use -1 to keep trailing empty strings
                if (data.length < 4) {
                    System.err.println("Skipping invalid user data (not enough fields): " + line);
                    return null;
                }
                String storedPasswordFromFile = data[2];
                String actualPasswordForUserObject = storedPasswordFromFile; // Assume plain text or already processed

                // If passwords in file are Base64 Object Serialized strings:
                if (isBase64(storedPasswordFromFile)) {
                    try {
                        byte[] decodedBytes = Base64.getDecoder().decode(storedPasswordFromFile);
                        try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(decodedBytes))) {
                            actualPasswordForUserObject = (String) in.readObject();
                        }
                    } catch (IOException | ClassNotFoundException | IllegalArgumentException e) {
                        System.err.println("Password deserialization failed for entry: " + line + ". Using raw value from file. Error: " + e.getMessage());
                        // Keep actualPasswordForUserObject as storedPasswordFromFile
                    }
                }
                // IMPORTANT: The User constructor should expect the password in the format it's meant to be used with,
                // NOT necessarily the format it's stored in the file if that format is for persistence only (e.g., hash).
                // For this example, we're assuming the User object holds the (potentially deserialized) password.

                try {
                    return new User(
                        data[0], // id
                        data[1], // username
                        actualPasswordForUserObject, // password
                        UserRoles.valueOf(data[3]) // role
                    );
                } catch (IllegalArgumentException e) {
                    System.err.println("Error parsing user data: " + line + " | " + e.getMessage());
                    return null;
                }
            }
        ).stream().filter(Objects::nonNull).collect(Collectors.toList()); // Filter out nulls from parsing errors

        // Check for duplicate username (case-insensitive)
        for (User existingUser : existingUsers) {
            if (existingUser.getUsername().equalsIgnoreCase(newUsername)) {
                return "Username '" + newUsername + "' already exists. Please choose a different username.";
            }
        }

        // "Serialize" the new password to Base64 (CONSIDER HASHING INSTEAD for security)
        String passwordToStoreInFile;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream out = new ObjectOutputStream(baos)) {
            out.writeObject(newPassword); // Serialize the plain text password given by the user
            out.flush();
            passwordToStoreInFile = Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (IOException e) {
            System.err.println("Error serializing password: " + e.getMessage());
            return "Error processing password. User not created.";
        }

        // Generate a new ID
        // Simple increment based on current count or max ID.
        // For more robustness, consider UUIDs or a more sophisticated ID generation.
        int maxId = 0;
        for (User user : existingUsers) {
            try {
                int currentId = Integer.parseInt(user.getUserID());
                if (currentId > maxId) {
                    maxId = currentId;
                }
            } catch (NumberFormatException e) {
                // Handle non-numeric IDs if they can exist, or log error
                System.err.println("Warning: Non-numeric user ID found: " + user.getUserID() + ". This might affect ID generation.");
            }
        }
        String newUserID = String.format("%03d", maxId + 1);


        // Create role-specific user object.
        // The constructor for these role-specific classes should take the password
        // that is intended to be STORED (i.e., passwordToStoreInFile).
        User roleSpecificUser;
        switch (role) {
            case FINANCE_MANAGER:
                roleSpecificUser = new FinanceManager(newUserID, newUsername, passwordToStoreInFile);
                break;
            case INVENTORY_MANAGER:
                roleSpecificUser = new InventoryManager(newUserID, newUsername, passwordToStoreInFile);
                break;
            case PURCHASE_MANAGER:
                roleSpecificUser = new PurchaseManager(newUserID, newUsername, passwordToStoreInFile);
                break;
            case SALES_MANAGER:
                roleSpecificUser = new SalesManager(newUserID, newUsername, passwordToStoreInFile);
                break;
            default:
                // This case should have been caught by the validation at the beginning of the method.
                return "Internal error: Invalid role specified after validation.";
        }

        // Write the new user to the file
        // The lambda here defines how to convert the roleSpecificUser object to a string for the file.
        // It should use user.getPassword() which should return the passwordToStoreInFile.
        boolean success = fileManager.writeToFile(
            roleSpecificUser,
            fileManager.getUserFilePath(),
            User::getUserID, // Method reference to get the ID for uniqueness check if writeToFile uses it
            user -> user.getUserID() + "," + user.getUsername() + "," + user.getPassword() + "," + user.getRole().name()
        );

        if (success) {
            return null; // Indicates success
        } else {
            return "There's something wrong";
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
    
    
     /**
     * Updates an existing user's information (e.g., username, role, password).
     *
     * @param userIdToUpdate The ID of the user to update.
     * @param newUsername    The new username (can be null or empty if not changing).
     * @param newPassword    The new plain text password (can be null or empty if not changing).
     * @param newRole        The new role (can be null if not changing).
     * @return null if update is successful, or an error message string if it fails.
     */
    public String updateUser(String userIdToUpdate, String newUsername, String newPassword, UserRoles newRole) {
        if (userIdToUpdate == null || userIdToUpdate.trim().isEmpty()) {
            return "User ID to update cannot be empty.";
        }

        User existingUser = getUserById(userIdToUpdate); // This should fetch the user with their current *storable* password
        if (existingUser == null) {
            return "User with ID '" + userIdToUpdate + "' not found.";
        }

        if (existingUser.getRole() == UserRoles.ADMINISTRATOR) {
            return "Administrator accounts cannot be modified through this function.";
        }

        // --- Prepare updated fields ---
        String finalUsername = existingUser.getUsername();
        if (newUsername != null && !newUsername.trim().isEmpty() && !newUsername.trim().equalsIgnoreCase(existingUser.getUsername())) {
            // Check if the new username is already taken by ANOTHER user
            List<User> allUsers = getAllUsers(); // Assumes getAllUsers is efficient or cached if called often
            for (User u : allUsers) {
                if (!u.getUserID().equals(userIdToUpdate) && u.getUsername().equalsIgnoreCase(newUsername.trim())) {
                    return "New username '" + newUsername.trim() + "' is already taken by another user.";
                }
            }
            finalUsername = newUsername.trim();
        }

        UserRoles finalRole = existingUser.getRole();
        if (newRole != null && newRole != existingUser.getRole()) {
            if (newRole == UserRoles.ADMINISTRATOR) {
                return "Cannot change role to Administrator through this function.";
            }
            finalRole = newRole;
        }

        String finalStorablePassword = existingUser.getPassword(); // Keep existing password by default
        if (newPassword != null && !newPassword.isEmpty()) {
            // Validate new password (e.g., minimum length)
            if (newPassword.length() < 6) { // Match this with registration validation
                return "New password must be at least 6 characters long.";
            }
            // Process the new plain text password to its storable format (e.g., Base64 serialize)
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                 ObjectOutputStream out = new ObjectOutputStream(baos)) {
                out.writeObject(newPassword); // Serialize the new plain text password
                out.flush();
                finalStorablePassword = Base64.getEncoder().encodeToString(baos.toByteArray());
            } catch (IOException e) {
                System.err.println("Error serializing new password during update: " + e.getMessage());
                return "Error processing new password. User not updated.";
            }
        }

        // --- Reconstruct the User object with updated fields ---
        // The password used here (finalStorablePassword) MUST be in the format expected by the User constructor
        // and also the format that will be written to the file.
        User updatedUserObject;
        switch (finalRole) { // Use finalRole for instantiation
            case FINANCE_MANAGER:
                updatedUserObject = new FinanceManager(userIdToUpdate, finalUsername, finalStorablePassword);
                break;
            case INVENTORY_MANAGER:
                updatedUserObject = new InventoryManager(userIdToUpdate, finalUsername, finalStorablePassword);
                break;
            case PURCHASE_MANAGER:
                updatedUserObject = new PurchaseManager(userIdToUpdate, finalUsername, finalStorablePassword);
                break;
            case SALES_MANAGER:
                updatedUserObject = new SalesManager(userIdToUpdate, finalUsername, finalStorablePassword);
                break;
            default:
                 // This case should ideally not be reached if role validation is done prior
                 // or if existingUser.getRole() is always a valid non-admin role for updatable users.
                 // Fallback to a generic User if User class is not abstract.
                 // If User is abstract, this is an error.
                 // For this example, assuming role has been validated or is one of the above.
                 // If User can be instantiated:
                 // updatedUserObject = new User(userIdToUpdate, finalUsername, finalStorablePassword, finalRole);
                 // break;
                return "Internal error: Invalid role encountered during user update.";
        }
        // If your User class constructor doesn't take role but it's set via a setter:
        // updatedUserObject.setRole(finalRole);


        // --- Write to file ---
        boolean success = fileManager.updateToFile(
            updatedUserObject,
            fileManager.getUserFilePath(),
            User::getUserID, // idExtractor
            user -> user.getUserID() + "," + user.getUsername() + "," + user.getPassword() + "," + user.getRole().name(), // toStringConverter
            line -> { // parser for readFile within updateToFile
                String[] data = line.split(",", -1);
                if (data.length < 4) return null;
                // The parser in updateToFile reads all existing users.
                // The password here (data[2]) is the storable form from the file.
                try {
                     // When constructing User objects for the list being modified,
                     // use the password directly from file (storable form)
                    return new User(data[0], data[1], data[2], UserRoles.valueOf(data[3]));
                } catch (IllegalArgumentException e) {
                    return null;
                }
            }
        );

        if (success) {
            return null; // Update successful
        } else {
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