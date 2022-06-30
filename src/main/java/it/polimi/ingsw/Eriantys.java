package it.polimi.ingsw;

import it.polimi.ingsw.client.cli.Cli;
import it.polimi.ingsw.client.gui.Gui;
import it.polimi.ingsw.server.ServerApp;

import java.io.IOException;
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
                case 1 -> startServer(args);
                case 2 -> startCli(args);
                case 3 -> startGui(args);
                default -> throw new InputMismatchException();
            }

            return true;
        } catch (InputMismatchException e) {
            System.out.println("Please choice a number...");
            return false;
        }
    }

    private static void startServer(String[] args){
        ServerApp.main(args);
    }

    private static void startCli(String[] args){
        Cli cli = new Cli();
        try {
            cli.run();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
    private static void startGui(String[] args){
        Gui gui = new Gui();
        gui.main(args);
    }
}