package it.polimi.ingsw;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.cli.Cli;
import it.polimi.ingsw.server.Server;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ClientApp {
    public static void main(String[] args) {
        System.out.println("Eryantis Server | Welcome!");
        String ip = "127.0.0.1";
        int port = 0;

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

        switch (input) {
            case 1 -> {
                Cli client = new Cli(ip, port);
                try {
                    client.run();
                } catch (IOException e) {
                    throw new RuntimeException(e);
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
