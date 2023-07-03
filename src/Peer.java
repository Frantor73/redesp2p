import java.io.*;
import java.net.Socket;
import java.util.*;

public class Peer {
    private String ipAddress;
    private int port;
    private Map<String, Integer> fileAvailability;
    private List<Peer> availablePeers;

    public Peer(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.fileAvailability = new HashMap<>();
        this.availablePeers = new ArrayList<>();
    }

    public void start() {
        // Entrar na rede P2P
        registerWithTracker();

        // Trocar arquivos com os outros pares disponíveis
        while (!availablePeers.isEmpty()) {
            String rarestFile = chooseRarestFile();
            Peer peerWithRarestFile = findPeerWithFile(rarestFile);

            if (peerWithRarestFile != null) {
                exchangeFile(peerWithRarestFile, rarestFile);
            } else {
                break;
            }
        }

        // Enviar lista de pedaços do arquivo para o Tracker a cada 1 minutos
        sendFileListToTrackerPeriodically();
    }

   public void registerWithTracker() {
    Tracker tracker = Tracker.getInstance();
    tracker.registerPeer(this);
    availablePeers.clear();
    availablePeers.addAll(tracker.getPeerList());
    
    System.out.println("Lista de pares disponíveis:");
    for (Peer peer : availablePeers) {
        System.out.println(peer.getIpAddress() + ":" + peer.getPort());
    }
}

    private String chooseRarestFile() {
        String rarestFile = null;
        int rarestFileCount = Integer.MAX_VALUE;

        for (Map.Entry<String, Integer> entry : fileAvailability.entrySet()) {
            String fileName = entry.getKey();
            int fileCount = entry.getValue();

            if (fileCount < rarestFileCount) {
                rarestFile = fileName;
                rarestFileCount = fileCount;
            }
        }

        return rarestFile;
    }

    private Peer findPeerWithFile(String fileName) {
        for (Peer peer : availablePeers) {
            if (peer != this && peer.hasFile(fileName)) {
                return peer;
            }
        }
        return null;
    }


    private void exchangeFile(Peer peer, String fileName) {
        try {
            System.out.println("Troca de arquivo entre pares:");
            System.out.println("Par local: " + getIpAddress() + ":" + getPort());
            System.out.println("Par remoto: " + peer.getIpAddress() + ":" + peer.getPort());
            System.out.println("Arquivo: " + fileName);

            // Estabelecer conexão com o par remoto
            Socket socket = new Socket(peer.getIpAddress(), peer.getPort());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(new Request(RequestType.FILE_EXCHANGE, fileName));

            // Receber resposta do par remoto
            boolean accepted = in.readBoolean();

            if (accepted) {
                // Peer remoto aceitou a troca de arquivo, iniciar a transferência
                out.writeBoolean(true); // Enviar confirmação para iniciar a transferência

                // Receber confirmação do peer remoto
                boolean confirmation = in.readBoolean();
                if (confirmation) {
                    System.out.println("Baixando arquivo " + fileName + " do par " + peer.getIpAddress() + ":" + peer.getPort());
                    InputStream input = socket.getInputStream();
                    FileOutputStream fileOutputStream = new FileOutputStream("downloaded_" + fileName);

                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = input.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, bytesRead);
                    }
                    fileOutputStream.close();
                    System.out.println("Download concluído.");
                    System.out.println("Troca de arquivo concluída.");
                } else {
                    System.out.println("O par remoto recusou a troca de arquivo.");
                }
            } else {
                System.out.println("O par remoto recusou a troca de arquivo.");
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendFileListToTrackerPeriodically() {
    Timer timer = new Timer();
    timer.scheduleAtFixedRate(new TimerTask() {
        @Override
        public void run() {
            try {
                // Conectar ao Tracker
                Socket socket = new Socket("192.168.0.211", 8888);
                ObjectOutputStream out = new     ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

                // Enviar a solicitação adequada com base no tipo
                if (getFiles().isEmpty()) {
                    out.writeObject(new Request(RequestType.FILE_LIST_EMPTY));
                    System.out.println("Solicitação de lista de arquivos vazia enviada para o tracker.");
                } else {
                    out.writeObject(new Request(RequestType.FILE_LIST, getFiles()));
                    System.out.println("Solicitação de lista de arquivos enviada para o tracker.");
                }

                // Receber a resposta do Tracker
                Object response = in.readObject();

                if (response instanceof PeerList) {
                    PeerList peerList = (PeerList) response;
                    List<Peer> availablePeers = peerList.getPeers();

                    // Processar a lista de pares disponíveis conforme necessário
                    for (Peer peer : availablePeers) {
                        System.out.println(peer.getIpAddress() + ":" + peer.getPort());
                    }
                }

                socket.close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }, 0, 60000); // Executar a cada 1 minuto (60000 milissegundos)
    }

    public boolean hasFile(String fileName) {
        return fileAvailability.containsKey(fileName);
    }

    public void addFile(String fileName) {
        int count = fileAvailability.getOrDefault(fileName, 0);
        fileAvailability.put(fileName, count + 1);
    }

    public void removeFile(String fileName) {
        int count = fileAvailability.getOrDefault(fileName, 0);
        if (count > 0) {
            fileAvailability.put(fileName, count - 1);
        }
    }

    public List<String> getFiles() {
        return new ArrayList<>(fileAvailability.keySet());
    }

    // Getters e setters

    public String getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }
}
