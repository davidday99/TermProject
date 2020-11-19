import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

public class ShortestPathTCPTest {
    //static String[] filePaths = {"matrix.txt", "matrix1.txt", "matrix2.txt"}; // represents filepaths to all matriies to be test
    static String[] filePaths = {"matrix1.txt"}; // represents filepaths to all matriies to be test
    static Matrix[] matricies = null; // list of matricies to be tested

    @BeforeClass
    public static void setup() {
        matricies = new Matrix[filePaths.length];
        for(int i = 0; i<filePaths.length; i++) {
            String path = filePaths[i];
            try {
                matricies[i] = Matrix.parseAdjacencyMatrix(path);
            }
            catch(FileNotFoundException e) {
                matricies[i] = null;
                System.out.println(path + " was invalid");
            }
        }
    }

    @Test
    public void test_size_3_against_sequential_using_distributed_bellman_ford() {
        Matrix currMatrix = null;
        try {
            currMatrix = Matrix.parseAdjacencyMatrix("matrix.txt");
        }
        catch(FileNotFoundException e) {
            e.printStackTrace();
        }

        int[] actual = distributed_bellman_ford_shortest_path(currMatrix, 0);
        int[] expected = dijkstra(currMatrix.matrix, 0, currMatrix.rows);
        System.out.println(Arrays.toString(expected));
        System.out.println(Arrays.toString(actual));
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void test_size_9_against_sequential_using_distributed_bellman_ford() {
        Matrix currMatrix = null;
        try {
            currMatrix = Matrix.parseAdjacencyMatrix("matrix1.txt");
        }
        catch(FileNotFoundException e) {
            e.printStackTrace();
        }

        int[] actual = distributed_bellman_ford_shortest_path(currMatrix, 0);
        int[] expected = dijkstra(currMatrix.matrix, 0, currMatrix.rows);
        System.out.println(Arrays.toString(expected));
        System.out.println(Arrays.toString(actual));
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void test_size_16_against_sequential_using_distributed_bellman_ford() {
        Matrix currMatrix = null;
        try {
            currMatrix = Matrix.parseAdjacencyMatrix("matrix2.txt");
        }
        catch(FileNotFoundException e) {
            e.printStackTrace();
        }

        int[] actual = distributed_bellman_ford_shortest_path(currMatrix, 0);
        int[] expected = dijkstra(currMatrix.matrix, 0, currMatrix.rows);
        System.out.println(Arrays.toString(expected));
        System.out.println(Arrays.toString(actual));
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void test_size_25_against_sequential_using_distributed_bellman_ford() {
        Matrix currMatrix = null;
        try {
            currMatrix = Matrix.parseAdjacencyMatrix("matrix3.txt");
        }
        catch(FileNotFoundException e) {
            e.printStackTrace();
        }

        int[] actual = distributed_bellman_ford_shortest_path(currMatrix, 0);
        int[] expected = dijkstra(currMatrix.matrix, 0, currMatrix.rows);
        System.out.println(Arrays.toString(expected));
        System.out.println(Arrays.toString(actual));
        Assert.assertArrayEquals(expected, actual);
    }

    public void cleanup(TCPProcess[] proc) {
        for(TCPProcess p : proc) {
            p.closeSocket();
        }
    }

    public int[] distributed_bellman_ford_shortest_path(Matrix w, int source) {
        int n = w.rows;

        String host = "127.0.0.1";
        String[] peers = new String[n];
        int[] ports = new int[n];

        TCPProcess[] proc = new TCPProcess[n];
        for(int i = 0 ; i < n; i++){
            ports[i] = 1100+i;
            peers[i] = host;
        }

        for(int i = 0; i < n; i++){
            proc[i] = new TCPProcess(host, ports[i], w.matrix, source, i);
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    proc[i].addPeer(host, j, ports[j]);
                }
            }
        }

        Thread[] threads = new Thread[n];

        long start = System.nanoTime();
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
        long end = System.nanoTime();

        double elapsed = (end - start) / 1000000.0;

        System.out.println("ELAPSED TIME: " + elapsed);

        System.out.println("MESSAGES SENT: " + TCPProcess.messages);


        int[] distances = new int[n];
        for(int i = 0; i<n; i++) {
            TCPProcess p = proc[i];
            distances[i] = p.G[p.me];
        }

        cleanup(proc);

        return distances;
    }

    /* everything below here is taken from geeksforgeeks
       https://www.geeksforgeeks.org/dijkstras-shortest-path-algorithm-greedy-algo-7/
     */
    private int minDistance(int dist[], Boolean sptSet[], int V)
    {
        // Initialize min value
        int min = Integer.MAX_VALUE, min_index = -1;

        for (int v = 0; v < V; v++)
            if (sptSet[v] == false && dist[v] <= min) {
                min = dist[v];
                min_index = v;
            }

        return min_index;
    }

    // Function that implements Dijkstra's single source shortest path
    // algorithm for a graph represented using adjacency matrix
    // representation
    public int[] dijkstra(int graph[][], int src, int V)
    {
        int dist[] = new int[V]; // The output array. dist[i] will hold
        // the shortest distance from src to i

        // sptSet[i] will true if vertex i is included in shortest
        // path tree or shortest distance from src to i is finalized
        Boolean sptSet[] = new Boolean[V];

        // Initialize all distances as INFINITE and stpSet[] as false
        for (int i = 0; i < V; i++) {
            dist[i] = Integer.MAX_VALUE;
            sptSet[i] = false;
        }

        // Distance of source vertex from itself is always 0
        dist[src] = 0;

        // Find shortest path for all vertices
        for (int count = 0; count < V - 1; count++) {
            // Pick the minimum distance vertex from the set of vertices
            // not yet processed. u is always equal to src in first
            // iteration.
            int u = minDistance(dist, sptSet, V);

            // Mark the picked vertex as processed
            sptSet[u] = true;

            // Update dist value of the adjacent vertices of the
            // picked vertex.
            for (int v = 0; v < V; v++)

                // Update dist[v] only if is not in sptSet, there is an
                // edge from u to v, and total weight of path from src to
                // v through u is smaller than current value of dist[v]
                if (!sptSet[v] && graph[u][v] != 0 && dist[u] != Integer.MAX_VALUE && dist[u] + graph[u][v] < dist[v])
                    dist[v] = dist[u] + graph[u][v];
        }

        return dist;
    }

}
