import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.concurrent.locks.ReentrantLock;

public class Bellman_Ford_Process implements ProcessRMI, Runnable {

    public String[] peers;
    public int[] ports;
    public int me;
    public int s;  // source node

    public int[] G;
    public int[][] w;

    Registry registry;
    ProcessRMI stub;

    public Bellman_Ford_Process(String[] peers, int[] ports, int me, int[][] w, int s) {
        this.peers = peers;
        this.ports = ports;
        this.me = me;
        this.s = s;
        G = new int[w.length];
        Arrays.fill(this.G, Integer.MAX_VALUE);
        G[s] = 0;
        this.w = w;

        try{
            System.setProperty("java.rmi.server.hostname", this.peers[this.me]);
            registry = LocateRegistry.createRegistry(this.ports[this.me]);
            stub = (ProcessRMI) UnicastRemoteObject.exportObject(this, this.ports[this.me]);
            registry.rebind("Process", stub);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Wrapper method for handling RMI
     * @param rmi RMI handler to envoke
     * @param p request packet
     * @param id process to which RMI should be made
     * @return response packet
     */
    public Packet Call(String rmi, Packet p, int id){
        Packet callReply = null;

        ProcessRMI stub;
        try{
            Registry registry = LocateRegistry.getRegistry(this.ports[id]);
            stub = (ProcessRMI) registry.lookup("Process");
            if(rmi.equals("Send"))
                callReply = stub.Send(p);
            else
                System.out.println("Wrong parameters!");
        } catch(Exception e){
            return null;
        }
        return callReply;
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
     * RMI handler for simulating requests/responses between processes
     * @param p request packet
     * @return response packet
     */
    public Packet Send(Packet p) {
        if (p.message.equals("G_i")) {
            return new Packet(G[me]);
        }
        return null;
    }

    /**
     * Compute whether the respective lattice-linear predicate is true for all processes
     * @return true if predicate true for all processes, else false
     */
    public boolean evalPredicate() {
        int n = w.length;

        // get up-to-date G[i] for all processes i
        for(int i = 0; i<n; i++) {
            Packet p = Call("Send", new Packet("G_i"), i);
            G[i] = (int) p.message;
        }

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
            if (G[me] > min) {
                return min;
            }
        }
        return -1;
    }

    /**
     * Advance on current state if it is forbidden
     * @param min value to which this process should advance
     */
    public void advance(int min) {
        G[me] = min;
    }

    /**
     * Compute all nodes i for which (i,j) is an edge in the directed graph
     * @param j node on which to compute
     * @return boolean array, if (i,j) is in the graph then element i true, else false
     */
    private boolean[] pre(int j) {
        boolean[] pre = new boolean[peers.length];
        Arrays.fill(pre, false);

        for (int i = 0; i < peers.length; i++) {
            if (w[i][j] > 0) {
                pre[i] = true;
            }
        }
        return pre;
    }

    /**
     * Used for RMI cleanup
     */
    public void Kill(){
        if(this.registry != null){
            try {
                UnicastRemoteObject.unexportObject(this.registry, true);
            } catch(Exception e){
                System.out.println("None reference");
            }
        }
    }
}
