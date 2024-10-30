package org.rzy;

public class Main {
    public static void main(String[] args) {
        // 启动外部服务（Multiplexer 和 Demultiplexer），以准备发送和接收
        new Thread(() -> {
            try {
                Multiplexer.main(new String[]{});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                Demultiplexer.main(new String[]{});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        // 创建发送者和接收者的图形界面
        // A1 发送者
        new SenderGUI("A1"); // 启动发送端 GUI
        new ReceiverGUI("A2", 0); // 启动 A2 接收端 GUI

        // B1 发送者
        new SenderGUI("B1");
        new ReceiverGUI("B2", 1);

        // C1 发送者
        new SenderGUI("C1");
        new ReceiverGUI("C2", 2);
    }
}
