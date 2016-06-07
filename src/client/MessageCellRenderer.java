package client;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;


/**
 *
 *
 * @author Jordan Hartwick
 * Jun 6, 2016
 */
public class MessageCellRenderer extends JLabel implements ListCellRenderer {


    public MessageCellRenderer() {
        this.setOpaque(true);
    }


    @Override
    public Component getListCellRendererComponent(JList list, Object value,
                            int index, boolean isSelected, boolean cellHasFocus) {

        Message data = (Message)value;

        this.setText(data.getName());

        if(isSelected && !(data.getName().equals("Received") && data.getName().equals("Sent"))) {
            this.setForeground(Color.WHITE);
            this.setBackground(Color.BLUE);
            GUI.sentAndReceivedMessageView.setText(data.getContent());
        } else {
            this.setForeground(Color.BLACK);
            this.setBackground(Color.WHITE);
        }

        return this;
    }
}
