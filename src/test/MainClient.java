package test;

import client.Client;

public class MainClient {
    public static void main(String[] args) {
        Client client = new Client(args[0], 2050);
        client.start();
    }
}
