import java.util.ArrayList;
import java.util.Set;
import java.util.HashMap;
import com.google.common.collect.BiMap; 
import com.google.common.collect.HashBiMap; 

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ian
 */
public class Team {
    private BiMap<String, Long> teamMembersNamesClients = HashBiMap.create(); 
    private int max = 8;
    private String teamName;
    private int numberOfMembers;
    private int score = 0;
    private boolean muted = true;
    private ArrayList<Player> players = new ArrayList<Player>();
    private HashMap<Integer, String> answers = new HashMap<Integer, String>();
    private Player captain = null;
    
    public void addPlayer(Player player){
        this.addPlayerToTeam(player.getName(), player.getChId());
    }
    
    public void removePlayer(String playerName){
        for (Player player: players){
            if (playerName.equals(player.getName())){
                players.remove(player);
                break;
            }
        }
    }
    
    public void removePlayer(long chId){
        for (Player player: players){
            if (chId == player.getChId()){
                players.remove(player);
                break;
            }
        }
    }
    
    public void addPlayerToTeam(String playerName, Long chId)
    {
        players.add(new Player(chId, playerName));
        System.out.println("Player added [" + playerName + ": " + String.valueOf(chId) + "]");
        
    }
    
    public void removeClientFromTeam(ClientConnection teamMemberClient)
    {
//        if (teamMembersClients.contains(teamMemberClient))
//        {
//            teamMembersClients.remove(teamMemberClient);
//            numberOfMembers--;
//        }
    }
    
    public int getNumberOfMembers()
    {
        return numberOfMembers;
    }
    
    public String getTeamName()
    {
        return teamName;
    }
    
    public void setTeamName(String teamName)
    {
        this.teamName = teamName;
    }
    
    public int getScore()
    {
        return score;
    }
    
    public int addScore(int points)
    {
        score += points;
        return score;
    }
    
    public ArrayList<String> getPlayerNames(){
        ArrayList<String> playerNames = new ArrayList<String>();
        Set<String> names = teamMembersNamesClients.inverse().values();
        for (String name : names) 
            playerNames.add(name); 
        return playerNames;
    }
    
    public ArrayList<Player> getPlayers(){
        return players;
    }
    
    public boolean toggleMute(){
        muted = !muted;
        return muted;
    }
    
    public boolean playerInTeam(String playerName){
        for (Player player : players){
            if (playerName.equals(player.getName())){
                return true;
            }
        }
        return false;
    }
    
    public boolean playerInTeam(long chId){
        for (Player player : players){
            if (chId == player.getChId()){
                return true;
            }
        }
        return false;
    }
    
    public void setAnswers(int roundNumber, String answer){
        answers.put(roundNumber, answer);
    }
    
    public String getAnswers(int roundNumber){
        try{
            return answers.get(roundNumber);
        } catch (Exception e){ return "TASTY";}
    }
    
    public Player getCaptain(){
        return captain;
    }
    
    public void setCaptain(Player player){
        captain = player;
    }
    
    public void removeCaptain(){
        captain = null;
    }
}
