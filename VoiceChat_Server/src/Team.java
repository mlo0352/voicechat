import java.util.ArrayList;
import java.util.Set;
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
    
    public void addPlayerToTeam(String playerName, Long chId)
    {
        if (!teamMembersNamesClients.containsKey(playerName)){
            if ((numberOfMembers + 1) <= max) {
                teamMembersNamesClients.put(playerName, chId);
                numberOfMembers++;
            }
        } else {
            throw new UnsupportedOperationException();
        }
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
    
    public boolean toggleMute(){
        muted = !muted;
        return muted;
    }
}
