import com.sun.net.httpserver.*;
import java.io.IOException;
import java.nio.charset.Charset;
import org.json.*;
import org.json.JSONArray;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ian
 */
public class SetSelectedRoundScoreForTeamHandler implements HttpHandler{
    
    private final Server mainServer;

    public SetSelectedRoundScoreForTeamHandler(Server mainServer) {
        this.mainServer = mainServer;
    }    
    
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        if (!("POST".equals(requestMethod))){
            exchange.sendResponseHeaders(405, 0);
            exchange.close();
        }
        long chId = Long.parseLong(exchange.getRequestHeaders().getFirst("chId"));
        JSONObject requestJson = new JsonFromInputStream().JsonFromInputStream(exchange.getRequestBody());
        String round = requestJson.get("selectedRound").toString();
        String teamName = requestJson.get("teamName").toString();
        String score = requestJson.get("score").toString();
        mainServer.setSelectedRoundScoreForTeam(Integer.valueOf(round), teamName, Integer.valueOf(score));
        exchange.sendResponseHeaders(200, 0);
        exchange.close();
    }
            
}
