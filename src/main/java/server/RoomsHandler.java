package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Guest;
import model.Room;
import service.RoomService;
import service.ServiceManager;

import java.io.IOException;
import java.util.List;

import static server.HttpUtils.sendResponse;

public class RoomsHandler implements HttpHandler {

    private RoomService roomService;

    public RoomsHandler() {
        roomService = ServiceManager.getInstance().getRoomService();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String requestMethod = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] segments = path.split("/");

        if ("GET".equals(requestMethod)) {
            if (segments.length == 4) {
                int roomNumber;

                try {
                    roomNumber = Integer.parseInt(segments[2]);
                    List<Integer> roomNumbers = roomService.getExistingRoomNumbers();
                    if (!roomNumbers.contains(roomNumber)) {
                        sendResponse(exchange, 404, "Room number " + roomNumber + " not found");
                    }
                } catch (NumberFormatException ex) {
                    sendResponse(exchange, 400, "Bad Request");
                    return;
                }

                if ("details".equals(segments[3])) {
                    handleGetRoomDetails(exchange, roomNumber);
                } else {
                    sendResponse(exchange, 404, "Not Found");
                }
            } else if (segments.length == 3) {
                switch (segments[2]) {
                    case "occupied":
                        handleGetOccupiedRooms(exchange);
                        break;
                    case "available":
                        handleGetAvailableRooms(exchange);
                        break;
                    default:
                        sendResponse(exchange, 404, "Not Found");
                }
            } else {
                sendResponse(exchange, 404, "Not Found");
            }
        } else {
            sendResponse(exchange, 405, "Method Not Allowed");
        }
    }

    private void handleGetRoomDetails(HttpExchange exchange, int roomNumber) throws IOException {
        List<Guest> guests = roomService.getRoomHistory(roomNumber);
        boolean isOccupied = roomService.isRoomOccupied(roomNumber);

        String response = "{" +
                "\"roomNumber\": " + roomNumber + "," +
                "\"status\": \"" + (isOccupied ? "occupied" : "available") + "\"," +
                "\"history\": " +
                Guest.guestsToString(guests) +
                "}";

        sendResponse(exchange, 200, response);
    }

    private void handleGetOccupiedRooms(HttpExchange exchange) throws IOException {
        List<Room> rooms = roomService.getOccupiedRooms();
        sendResponse(exchange, 200, Room.roomsToString(rooms));
    }

    private void handleGetAvailableRooms(HttpExchange exchange) throws IOException {
        List<Room> rooms = roomService.getAvailableRooms();
        sendResponse(exchange, 200, Room.roomsToString(rooms));
    }
}
