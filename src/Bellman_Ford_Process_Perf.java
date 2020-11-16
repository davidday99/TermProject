import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Random;

public class Bellman_Ford_Process_Perf {
    static String[] filePaths = {"matrix.txt", "matrix1.txt", "matrix2.txt"}; // represents filepaths to all matriies to be test
    //static String[] filePaths = {"matrix1.txt"}; // represents filepaths to all matriies to be test
    static Matrix[] matricies = null; // list of matricies to be tested

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

    public static void main(String[] args) {
       setup();

       ShortestPathTest s = new ShortestPathTest();

       int[][] mat = new int[100][100];
       Random random = new Random();

       for (int i = 0; i < 100; i++) {
           for (int j = 0; j < 100; j++) {
               mat[i][j] = random.nextInt(99) + 1;
           }
       }

       Matrix m = new Matrix(mat, 100, 100);

       int[] vals = s.distributed_bellman_ford_shortest_path(m, 0);
       System.out.println(Arrays.toString(vals));

       //System.exit(0);
    }
}
