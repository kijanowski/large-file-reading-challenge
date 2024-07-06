package lfrc.api.web;

import lfrc.api.ResponseConverter;
import lfrc.model.Temperatures;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.logging.Logger;

public class WebServer {

    private static final int WEBSERVER_PORT = 8000;
    private static final int SOCKET_BACKLOG = 0; // zero => a system default value is used
    private static final int OK = 200;
    private static final int NOT_FOUND = 404;
    private static final int WITHOUT_SLASH = 1;

    private static final Logger LOG = Logger.getLogger(WebServer.class.getName());

    private final Temperatures temps;
    private final ResponseConverter responseConverter;

    public WebServer(Temperatures temps, JsonResponseConverter responseConverter) {
        this.temps = temps;
        this.responseConverter = responseConverter;
    }

    public void start() {
        try {
            var server = HttpServer.create(new InetSocketAddress(WEBSERVER_PORT), SOCKET_BACKLOG);
            server.createContext("/", new RootHandler(temps, responseConverter));
            server.start();
            LOG.info(() -> "Server is running on port " + WEBSERVER_PORT);
        } catch (IOException ioe) {
            LOG.severe(() -> "Failed to create server: " + ioe.getMessage());
            throw new RuntimeException(ioe);
        }
    }

    static class RootHandler implements HttpHandler {

        private final Temperatures temps;
        private final ResponseConverter responseConverter;

        public RootHandler(Temperatures temps, ResponseConverter responseConverter) {
            this.temps = temps;
            this.responseConverter = responseConverter;
        }

        @Override
        public void handle(HttpExchange exchange) {
            LOG.fine(() -> "Handling request: " + exchange.getRequestURI().getPath());

            Optional.ofNullable(exchange.getRequestURI().getPath())
                    // sanitize(?), then get rid of the root slash
                    .map(path -> path.substring(WITHOUT_SLASH))
                    .map(city -> responseConverter.toResponse(temps, city))
                    .ifPresentOrElse(
                            response -> sendResponse(exchange, response),
                            () -> sendNotFound(exchange) // for undefined path
                    );
        }

        private void sendResponse(HttpExchange exchange, String response) {
            try {
                exchange.sendResponseHeaders(OK, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                exchange.close();
            } catch (IOException ioe) {
                LOG.severe(() -> "Failed to send response: " + ioe.getMessage());
            }
        }

        private void sendNotFound(HttpExchange exchange) {
            try {
                exchange.sendResponseHeaders(NOT_FOUND, 0);
                exchange.close();
            } catch (IOException ioe) {
                LOG.severe(() -> "Failed to send not found response: " + ioe.getMessage());
            }
        }

    }
}
