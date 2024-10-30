package org.rzy;

import java.io.*;
import java.net.*;
import java.security.spec.RSAOtherPrimeInfo;

public class Receiver {
    private String name;
    private int subBand;

    private static final int J2R_PORT = 52000;

    public Receiver(String name, int subBand) {
        this.name = name;
        this.subBand = subBand;
    }

    public void receive() {
        // 根据子频带选择端口，与 ChannelJ 通信
        int port = J2R_PORT + this.subBand; // 对应 ChannelJ

        try (ServerSocket serverSocket = new ServerSocket(port);
             Socket clientSocket = serverSocket.accept();
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            // 从 ChannelJ 接收消息
            String response;
            while ((response = in.readLine()) != null) {
                String[] strs = response.split("#");

                System.out.println(name + " received from " + strs[0] + " on subband " + strs[1] + ": " + strs[2]);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

