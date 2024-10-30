package org.rzy;

class SendWorker extends Thread {
    private String name;
    private int subBand;
    private String message;

    public SendWorker(String name, int subBand, String message) {
        this.name = name;
        this.subBand = subBand;
        this.message = message;
    }

    @Override
    public void run() {
        Sender sender = new Sender(name);
        sender.send(subBand, message);
    }
}

class ReceiveWorker extends Thread {
    private String name;
    private int subBand;

    public ReceiveWorker(String name, int subBand) {
        this.name = name;
        this.subBand = subBand;
    }

    @Override
    public void run() {
        Receiver receiver = new Receiver(name, subBand);
        receiver.receive();
    }
}

public class Main {
    public static void main(String[] args) {
        SendWorker a1 = new SendWorker("A1", 0, "nihaowa");
        ReceiveWorker a2 = new ReceiveWorker("A2", 0);
        SendWorker b1 = new SendWorker("B1", 1, "qyy");
        ReceiveWorker b2 = new ReceiveWorker("B2", 1);
        SendWorker c1 = new SendWorker("C1", 2, "rzy");
        ReceiveWorker c2 = new ReceiveWorker("C2", 2);

        a1.start();
        a2.start();
        b1.start();
        b2.start();
        c1.start();
        c2.start();
    }
}