package test;

import server.HangedServer;

public class MainServer {
    public static void main(String[] args) {
        HangedServer server = new HangedServer(2050, 50);
        server.start();
    }
}
