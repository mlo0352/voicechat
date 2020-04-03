import java.io.IOException;
import java.io.InputStream;
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
public class JsonFromInputStream{
    public JSONObject JsonFromInputStream(InputStream requestBody) throws IOException{
        StringBuilder sb = new StringBuilder();
        int i;
        while ((i = requestBody.read()) != -1) {
            sb.append((char) i);
        }
        JSONObject requestJson = new JSONObject(sb.toString());
        return requestJson;
    }
}
