import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Tracker {
    private static Tracker instance;
    private List<Peer> peers;

    private Tracker() {
        peers = new ArrayList<>();
    }

    public static Tracker getInstance() {
        if (instance == null) {
            instance = new Tracker();
        }
        return instance;
    }

    public void registerPeer(Peer peer) {
        peers.add(peer);
    }


    public List<Peer> getPeerList() {
        return new ArrayList<>(peers);
    }

    public void start(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Tracker iniciado. Aguardando conexões...");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Conexão estabelecida com: " + socket.getInetAddress());

                new Thread(new ClientHandler(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ClientHandler implements Runnable {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

                Request request = (Request) in.readObject();

                if (request.getRequestType() == RequestType.PEER_LIST) {
                    PeerList peerList = new PeerList(getPeerList());
                    out.writeObject(peerList);
                }

                out.close();
                in.close();
                socket.close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            
            // Exibir a informação completa do socket
            System.out.println("Conexão encerrada com: " + socket.getInetAddress() + ":" + socket.getPort());
        }
    }


    public static void main(String[] args) {
        int port = 8888;

        Tracker tracker = Tracker.getInstance();
        tracker.start(port);
    }
}
