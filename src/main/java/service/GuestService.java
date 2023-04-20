package service;

import dao.GuestDao;
import model.Guest;

import java.util.Optional;
import java.util.UUID;

public class GuestService {

    private GuestDao guestDao;

    public GuestService() {
        guestDao = new GuestDao();
    }

    public Guest createGuest(String name, String surname) {
        Optional<Guest> existingGuest = findGuestByNameAndSurname(name, surname);

        if (existingGuest.isPresent()) {
            return existingGuest.get();
        }

        return guestDao.insert(new Guest(UUID.randomUUID(), name, surname)); }

    public Optional<Guest> findGuestByNameAndSurname(String name, String surname) { return guestDao.get(name, surname); }
}
