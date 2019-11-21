package com;

import java.io.IOException;
import java.util.UUID;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;


public class SimpleServer {
    public static int PORT = 8080;
    public static String SEED;

    public static int VISITS_COUNTER = 1;

    public static void main(String[] args) throws IOException {
        // Generate unique random seed
        SEED = UUID.randomUUID().toString();

        ResourceConfig config = new ResourceConfig();
        config.packages("com");
        ServletHolder servlet = new ServletHolder(new ServletContainer(config));

        Server server = new Server(PORT);
        ServletContextHandler context = new ServletContextHandler(server, "/*");
        context.addServlet(servlet, "/*");

        try {
            server.start();
            server.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            server.destroy();
        }
    }
}
