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
public class GetQuizzoMasterHandler implements HttpHandler{
    
    private final Server mainServer;

    public GetQuizzoMasterHandler(Server mainServer) {
        this.mainServer = mainServer;
    } 
    
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        if (!("GET".equals(requestMethod))){
            exchange.sendResponseHeaders(405, 0);
            exchange.close();
        }
        long chId = Long.parseLong(exchange.getRequestHeaders().getFirst("chId"));
        
        JSONObject responseJson = new JSONObject();
        Player quizzoMaster = mainServer.getQuizzoMaster();
        if (quizzoMaster != null){
            responseJson.put("quizzoMaster", quizzoMaster.getName());
            String responseString = responseJson.toString();
            exchange.sendResponseHeaders(200, responseString.length());
            exchange.getResponseBody().write(responseString.getBytes(Charset.forName("UTF-8")));
            exchange.close();
        } else{
            exchange.sendResponseHeaders(404, 0);
            exchange.close();
        }
    }
            
}
