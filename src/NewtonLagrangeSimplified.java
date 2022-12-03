import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/***
 * Code by Alex Mariano
 * Numerical Methods and Computing (CS3010)
 * Professor Dr. Lajpat Rai Raheja
 * California State Polytechnic University, Pomona
 * Programming Project 4
 */

public class NewtonLagrangeSimplified {
    private static double[][] augmentedCoefficientMatrix;
    private static Scanner userInput = new Scanner(System.in);
    public static void main(String[] args){
        augmentedCoefficientMatrix = getACMFromFile();

        System.out.println("Solving: ");
    }

    private static double[] doNewtonMethodOnce(double[] variables){
        return new double[0];
    }

    private static double[] doLagrangeMethod(double[] variables){
        return new double[0];
    }

    private static double[] doSimplifiedMethod(double[] variables){
        return new double[0];
    }


    /***
     * Asks users for the name of a file
     * @return a matrix of the coefficients if the file is found.
     */
    private static double[][] getACMFromFile() {
        ArrayList<ArrayList<Integer>> rowLists = new ArrayList<ArrayList<Integer>>();
        while (true) {
            System.out.println("Enter the name of the file");
            String fileName = userInput.nextLine();
            try {
                FileReader fileReader = new FileReader(fileName);
                Scanner fileScanner = new Scanner(fileReader);
                while (fileScanner.hasNextLine()) {
                    String currentLine = fileScanner.nextLine();
                    ArrayList coefficientList = new ArrayList();
                    Scanner coefficientScanner = new Scanner(currentLine);
                    while (coefficientScanner.hasNextInt()) {
                        coefficientList.add(coefficientScanner.nextInt());
                    }
                    rowLists.add(coefficientList);
                }
                fileReader.close();
                break;
            } catch (IOException e) {
                System.out.println("Error: Unable to get file: " + fileName + ", please try again.");
            }
        }
        int rows = rowLists.size();
        int cols = rowLists.get(0).size();
        double[][] returnMatrix = new double[rows][cols];
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                returnMatrix[y][x] = rowLists.get(y).get(x);
            }
        }
        return returnMatrix;
    }
}
