import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Random;

public class ShortestPathTCPTest {
    //static String[] filePaths = {"3_node_sparse.txt", "9_node_sparse.txt", "16_node_sparse.txt"}; // represents filepaths to all matriies to be test
    static String[] filePaths = {"9_node_sparse.txt"}; // represents filepaths to all matriies to be test
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
    public void test_size_3_sparse_against_sequential_using_distributed_bellman_ford() {
        Matrix currMatrix = null;
        try {
            currMatrix = Matrix.parseAdjacencyMatrix("3_node_sparse.txt");
        }
        catch(FileNotFoundException e) {
            e.printStackTrace();
        }

        int[] actual = distributed_bellman_ford_shortest_path(currMatrix, 0);
        int[] expected = sequential_bellman_ford_shortest_path(currMatrix.matrix, 0, currMatrix.rows);
        System.out.println(Arrays.toString(expected));
        System.out.println(Arrays.toString(actual));
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void test_size_3_dense_against_sequential_using_distributed_bellman_ford() {
        Matrix currMatrix = null;
        try {
            currMatrix = Matrix.parseAdjacencyMatrix("3_node_dense.txt");
        }
        catch(FileNotFoundException e) {
            e.printStackTrace();
        }

        int[] actual = distributed_bellman_ford_shortest_path(currMatrix, 0);
        int[] expected = sequential_bellman_ford_shortest_path(currMatrix.matrix, 0, currMatrix.rows);
        System.out.println(Arrays.toString(expected));
        System.out.println(Arrays.toString(actual));
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void test_size_9_sparse_against_sequential_using_distributed_bellman_ford() {
        Matrix currMatrix = null;
        try {
            currMatrix = Matrix.parseAdjacencyMatrix("9_node_sparse.txt");
        }
        catch(FileNotFoundException e) {
            e.printStackTrace();
        }

        int[] actual = distributed_bellman_ford_shortest_path(currMatrix, 0);
        int[] expected = sequential_bellman_ford_shortest_path(currMatrix.matrix, 0, currMatrix.rows);
        System.out.println(Arrays.toString(expected));
        System.out.println(Arrays.toString(actual));
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void test_size_9_dense_against_sequential_using_distributed_bellman_ford() {
        Matrix currMatrix = null;
        try {
            currMatrix = Matrix.parseAdjacencyMatrix("9_node_dense.txt");
        }
        catch(FileNotFoundException e) {
            e.printStackTrace();
        }

        int[] actual = distributed_bellman_ford_shortest_path(currMatrix, 0);
        int[] expected = sequential_bellman_ford_shortest_path(currMatrix.matrix, 0, currMatrix.rows);
        System.out.println(Arrays.toString(expected));
        System.out.println(Arrays.toString(actual));
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void test_size_16_sparse_against_sequential_using_distributed_bellman_ford() {
        Matrix currMatrix = null;
        try {
            currMatrix = Matrix.parseAdjacencyMatrix("16_node_sparse.txt");
        }
        catch(FileNotFoundException e) {
            e.printStackTrace();
        }

        int[] actual = distributed_bellman_ford_shortest_path(currMatrix, 0);
        int[] expected = sequential_bellman_ford_shortest_path(currMatrix.matrix, 0, currMatrix.rows);
        System.out.println(Arrays.toString(expected));
        System.out.println(Arrays.toString(actual));
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void test_size_16_dense_against_sequential_using_distributed_bellman_ford() {
        Matrix currMatrix = null;
        try {
            currMatrix = Matrix.parseAdjacencyMatrix("16_node_dense.txt");
        }
        catch(FileNotFoundException e) {
            e.printStackTrace();
        }

        int[] actual = distributed_bellman_ford_shortest_path(currMatrix, 0);
        int[] expected = sequential_bellman_ford_shortest_path(currMatrix.matrix, 0, currMatrix.rows);
        System.out.println(Arrays.toString(expected));
        System.out.println(Arrays.toString(actual));
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void test_size_25_sparse_against_sequential_using_distributed_bellman_ford() {
        Matrix currMatrix = null;
        try {
            currMatrix = Matrix.parseAdjacencyMatrix("25_node_sparse.txt");
        }
        catch(FileNotFoundException e) {
            e.printStackTrace();
        }

        int[] actual = distributed_bellman_ford_shortest_path(currMatrix, 0);
        int[] expected = sequential_bellman_ford_shortest_path(currMatrix.matrix, 0, currMatrix.rows);
        System.out.println(Arrays.toString(expected));
        System.out.println(Arrays.toString(actual));
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void test_size_25_dense_against_sequential_using_distributed_bellman_ford() {
        Matrix currMatrix = null;
        try {
            currMatrix = Matrix.parseAdjacencyMatrix("25_node_dense.txt");
        }
        catch(FileNotFoundException e) {
            e.printStackTrace();
        }

        int[] actual = distributed_bellman_ford_shortest_path(currMatrix, 0);
        int[] expected = sequential_bellman_ford_shortest_path(currMatrix.matrix, 0, currMatrix.rows);
        System.out.println(Arrays.toString(expected));
        System.out.println(Arrays.toString(actual));
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void test_size_30_sparse_against_sequential_using_distributed_bellman_ford() {
        Matrix currMatrix = null;
        try {
            currMatrix = Matrix.parseAdjacencyMatrix("30_node_sparse.txt");
        }
        catch(FileNotFoundException e) {
            e.printStackTrace();
        }

        int[] actual = distributed_bellman_ford_shortest_path(currMatrix, 0);
        int[] expected = sequential_bellman_ford_shortest_path(currMatrix.matrix, 0, currMatrix.rows);
        System.out.println(Arrays.toString(expected));
        System.out.println(Arrays.toString(actual));
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void test_size_30_dense_against_sequential_using_distributed_bellman_ford() {
        Matrix currMatrix = null;
        try {
            currMatrix = Matrix.parseAdjacencyMatrix("30_node_dense.txt");
        }
        catch(FileNotFoundException e) {
            e.printStackTrace();
        }

        int[] actual = distributed_bellman_ford_shortest_path(currMatrix, 0);
        int[] expected = sequential_bellman_ford_shortest_path(currMatrix.matrix, 0, currMatrix.rows);
        System.out.println(Arrays.toString(expected));
        System.out.println(Arrays.toString(actual));
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void test_size_50_sparse_against_sequential_using_distributed_bellman_ford() {
        Matrix currMatrix = null;
        try {
            currMatrix = Matrix.parseAdjacencyMatrix("50_node_sparse.txt");
        }
        catch(FileNotFoundException e) {
            e.printStackTrace();
        }

        int[] actual = distributed_bellman_ford_shortest_path(currMatrix, 0);
        int[] expected = sequential_bellman_ford_shortest_path(currMatrix.matrix, 0, currMatrix.rows);
        System.out.println(Arrays.toString(expected));
        System.out.println(Arrays.toString(actual));
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void test_size_50_dense_against_sequential_using_distributed_bellman_ford() {
        Matrix currMatrix = null;
        try {
            currMatrix = Matrix.parseAdjacencyMatrix("50_node_dense.txt");
        }
        catch(FileNotFoundException e) {
            e.printStackTrace();
        }

        int[] actual = distributed_bellman_ford_shortest_path(currMatrix, 0);
        int[] expected = sequential_bellman_ford_shortest_path(currMatrix.matrix, 0, currMatrix.rows);
        System.out.println(Arrays.toString(expected));
        System.out.println(Arrays.toString(actual));
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void test_size_100_sparse_against_sequential_using_distributed_bellman_ford() {
        Matrix currMatrix = null;
        try {
            currMatrix = Matrix.parseAdjacencyMatrix("100_node_sparse.txt");
        }
        catch(FileNotFoundException e) {
            e.printStackTrace();
        }

        int[] actual = distributed_bellman_ford_shortest_path(currMatrix, 0);
        int[] expected = sequential_bellman_ford_shortest_path(currMatrix.matrix, 0, currMatrix.rows);
        System.out.println(Arrays.toString(expected));
        System.out.println(Arrays.toString(actual));
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void test_size_100_dense_against_sequential_using_distributed_bellman_ford() {
        Matrix currMatrix = null;
        try {
            currMatrix = Matrix.parseAdjacencyMatrix("100_node_dense.txt");
        }
        catch(FileNotFoundException e) {
            e.printStackTrace();
        }

        int[] actual = distributed_bellman_ford_shortest_path(currMatrix, 0);
        int[] expected = sequential_bellman_ford_shortest_path(currMatrix.matrix, 0, currMatrix.rows);
        System.out.println(Arrays.toString(expected));
        System.out.println(Arrays.toString(actual));
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void test_size_100_random_against_sequential_using_distributed_bellman_ford() {
        Matrix currMatrix = null;
        int n = 100;

        int[][] matrix = new int[n][n];
        Random random = new Random();

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = random.nextInt(9) + 1;
            }
        }

        currMatrix = new Matrix(matrix, matrix[0].length, matrix[0].length);

        int[] actual = distributed_bellman_ford_shortest_path(currMatrix, 0);
        int[] expected = sequential_bellman_ford_shortest_path(currMatrix.matrix, 0, currMatrix.rows);
        System.out.println(Arrays.toString(expected));
        System.out.println(Arrays.toString(actual));
        Assert.assertArrayEquals(expected, actual);
    }

    /**
     * Used to verify correctness of our implemented sequential Bellman-Ford
     */
    @Test
    public void testbellmanford_v_dijstkra() {
        Matrix currMatrix = null;
        try {
            currMatrix = Matrix.parseAdjacencyMatrix("100_node_dense.txt");
        }
        catch(FileNotFoundException e) {
            e.printStackTrace();
        }

        int[] actual = sequential_bellman_ford_shortest_path(currMatrix.matrix, 0, currMatrix.rows);
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

    /**
     * Sequential Bellman-Ford Algorithm
     * Slightly modified because the original paper made the assumption of strictly positive edge weights at first
     * To keep in line with the paper, this Bellman-Ford was constructed under the same assumption
     * @param graph
     * @param src
     * @param numVerticies
     * @return minimized distance array from src to all verticies in graph
     */
    public int[] sequential_bellman_ford_shortest_path(int[][] graph, int src, int numVerticies) {
        int dist[] = new int[numVerticies];
        // initialization
        for(int i = 0; i<numVerticies; i++) {
            dist[i] = Integer.MAX_VALUE;
        }
        dist[src] = 0;

        // relax edges
        for(int i = 1; i<numVerticies; i++) {
            for(int j = 0; j<graph.length; j++) {
                for(int k = 0; k<graph[0].length; k++) {
                    if(graph[j][k] > 0) {
                        if(dist[j] != Integer.MAX_VALUE && dist[j] + graph[j][k] < dist[k]) {
                            dist[k] = dist[j] + graph[j][k];
                        }
                    }
                }
            }
        }

        // we do not need to check for negative cycles because original paper was not concerned with negative edge weights when it came to bellman-ford

        return dist;
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
