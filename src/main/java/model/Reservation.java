package model;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.UUID;

public class Reservation {
    private UUID id;
    private UUID guestId;
    private UUID roomId;
    private Timestamp checkInDate;
    private Timestamp checkOutDate;

    public Reservation(UUID id, UUID guestId, UUID roomId, Timestamp checkInDate) {
        this.id = id;
        this.guestId = guestId;
        this.roomId = roomId;
        this.checkInDate = checkInDate;
    }

    public UUID getId() {
        return id;
    }

    public UUID getGuestId() {
        return guestId;
    }

    public UUID getRoomId() {
        return roomId;
    }

    public Timestamp getCheckInDate() {
        return checkInDate;
    }

    public Timestamp getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(Timestamp checkOutDate) {
        this.checkOutDate = checkOutDate;
    }
}
