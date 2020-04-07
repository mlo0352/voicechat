/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ian
 */
public class Player {
    private long chId;
    private String name;
    
    public Player(long chId, String name)
    {
        this.chId = chId;
        this.name = name;
    }
    
    public String getName(){
        return name;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public long getChId(){
        return chId;
    }
    
    public void setChId(long chId){
        this.chId = chId;
    }
}
