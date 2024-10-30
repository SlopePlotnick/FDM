package org.rzy;

import java.io.*;
import java.net.*;

public class Sender {
    private String name;

    private static final int S2I_PORT = 50000;
    private static final String I_IP = "127.0.0.1";

    public Sender(String name) {
        this.name = name;
    }

    public void send(int subBand, String message) {
        // Use predefined sub-band for this client
        int port = S2I_PORT + subBand; // Corresponds to ChannelI

        try (Socket socket = new Socket(I_IP, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Sending a message to sub-band
            String packet = name + "#" + subBand + "#" + message;
            out.println(packet);

            // Reading response
            String response = in.readLine();
            String[] strs = response.split("#");
            System.out.println(strs[0] + " says on subband " + strs[1] + ": " + strs[2]);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

