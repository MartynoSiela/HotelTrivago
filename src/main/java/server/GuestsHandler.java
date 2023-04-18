package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Guest;
import service.GuestService;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GuestsHandler implements HttpHandler {
    private GuestService guestService;

    public GuestsHandler() {
        guestService = new GuestService();
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
//            case "PUT":
//                handlePutRequest(exchange);
//                break;
//            case "DELETE":
//                handleDeleteRequest(exchange);
//                break;
            default:
                sendResponse(exchange, 405, "Method Not Allowed");
        }
    }

    private void handlePostRequest(HttpExchange exchange) throws IOException {
        URI requestUri = exchange.getRequestURI();
        String query = requestUri.getRawQuery();

        Map<String, String> parameters = new HashMap<>();
        for (String parameter : query.split("&")) {
            String[] pair = parameter.split("=");
            if (pair.length > 1) {
                parameters.put(pair[0], pair[1]);
            } else {
                parameters.put(pair[0], "");
            }
        }
        Guest guest = new Guest(UUID.randomUUID(), parameters.get("name"), parameters.get("surname"));
        Guest createdGuest = guestService.createGuest(guest);
        sendResponse(exchange, 201, createdGuest.toString());
    }

    private void handleGetRequest(HttpExchange exchange) throws IOException {
        List<Guest> guests = guestService.getAllGuests();
        sendResponse(exchange, 200, guests.toString());
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.length());
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
