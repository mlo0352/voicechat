import com.sun.net.httpserver.*;
import java.io.IOException;
import groovy.json.*;
import java.nio.charset.Charset;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ian
 */
public class TestHandler implements HttpHandler{
    
    HttpServer server;
    
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        //exchange.responseHeaders["Content-Type"] = "application/json";
        //JsonBuilder builder = new JsonBuilder();
        //        builder {
//            success true
//            method requestMethod
//        }
        //String response = builder.toString();
        String response = "{\"test\": \"done\"";
        exchange.sendResponseHeaders(200, response.length());
//        exchange.getResponseBody().write(response.getBytes(Charset.forName("UTF-8")));
        exchange.close();
    }
            
}
