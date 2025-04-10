package MainServer.SocketServer;

import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Set;

public class ConnectedClients {
    private static final ConcurrentHashMap<String, Socket> clients = new ConcurrentHashMap<>();

    public static void add(String email, Socket socket) {
        clients.put(email, socket);
    }

    public static void remove(String email) {
        clients.remove(email);
    }

    public static Set<String> getAllEmails() {
        return clients.keySet();
    }

    public static Socket getSocketByEmail(String email) {
        return clients.get(email);
    }

    public static boolean isOnline(String email) {
        return clients.containsKey(email);
    }
}
