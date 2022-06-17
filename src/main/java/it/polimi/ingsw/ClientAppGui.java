package it.polimi.ingsw;

import it.polimi.ingsw.client.gui.Gui;

public class ClientAppGui {
    public static void main(String[] args) {
        System.out.println("Eriantys Client | Welcome!");

        System.out.println("You selected the GUI interface, have fun!\nStarting...");
        Gui gui = new Gui();
        gui.main(null);
    }

}
