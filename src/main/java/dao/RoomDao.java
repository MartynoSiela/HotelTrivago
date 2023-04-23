package dao;

import model.Guest;
import model.Room;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RoomDao {

    public RoomDao() {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public List<Guest> getRoomHistory(int roomNumber) {
        List<Guest> guests = new ArrayList<>();
        String sql = "SELECT GUESTS.NAME, GUESTS.SURNAME\n" +
                "FROM RESERVATIONS\n" +
                "JOIN GUESTS ON RESERVATIONS.GUEST_ID = GUESTS.ID\n" +
                "JOIN ROOMS ON RESERVATIONS.ROOM_ID = ROOMS.ID\n" +
                "WHERE ROOMS.ROOM_NUMBER = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, String.valueOf(roomNumber));

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("name");
                    String surname = rs.getString("surname");
                    Guest guest = new Guest(name, surname);
                    guests.add(guest);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return guests;
    }

    public Boolean isRoomOccupied(int roomNumber) {
        String sql = "SELECT GUEST_ID FROM ROOMS WHERE ROOM_NUMBER = ?;";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, String.valueOf(roomNumber));
            statement.executeQuery();

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("guest_Id") != null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<Room> getOccupiedRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM ROOMS WHERE GUEST_ID IS NOT NULL";

        try (Connection connection = DatabaseConfig.getConnection();
             Statement statement = connection.createStatement()) {

            try (ResultSet rs = statement.executeQuery(sql)) {
                while (rs.next()) {
                    UUID id = UUID.fromString(rs.getString("id"));
                    int roomNumber = Integer.parseInt(rs.getString("room_number"));
                    UUID guestId = UUID.fromString(rs.getString("guest_id"));
                    Room room = new Room(id, roomNumber, guestId);
                    rooms.add(room);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rooms;
    }

    public List<Room> getAvailableRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM ROOMS WHERE GUEST_ID IS NULL";

        try (Connection connection = DatabaseConfig.getConnection();
             Statement statement = connection.createStatement()) {

            try (ResultSet rs = statement.executeQuery(sql)) {
                while (rs.next()) {
                    UUID id = UUID.fromString(rs.getString("id"));
                    int roomNumber = Integer.parseInt(rs.getString("room_number"));
                    Room room = new Room(id, roomNumber);
                    rooms.add(room);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rooms;
    }

    public List<Integer> getExistingRoomNumbers() {
        List<Integer> roomNumbers = new ArrayList<>();
        String sql = "SELECT ROOM_NUMBER FROM ROOMS";

        try (Connection connection = DatabaseConfig.getConnection();
             Statement statement = connection.createStatement()) {

            try (ResultSet rs = statement.executeQuery(sql)) {
                while (rs.next()) {
                    int roomNumber = Integer.parseInt(rs.getString("room_number"));
                    roomNumbers.add(roomNumber);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return roomNumbers;
    }

    public void reserveRoom(UUID roomId, UUID guestId) {
        List<Integer> roomNumbers = new ArrayList<>();
        String sql = "UPDATE ROOMS SET GUEST_ID = ? WHERE ID = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, String.valueOf(guestId));
            statement.setString(2, String.valueOf(roomId));
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Optional<Room> getRoomById(UUID roomId) {
        Optional<Room> room = Optional.empty();
        String sql = "SELECT * FROM ROOMS WHERE ID = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, roomId.toString());

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    UUID id = UUID.fromString(rs.getString("id"));
                    int roomNumber = Integer.parseInt(rs.getString("room_number"));
                    return Optional.of(new Room(id, roomNumber));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return room;
    }

    public void freeUpRoom(Room room) {
        String sql = "UPDATE ROOMS SET GUEST_ID = null WHERE ID = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, room.getId().toString());
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
