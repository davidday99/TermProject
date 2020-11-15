import java.util.Arrays;
import java.util.function.Predicate;

public class Main {
    public static void main(String[] args) {
        final int n = 3;
        String host = "127.0.0.1";
        String[] peers = new String[n];
        int[] ports = new int[n];

        Process[] proc = new Process[n];
        for(int i = 0 ; i < n; i++){
            ports[i] = 1100+i;
            peers[i] = host;
        }

        int[][] w = {{-1, 1, 5, },{-1,-1, 3},{-1,-1,-1}};

        int source = 0;

        for(int i = 0; i < n; i++){
            proc[i] = new Process(peers, ports, i, w, source);
        }

        Thread[] threads = new Thread[n];

        for (int i= 0; i < n; i++) {
            threads[i] = new Thread(proc[i]);
            threads[i].start();
        }


        for (Thread t : threads) {
            try {
                t.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (Process p : proc) {
            System.out.println("d(" + source + "," + p.me + ") = " + p.G_me);
        }

        cleanup(proc);

        System.exit(0);
    }

    private static void cleanup(Process[] pxa){
        for(int i = 0; i < pxa.length; i++){
            if(pxa[i] != null){
                pxa[i].Kill();
            }
        }
    }
}
