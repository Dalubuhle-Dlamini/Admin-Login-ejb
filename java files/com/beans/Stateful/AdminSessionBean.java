package com.beans.Stateful;

import com.models.Stateful.User;
import com.util.Stateful.DBConnection;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author dsihl
 */
@Stateless
public class AdminSessionBean implements AdminSessionBeanLocal {

    private static final Logger logger = Logger.getLogger(AdminSessionBean.class.getName());

    @Override
    public void insertUser(String username, String email, String password) throws SQLException {
        if (findByUsername(username) != null) {
            throw new SQLException("Username already exists.");
        }

        if (findByEmail(email) != null) {
            throw new SQLException("Email already exists.");
        }

        String hashedPassword = hashPassword(password);

        String sql = "INSERT INTO users (username, email, password_hash) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, hashedPassword);

            stmt.executeUpdate();
        }
    }

    @Override
    public User findByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";
        User user = null;

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    user = extractUser(rs);
                }
            }
        }
        return user;
    }

    @Override
    public User findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ?";
        User user = null;

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    user = extractUser(rs);
                }
            }
        }
        return user;
    }

    @Override
    public User extractUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setPasswordHash(rs.getString("password_hash"));
        // Set other properties as needed
        return user;
    }

    private String hashPassword(String password) {
        String salt = BCrypt.gensalt();

        String hashedPassword = BCrypt.hashpw(password, salt);

        return hashedPassword;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public boolean loginUser(String username, String password) throws SQLException {
        String sql = "SELECT password_hash, is_admin FROM users WHERE username = ?";

        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);

            // Execute query to get the stored hashed password and is_admin flag
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedHashedPassword = rs.getString("password_hash");
                    boolean isAdmin = rs.getBoolean("is_admin");

                    if (BCrypt.checkpw(password, storedHashedPassword)) {
                        logger.log(Level.INFO, "User {0} logged in successfully", username);
                        return isAdmin;
                    } else {
                        throw new SQLException("Invalid username or password");
                    }
                } else {
                    throw new SQLException("Invalid Username or Password");
                }
            }
        }
    }

    @Override
    public void updateUser(String oldUsername, String newUsername, String newEmail, String newPassword) throws SQLException {
        String sql = "UPDATE users SET username = ?, email = ?, password_hash = ? WHERE username = ?";
        String hashedPassword = hashPassword(newPassword);

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newUsername);
            stmt.setString(2, newEmail);
            stmt.setString(3, hashedPassword);
            stmt.setString(4, oldUsername);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                logger.log(Level.INFO, "User {0} updated successfully", oldUsername);
            } else {
                throw new SQLException("User update failed. User not found.");
            }
        }
    }

    public void updatePassword(String username, String oldPassword, String newPassword) throws SQLException {
        String fetchPassword = "SELECT password_hash FROM users WHERE username = ?";
        String updatePassword = "UPDATE users SET password_hash = ? WHERE username = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement fetchStmt = conn.prepareStatement(fetchPassword)) {

            fetchStmt.setString(1, username);
            ResultSet rs = fetchStmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password_hash");

                if (!BCrypt.checkpw(oldPassword, storedHash)) {
                    throw new SQLException("The old password is incorrect.");
                }

                String hashedNewPassword = hashPassword(newPassword);
                try (PreparedStatement updateStmt = conn.prepareStatement(updatePassword)) {
                    updateStmt.setString(1, hashedNewPassword);
                    updateStmt.setString(2, username);
                    int rowsUpdated = updateStmt.executeUpdate();

                    if (rowsUpdated > 0) {
                        logger.log(Level.INFO, "Password for user {0} updated successfully", username);
                    } else {
                        throw new SQLException("Password update failed. User not found.");
                    }
                }
            } else {
                throw new SQLException("User not found.");
            }
        }
    }
}
