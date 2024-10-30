package org.rzy;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class ReceiverGUI extends JFrame {
    private JTextArea textArea;
    private String name;
    private int subBand;

    public ReceiverGUI(String name, int subBand) {
        this.name = name;
        this.subBand = subBand;

        setTitle("Receiver - " + name);
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());

        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
        startReceiving(); // Start receiving messages
    }

    private void startReceiving() {
        new Thread(() -> {
            Receiver receiver = new Receiver(name, subBand);
            try (ServerSocket serverSocket = new ServerSocket(52000 + subBand)) {
                Socket clientSocket = serverSocket.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String message;
                while ((message = in.readLine()) != null) {
                    String[] strs = message.split("#");
                    textArea.append(name + " received from " + strs[0] + " on subband " + strs[1] + ": " + strs[2] + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
