package model;

import java.util.List;
import java.util.UUID;

public class Room {
    private UUID id;
    private int roomNumber;
    private UUID guestId;

    public Room(UUID id, int roomNumber, UUID guestId) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.guestId = guestId;
    }

    public Room(UUID id, int roomNumber) {
        this.id = id;
        this.roomNumber = roomNumber;
    }

    public UUID getId() {
        return id;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setGuestId(UUID guestId) {
        this.guestId = guestId;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\": \"" + id + "\"," +
                "\"roomNumber\": " + roomNumber + "," +
                "\"guestId\": \"" + guestId + "\"" +
                "}";
    }

    public static String roomsToString(List<Room> rooms) {
        StringBuilder jsonString = new StringBuilder("[");
        for (int i = 0; i < rooms.size(); i++) {
            jsonString.append(rooms.get(i).toString());
            if (i < rooms.size() - 1) {
                jsonString.append(",");
            }
        }
        jsonString.append("]");
        return jsonString.toString();
    }
}
