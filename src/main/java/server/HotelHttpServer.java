package server;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HotelHttpServer {

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/guests", new GuestsHandler());
        server.createContext("/rooms", new RoomsHandler());
        server.createContext("/reservations", new ReservationsHandler());
        server.start();
    }
}
