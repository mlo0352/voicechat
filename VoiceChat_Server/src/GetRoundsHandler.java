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
public class GetRoundsHandler implements HttpHandler{
    
    private final Server mainServer;

    public GetRoundsHandler(Server mainServer) {
        this.mainServer = mainServer;
    }    
    
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        if (!("POST".equals(requestMethod))){
            exchange.sendResponseHeaders(405, 0);
            exchange.close();
        }
        int roundNumber = mainServer.getRoundNumber();
        JSONObject responseJson = new JSONObject();
        responseJson.put("roundNumber", String.valueOf(roundNumber));
        String responseString = responseJson.toString();
        exchange.sendResponseHeaders(200, responseString.length());
        exchange.getResponseBody().write(responseString.getBytes(Charset.forName("UTF-8")));
        exchange.close();
    }
            
}
