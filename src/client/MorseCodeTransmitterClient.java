package client;

import java.io.IOException;
import java.io.PrintStream;


/**
 * A client program that allows the user to transmit Morse Code over a network.
 *
 * @author  Jordan Hartwick
 * Jun 4, 2016
 */
public class MorseCodeTransmitterClient {


    /**
     * Main method of the application.
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            PrintStream out = new PrintStream("log.txt");
            System.setErr(out);
        } catch (IOException err) {

        }
        GUI gui = new GUI();
        gui.createAndShowGUI();
    }

}
