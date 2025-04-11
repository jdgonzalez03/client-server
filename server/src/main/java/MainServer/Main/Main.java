package MainServer.Main;

import MainServer.GUI.ServerGUI;
import MainServer.GUI.SplashScreen;
import MainServer.SocketServer.SocketServer;

import javax.swing.*;

public class Main {
    public static ServerGUI gui;

    public static void main(String[] args) {
        SplashScreen splash = new SplashScreen();
        splash.setVisible(true);

        new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            SwingUtilities.invokeLater(() -> {
                gui = new ServerGUI();
                splash.setVisible(false);
                splash.dispose();

                new Thread(() -> {
                    SocketServer socketServer = new SocketServer();
                    socketServer.start(12345);
                }).start();
            });
        }).start();
    }
}
