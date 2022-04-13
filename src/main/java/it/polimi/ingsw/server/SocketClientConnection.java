package it.polimi.ingsw.server;

import it.polimi.ingsw.model.ColorOfTower;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.StudsAndProfsColor;
import it.polimi.ingsw.observer.Observable;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

//Interagisce con il client
//Connessione lato server
public class SocketClientConnection extends Observable<String> implements Runnable{
    private Socket socket;
    private ObjectOutputStream out;
    private Server server;

    private boolean active = true;

    public SocketClientConnection(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
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
        } catch(IOException e){
            System.err.println(e.getMessage());
        }

    }

    public int askHowManyPlayers() {
        Scanner in;
        String number;
        try {
            in = new Scanner(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
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
        String number;
        try {
            in = new Scanner(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            send("How many players?"); //manda al client
            String read = in.nextLine(); // legge dal client il nome
            return read;
        } catch (IOException | NoSuchElementException e) {
            System.err.println("Error! " + e.getMessage());
            return "wrong";
        }
    }

    //TODO: rivedere la return
    public boolean askMode() {
        Scanner in;
        String number;
        try {
            in = new Scanner(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            send("Typer 0 for normal mode or 1 for expert mode"); //manda al client
            String read = in.nextLine(); // legge dal client il nome
            return Boolean.parseBoolean(read);
        } catch (IOException | NoSuchElementException e) {
            System.err.println("Error! " + e.getMessage());
            return false;
        }
    }

    public ColorOfTower askColor() {
        Scanner in;
        int number;
        try {
            in = new Scanner(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            send("Typer 0 for normal mode or 1 for expert mode"); //manda al client
            String read = in.nextLine(); // legge dal client il nome
            number = Integer.parseInt(read);
            return ColorOfTower.values()[number];
        } catch (IOException | NoSuchElementException e) {
            System.err.println("Error! " + e.getMessage());
            return ColorOfTower.BLACK;
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
    public void asyncSend(final Object message){
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
            out = new ObjectOutputStream(socket.getOutputStream());
            send("Welcome!");
            server.lobby(this);
            while(isActive()){        //legge dal client tutti imessaggi e notifica l'observer della view
                read = in.nextLine();
                notify(read);
            }
        } catch (IOException | NoSuchElementException e) {
            System.err.println("Error! " + e.getMessage());
        }finally{
            close();
        }
    }

}
