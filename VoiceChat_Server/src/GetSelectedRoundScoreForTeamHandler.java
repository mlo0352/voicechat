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
public class GetSelectedRoundScoreForTeamHandler implements HttpHandler{
    
    private final Server mainServer;

    public GetSelectedRoundScoreForTeamHandler(Server mainServer) {
        this.mainServer = mainServer;
    }    
    
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        if (!("POST".equals(requestMethod))){
            exchange.sendResponseHeaders(405, 0);
            exchange.close();
        }
        JSONObject requestJson = new JsonFromInputStream().JsonFromInputStream(exchange.getRequestBody());
        String teamName = requestJson.get("teamName").toString();
        String selectedRound = requestJson.get("selectedRound").toString();
        
        
        JSONObject responseJson = new JSONObject();
        int score = mainServer.getSelectedRoundScoreForTeam(Integer.valueOf(selectedRound), teamName);
        responseJson.put("score", String.valueOf(score));
        String responseString = responseJson.toString();
        exchange.sendResponseHeaders(200, responseString.length());
        exchange.getResponseBody().write(responseString.getBytes(Charset.forName("UTF-8")));
        exchange.close();
    }
            
}
