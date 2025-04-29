package com.bookstore;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import java.util.logging.Logger;
import java.io.IOException;
import java.net.URI;
public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static final String BASE_URI = "http://localhost:8081/";

    public static HttpServer startServer() {
        ResourceConfig config = new ResourceConfig()
                .packages("com.bookstore.resource", "com.bookstore.exception");

        return GrizzlyHttpServerFactory.createHttpServer(
                URI.create(BASE_URI),
                config
        );
    }

    public static void main(String[] args) throws IOException {
        HttpServer server = startServer();
        System.out.println("Server running at " + BASE_URI);
        System.out.println("Press CTRL+C to exit...");

        // Modern blocking alternative to deprecated suspend()
        System.in.read();
        server.shutdownNow();
    }
}



