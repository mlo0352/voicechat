//import kong.unirest.*;
//import kong.unirest.json.*;
import org.json.*;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.InputStream;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ian
 */
public class RestClient{
    String baseUrl;
    String port;
    String url;
    String chId;
    
    public RestClient(String baseUrl, int port, long chId){
        this.baseUrl = baseUrl;
        this.port = String.valueOf(port);
        this.url = "http://" + baseUrl + ":" + port + "/";
        this.chId = String.valueOf(chId);
    }
    
    public ArrayList<String> getTeams() throws IOException, MalformedURLException{
        ArrayList<String> responseList = new ArrayList<String>();
        URL url = new URL(this.url + "getTeams");
        String input = "";
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("ChId", this.chId);
        this.getOSAndVerifyResponseCode(conn, HttpURLConnection.HTTP_OK, input);
        JSONObject requestJson = jsonFromInputStream(conn.getInputStream());
        System.out.println(requestJson);
        return responseList;
    }
    
    public void createTeam(String teamName) throws IOException, MalformedURLException{
        URL url = new URL(this.url + "createTeam");
        String input = "";
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("ChId", this.chId);
        JSONObject j = new JSONObject();
        j.put("name", teamName);
        this.getOSAndVerifyResponseCode(conn, HttpURLConnection.HTTP_CREATED, j.toString());
        conn.disconnect();
    }
    
    public OutputStream getOSAndVerifyResponseCode(HttpURLConnection conn, int httpCode, String input) throws IOException{
        OutputStream os = conn.getOutputStream();
        os.write(input.getBytes());
        os.flush();
        if (conn.getResponseCode() != httpCode) {
            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
        }
        return os;
    }
    
    public JSONObject jsonFromInputStream(InputStream requestBody) throws IOException{
        StringBuilder sb = new StringBuilder();
        int i;
        while ((i = requestBody.read()) != -1) {
            sb.append((char) i);
        }
        JSONObject requestJson = new JSONObject(sb.toString());
        return requestJson;
    }
    
}
