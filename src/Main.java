public class Main {
    public static void main(String[] args) {
        final int n = 5;
        String host = "127.0.0.1";
        String[] peers = new String[n];
        int[] ports = new int[n];

        Process[] proc = new Process[n];
        for(int i = 0 ; i < n; i++){
            ports[i] = 1100+i;
            peers[i] = host;
        }
        for(int i = 0; i < n; i++){
            proc[i] = new Process(peers, ports, i);
        }

        proc[0].Start();
    }
}
