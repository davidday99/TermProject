import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.concurrent.locks.ReentrantLock;

public class Bellman_Ford_Process implements ProcessRMI, Runnable {

    public String[] peers;
    public int[] ports;
    public int me;
    public int s;

    ReentrantLock mutex;

    public int[] G;
    public int[][] w;

    Registry registry;
    ProcessRMI stub;

    Bellman_Ford_Process(String[] peers, int[] ports, int me, int[][] w, int s) {
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

    public Packet Call(String rmi, Packet p, int id){
        Packet callReply = null;

        ProcessRMI stub;
        try{
            Registry registry = LocateRegistry.getRegistry(this.ports[id]);
            stub = (ProcessRMI) registry.lookup("Process");
            if(rmi.equals("Send"))
                callReply = stub.Send(p);
            else if(rmi.equals("Receive"))
                //callReply = stub.Receive();
                System.out.println("no receive");
            else
                System.out.println("Wrong parameters!");
        } catch(Exception e){
            return null;
        }
        return callReply;
    }

    public void Start() {
        Thread t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        int n = 0;

        int ret = -1;
//        while ((ret = forbidden(me)) != -1) {
//            advance(ret);
//        }
        while(!evalPredicate()) {
            ret = forbidden(me);
            if(ret != -1) {
                advance(ret);
            }
        }
    }

    public Packet Send(Packet p) {
        if (p.message.equals("G_i")) {
            return new Packet(G[me]);
        }
        return null;
    }

    public boolean evalPredicate() {
        int n = w.length;
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

    public void advance(int min) {
        G[me] = min;
    }

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
