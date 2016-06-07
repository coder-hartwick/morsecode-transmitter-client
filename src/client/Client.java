package client;

import java.util.List;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;


/**
 *
 *
 * @author Jordan Hartwick
 * Jun 5, 2016
 */
public class Client extends Thread {


    private final String positioningCharacters, positioningPunctuation;


    private List<String> morseCodeCharacters, morseCodePunctuation;


    private Clip shortClip, longClip;


    private Socket socket;


    private final String host = "localhost";
    private final int port = 7879;


    private DataOutputStream out;


    private DataInputStream in;


    public Client() {
        positioningCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        positioningPunctuation = ".,:?'-/()\"@=";

        try {
            morseCodeCharacters = getList("src/alphanumeric_codes.txt");
            morseCodePunctuation = getList("src/punctuation.txt");
            socket = new Socket(host, port);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException err) {
            System.err.println("Error: Unable to connect to the server.");
            System.exit(0);
        }
    }


    private List<String> getList(final String filePath) throws IOException {
        List<String> list = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(filePath));

        String line;
        while((line = br.readLine()) != null) {
            list.add(line);
        }

        return list;
    }


    public void sendMessage(final String text) {
        try {
            if(!socket.isClosed()) {
                out.writeUTF(text);
                out.flush();

                String time = System.nanoTime()+"";
                GUI.sentMessagesModel.addElement(new Message(time, text));
            }
        } catch (IOException err) {
            System.err.println("Error: Message could not be sent!");
        }
    }


    public String translateMessage(final String regularText) {
        String translatedData = "";
        char[] text = regularText.toCharArray();

        for(char c : text) {
            String s = Character.toString(c).toUpperCase();

            if (c == ' ') {
                translatedData += "/";
            } else if (positioningCharacters.contains(s)) {
                translatedData += morseCodeCharacters.get(positioningCharacters.indexOf(s)) + " ";
            } else if (c == '(' || c == ')') {
                translatedData += morseCodePunctuation.get(positioningPunctuation.indexOf("(")) + " ";
            } else if (positioningPunctuation.contains(s)) {
                translatedData += morseCodePunctuation.get(positioningPunctuation.indexOf(s)) + " ";
            } else {
                System.out.println("Character not found: " + c);
            }

            s += " ";
        }

        return translatedData;
    }


    public String reverseTranslateMessage(final String codedText) {
        String regularData = "";
        String[] codedWords = codedText.split("/"); // Split the morse code into words.

        for(String s : codedWords) {
            String[] codedLetters = s.split("\\s"); // Split the morse code into letters.
            for(String l : codedLetters) {
                if(morseCodeCharacters.contains(l)) {
                    regularData += positioningCharacters.charAt(morseCodeCharacters.indexOf(l));
                } else if(morseCodePunctuation.contains(l)) {
                    regularData += positioningPunctuation.charAt(morseCodePunctuation.indexOf(l));
                } else {
                    System.out.println(l + "was not found.");
                }
            }
            regularData += " ";
        }

        return regularData;
    }


    /*
        Listen for any incoming morse code transmissions.
    */
    @Override
    public void run() {

        try {
            File sClip = new File("src/short.wav");
            File lClip = new File("src/long.wav");

            AudioInputStream sis = AudioSystem.getAudioInputStream(sClip);
            shortClip = AudioSystem.getClip();
            shortClip.open(sis);

            AudioInputStream lis = AudioSystem.getAudioInputStream(lClip);
            longClip = AudioSystem.getClip();
            longClip.open(lis);


            while(!socket.isClosed()) {
                String msg = in.readUTF();
                GUI.sentAndReceivedMessageView.setText(msg);

                String time = System.nanoTime()+"";
                GUI.receivedMessagesModel.addElement(new Message(time, msg));

                playMessage(msg);
            }
        } catch (Exception err) {
            System.err.println("Error listening on port!");
        }
    }


    private void playMessage(final String msg) throws Exception {
        char[] message = msg.toCharArray();

        long sTime = (shortClip.getMicrosecondLength() / 1000) + 150;
        long lTime = (longClip.getMicrosecondLength() / 1000) + 150;

        for (char c : message) {
            switch(c) {
                case '.':
                    shortClip.stop();
                    shortClip.setFramePosition(0);
                    shortClip.start();
                    Thread.sleep(sTime);
                    break;

                case '-':
                    longClip.stop();
                    longClip.setFramePosition(0);
                    longClip.start();
                    Thread.sleep(lTime);
                    break;

                default:
                    break;
            }
        }
    }
}