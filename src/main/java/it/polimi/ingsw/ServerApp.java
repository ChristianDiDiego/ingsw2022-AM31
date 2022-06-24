package it.polimi.ingsw;

import it.polimi.ingsw.server.Server;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Launch server with the port chosen
 */
public class ServerApp {
    static PrintStream ps = new PrintStream(new FileOutputStream(FileDescriptor.out), true, StandardCharsets.UTF_8);

    public static void main( String[] args ) {
        ps.println("" +
                "███████ ██████  ██  █████  ███    ██ ████████ ██    ██ ███████ \n" +
                "██      ██   ██ ██ ██   ██ ████   ██    ██     ██  ██  ██      \n" +
                "█████   ██████  ██ ███████ ██ ██  ██    ██      ████   ███████ \n" +
                "██      ██   ██ ██ ██   ██ ██  ██ ██    ██       ██         ██ \n" +
                "███████ ██   ██ ██ ██   ██ ██   ████    ██       ██    ███████ \n" +
                "                                                               \n" +
                "                                                               ");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Insert the port which server will listen on:");
        int port = 0;
        String read = scanner.nextLine();
        while (read.length() < 2) {
            System.out.println("Port not valid, try again: ");
            read = scanner.nextLine();
        }
        try {
            port = Integer.parseInt(read);
        } catch (InputMismatchException e) {
            System.err.println("Numeric format requested, application will now close...");
            System.exit(-1);
        } catch (NumberFormatException e) {
            System.err.println("Numeric format requested, application will now close...");
            System.exit(-1);
        }

        if (port < 0 || (port > 0 && port < 1024)) {
            System.err.println("Error: ports accepted started from 1024! Please insert a new value.");
            main(null);
        }

        Server server = null;
        try {
            server = new Server(port);
            server.run();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}