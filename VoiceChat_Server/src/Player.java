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
    private ClientConnection conn;
    private String name;
    
    public Player(ClientConnection newConn, String newName)
    {
        conn = newConn;
        name = newName;
    }
    
    public String getName(){
        return name;
    }
    
    public void setName(String newName){
        name = newName;
    }
}
