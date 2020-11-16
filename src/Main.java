import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.function.Predicate;

public class Main {
    private static class Matrix {
        public int[][] matrix;
        public int rows;
        public int cols;
        public Matrix(int[][] matrix, int rows, int cols) {
            this.matrix = matrix;
            this.rows = rows;
            this.cols = cols;
        }
    }

    private static void cleanup(Process[] pxa){
        for(int i = 0; i < pxa.length; i++){
            if(pxa[i] != null){
                pxa[i].Kill();
            }
        }
    }

    /**
     * format of file must be
     * line1: # rows
     * line2: # cols
     * rest of lines: matrix according to # rows and # cols with each integer being space separated
     * @param filePath
     * @return int[][] representing the matrix contained in the file
     * @throws FileNotFoundException
     */
    private static Matrix parseAdjacencyMatrix(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        Scanner in = new Scanner(file);
        int rows = in.nextInt(), cols = in.nextInt();
        int[][] matrix = new int[rows][cols];
        for(int i = 0; i<rows; i++) {
            for(int j = 0; j<cols; j++) {
                matrix[i][j] = in.nextInt();
            }
        }
        return new Matrix(matrix, rows, cols);
    }


    public static void main(String[] args) {
        Matrix w = null;
        try {
            w = parseAdjacencyMatrix("matrix.txt");
        }
        catch(FileNotFoundException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        int n = w.rows;

        String host = "127.0.0.1";
        String[] peers = new String[n];
        int[] ports = new int[n];

        Process[] proc = new Process[n];
        for(int i = 0 ; i < n; i++){
            ports[i] = 1100+i;
            peers[i] = host;
        }

        int source = 0;

        for(int i = 0; i < n; i++){
            proc[i] = new Process(peers, ports, i, w.matrix, source);
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
}
