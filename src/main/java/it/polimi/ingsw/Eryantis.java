package it.polimi.ingsw;

import it.polimi.ingsw.client.cli.Cli;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Eryantis {
    public static void main(String[] args) {

        while (!askNumber()) {}

    }

    private static boolean askNumber() {
        System.out.println("1- Client \n2- Server \n3- Gui");
        Scanner scanner = new Scanner(System.in);
        int input = 0;
        try {
            input = scanner.nextInt();


        switch (input) {
            case 1 -> ClientApp.main(null);
            case 2 -> ServerApp.main(null);
            case 3 -> ClientAppGui.main(null);
            default -> throw new InputMismatchException();
        }
        return true;
        } catch (InputMismatchException e) {
            System.out.println("Please choice a number...");
            return false;
        }
    }
}
