package de.yehorsh.authservice.controller;

import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Utility class for database operations related to users.
 * This class provides methods to check for user existence and retrieve user details
 * based on the email address.
 */
@Component
public class DBUtil {
    private final DataSource dataSource;

    public DBUtil(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public boolean userExistsByEmail(String email) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String query = "SELECT COUNT(*) FROM users WHERE email = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    public UserDetails getUserByEmail(String email) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String query = "SELECT first_name, last_name, email, phone_number FROM users WHERE email = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new UserDetails(
                        rs.getString("user_name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("role_name")
                );
            }
        }
        throw new IllegalArgumentException("User not found with email: " + email);
    }

    public static class UserDetails {
        private final String userName;
        private final String email;
        private final String password;
        private final String roleName;

        public UserDetails(String userName, String email, String password, String roleName) {
            this.userName = userName;
            this.email = email;
            this.password = password;
            this.roleName = roleName;
        }

        @Override
        public String toString() {
            return "UsersDetails{" +
                    "firstName='" + userName + '\'' +
                    ", email='" + email + '\'' +
                    ", roleName='" + roleName + '\'' +
                    '}';
        }
    }
}
