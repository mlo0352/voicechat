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
public class setPlayerNameHandler implements HttpHandler{
    
    private final Server mainServer;

    public setPlayerNameHandler(Server mainServer) {
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
        //long chId = Long.parseLong(headers.getFirst("chId"));
        exchange.sendResponseHeaders(200, 0);
        exchange.close();
        
//        long chId = (addr[0] << 48 | addr[1] << 32 | addr[2] << 24 | addr[3] << 16) + port;
        
//        if (mainServer.findPlayerByNameChId(playerName, chId)) {
//            exchange.sendResponseHeaders(200, 0);
//            exchange.close();
//        } else {
//            mainServer.addPlayer(playerName, chId);
//            exchange.sendResponseHeaders(200, 0);
//            exchange.close();
//        }
    }
            
}
