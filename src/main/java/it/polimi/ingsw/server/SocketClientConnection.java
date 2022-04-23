package it.polimi.ingsw.server;

import it.polimi.ingsw.model.ColorOfTower;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.StudsAndProfsColor;
import it.polimi.ingsw.observer.Observable;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

//Interagisce con il client
//Connessione lato server
//public class SocketClientConnection extends Observable<String> implements Runnable
public class SocketClientConnection implements Runnable{
    private Socket socket;
    private ObjectOutputStream out;

   // private ByteArrayOutputStream baos;
    private Server server;
    private PropertyChangeSupport support;

    private boolean active = true;

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
            System.out.println("sent");
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
            send("How many players?"); //manda al client
            String read = in.nextLine(); // legge dal client il nome
            number = read;
            return Integer.parseInt(number);
        } catch (IOException | NoSuchElementException e) {
            System.err.println("Error! " + e.getMessage());
            return -1;
        }
    }

    public String askNickname() {
        Scanner in;
        try {
            in = new Scanner(socket.getInputStream());
            send("What is your nickname?"); //manda al client
            return in.nextLine();
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
            send("Type 0 for normal mode or 1 for expert mode"); //manda al client
            String read = in.nextLine(); // legge dal client il nome
            return Integer.parseInt(read);
        } catch (IOException | NoSuchElementException e) {
            System.err.println("Error! " + e.getMessage());
            return -1;
        }
    }

    public ColorOfTower askColor() {
        Scanner in;
        int number;
        try {
            in = new Scanner(socket.getInputStream());
            //TODO: ask grey only if expertMode
            send("Choose a color - write 0 for black, 1 for white"); //manda al client
            String read = in.nextLine(); // legge dal client il nome
            number = Integer.parseInt(read);
            return ColorOfTower.values()[number];
        } catch (IOException | NoSuchElementException e) {
            System.err.println("Error! " + e.getMessage());
            return null;
        }
    }

    //invia il messaggio di chiusura al client
    public synchronized void closeConnection() {
        send("Connection closed!");
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("Error when closing socket!");
        }
        active = false;
    }

    private void close() { //stampa sul server
        closeConnection();
        System.out.println("Deregistering client...");
        server.deregisterConnection(this);
        System.out.println("Done!");
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

    @Override
    public void run() {
        Scanner in;
        String read;
        Player player;
        try{
            in = new Scanner(socket.getInputStream());
           // baos = new ByteArrayOutputStream();
            out = new ObjectOutputStream(socket.getOutputStream());
            send("Welcome!");
            server.lobby(this);
            while(isActive()){        //legge dal client tutti i messaggi e notifica il listener della view
                read = in.nextLine();
                support.firePropertyChange("MessageForParser","aaa", read);
            }
        } catch(IOException | NoSuchElementException e) {
            System.err.println("Error! " + e.getMessage());
        } finally {
            close();
        }
    }

}