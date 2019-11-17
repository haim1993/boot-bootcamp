import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.OutputStream;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class SimpleServer {
    public static int VISITS_COUNTER = 1;
    public static int STATUS_CODE_OKAY = 200;
    public static int PORT = 9000;


    public static Logger logger;

    public static void main(String[] args) throws IOException {
        logger = LogManager.getLogger(SimpleServer.class);

        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        HttpContext context = server.createContext("/boot-bootcamp");
        context.setHandler(SimpleServer::handleRequest);

        server.start();
    }

    private static void handleRequest(HttpExchange exchange) throws IOException {
        String response = "Number of visits: " + (VISITS_COUNTER++);
        exchange.sendResponseHeaders(STATUS_CODE_OKAY, response.getBytes().length); //response code and length

        logger.info("date: " + new Date() +  ", message: " + response);

        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

}
