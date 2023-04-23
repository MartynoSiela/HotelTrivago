package dao;

import model.Reservation;

import java.sql.*;
import java.util.Optional;
import java.util.UUID;

public class ReservationDao {

    public ReservationDao() {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void createReservation(Reservation reservation) {
        String sql = "INSERT INTO RESERVATIONS (id, guest_id, room_id, check_in_date) VALUES (?, ?, ?, ?);";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, reservation.getId().toString());
            statement.setString(2, reservation.getGuestId().toString());
            statement.setString(3, reservation.getRoomId().toString());
            statement.setString(4, reservation.getCheckInDate().toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Optional<Reservation> checkForExistingReservation(String name, String surname) {
        Optional<Reservation> reservation = Optional.empty();

        String sql = "SELECT\n" +
                "    r.ID,\n" +
                "    r.GUEST_ID,\n" +
                "    r.ROOM_ID,\n" +
                "    r.CHECK_IN_DATE,\n" +
                "    r.CHECK_OUT_DATE\n" +
                "FROM\n" +
                "    GUESTS g\n" +
                "JOIN RESERVATIONS r ON g.ID = r.GUEST_ID\n" +
                "WHERE\n" +
                "    g.NAME = ? AND g.SURNAME = ? AND r.CHECK_OUT_DATE IS NULL;";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, name);
            statement.setString(2, surname);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    UUID id = UUID.fromString(rs.getString("id"));
                    UUID guestId = UUID.fromString(rs.getString("guest_id"));
                    UUID roomId = UUID.fromString(rs.getString("room_id"));
                    Timestamp checkInDate = Timestamp.valueOf(rs.getString("check_in_date"));
                    reservation = Optional.of(new Reservation(id, guestId, roomId, checkInDate));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservation;
    }

    public void updateReservation(Reservation reservation) {
        String sql = "UPDATE RESERVATIONS SET CHECK_OUT_DATE = ? WHERE ID = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, reservation.getCheckOutDate().toString());
            statement.setString(2, reservation.getId().toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
