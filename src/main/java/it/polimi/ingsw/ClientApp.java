package it.polimi.ingsw;

import it.polimi.ingsw.client.cli.Cli;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.utilities.GameMessage;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.InputMismatchException;
import java.util.Scanner;

/*
The app the client will open, entering the ip and port of the server
 */
public class ClientApp {
    public static void main(String[] args) {
        PrintStream ps = new PrintStream(new FileOutputStream(FileDescriptor.out), true, StandardCharsets.UTF_8);
        System.out.println("Eryantis Client | Welcome!");
        System.out.println("With system out the tower looks like this " + "♜ ");
        ps.println("But actually the tower symbol is " + "♜ ");

        String ip = "127.0.0.1";
        int port = 5000;
        int input = 1;
/*
        Scanner scanner = new Scanner(System.in);
        System.out.println("Insert ip server");
        System.out.print(">");
        ip = scanner.nextLine();
        System.out.println(">Insert the port which server will listen on.");
        System.out.print(">");
        try {
            port = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.err.println("Numeric format requested, application will now close...");
            System.exit(-1);
        }

        System.out.print("What do you want to launch? \n 1. CLIENT (CLI INTERFACE) \n 2. CLIENT (GUI INTERFACE)");
        System.out.println("\n>Type the number of the desired option!");
        System.out.print(">");
        int input = 0;
        try {
            input = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.err.println("Numeric format requested, application will now close...");
            System.exit(-1);
        }


 */

        switch (input) {
            case 1 -> {
                Cli client = new Cli(ip, port);
                try {
                    client.run();
                } catch (IOException e) {
                    //throw new RuntimeException(e);
                    System.err.println(e.getMessage());
                }
            }
            case 2 -> {
                System.out.println("You selected the GUI interface, have fun!\nStarting...");
                //GUI.main(null);
            }
            default -> System.err.println("Invalid argument, please run the executable again with one of these options:\n 2.client");
        }


    }
}
