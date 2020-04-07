import com.sun.net.httpserver.*;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.charset.Charset;
import org.json.*;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ian
 */
public class GetPlayersByTeamHandler implements HttpHandler{
    
    private final Server mainServer;

    public GetPlayersByTeamHandler(Server mainServer) {
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
        if ("".equals(teamName)){
            teamName = "Lobby";
        }
        ArrayList<Player> players = mainServer.getPlayersByTeam(teamName);
        ArrayList<String> playersArray = new ArrayList<String>();
        for (Player player : players){
            playersArray.add(player.getName());
        }
        JSONObject responseJson = new JSONObject();
        responseJson.put("players", new JSONArray(playersArray));
        String responseString = responseJson.toString();
        exchange.sendResponseHeaders(200, responseString.length());
        exchange.getResponseBody().write(responseString.getBytes(Charset.forName("UTF-8")));
        exchange.close();
    }
            
}
