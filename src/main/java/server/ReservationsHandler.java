package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Reservation;
import model.Room;
import service.ReservationService;
import service.ServiceManager;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;


public class ReservationsHandler implements HttpHandler {
    private ReservationService reservationService;

    public ReservationsHandler() {
        reservationService = ServiceManager.getInstance().getReservationService();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String requestMethod = exchange.getRequestMethod();

        switch (requestMethod) {
            case "PUT":
                handleEndReservation(exchange);
                break;
            case "POST":
                handleCreateReservation(exchange);
                break;
            default:
                HttpUtils.sendResponse(exchange, 405, "Method Not Allowed");
        }
    }

    private void handleEndReservation(HttpExchange exchange) throws IOException {
        Map<String, String> parameters = HttpUtils.getQueryParameters(exchange.getRequestURI());
        String name = parameters.get("name");
        String surname = parameters.get("surname");

        if (name == null || name.isEmpty() || surname == null || surname.isEmpty()) {
            HttpUtils.sendResponse(exchange, 400, "Both 'name' and 'surname' are required");
        } else {
            Optional<Reservation> reservation = reservationService.checkForExistingReservation(name, surname);

            if (reservation.isPresent()) {
                reservationService.endReservation(reservation.get());
                Optional<Room> room = ServiceManager.getInstance().getRoomService().getRoomById(reservation.get().getRoomId());

                String responseMessage = "Reservation ended successfully. Guest: " + name + " " + surname + ", Room number: " + room.get().getRoomNumber();
                HttpUtils.sendResponse(exchange, 200, responseMessage);
            } else {
                HttpUtils.sendResponse(exchange, 404, "Reservation not found");
            }
        }
    }

    private void handleCreateReservation(HttpExchange exchange) throws IOException {
        Map<String, String> parameters = HttpUtils.getQueryParameters(exchange.getRequestURI());
        String name = parameters.get("name");
        String surname = parameters.get("surname");

        if (name == null || name.isEmpty() || surname == null || surname.isEmpty()) {
            HttpUtils.sendResponse(exchange, 400, "Both 'name' and 'surname' are required");
        } else {
            Optional<Reservation> reservation = reservationService.checkForExistingReservation(name, surname);

            if (reservation.isPresent()) {
                HttpUtils.sendResponse(exchange, 409, "Reservation already exists");
            } else {
                Optional<Room> room = reservationService.createReservation(name, surname);

                if (room.isPresent()) {
                    String responseMessage = "Reservation created successfully. Guest: " + name + " " + surname + ", Room number: " + room.get().getRoomNumber();
                    HttpUtils.sendResponse(exchange, 200, responseMessage);
                } else {
                    HttpUtils.sendResponse(exchange, 422, "No available rooms");
                }
            }
        }
    }
}
