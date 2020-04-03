import com.sun.net.httpserver.*;
import java.io.IOException;
import java.nio.charset.Charset;
import org.json.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ian
 */
public class DeleteTeamHandler implements HttpHandler{
    
    HttpServer server;
    
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        exchange.sendResponseHeaders(501, 0);
        exchange.close();
    }
            
}
