import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Random;

public class Dijkstra_Sequential_Perf {
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

        long start = System.nanoTime();
        int[] vals = s.dijkstra(mat, 0, 100);
        long end = System.nanoTime();

        double elapsed = (end - start) / 1000000.0;

        System.out.println("ELAPSED TIME: " + elapsed);

        System.out.println(Arrays.toString(vals));

        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.exit(0);
    }
}
