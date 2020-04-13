
import com.sun.net.httpserver.*;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.NoSuchElementException;
import java.util.ArrayList;
import java.util.Enumeration;
import org.teleal.cling.UpnpService;
import org.teleal.cling.UpnpServiceImpl;
import org.teleal.cling.support.igd.PortMappingListener;
import org.teleal.cling.support.model.PortMapping;



/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 * opens a socket, listens for incoming connections, and creates a
 * ClientConnection for each client. also creates a BroadcastThread that passes
 * messages from the broadcastQueue to all the instances of ClientConnection
 *
 * @author dosse
 */
public class Server {
    
    private ArrayList<Message> broadCastQueue = new ArrayList<Message>();    
    private ArrayList<ClientConnection> clients = new ArrayList<ClientConnection>();
    private int audioPort;
    private int controlPort;
    private ArrayList<Team> teams = new ArrayList<Team>();
    private ArrayList<Player> players = new ArrayList<Player>();
    private Player quizzoMaster = null;
    private Team lobby = new Team();
    
    private UpnpService u; //when upnp is enabled, this points to the upnp service
    
    public void addToBroadcastQueue(Message m) { //add a message to the broadcast queue. this method is used by all ClientConnection instances
        try {
            broadCastQueue.add(m);
        } catch (Throwable t) {
            //mutex error, try again
            Utils.sleep(1);
            addToBroadcastQueue(m);
        }
    }
    private ServerSocket s;
    
    public Server(int port, boolean upnp) throws Exception{
        this.controlServer(port, upnp);
        this.audioServer(port, upnp);
    }
    
    public void controlServer(int port, boolean upnp) throws Exception{
        this.controlPort = ++port;
//        if(upnp){
//            u = uPNPSetup(port);
//        }
        HttpServer server = HttpServer.create(new InetSocketAddress(port),0);
        server.createContext("/createTeam", new CreateTeamHandler(this));
        server.createContext("/deleteTeam", new DeleteTeamHandler(this));
        server.createContext("/getTeams", new GetTeamsHandler(this));
        server.createContext("/getPlayersByTeam", new GetPlayersByTeamHandler(this));
        server.createContext("/setPlayerName", new SetPlayerNameHandler(this));
        server.createContext("/addPlayerToTeam", new AddPlayerToTeamHandler(this));
        server.createContext("/removePlayerFromTeam", new RemovePlayerFromTeamHandler(this));
        server.createContext("/killPlayer", new KillPlayerHandler(this));
        server.createContext("/toggleMuteTeamBroadcast", new ToggleMuteTeamBroadcastHandler());
        server.createContext("/elevateToQuizzoMaster", new ElevateToQuizzoMasterHandler(this));
        server.setExecutor(null); // creates a default executor
        server.start();
    }
    
    public void audioServer(int port, boolean upnp) throws Exception{
        this.audioPort = port;
        //<editor-fold defaultstate="collapsed" desc="UPNP Setup">
        if(upnp){
            u = uPNPSetup(port);
        }
        //</editor-fold>
        try {
            s = new ServerSocket(port); //listen on specified port
            Log.add("Port " + port + ": server started");
        } catch (IOException ex) {
            Log.add("Server error " + ex + "(port " + port + ")");
            throw new Exception("Error "+ex);
        }
        new BroadcastThread().start(); //create a BroadcastThread and start it
        for (;;) { //accept all incoming connection
            try {
                Socket c = s.accept();
                ClientConnection cc = new ClientConnection(this, c); //create a ClientConnection thread
                cc.start();
                addToClients(cc);
                Log.add("new client " + c.getInetAddress() + ":" + c.getPort() + " on port " + port);
            } catch (IOException ex) {
            }
        }
    }
    
