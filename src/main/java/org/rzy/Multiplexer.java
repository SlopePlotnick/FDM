package org.rzy;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Multiplexer {

    private static final int S2I_PORT = 50000; // Basic port number for sub-bands

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        for (int subBand = 0; subBand < 3; subBand++) {
            final int frequencyPort = S2I_PORT + subBand;
            int finalSubBand = subBand;
            executor.submit(() -> {
                try (ServerSocket serverSocket = new ServerSocket(frequencyPort)) {
                    System.out.println("Sub-band " + finalSubBand + "'s sender side ready at port " + frequencyPort);

                    while (true) {
                        Socket clientSocket = serverSocket.accept();
                        new Thread(new SubBandHandler(clientSocket, finalSubBand)).start();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}

class SubBandHandler implements Runnable {
    private Socket clientSocket;
    private int subBand;

    private static final int I2J_PORT = 51000;
    private static final String J_IP = "127.0.0.1";

    SubBandHandler(Socket socket, int subBand) {
        this.clientSocket = socket;
        this.subBand = subBand;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true))
        {
            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Sub-band " + subBand + " received: " + message);
                out.println("Processed: " + message); // Forward to sender after processing

                // 转发到对应的 ChannelJ
                int jPort = I2J_PORT + subBand; // 对应的 ChannelJ 端口
                try (Socket jSocket = new Socket(J_IP, jPort);
                     PrintWriter jOut = new PrintWriter(jSocket.getOutputStream(), true)) {

                    jOut.println(message); // 转发消息
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}