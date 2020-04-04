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
        String playerName = requestJson.get("playername").toString();
        
        byte[] addr = exchange.getRemoteAddress().getAddress().getAddress();
        int port = exchange.getRemoteAddress().getPort();
        
        long chId = (addr[0] << 48 | addr[1] << 32 | addr[2] << 24 | addr[3] << 16) + port;
        
        if (mainServer.getPlayersOnTeam(teamName).contains(playerName)) {
            exchange.sendResponseHeaders(400, 0);
            exchange.close();
        } else {
            mainServer.addPlayerToTeam(teamName, playerName, chId);

            exchange.sendResponseHeaders(201, 0);
            exchange.close();
        }
    }
            
}
