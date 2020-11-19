import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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

    public static int messages; // for counting number of messages sent

    public TCPProcess(String ip, int port, int[][] w, int source, int me) {
        this.ip = ip;
        this.port = port;
        this.peers = new HashMap<>();

        this.w = w;
        this.source = source;
        this.me = me;

        G = new int[w.length];
        Arrays.fill(this.G, Integer.MAX_VALUE);
        G[source] = 0;

        try {
            this.server = new ServerSocket(port);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // create a separate thread to handle all incoming connection requests
        Thread connectionHandler = new Thread(() -> {
            while (true) {
                try {
                    Socket peer = server.accept();

                    // for each new connection, create a separate thread to handle incoming messages from the process
                    Thread clientHandler = new Thread(() -> {
                        String s;
                        DataInputStream dataInputStream;
                        try {
                            dataInputStream = new DataInputStream(peer.getInputStream());

                            while ((s = dataInputStream.readUTF()) != null) {
                                String[] nums = s.split(" ");
                                int index = Integer.parseInt(nums[0]); // first integer is sender's process number, i
                                int val = Integer.parseInt(nums[1]); // second integer is their value at G[i]
                                G[index] = val;
                                int ret = forbidden(me);
                                if(ret != -1) {
                                    advance(ret);
                                }
                            }

                        } catch (Exception e) {
                            System.out.println("Connection lost");
                        }
                    });

                    clientHandler.start();

                } catch (Exception e) {
                    break; // if we reach this, the server is shutting down
                }
            }
        });

        connectionHandler.start();
    }

    /**
     * Create a unidirectional communication channel from yourself to another process.
     * @param ip IP address of the process you want to communicate with
     * @param processNum unique number between 0 and n for a network of size n processes
     * @param port port number of the process' server
     * @return true if connection successful, else false
     */
    public boolean addPeer(String ip, int processNum, int port) {
        try {
            this.peers.put(processNum, new DataOutputStream(new Socket(ip, port).getOutputStream()));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Send a messagage to another process in the network.
     * @param recipient process number to which message will be sent
     * @param s UTF string message
     * @return true if write successful, else false
     */
    public boolean send(Integer recipient, String s) {
        try {
            this.peers.get(recipient).writeUTF(s);
            messages++;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Effectively shut down the server running on this process.
     */
    public void closeSocket() {
        try {
            server.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Once started, process will continue to run until
     * the lattice-linear predicate is true for all processes
     */
    @Override
    public void run() {
        int ret;

        if (source == me) {
            return;
        }
        ret = forbidden(me);
        if(ret != -1) {
            advance(ret);
        }

        while(!evalPredicate()) {}

        System.out.println("Process: " + me + ", G: " + Arrays.toString(G));
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
