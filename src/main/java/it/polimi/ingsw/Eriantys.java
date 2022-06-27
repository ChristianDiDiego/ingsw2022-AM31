package it.polimi.ingsw;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * In this class players choose whether to launch server, cli or gui app
 */
public class Eriantys {
    public static void main(String[] args) {
        while (!askNumber(args)) {}
    }

    private static boolean askNumber(String[] args) {
        System.out.println("1- Server \n2- Client CLI \n3- Client GUI");
        Scanner scanner = new Scanner(System.in);
        int input = 0;
        try {
            input = scanner.nextInt();

            switch (input) {
                case 1 -> ServerApp.main(args);
                case 2 -> ClientAppCli.main(args);
                case 3 -> ClientAppGui.main(args);
                default -> throw new InputMismatchException();
            }

            return true;
        } catch (InputMismatchException e) {
            System.out.println("Please choice a number...");
            return false;
        }
    }
}