package dao;

import model.Guest;

import java.sql.*;
import java.util.Optional;
import java.util.UUID;

public class GuestDao {

    public GuestDao() {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Guest createGuest(Guest guest) {
        String sql = "INSERT INTO GUESTS (id, name, surname) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, guest.getId().toString());
            statement.setString(2, guest.getName());
            statement.setString(3, guest.getSurname());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return guest;
    }

    public Optional<Guest> get(String name, String surname) {
        String sql = "SELECT * FROM GUESTS WHERE name = ? AND surname = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, name);
            statement.setString(2, surname);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    UUID id = UUID.fromString(rs.getString("id"));
                    Guest guest = new Guest (id, rs.getString("name"), rs.getString("surname"));
                    return Optional.of(guest);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }
}
