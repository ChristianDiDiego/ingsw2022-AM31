package it.polimi.ingsw;

import it.polimi.ingsw.client.cli.Cli;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Eryantis {
    public static void main(String[] args) {
        System.out.println("1- Client \n2- Server");
        Scanner scanner = new Scanner(System.in);
        int input = 0;
        try {
            input = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.err.println("Numeric format requested, application will now close...");
            System.exit(-1);
        }

        switch (input) {
            case 1 ->
                ClientApp.main(null);
            case 2 ->
                ServerApp.main(null);
            default ->
                    System.err.println("Invalid argument, please run the executable again with one of these options:\n 2.client");
        }
    }
}
