package service;

import dao.GuestDao;
import model.Guest;

import java.util.List;

public class GuestService {

    private GuestDao guestDao;

    public GuestService() {
        guestDao = new GuestDao();
    }

    public Guest createGuest(Guest guest) {
        return guestDao.insert(guest);
    }

    public List<Guest> getAllGuests() {
        return guestDao.getAllGuests();
    }
}
