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
            try (ServerSocket serverSocket = new ServerSocket(52000 + subBand)) {
                while (true) { // 无限循环以接受多个连接
                    Socket clientSocket = serverSocket.accept(); // 接受一个新的客户端连接
                    new Thread(() -> handleClient(clientSocket)).start(); // 为每个连接启动新线程进行处理
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            String message;
            while ((message = in.readLine()) != null) { // 从该连接读取消息
                String[] strs = message.split("#");
                textArea.append(name + " received from " + strs[0] + " on subband " + strs[1] + ": " + strs[2] + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close(); // 确保最终关闭socket，以释放资源
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
