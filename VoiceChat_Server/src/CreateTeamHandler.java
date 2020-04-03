import com.sun.net.httpserver.*;
import java.io.IOException;
import java.io.InputStream;
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
public class CreateTeamHandler implements HttpHandler{
    
    private final Server mainServer;

    public CreateTeamHandler(Server mainServer) {
        // there is an implied super() here
        this.mainServer = mainServer;
    }    
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        if (!("POST".equals(requestMethod))){
            exchange.sendResponseHeaders(405, 0);
            exchange.close();
        }
        JSONObject requestJson = new JsonFromInputStream().JsonFromInputStream(exchange.getRequestBody());
        
        String name = requestJson.get("name").toString();
        
        if (mainServer.getTeams().contains(name)) {
            exchange.sendResponseHeaders(400, 0);
            exchange.close();
        } else {
            mainServer.createTeam(name);

            exchange.sendResponseHeaders(201, 0);
            exchange.close();
        }
    }
            
}
