package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Guest;
import service.GuestService;
import service.ServiceManager;

import java.io.IOException;
import java.util.*;

public class GuestsHandler implements HttpHandler {
    private GuestService guestService;

    public GuestsHandler() {
        guestService = ServiceManager.getInstance().getGuestService();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String requestMethod = exchange.getRequestMethod();

        switch (requestMethod) {
            case "GET":
                handleGetRequest(exchange);
                break;
            case "POST":
                handlePostRequest(exchange);
                break;
            default:
                HttpUtils.sendResponse(exchange, 405, "Method Not Allowed");
        }
    }

    private void handlePostRequest(HttpExchange exchange) throws IOException {
        Map<String, String> parameters = HttpUtils.getQueryParameters(exchange.getRequestURI());
        String name = parameters.get("name");
        String surname = parameters.get("surname");

        if (name == null || name.isEmpty() || surname == null || surname.isEmpty()) {
            HttpUtils.sendResponse(exchange, 400, "Both 'name' and 'surname' are required");
        } else {
            Guest createdGuest = guestService.createOrReturnExistingGuest(name, surname);
            HttpUtils.sendResponse(exchange, 201, createdGuest.toString());
        }
    }

    private void handleGetRequest(HttpExchange exchange) throws IOException {
        Map<String, String> parameters = HttpUtils.getQueryParameters(exchange.getRequestURI());
        String name = parameters.get("name");
        String surname = parameters.get("surname");

        if (name == null || name.isEmpty() || surname == null || surname.isEmpty()) {
            HttpUtils.sendResponse(exchange, 400, "Both 'name' and 'surname' are required");
        } else {
            Optional<Guest> guest = guestService.findGuestByNameAndSurname(name, surname);
            if (guest.isPresent()) {
                HttpUtils.sendResponse(exchange, 200, guest.get().toString());
            } else {
                HttpUtils.sendResponse(exchange, 404, "Guest not found");
            }
        }
    }
}
