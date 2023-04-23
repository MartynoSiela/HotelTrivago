package service;

import dao.ReservationDao;
import model.Guest;
import model.Reservation;
import model.Room;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ReservationService {

    private ReservationDao reservationDao;
    private RoomService roomService;
    private GuestService guestService;

    public ReservationService(ReservationDao reservationDao, RoomService roomService, GuestService guestService) {
        this.reservationDao = reservationDao;
        this.roomService = roomService;
        this.guestService = guestService;
    }

    public Optional<Reservation> checkForExistingReservation(String name, String surname) {
        return reservationDao.checkForExistingReservation(name, surname);
    }

    public Optional<Room> createReservation(String name, String surname) {
        Optional<Room> reservedRoom = Optional.empty();

        List<Room> availableRooms = roomService.getAvailableRooms();
        Guest guest = guestService.createOrReturnExistingGuest(name, surname);

        if (availableRooms.isEmpty()) {
            return reservedRoom;
        } else {
            Reservation reservation = new Reservation(UUID.randomUUID(), guest.getId(), availableRooms.get(0).getId(), Timestamp.valueOf(LocalDateTime.now()));
            reservedRoom = roomService.getRoomById(reservation.getRoomId());
            roomService.reserveRoom(reservation.getRoomId(), reservation.getGuestId());
            reservationDao.createReservation(reservation);
        }

        return reservedRoom;
    }

    public void endReservation(Reservation reservation) {
        reservation.setCheckOutDate(Timestamp.valueOf(LocalDateTime.now()));
        reservationDao.updateReservation(reservation);

        Optional<Room> room = roomService.getRoomById(reservation.getRoomId());
        room.get().setGuestId(null);
        roomService.freeUpRoom(room.get());
    }
}
