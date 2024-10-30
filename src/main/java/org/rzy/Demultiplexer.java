package org.rzy;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Demultiplexer {

    private static final int I2J_PORT = 51000; // ChannelJ 监听的基础端口号

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        for (int subBand = 0; subBand < 3; subBand++) {
            final int frequencyPort = I2J_PORT + subBand;
            int finalSubBand = subBand;
            executor.submit(() -> {
                try (ServerSocket serverSocket = new ServerSocket(frequencyPort)) {
                    System.out.println("Sub-band " + finalSubBand + "'s receiver side ready at port " + frequencyPort);
                    
                    while (true) {
                        Socket channelISocket = serverSocket.accept();
                        new Thread(new SubBandReceiver(channelISocket, finalSubBand)).start();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}

class SubBandReceiver implements Runnable {
    private Socket channelISocket;
    private int subBand;
    private Socket receiverSocket;

    private static final int J2R_PORT = 52000; // 假设与A2客户端连接的基础端口
    private static final String R_IP = "127.0.0.1";

    SubBandReceiver(Socket channelISocket, int subBand) {
        this.channelISocket = channelISocket;
        this.subBand = subBand;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(channelISocket.getInputStream()))) {

            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Sub-band " + subBand + " 's receiver side received from ChannelI: " + message);

                // 转发信息到 A2 客户端
                forwardToClient(message);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void forwardToClient(String message) {
        try {
            int receiverPort = J2R_PORT + subBand; // 假设 A2 在子频带0上监听52000端口
            receiverSocket = new Socket(R_IP, receiverPort);
            PrintWriter receiverOut = new PrintWriter(receiverSocket.getOutputStream(), true);
            receiverOut.println(message);
            receiverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

