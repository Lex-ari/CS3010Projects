import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/***
 * Code by Alex Mariano
 * Numerical Methods and Computing (CS3010)
 * Professor Dr. Lajpat Rai Raheja
 * California State Polytechnic University, Pomona
 * Programming Project 4
 */

public class NewtonLagrangeSimplified {
    private static double[][] xAndFxValues; // [0][] is x values, [1][] are f(x) values
    private static Scanner userInput = new Scanner(System.in);
    public static void main(String[] args){
        xAndFxValues = getACMFromFile();

        System.out.println("Solving: ");
        ArrayList<double[]> thing = doNewtonMethod(xAndFxValues[0],xAndFxValues[1]);
        System.out.println("Done");
    }

    /**
     * Uses Newton's Method and returns an Array List of each column (x, f[], f[,], f[,,], etc)
     * @param xVars initial starting x values
     * @param fxVars initial starting f(x) values
     * @return ArrayList of each column in double[] format (x, f[], f[,], f[,,], etc)
     */
    private static ArrayList<double[]> doNewtonMethod(double[] xVars, double[] fxVars){
        ArrayList<double[]> fLayers = new ArrayList();
        double[] workingFx = Arrays.copyOf(fxVars, fxVars.length);
        do {
            int offset = xVars.length - workingFx.length + 1;
            double[] returnFxValues = new double[workingFx.length - 1];
            for (int i = 0; i < returnFxValues.length; i++){
                returnFxValues[i] = (workingFx[i + 1] - workingFx[i ]) / (xVars[i + offset] - xVars[i]);
            }
            workingFx = Arrays.copyOf(returnFxValues, returnFxValues.length);
            fLayers.add(workingFx);
        } while (fLayers.get(fLayers.size() - 1).length > 1);

        return fLayers;
    }

    private static double[] doLagrangeMethod(double[][] variables){
        return new double[0];
    }

    private static double[] doSimplifiedMethod(double[][] variables){
        return new double[0];
    }


    /***
     * Asks users for the name of a file
     * @return a matrix of the coefficients if the file is found.
     */
    private static double[][] getACMFromFile() {
        ArrayList<ArrayList<Double>> rowLists = new ArrayList<ArrayList<Double>>();
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
                    while (coefficientScanner.hasNextDouble()) {
                        coefficientList.add(coefficientScanner.nextDouble());
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
