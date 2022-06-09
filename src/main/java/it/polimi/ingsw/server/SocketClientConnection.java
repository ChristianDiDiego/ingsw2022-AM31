package it.polimi.ingsw.server;

import it.polimi.ingsw.model.ColorOfTower;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.StudsAndProfsColor;
import it.polimi.ingsw.utilities.ServerMessage;
import it.polimi.ingsw.utilities.constants.Constants;
import it.polimi.ingsw.view.RemoteView;

import javax.sound.midi.Soundbank;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

/*
Instanced by the server, it interacts with a client sending and receiving message from it
 */
public class SocketClientConnection implements Runnable{
    private Socket socket;
    private String nickname;
     private ObjectOutputStream out;

    transient private Scanner inGeneral;

    private boolean playerQuitted = false;

    private Server server;
     private PropertyChangeSupport support;

    private boolean active = true;

    private boolean clientAlive = true;

    public SocketClientConnection(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        this.support = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    private synchronized boolean isActive(){
        return active;
    }

    /**
     * questa send manda il messaggio così com'è senza metterlo in stringa
     * @param message
     */
    public synchronized void send(Object message) {
        try {
            out.reset();
            out.writeObject(message);
            out.flush();
            System.out.println("sent " + message);
        } catch(IOException e){
            System.out.println("error when sending " + message.toString());
            System.err.println(e.getMessage());
        }
    }

    public int askHowManyPlayers() {
        Scanner in;
        String number;
        try {
            in = new Scanner(socket.getInputStream());
            send(ServerMessage.howManyPlayers); //manda al client
            String read = in.nextLine(); // legge dal client il nome
            System.out.println("received " + read + "from " + nickname);
            if(read.equalsIgnoreCase(Constants.QUIT)){
                System.out.println("quit received");
                //playerQuitted = true;
                close();
                return -2;
            }
            number = read;
                try{
                    return Integer.parseInt(number);
                }catch (NumberFormatException e){
                    return -1 ;
                }

        } catch (IOException | NoSuchElementException e) {
            System.err.println("Error! " + e.getMessage());
            return -1;
        }
    }

    public String askNickname() {
        Scanner in;
        try {
            in = new Scanner(socket.getInputStream());
            send(ServerMessage.askNickname); //manda al client
            String read = in.nextLine();
            System.out.println("received " + read + "from " + nickname);
            if(read.equalsIgnoreCase(Constants.QUIT)){
                System.out.println("quit received");
                //playerQuitted = true;
                close();
                return read;
            }
            return read;
        } catch (IOException | NoSuchElementException e) {
            System.err.println("Error! " + e.getMessage());
            return "wrong";
        }
    }

    //TODO: rivedere la return
    public int askMode() {
        Scanner in;
        try {
            in = new Scanner(socket.getInputStream());
            send(ServerMessage.askMode); //manda al client
            String read = in.nextLine();// legge dal client il nome
            System.out.println("received " + read + "from " + nickname);
            if(read.equalsIgnoreCase(Constants.QUIT)){
                System.out.println("quit received");
                //playerQuitted = true;
                close();
                return -2;
            }
            try{
                if(Integer.parseInt(read)== 0 || Integer.parseInt(read) ==1) {
                    return Integer.parseInt(read);
                }else{
                    return -1;
                }
            }catch (NumberFormatException e){
                return -1 ;
            }
        } catch (IOException | NoSuchElementException e ) {
            System.err.println("Error! " + e.getMessage());
            return -1;
        }
    }

    public ColorOfTower askColor() {
        Scanner in;
        int number;
        try {
            in = new Scanner(socket.getInputStream());
            switch (server.getNumberOfPlayers()){
                case 2:
                case 4:
                    send(ServerMessage.askColor2_4Players); //manda al client
                    break;
                case 3:
                    send(ServerMessage.askColor3Players); //manda al client
            }

            String read = in.nextLine(); // legge dal client il nome
            System.out.println("received " + read + "from " + nickname);
            if(read.equalsIgnoreCase(Constants.QUIT)){
                System.out.println("quit received");
                //playerQuitted = true;
                close();
                return null;
            }
            try{
                number = Integer.parseInt(read);
                if((server.getNumberOfPlayers() == 3 && (number < 0 || number > 2))
                || (server.getNumberOfPlayers()%2 == 0  && (number < 0 || number > 1) )){
                    return null;
                }else {
                    return ColorOfTower.values()[number];
                }
            }catch (NumberFormatException e){
                return null;
            }
        } catch (IOException | NoSuchElementException e) {
            System.err.println("Error! " + e.getMessage());
            return null;
        }
    }

    //invia il messaggio di chiusura al client
    public synchronized void closeConnection() {
        Object lock1 = new Object();
        synchronized (lock1){
            send(ServerMessage.connectionClosed);
        }
        synchronized (lock1){
            try {
                System.out.println( "I'm disconnecting " + getNickname());
                socket.close();
                inGeneral.close();
            } catch (IOException e) {
                System.err.println("Error when closing socket!");
            }
            active = false;
        }

    }

    private void close() { //stampa sul server
        server.deregisterConnection(this);
        closeConnection();
        System.out.println("Deregistering client...");
        System.out.println("Done!");
    }

    /**
     *     closes only one connection and, if the connection is the last one still active in
     *     game, removes the connection list from server's gameList
     */
    public void closeOnlyThis(){
        server.checkEmptyGames(this);
        closeConnection();
    }

    /**
     * riceve dalla remoteview il messaggio con la nuova board (+vittoria ecc)
     * elo manda al client che è in ascolto con readfromsocket
     *qualsiasi cosa il client riceva la stampa a video
     */
    public synchronized void asyncSend( Object message){
        System.out.println("Async sent");
        new Thread(new Runnable() {
            @Override
            public void run() {
                send(message);
            }
        }).start();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPlayerQuitted(boolean playerQuitted) {
        this.playerQuitted = playerQuitted;
    }

    public boolean ping(String ip) throws IOException, InterruptedException {
        String ping;
        if(System.getProperty("os.name").startsWith("Windows")) {
            ping = "ping -n 1 " + ip;
        } else {
            ping = "ping -c 1 " + ip;
        }

        Process p1 = java.lang.Runtime.getRuntime().exec(ping);
        int returnVal = 0;
        returnVal = p1.waitFor();
        boolean reachable = (returnVal == 0);
        return reachable;
    }

    public Thread pingToClient(String ip) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (isActive()) {
                        Thread.sleep(10000);
                        if(!ping(ip)) {
                            System.out.println("The client is unreachable, exiting...");
                            close();
                        } else {
                            System.out.println("Ping avvenuto di " + ip + " con successo");
                        }
                    }
                } catch (Exception e) {
                    //setActive(false);
                }
            }
        });
        //t.start();
        return t;
    }

    @Override
    public void run() {
        String read;
        try{
            inGeneral = new Scanner(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            send(ServerMessage.welcome);
            InetAddress geek = socket.getInetAddress();
            Thread t0 = pingToClient(geek.getHostAddress());
            t0.start();
            server.lobby(this);
            while(isActive() && inGeneral.hasNextLine()){        //legge dal client tutti i messaggi e notifica il listener della view
                
                read = inGeneral.nextLine();
                if(read.equalsIgnoreCase(Constants.QUIT)){
                    System.out.println("quit received");
                    playerQuitted = true;
                    close();
                    return;
                }else{
                    System.out.println("received " + read + "from " + nickname);
                    support.firePropertyChange("MessageForParser","aaa", read);
                }
            }
        } catch(IOException | NoSuchElementException e) {
            System.err.println("Error! " + e.getMessage() + getNickname());
        }

        if(!playerQuitted) {
            close();
        }
    }

}