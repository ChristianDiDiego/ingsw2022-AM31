package it.polimi.ingsw.server;

import it.polimi.ingsw.model.ColorOfTower;
import it.polimi.ingsw.utilities.ServerMessage;
import it.polimi.ingsw.utilities.Constants;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Instanced by the server, it interacts with a client sending and receiving message from it
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

    /**
     * Add a listener to this class
     *
     * @param pcl
     */
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    private synchronized boolean isActive(){
        return active;
    }

    /**
     * send message to client
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

    /**
     * asks the first player how many players he wants the game to be played by
     * @return number of players or negative number in case of error or quit
     */
    public int askHowManyPlayers() {
        Scanner in;
        String number;
        try {
            in = new Scanner(socket.getInputStream());
            send(ServerMessage.howManyPlayers);
            String read = in.nextLine();
            System.out.println("received " + read + "from " + nickname);
            if(read.equalsIgnoreCase(Constants.QUIT)){
                System.out.println("quit received");
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

    /**
     * asks nickname to player
     * @return nickname or "wrong" in case of error
     */
    public String askNickname() {
        Scanner in;
        try {
            in = new Scanner(socket.getInputStream());
            send(ServerMessage.askNickname); //manda al client
            String read = in.nextLine();
            if(read.equalsIgnoreCase(Constants.QUIT)){
                System.out.println("quit received");
                close();
                return read;
            }
            return read;
        } catch (IOException | NoSuchElementException e) {
            System.err.println("Error! " + e.getMessage());
            return "wrong";
        }
    }

    /**
     * asks only to first player if he wants to play in normal or expert mode
     * @return 0 for normal, 1 for exepert, negative number in case of error
     */
    public int askMode() {
        Scanner in;
        try {
            in = new Scanner(socket.getInputStream());
            send(ServerMessage.askMode); //manda al client
            String read = in.nextLine();// legge dal client il nome
            System.out.println("received " + read + "from " + nickname);
            if(read.equalsIgnoreCase(Constants.QUIT)){
                System.out.println("quit received");
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

    /**
     * asks color of towers
     * @return color chosen
     */
    public ColorOfTower askColor() {
        Scanner in;
        int number;
        try {
            in = new Scanner(socket.getInputStream());
            switch (server.getNumberOfPlayers()){
                case 2:
                case 4:
                    send(ServerMessage.askColor2_4Players);
                    break;
                case 3:
                    send(ServerMessage.askColor3Players);
            }

            String read = in.nextLine();
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

    /**
     * sends disconnection message to client and closes connection
     */
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

    /**
     * Communicates to server to deregister this client and then closes the connection
     */
    private void close() {
        server.deregisterConnection(this);
        closeConnection();
        System.out.println("Deregistering client...");
        System.out.println("Done!");
    }

    /**
     *closes only one connection and, if the connection is the last one still active in
     *game, removes the connection list from server's gameList
     */
    public void closeOnlyThis(){
        server.checkEmptyGames(this);
        closeConnection();
    }

    /**
     * Send a message to client by the socket asynchronously
     */
    public synchronized void asyncSend(Object message){
        new Thread(() -> send(message)).start();
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

    /**
     * Thread that every 15 seconds sends a ping to the client and
     * waits 5 seconds maximum to receive the reply. if the answer does
     * not arrive it means that the client is offline and the close will be invoked
     *
     * @param geek
     * @return
     */
    public Thread pingToClient(InetAddress geek) {
        Thread t = new Thread(() -> {
            try {
                while (isActive()) {
                    Thread.sleep(15000);
                    if(geek.isReachable(5000)) {
                        System.out.println("client " + nickname + " is reachable");
                    } else {
                        System.out.println("client " + nickname + " is unreachable, exiting...");
                        close();
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        return t;
    }

    /**
     * Run method of this class
     */
    @Override
    public void run() {
        String read;
        try{
            inGeneral = new Scanner(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            send(ServerMessage.welcome);
            InetAddress geek = socket.getInetAddress();
            Thread t0 = pingToClient(geek);
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