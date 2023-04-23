package service;

import dao.RoomDao;
import model.Guest;
import model.Room;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RoomService {

    private RoomDao roomDao;

    public RoomService(RoomDao roomDao) {
        this.roomDao = roomDao;
    }

    public List<Integer> getExistingRoomNumbers() { return roomDao.getExistingRoomNumbers(); }

    public List<Guest> getRoomHistory(Integer roomNumber) { return roomDao.getRoomHistory(roomNumber); }

    public List<Room> getAvailableRooms() { return roomDao.getAvailableRooms(); }

    public List<Room> getOccupiedRooms() { return roomDao.getOccupiedRooms(); }

    public void reserveRoom(UUID roomId, UUID guestId) { roomDao.reserveRoom(roomId, guestId); }

    public Optional<Room> getRoomById(UUID roomId) { return roomDao.getRoomById(roomId); }

    public boolean isRoomOccupied(int roomNumber) { return roomDao.isRoomOccupied(roomNumber); }

    public void freeUpRoom(Room room) { roomDao.freeUpRoom(room); }
}
