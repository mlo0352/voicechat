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
        JSONArray ja = requestJson.getJSONArray("teams");
        for(int i = 0; i < ja.length(); i++)
            responseList.add(ja.getString(i));
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
    
    public void deleteTeam(String teamName) throws IOException, MalformedURLException{
        URL url = new URL(this.url + "deleteTeam");
    }
    
    public void setPlayerName(String playerName) throws IOException, MalformedURLException {
        URL url = new URL(this.url + "setPlayerName");
        String input = "";
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("ChId", this.chId);
        JSONObject j = new JSONObject();
        j.put("name", playerName);
        j.put("chId", chId);
        this.getOSAndVerifyResponseCode(conn, HttpURLConnection.HTTP_OK, j.toString());
        conn.disconnect();
    }
    
    public ArrayList<String> getPlayersOnTeam(String teamName)  throws IOException, MalformedURLException {
        ArrayList<String> responseList = new ArrayList<String>();
        URL url = new URL(this.url + "getPlayersByTeam");
        JSONObject j = new JSONObject();
        j.put("teamname", teamName);
        j.put("chId", chId);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("ChId", this.chId);
        this.getOSAndVerifyResponseCode(conn, HttpURLConnection.HTTP_OK, j.toString());
        JSONObject requestJson = jsonFromInputStream(conn.getInputStream());
        System.out.println(requestJson);
        JSONArray ja = requestJson.getJSONArray("players");
        for(int i = 0; i < ja.length(); i++)
            responseList.add(ja.getString(i));
        return responseList;
    }
    
    public void addPlayerToTeam(String teamName) throws IOException, MalformedURLException{
        URL url = new URL(this.url + "addPlayerToTeam");
        String input = "";
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("ChId", this.chId);
        JSONObject j = new JSONObject();
        if (teamName.isEmpty()){
            j.put("teamname", input);
        } else{
            j.put("teamname", teamName);
        }
        j.put("chId", chId);
        this.getOSAndVerifyResponseCode(conn, HttpURLConnection.HTTP_OK, j.toString());
        conn.disconnect();
    }
    
    public void removePlayerFromTeam(String teamName) throws IOException, MalformedURLException{
        URL url = new URL(this.url + "removePlayerFromTeam");
        String input = "";
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("ChId", this.chId);
        JSONObject j = new JSONObject();
        if (teamName.isEmpty()){
            j.put("teamname", input);
        } else{
            j.put("teamname", teamName);
        }
        j.put("chId", chId);
        this.getOSAndVerifyResponseCode(conn, HttpURLConnection.HTTP_OK, j.toString());
        conn.disconnect();
    }
    
    public void toggleMutePlayerBroadcast() throws IOException, MalformedURLException{
        URL url = new URL(this.url + "toggleMutePlayerBroadcast");
    }
    
    public void toggleMuteTeamBroadcast() throws IOException, MalformedURLException{
        URL url = new URL(this.url + "toggleMuteTeamBroadcast");
    }
    
    public void killPlayer() throws IOException, MalformedURLException{
        //implement
        URL url = new URL(this.url + "killPlayer");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("ChId", this.chId);
        String j = "";
        this.getOSAndVerifyResponseCode(conn, HttpURLConnection.HTTP_OK, j.toString());
        conn.disconnect();
    }
    
    public String getQuizzoMaster()  throws IOException, MalformedURLException {
        URL url = new URL(this.url + "getQuizzoMaster");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        try {
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("ChId", this.chId);
            String j = "";
            JSONObject responseJson = jsonFromInputStream(conn.getInputStream());
            conn.disconnect();
            Object quizzoMaster = responseJson.get("quizzoMaster");
            return quizzoMaster.toString();
        } catch (java.io.FileNotFoundException e) {
            return null;
        }
    }
    
    public String getTeamCaptain()  throws IOException, MalformedURLException {
        URL url = new URL(this.url + "getTeamCaptain");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        try {
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("ChId", this.chId);
            String j = "";
            JSONObject responseJson = jsonFromInputStream(conn.getInputStream());
            conn.disconnect();
            Object teamCaptain = responseJson.get("teamCaptain");
            return teamCaptain.toString();
        } catch (java.io.FileNotFoundException e) {
            return null;
        }
    }
    
    public void elevatePlayerToQuizzoMaster() throws IOException, MalformedURLException{
        //implement
        URL url = new URL(this.url + "elevatePlayerToQuizzoMaster");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("ChId", this.chId);
        String j = "";
        this.getOSAndVerifyResponseCode(conn, HttpURLConnection.HTTP_OK, j.toString());
        conn.disconnect();
    }
    
    public void elevatePlayerToTeamCaptain() throws IOException, MalformedURLException{
        //implement
        URL url = new URL(this.url + "elevatePlayerToTeamCaptain");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("ChId", this.chId);
        String j = "";
        this.getOSAndVerifyResponseCode(conn, HttpURLConnection.HTTP_OK, j.toString());
        conn.disconnect();
    }
    
    public String getSelectedRoundAnswersForTeam(String selectedRound, String teamName) throws IOException, MalformedURLException{
        URL url = new URL(this.url + "getSelectedRoundAnswersForTeam");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("ChId", this.chId);
        JSONObject j = new JSONObject();
        j.put("selectedRound", selectedRound);
        j.put("teamName", teamName);
        this.getOSAndVerifyResponseCode(conn, HttpURLConnection.HTTP_OK, j.toString());
        JSONObject responseJson = jsonFromInputStream(conn.getInputStream());
        conn.disconnect();
        String response = responseJson.getString("answers");
        return response;
    }
    
    public void setSelectedRoundScoreForTeam(String selectedRound, String teamName, String score) throws IOException, MalformedURLException{
        URL url = new URL(this.url + "setSelectedRoundScoreForTeam");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("ChId", this.chId);
        JSONObject j = new JSONObject();
        j.put("selectedRound", selectedRound);
        j.put("teamName", teamName);
        j.put("score", score);
        this.getOSAndVerifyResponseCode(conn, HttpURLConnection.HTTP_OK, j.toString());
        conn.disconnect();
    }
    
    public String getSelectedRoundScoreForTeam(String selectedRound, String teamName) throws IOException, MalformedURLException{
        URL url = new URL(this.url + "getSelectedRoundScoreForTeam");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("ChId", this.chId);
        JSONObject j = new JSONObject();
        j.put("selectedRound", selectedRound);
        j.put("teamName", teamName);
        this.getOSAndVerifyResponseCode(conn, HttpURLConnection.HTTP_OK, j.toString());
        JSONObject responseJson = jsonFromInputStream(conn.getInputStream());
        conn.disconnect();
        String response = responseJson.getString("score");
        return response;
    }
    
    public String getTotalScoreForTeam(String teamName) throws IOException, MalformedURLException{
        URL url = new URL(this.url + "getTotalScoreForTeam");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("ChId", this.chId);
        JSONObject j = new JSONObject();
        j.put("teamName", teamName);
        this.getOSAndVerifyResponseCode(conn, HttpURLConnection.HTTP_OK, j.toString());
        JSONObject responseJson = jsonFromInputStream(conn.getInputStream());
        conn.disconnect();
        String response = responseJson.getString("score");
        return response;
    
    }
    
    public void setAnswersForTeam(String answers) throws IOException, MalformedURLException{
        URL url = new URL(this.url + "setAnswersForTeam");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("ChId", this.chId);
        JSONObject j = new JSONObject();
        j.put("answers", answers);
        this.getOSAndVerifyResponseCode(conn, HttpURLConnection.HTTP_OK, j.toString());
        conn.disconnect();
    }
    
    public void setNewRound() throws IOException, MalformedURLException{
        URL url = new URL(this.url + "setNewRound");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("ChId", this.chId);
        String j = "";
        this.getOSAndVerifyResponseCode(conn, HttpURLConnection.HTTP_OK, j.toString());
        conn.disconnect();
    }
    
    
    public String getRoundNumber() throws IOException, MalformedURLException{
        URL url = new URL(this.url + "getRoundNumber");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("ChId", this.chId);
        String j="";
        this.getOSAndVerifyResponseCode(conn, HttpURLConnection.HTTP_OK, j.toString());
        JSONObject responseJson = jsonFromInputStream(conn.getInputStream());
        conn.disconnect();
        String response = responseJson.getString("roundNumber");
        return response;
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
