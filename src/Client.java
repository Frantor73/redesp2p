
public class Client {
    private String clientIp;
    private int clientPort;
    private final Peer peer;

    public Client(String clientIp, int clientPort) {
        this.clientIp = clientIp;
        this.clientPort = clientPort;
        this.peer = new Peer(clientIp, clientPort);
    }

    public void start() {
        this.peer.addFile("C:\\Users\\frantor\\IdeaProjects\\redesp2p\\src\\peer_to_peer_Files\\Client1\\1.1.txt");
        this.peer.addFile("C:\\Users\\frantor\\IdeaProjects\\redesp2p\\src\\peer_to_peer_Files\\Client1\\1.2.txt");
        this.peer.start();
    }

    public static void main(String[] args) {
        Client client = new Client("192.168.0.153", 8889);
        client.start();
    }
}
