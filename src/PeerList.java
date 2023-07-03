import java.util.List;

public class PeerList {
    private List<Peer> peers;

    public PeerList(List<Peer> peers) {
        this.peers = peers;
    }

    public List<Peer> getPeers() {
        return peers;
    }
}
