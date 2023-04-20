package server;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class HttpUtils {

    protected static Map<String, String> getQueryParameters(URI requestUri) {
        String query = requestUri.getRawQuery();
        Map<String, String> parameters = new HashMap<>();
        if (query != null) {
            for (String parameter : query.split("&")) {
                String[] pair = parameter.split("=");
                if (pair.length > 1) {
                    parameters.put(pair[0], pair[1]);
                } else {
                    parameters.put(pair[0], "");
                }
            }
        }
        return parameters;
    }

    protected static void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.length());
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
