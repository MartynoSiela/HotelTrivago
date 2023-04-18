package dao;

import model.Guest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GuestDao {

    private static final String DB_URL = "jdbc:h2:~/hotel;";

    public GuestDao() {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<Guest> getAllGuests() {
        List<Guest> guests = new ArrayList<>();
        String sql = "SELECT * FROM GUESTS";

        try (Connection connection = DriverManager.getConnection(DB_URL, "admin", "password");
             Statement statement = connection.createStatement()) {

            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                String id = rs.getString(1);
                String name = rs.getString(2);
                String surname = rs.getString(3);
                guests.add(new Guest(UUID.fromString(id), name, surname));
            }
        }  catch (SQLException e) {
            e.printStackTrace();
        }

        return guests;
    }

    public Guest insert(Guest guest) {
        String sql = "INSERT INTO GUESTS (id, name, surname) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(DB_URL, "admin", "password");
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
}
