
import java.util.ArrayList;

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
    private ArrayList<ClientConnection> teamMembersClients = new ArrayList<ClientConnection>();
    private int max;
    private String teamName;
    private int numberOfMembers;
    private int score = 0;
    
    public void addClientToTeam(ClientConnection newTeamMemberClient)
    {
        if (!teamMembersClients.contains(newTeamMemberClient) && ((numberOfMembers + 1) <= max)) 
        {
            teamMembersClients.add(newTeamMemberClient);
            numberOfMembers++;
        } else {
            throw new UnsupportedOperationException();
        }
    }
    
    public void removeClientFromTeam(ClientConnection teamMemberClient)
    {
        if (teamMembersClients.contains(teamMemberClient))
        {
            teamMembersClients.remove(teamMemberClient);
            numberOfMembers--;
        }
    }
    
    public int getNumberOfMembers()
    {
        return numberOfMembers;
    }
    
    public String getTeamName()
    {
        return teamName;
    }
    
    public void setTeamName(String name)
    {
        teamName = name;
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
}
