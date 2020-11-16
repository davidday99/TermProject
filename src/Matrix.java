import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Matrix {
    public int[][] matrix;
    public int rows;
    public int cols;
    public Matrix(int[][] matrix, int rows, int cols) {
        this.matrix = matrix;
        this.rows = rows;
        this.cols = cols;
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
    public static Matrix parseAdjacencyMatrix(String filePath) throws FileNotFoundException {
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
}
