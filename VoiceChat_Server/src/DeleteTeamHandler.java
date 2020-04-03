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
        String jsonString = new JSONObject()
                .put("JSON1", "Hello World!")
                .put("JSON2", "Hello my World!")
                .put("JSON3", new JSONObject()
                        .put("key1", "value1")).toString();
        exchange.sendResponseHeaders(200, jsonString.length());
        exchange.getResponseBody().write(jsonString.getBytes(Charset.forName("UTF-8")));
        exchange.close();
    }
            
}
