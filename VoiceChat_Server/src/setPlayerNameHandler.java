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
public class SetPlayerNameHandler implements HttpHandler{
    
    private final Server mainServer;

    public SetPlayerNameHandler(Server mainServer) {
        this.mainServer = mainServer;
    }    
    
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        if (!("POST".equals(requestMethod))){
            exchange.sendResponseHeaders(405, 0);
            exchange.close();
        }
        JSONObject requestJson = new JsonFromInputStream().JsonFromInputStream(exchange.getRequestBody());
        
        String playerName = requestJson.get("name").toString();
        long chId = Long.parseLong(requestJson.get("chId").toString());
        System.out.println(playerName + ": " + String.valueOf(chId));
        
        Headers headers = exchange.getRequestHeaders();
        exchange.sendResponseHeaders(200, 0);
        exchange.close();
        
        mainServer.addPlayer(playerName, chId);
        exchange.sendResponseHeaders(200, 0);
        exchange.close();
    }
            
}
