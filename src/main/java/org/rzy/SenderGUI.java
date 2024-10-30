package org.rzy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SenderGUI extends JFrame {
    private JTextField messageField;
    private JButton sendButton;
    private JComboBox<String> subBandSelect;
    private String name;

    public SenderGUI(String name) {
        this.name = name;

        setTitle("Sender - " + name);
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(new FlowLayout());

        subBandSelect = new JComboBox<>(new String[]{"0", "1", "2"});
        add(new JLabel("Select Sub-Band:"));
        add(subBandSelect);

        messageField = new JTextField(20);
        add(new JLabel("Message:"));
        add(messageField);

        sendButton = new JButton("Send");
        add(sendButton);

        sendButton.addActionListener(new SendButtonListener());

        setVisible(true);
    }

    private class SendButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int subBand = Integer.parseInt((String) subBandSelect.getSelectedItem());
            String message = messageField.getText().trim();
            if (!message.isEmpty()) {
                Sender sender = new Sender(name);
                sender.send(subBand, message);
                messageField.setText("");
            } else {
                JOptionPane.showMessageDialog(null, "Message cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
