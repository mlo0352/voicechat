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
public class ElevateToQuizzoMasterHandler implements HttpHandler{
    
    private final Server mainServer;

    public ElevateToQuizzoMasterHandler(Server mainServer) {
        this.mainServer = mainServer;
    } 
    
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        if (!("POST".equals(requestMethod))){
            exchange.sendResponseHeaders(405, 0);
            exchange.close();
        }
        //JSONObject requestJson = new JsonFromInputStream().JsonFromInputStream(exchange.getRequestBody());
        
        long chId = Long.parseLong(exchange.getRequestHeaders().getFirst("chId"));
        //long chId = Long.parseLong(requestJson.get("chId").toString());
        if (mainServer.elevateToQuizzoMaster(chId)){
            exchange.sendResponseHeaders(200, 0);
            exchange.close();
        } else{
            exchange.sendResponseHeaders(400, 0);
            exchange.close();
        }
        
            

        
    }
            
}
