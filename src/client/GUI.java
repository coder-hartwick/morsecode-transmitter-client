package client;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;


/**
 * The Morse Code Transmitter Client's GUI.
 *
 * @author Jordan Hartwick
 * Jun 4, 2016
 */
public class GUI {


    protected static DefaultListModel sentMessagesModel;


    protected static DefaultListModel receivedMessagesModel;


    protected static JTextArea sentAndReceivedMessageView;


    private Client client;


    public GUI() {}


    public void createAndShowGUI() {
        JFrame window = new JFrame("Morse Code Client");
        JPanel container = new JPanel(new GridBagLayout());

        createAndAddComponents(container);
        client = new Client();

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setContentPane(container);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        client.start();
    }


    private void createAndAddComponents(final JPanel container) {
        JTextArea regularText = new JTextArea(5,20);
        regularText.setLineWrap(true);
        regularText.setWrapStyleWord(true);

        JTextArea translatedText = new JTextArea(5,20);
        translatedText.setLineWrap(true);
        translatedText.setWrapStyleWord(true);
        translatedText.setEditable(false);

        sentAndReceivedMessageView = new JTextArea(5,20);
        sentAndReceivedMessageView.setLineWrap(true);
        sentAndReceivedMessageView.setWrapStyleWord(true);
        sentAndReceivedMessageView.setEditable(false);

        sentMessagesModel = new DefaultListModel();
        receivedMessagesModel = new DefaultListModel();

        JList sentMessages = new JList(sentMessagesModel);
        JList receivedMessages = new JList(receivedMessagesModel);

        sentMessages.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sentMessages.setLayoutOrientation(JList.VERTICAL);
        sentMessages.setFixedCellWidth(150);
        sentMessages.addMouseListener(new ListMouseAdapter(sentMessages));
        sentMessages.setCellRenderer(new MessageCellRenderer());

        receivedMessages.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        receivedMessages.setLayoutOrientation(JList.VERTICAL);
        receivedMessages.setFixedCellWidth(150);
        receivedMessages.addMouseListener(new ListMouseAdapter(receivedMessages));
        receivedMessages.setCellRenderer(new MessageCellRenderer());

        JPopupMenu popup1 = new JPopupMenu();
        JPopupMenu popup2 = new JPopupMenu();
        JMenuItem remove1 = new JMenuItem("Remove");
        JMenuItem remove2 = new JMenuItem("Remove");

        remove1.addActionListener(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                sentMessagesModel.removeElementAt(sentMessages.getSelectedIndex());
                sentAndReceivedMessageView.setText("");
            }
        });

        remove2.addActionListener(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                receivedMessagesModel.removeElementAt(receivedMessages.getSelectedIndex());
                sentAndReceivedMessageView.setText("");
            }
        });

        receivedMessages.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                sentMessages.clearSelection();
                if(sentMessages.getSelectedIndex() > 0
                        && e.getButton() == MouseEvent.BUTTON3) {

                    popup1.show(null, e.getX(), e.getY());
                }
            }
        });

        sentMessages.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                receivedMessages.clearSelection();
                if(receivedMessages.getSelectedIndex() > 0
                        && e.getButton() == MouseEvent.BUTTON3) {

                    popup2.show(null, e.getX(), e.getY());
                }
            }
        });

        receivedMessagesModel.addElement(new Message("Received", null));
        sentMessagesModel.addElement(new Message("Sent", null));

        JButton translate = new JButton("Translate");
        translate.addActionListener(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(regularText.getText().length() != 0) {
                    translatedText.setText(
                            client.translateMessage(
                                    regularText.getText()));
                }
            }
        });

        JButton reverseTranslation = new JButton("Reverse Translation");
        reverseTranslation.addActionListener(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                sentAndReceivedMessageView.setText(
                        client.reverseTranslateMessage(
                                sentAndReceivedMessageView.getText()));
            }
        });

        JButton send = new JButton("Send");
        send.addActionListener(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                client.sendMessage(
                        client.translateMessage(
                                regularText.getText()));
            }
        });

        JLabel title = new JLabel("Created by Jordan Hartwick (GitHub: @coder-hartwick)");
        JLabel receivedTransmission = new JLabel("Received Transmission");
        JLabel translated = new JLabel("Translated Text");

        GridBagConstraints c = new GridBagConstraints();

        // Positioning the title.
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 7;
        c.gridheight = 1;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(5,5,5,5);
        container.add(title, c);

        // Positioning the list on the left side of the layout.
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        c.gridheight = 10;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(5,5,5,5);
        container.add(new JScrollPane(receivedMessages), c);

        // Positioning the received message display area title.
        c.gridx = 2;
        c.gridy = 1;
        c.gridwidth = 3;
        c.gridheight = 1;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(5,5,5,5);
        container.add(receivedTransmission, c);

        // Positioning the text area that displays the latest received message.
        c.gridx = 2;
        c.gridy = 2;
        c.gridwidth = 3;
        c.gridheight = 2;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(5,5,5,5);
        container.add(new JScrollPane(sentAndReceivedMessageView), c);

        // Add the reverse translation button.
        c.gridx = 2;
        c.gridy = 4;
        c.gridwidth = 3;
        c.gridheight = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5,5,5,5);
        container.add(reverseTranslation, c);

        // Positioning the translated message display area title.
        c.gridx = 2;
        c.gridy = 5;
        c.gridwidth = 3;
        c.gridheight = 1;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(5,5,5,5);
        container.add(translated, c);

        // Positioning the text area that displays the translated user message.
        c.gridx = 2;
        c.gridy = 6;
        c.gridwidth = 3;
        c.gridheight = 2;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(5,5,5,5);
        container.add(new JScrollPane(translatedText), c);

        // Positioning the translate button.
        c.gridx = 2;
        c.gridy = 8;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5,5,5,5);
        container.add(translate, c);

        // Positioning the send button.
        c.gridx = 4;
        c.gridy = 8;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5,5,5,5);
        container.add(send, c);

        // Positioning the text area that the user types in.
        c.gridx = 2;
        c.gridy = 9;
        c.gridwidth = 3;
        c.gridheight = 2;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(5,5,5,5);
        container.add(new JScrollPane(regularText), c);

        // Positioning the list on the right side of the layout.
        c.gridx = 6;
        c.gridy = 1;
        c.gridwidth = 2;
        c.gridheight = 10;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(5,5,5,5);
        container.add(new JScrollPane(sentMessages), c);
    }   // createAndAddComponents(JPanel container);


    private class ListMouseAdapter extends MouseAdapter {

        private final JList monitoredList;


        private final JPopupMenu popup;


        private final DefaultListModel monitoredListModel;


        public ListMouseAdapter(JList list) {
            monitoredList = list;
            monitoredListModel = (DefaultListModel)monitoredList.getModel();
            popup = new JPopupMenu();

            JMenuItem remove = new JMenuItem("Remove Message");
            remove.addActionListener(new AbstractAction() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    monitoredListModel.removeElementAt(monitoredList.getSelectedIndex());
                }
            });
            this.popup.add(remove);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.getButton() == MouseEvent.BUTTON3) {
                monitoredList.setSelectedIndex(monitoredList.locationToIndex(e.getPoint()));
                if(monitoredList.getSelectedIndex() != 0) {
                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        }
    }
}
