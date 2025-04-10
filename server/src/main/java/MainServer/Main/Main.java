package MainServer.Main;


import MainServer.SocketServer.SocketServer;

public class Main {
    public static void main(String[] args) {
        SocketServer socketServer = new SocketServer();
        socketServer.start(12345);
    }
}