import com.sun.net.httpserver.*;
import java.io.IOException;
import java.net.InetAddress;
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
public class AddPlayerToTeamHandler implements HttpHandler{
    
    private final Server mainServer;

    public AddPlayerToTeamHandler(Server mainServer) {
        this.mainServer = mainServer;
    }    
    
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        if (!("POST".equals(requestMethod))){
            exchange.sendResponseHeaders(405, 0);
            exchange.close();
        }
        JSONObject requestJson = new JsonFromInputStream().JsonFromInputStream(exchange.getRequestBody());
        
        String teamName = requestJson.get("teamname").toString();
        long chId = Long.parseLong(requestJson.get("chId").toString());
        
        // TODO: replace this with an exception-based system - chId check should be in addPlayerToTeam method in Team class
        if (mainServer.isChIdOnTeam(teamName, chId)) {
            exchange.sendResponseHeaders(400, 0);
            exchange.close();
        } else {
            mainServer.addPlayerToTeam(teamName, chId);
            exchange.sendResponseHeaders(201, 0);
            exchange.close();
        }
    }
            
}
