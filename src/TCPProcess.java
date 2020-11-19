import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;


public class TCPProcess implements Runnable{
    String ip;
    int port;
    int me;

    ServerSocket server;

    HashMap<Integer, DataOutputStream> peers;

    public int[] G;
    public int[][] w;
    int source;

    public TCPProcess(String ip, int port, int[][] w, int source, int me) {
        this.ip = ip;
        this.port = port;
        this.peers = new HashMap<>();

        this.w = w;
        this.source = source;
        this.me = me;

        G = new int[w.length];
        Arrays.fill(this.G, Integer.MAX_VALUE);

        try {
            this.server = new ServerSocket(port);

        } catch (Exception e) {
            e.printStackTrace();
        }

        Thread connectionHandler = new Thread(() -> {
            while (true) {
                try {
                    Socket peer = server.accept();
                    System.out.println("connection established to " + peer.getInetAddress().getHostAddress());

                    Thread clientHandler = new Thread(() -> {
                        String sender = peer.getInetAddress().getHostAddress();
                        String s;
                        DataInputStream dataInputStream;
                        try {
                            dataInputStream = new DataInputStream(peer.getInputStream());

                            while ((s = dataInputStream.readUTF()) != null) {
                                System.out.println(sender + ": " + s);
                            }

                        } catch (Exception e) {
                            System.out.println("Connection lost");
                        }
                    });

                    clientHandler.start();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        connectionHandler.start();
    }

    public boolean addPeer(String ip, int processNum, int port) {
        try {
            this.peers.put((Integer) processNum, new DataOutputStream(new Socket(ip, port).getOutputStream()));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean send(Integer recipient, String s) {
        try {
            this.peers.get(recipient).writeUTF(s);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Once started, process will continue to run until
     * the lattice-linear predicate is true for all processes
     */
    @Override
    public void run() {
        int ret;
        while(!evalPredicate()) {
            ret = forbidden(me);
            if(ret != -1) {  // if ret != -1 process is in a forbidden state and must advance
                advance(ret);
            }
        }
    }

    /**
     * Compute whether the respective lattice-linear predicate is true for all processes
     * @return true if predicate true for all processes, else false
     */
    public boolean evalPredicate() {
        int n = w.length;

        for(int j = 0; j<n; j++) {
            boolean[] pre = pre(j);
            int min = Integer.MAX_VALUE;
            for(int i = 0; i<pre.length; i++) {
                if (pre[i]) {
                    int temp = G[i];
                    if (temp + w[i][j] > temp) temp += w[i][j];
                    min = Math.min(min, temp);
                }
            }
            if(G[j] > min) {
                return false;
            }
        }
        return true;
    }

    /**
     * Determine if process j is in a forbidden state using the respective lattice-linear predicate
     * @param j process on which to compute
     * @return minimum value of G[i] + w[i,j] over all i in pre(j) if forbidden(j), else -1
     */
    public int forbidden(int j) {
        boolean[] pre = pre(j);
        int min = Integer.MAX_VALUE;

        for (int i = 0; i < pre.length; i++) {
            if (pre[i]) {
                int temp = G[i];
                if (temp + w[i][j] > temp) temp += w[i][j];
                min = Math.min(min, temp);
            }
        }
        if (G[me] > min) {
            return min;
        }
        return -1;
    }

    /**
     * Advance on current state if it is forbidden
     * @param min value to which this process should advance
     */
    public void advance(int min) {
        G[me] = min;
        for (Integer peer : peers.keySet()) {
            if (peer != me) {
                send(peer, me + " " + G[me]);
            }
        }
    }

    /**
     * Compute all nodes i for which (i,j) is an edge in the directed graph
     * @param j node on which to compute
     * @return boolean array, if (i,j) is in the graph then element i true, else false
     */
    private boolean[] pre(int j) {
        boolean[] pre = new boolean[w.length];
        Arrays.fill(pre, false);

        for (int i = 0; i < w.length; i++) {
            if (w[i][j] > 0) {
                pre[i] = true;
            }
        }
        return pre;
    }

}
