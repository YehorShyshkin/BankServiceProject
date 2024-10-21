package de.yehorsh.managerservice.controller;

import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Utility class for database operations related to managers.
 * This class provides methods to check for manager existence and retrieve manager details
 * based on the email address.
 */
@Component
public class DBUtil {
    private final DataSource dataSource;

    public DBUtil(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public boolean managerExistsByEmail(String email) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String query = "SELECT COUNT(*) FROM managers WHERE email = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    public ManagerDetails getManagerByEmail(String email) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String query = "SELECT first_name, last_name, email, phone_number FROM managers WHERE email = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new ManagerDetails(
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("phone_number")
                );
            }
        }
        throw new IllegalArgumentException("Manager not found with email: " + email);
    }

    public static class ManagerDetails {
        private final String firstName;
        private final String lastName;
        private final String email;
        private final String phoneNumber;

        public ManagerDetails(String firstName, String lastName, String email, String phoneNumber) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.phoneNumber = phoneNumber;
        }

        @Override
        public String toString() {
            return "ManagerDetails{" +
                    "firstName='" + firstName + '\'' +
                    ", lastName='" + lastName + '\'' +
                    ", email='" + email + '\'' +
                    ", phoneNumber='" + phoneNumber + '\'' +
                    '}';
        }
    }
}