    private UpnpService uPNPSetup(int port) throws Exception{
        Log.add("Setting up NAT Port Forwarding...");
            //first we need the address of this machine on the local network
            try {
                Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            } catch (SocketException ex) {
                Log.add("Network error");
                throw new Exception("Network error");
            }
            String ipAddress = null;
            Enumeration<NetworkInterface> net = null;
            try {
                net = NetworkInterface.getNetworkInterfaces();
            } catch (SocketException e) {
                Log.add("Not connected to any network");
                throw new Exception("Network error");
            }

            while (net.hasMoreElements()) {
                NetworkInterface element = net.nextElement();
                Enumeration<InetAddress> addresses = element.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress ip = addresses.nextElement();
                    if (ip instanceof Inet4Address) {
                        if (ip.isSiteLocalAddress()) {
                            ipAddress = ip.getHostAddress();
                            break;
                        }
                    }
                }
                if (ipAddress != null) {
                    break;
                }
            }
            if (ipAddress == null) {
                Log.add("Not connected to any IPv4 network");
                throw new Exception("Network error");
            }
            u = new UpnpServiceImpl(new PortMappingListener(new PortMapping(port, ipAddress, PortMapping.Protocol.TCP)));
            u.getControlPoint().search();
            return u;
    }

    private void addToClients(ClientConnection cc) {
        try {
            clients.add(cc); //add the new connection to the list of connections
        } catch (Throwable t) {
            //mutex error, try again
            Utils.sleep(1);
            addToClients(cc);
        }
    }

    /**
     * broadcasts messages to each ClientConnection, and removes dead ones
     */
    private class BroadcastThread extends Thread {
        
        public BroadcastThread() {
        }
        
        @Override
        public void run() {
            for (;;) {
                try {
                    ArrayList<ClientConnection> toRemove = new ArrayList<ClientConnection>(); //create a list of dead connections
                    for (ClientConnection cc : clients) {
                        if (!cc.isAlive()) { //connection is dead, need to be removed
                            Log.add("dead connection closed: " + cc.getInetAddress() + ":" + cc.getPort() + " on port " + audioPort);
                            toRemove.add(cc);
                        }
                    }
                    clients.removeAll(toRemove); //delete all dead connections
                    if (broadCastQueue.isEmpty()) { //nothing to send
                        Utils.sleep(10); //avoid busy wait
                        continue;
                    } else { //we got something to broadcast
                        Message m = broadCastQueue.get(0);
                        boolean inTeam = getPlayerByChId(m.getChId()).getInTeam();
                        if (inTeam){
                            for (Team t : teams){
                                Team senderTeam = new Team();
                                if (t.playerInTeam(m.getChId())){
                                    senderTeam = t;
                                }
                                ArrayList<Player> senderTeamPlayers = getPlayersByTeam(t.getTeamName());
                                for (Player p: senderTeamPlayers){
                                    if (p.getChId() != m.getChId()) {
                                        getClientConnectionByChId(p.getChId()).addToQueue(m);
                                        System.out.println(m.getChId() + "sending to: " + p.getChId());
                                    }
                                }
                            }
                        } else {
                            //if the current player isn't in a team, send to any other players not in a team
                            for (ClientConnection cc : clients){
                                if (!getPlayerByChId(cc.getChId()).getInTeam()) {
                                    if (cc.getChId() != m.getChId()) {
                                        //don't send to self
                                        cc.addToQueue(m);
                                    }
                                }
                            }
                        }
                        broadCastQueue.remove(m); //remove it from the broadcast queue
                    }
                } catch (Throwable t) {
                    //mutex error, try again
                }
            }
        }
    }
    
    public void createTeam(String teamName){
        System.out.println(teamName);
        Team team = new Team();
        team.setTeamName(teamName);
        teams.add(team);
    }
    
    public ArrayList<String> getTeams(){
        ArrayList<String> teamsStrings = new ArrayList<String>();
        for (Team t : teams){
            teamsStrings.add(t.getTeamName());
        }
        return teamsStrings;
    }
    
    public void addPlayer(String playerName, long chId){
        boolean found = false;
        for (Player player: players) {
            if (playerName.equals(player.getName()) || (player.getChId() == chId)) {
                player.setName(playerName);
                player.setChId(chId);
                found = true;
                break;
            }
        }
        if (!found) {
            players.add(new Player(chId, playerName));
        }
    }
    
    public void addPlayerToTeam(String teamName, long chId){
        String playerName = "";
        //get the player name
        for (Player player: players){
            if (player.getChId() == chId){
                playerName = player.getName();
                break;
            }
        }
        //remove player from any existing team
        for (Team t : teams){
            if (t.playerInTeam(playerName)){
                t.removePlayer(playerName);
                getPlayerByName(playerName).setInTeam(false); //set team status of main list to false
            }
        }
        
        //add player to correct team
        for (Team t : teams){
            if (teamName.equals(t.getTeamName())){
                t.addPlayerToTeam(playerName, chId);
                getPlayerByName(playerName).setInTeam(true); //set team status of main list to true
            }
        }
    }
    
    public void removePlayerFromTeam(String teamname, long chId) {
        //remove player from any existing team
        String playerName = "";
        for (Player player: players){
            if (player.getChId() == chId){
                playerName = player.getName();
                break;
            }
        }
        for (Team t : teams){
            if (t.playerInTeam(playerName)){
                t.removePlayer(playerName);
                getPlayerByName(playerName).setInTeam(false); //set team status of main list to false
            }
        }
    }
    
    public ArrayList<Player> getPlayersByTeam(String teamName){
        ArrayList<Player> players = new ArrayList<Player>();
        if ("Lobby".equals(teamName)){
            for (Player player : this.players){
                if (!player.getInTeam()){
                    players.add(player);
                }
            }
            return players;
        }
        for (Team t : teams){
            if (teamName.equals(t.getTeamName())){
                players = t.getPlayers();
            }
        }
        return players;
    }
    
    public boolean isChIdOnTeam(String teamName, long chId){
        ArrayList<Player> players = this.getPlayersByTeam(teamName);
        for (Player player: players){
            if (player.getChId() == chId){
                return true;
            }
        }
        return false;
    }
    
    public int getAudioPort(){
        return audioPort;
    }
    
    public Player getPlayerByName(String name) throws NoSuchElementException{
        for (Player player: players){
            if (name.equals(player.getName())){
                return player;
            }
        }
        throw new NoSuchElementException(name + " is not in the list.");
    }
    
    public Player getPlayerByChId(long chId) throws NoSuchElementException{
        for (Player player: players){
            if (player.getChId() == chId){
                return player;
            }
        }
        throw new NoSuchElementException(chId + " is not in the list.");
    }
    
    public void killPlayer(long chId){
            for (Team t : teams){
                if (t.playerInTeam(chId)){
                    t.removePlayer(chId);
                    getPlayerByChId(chId).setInTeam(false); //set team status of main list to false
            }
        }
        Player p = getPlayerByChId(chId);
        players.remove(p);
    }
    
    public boolean elevateToQuizzoMaster(long chId){
        //If master already exists, return false
        if (quizzoMaster != null){
            quizzoMaster = getPlayerByChId(chId);
            return true;
        } else {
            //Else set master to player and return true
            return false;
        }
    }
    
    public ClientConnection getClientConnectionByChId(long chId) throws Exception{
        for (ClientConnection cc: clients){
            if (chId == cc.getChId()){
                return(cc);
            }
        }
        throw(new NoSuchElementException("chId not in clients list: " + chId));
    }
    
}
