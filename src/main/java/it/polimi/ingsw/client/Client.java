package it.polimi.ingsw.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Client {

    private String ip;
    private int port;

    public Client(String ip, int port){
        this.ip = ip;
        this.port = port;
    }

    private boolean active = true;

    public synchronized boolean isActive(){
        return active;
    }

    public synchronized void setActive(boolean active){
        this.active = active;
    }

    /**
     * questo thread rimane in ascolto di ciò che invia il server tramite SocketClientConnection,
     * che gli manda i messaggi e tutti i messaggi che lui riceve li stampa al client
     * isActive == nostro isStarted
     *
     * legge dal socketClientConnection (run) welcome, what's your name?
     *
     *
     * @param socketIn
     * @return
     */
    public Thread asyncReadFromSocket(final ObjectInputStream socketIn){

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (isActive()) {
                        //leggo un oggetto
                        Object inputObject = socketIn.readObject();
                        //controllo se ho ricevuto una stringa
                        if(inputObject instanceof String){
                            System.out.println((String)inputObject);
                        //o se ho ricevuto una board
                        } else {
                            throw new IllegalArgumentException();
                        }
                    }
                } catch (Exception e){
                    setActive(false);
                    //termino
                }
            }
        });
        t.start();
        return t;
    }

    /**
     * in modo asincono mando la mia scelta al server
     * @param stdin
     * @param socketOut
     * @return
     */
    public Thread asyncWriteToSocket(final Scanner stdin, final PrintWriter socketOut){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (isActive()) {
                        //mando sia board che la stringa
                        String inputLine = stdin.nextLine();
                        //in inputline salvo quello che leggo da tastiera
                        socketOut.println(inputLine);
                        socketOut.flush();
                    }
                }catch(Exception e){
                    setActive(false);
                }
            }
        });
        t.start();
        return t;
    }

    /**
     * chiede connessione al server e prende input e output
     * crea socket
     * lancia in modo parallelo le async read e write
     * @throws IOException
     */

    public void run() throws IOException {
        Socket socket = new Socket(ip, port);
        System.out.println("Connection established");
        ObjectInputStream socketIn = new ObjectInputStream(socket.getInputStream());
        PrintWriter socketOut = new PrintWriter(socket.getOutputStream());
        Scanner stdin = new Scanner(System.in); //Scanner da tastiera

        //Nel client avrò 2 thread :
        // uno resta in ascolto dell'input dell'utente
        // L'altro di ciò che riceve dal server
        try{
            Thread t0 = asyncReadFromSocket(socketIn);
            Thread t1 = asyncWriteToSocket(stdin, socketOut);
            t0.join();
            t1.join();
        } catch(InterruptedException | NoSuchElementException e){
            System.out.println("Connection closed from the client side");
        } finally {
            stdin.close();
            socketIn.close();
            socketOut.close();
            socket.close();
        }
    }

}
