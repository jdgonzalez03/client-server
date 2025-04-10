package MainServer.SocketServer;

import Controllers.Socket.UserController;
import Utils.Logger.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {
    private ServerSocket serverSocket;
    private final Logger logger = Logger.getInstance();

    public SocketServer() {
    }

    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            logger.log("Servidor TCP iniciado en el puerto " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                logger.log("Servidor TCP encontrado " + clientSocket.getInetAddress().getHostAddress());
                new Thread(new ClientHandler(clientSocket)).start();
            }

        } catch (IOException e) {
            logger.log("Error al registrar usuario: " + e.getMessage());
        }
    }
}
