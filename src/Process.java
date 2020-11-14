import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.Registry;

public class Process implements ProcessRMI, Runnable {

    public String[] peers;
    public int[] ports;
    public int me;

    Registry registry;
    ProcessRMI stub;

    Process(String[] peers, int[] ports, int me) {
        this.peers = peers;
        this.ports = ports;
        this.me = me;

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

        while (n < peers.length) {
            Packet p;
            for (int i = 0; i < peers.length; i++) {
                p = Call("Send", new Packet(false, "test"), i);

                if (p != null) {
                    System.out.println(p.message);
                    n++;
                }
            }
        }
        System.exit(0);
    }

    public Packet Send(Packet p) {
        if (!p.success) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            p.success = true;
            p.message = "received";
        }
        return p;
    }
}
