package service;

import dao.GuestDao;
import dao.ReservationDao;
import dao.RoomDao;

public class ServiceManager {
    private static ServiceManager instance;

    private GuestDao guestDao;
    private RoomDao roomDao;
    private ReservationDao reservationDao;

    private GuestService guestService;
    private RoomService roomService;
    private ReservationService reservationService;

    private ServiceManager() {
        guestDao = new GuestDao();
        roomDao = new RoomDao();
        reservationDao = new ReservationDao();

        guestService = new GuestService(guestDao);
        roomService = new RoomService(roomDao);
        reservationService = new ReservationService(reservationDao, roomService, guestService);
    }

    public static ServiceManager getInstance() {
        if (instance == null) {
            instance = new ServiceManager();
        }
        return instance;
    }

    public GuestService getGuestService() {
        return guestService;
    }

    public RoomService getRoomService() {
        return roomService;
    }

    public ReservationService getReservationService() {
        return reservationService;
    }
}
