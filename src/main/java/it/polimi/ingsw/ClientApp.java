package it.polimi.ingsw;

import it.polimi.ingsw.client.Client;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ClientApp {
    public static void main(String[] args) {
        System.out.println("Eryantis Server | Welcome!");
        String ip ="127.0.0.1";
        int port = 5000;
       /*
        Scanner scanner = new Scanner(System.in);
        System.out.println("Insert ip server");
        System.out.print(">");
        ip = scanner.nextLine();
        System.out.println(">Insert the port which server will listen on.");
        System.out.print(">");
        int port = 0;
        try {
            port = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.err.println("Numeric format requested, application will now close...");
            System.exit(-1);
        }

        */
        Client client = new Client(ip, port);
        try {
            client.run();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
