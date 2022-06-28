package it.polimi.ingsw;

import it.polimi.ingsw.client.gui.Gui;

/**
 * Launch the GUI
 */
public class ClientAppGui {
    public static void main(String[] args) {
        System.out.println("Eriantys Client | Welcome!");
        Gui gui = new Gui();
        gui.main(args);
    }
}