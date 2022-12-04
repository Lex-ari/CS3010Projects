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
    private static double[][] xAndFxValues; // [0][] is x values, [1][] are f(x) values
    private static Scanner userInput = new Scanner(System.in);
    public static void main(String[] args){
        xAndFxValues = getACMFromFile();

        System.out.println("Solving: ");
        ArrayList<double[]> thing = doNewtonMethod(xAndFxValues[0],xAndFxValues[1]);
        printDividedDifferenceTable(thing);
        printNewtonForm(thing);
        System.out.println();
        printLagrangeMethod(xAndFxValues[0], xAndFxValues[1]);
        System.out.println();
        System.out.println(Arrays.toString(doSimplifiedMethod(thing)));
        System.out.println("Done");
    }

    private static void printDividedDifferenceTable(ArrayList<double[]> unFormattedTable){
        int cols = unFormattedTable.size();
        int rows = unFormattedTable.get(0).length;
        System.out.printf("%10s ", "x");
        for (int i = 0; i < cols - 1; i++){
            String fString = "f[," + i + "]";
            System.out.printf("%10s ", fString);
        }
        System.out.println();
        for (int row = 0; row < rows; row++){
            for (int col = 0; col < cols - row; col++){
                System.out.printf("%10.3f ", unFormattedTable.get(col)[row]);
            }
            System.out.println();
        }
    }

    /***
     * Rounds a double value if it is extremely close to an integer. Ex. 0.9999999999 --> 1.0
     * @param value to be check if it is an integer
     * @return rounded value, or value if it is not an integer.
     */
    private static double roundIfInteger(double value){
        double floorSub = Math.abs(value - Math.floor(value));
        double ceilingSub = Math.abs(value - Math.ceil(value));
        if (floorSub < 1E-4 || ceilingSub < 1E-4){
            return Math.round(value);
        }
        return value;
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
        fLayers.add(xVars);
        fLayers.add(fxVars);
        do {
            int offset = xVars.length - workingFx.length + 1;
            double[] returnFxValues = new double[workingFx.length - 1];
            for (int i = 0; i < returnFxValues.length; i++){
                returnFxValues[i] = (workingFx[i + 1] - workingFx[i ]) / (xVars[i + offset] - xVars[i]);
                returnFxValues[i] = roundIfInteger(returnFxValues[i]);
            }
            workingFx = Arrays.copyOf(returnFxValues, returnFxValues.length);
            fLayers.add(workingFx);
        } while (fLayers.get(fLayers.size() - 1).length > 1);

        return fLayers;
    }

    /**
     * Prints out polynomail in Newton's Form given an unformatted table (ArrayList containing cols of x, f[], f[,], etc)
     * @param unFormattedTable ArrayList containing cols of x, f[], f[,], etc
     */
    private static void printNewtonForm(ArrayList<double[]> unFormattedTable){
        System.out.println("Interpolating polynomial in Newton's Form:");
        for (int col = 1; col < unFormattedTable.size(); col++){
            if (col > 1){
                System.out.print(" + ");
            }
            System.out.printf("%3.3f", unFormattedTable.get(col)[0]);
            for (int row = 0; row < col - 1; row++){
                double xIntercept = unFormattedTable.get(0)[row];
                if (xIntercept == 0){
                    System.out.print("x");
                } else {
                    System.out.printf("(x-%3.3f)", xIntercept);
                }
            }
        }
        System.out.println();
    }

    /**
     * Prints out polynomial using Lagrange's Method
     * @param xVars double[] of x variables
     * @param fxVars double[] of f(x) variables
     */
    private static void printLagrangeMethod(double[] xVars, double[] fxVars){
        // f(xi) = p * pisum (xi - xVars)     for xi - xVari is not included.
        // p = f(xi) / pisum(xi - xVars)     for xi - xVari is not included.
        System.out.println("Interpolating polynomial in Lagrange's Form:");
        double[] pVars = new double[xVars.length];
        for (int p = 0; p < pVars.length; p++){
            double pisum = 1;
            for (int i = 0; i < xVars.length; i++){
                if (i == p){
                    continue;
                }
                pisum *= (xVars[p] - xVars[i]);
            }
            pVars[p] = fxVars[p] / pisum;
        }

        for (int i = 0; i < xVars.length; i++){
            if (i > 0){
                System.out.print(" + ");
            }
            System.out.printf("%3.3f", pVars[i]);
            for (int j = 0; j < xVars.length; j++){
                if (i == j){
                    continue;
                }
                double xIntercept = xVars[j];
                if (xIntercept == 0){
                    System.out.print("x");
                } else {
                    System.out.printf("(x-%3.3f)", xIntercept);
                }
            }
        }
    }

    private static double[] doSimplifiedMethod(ArrayList<double[]> unFormattedTable){
        // Using nested form Newton's Method to create polynomial
        // put rightmost x f[,,,] value at the left
        // shift double right
        // multiply original value by x-intercept and add to double[]
        // repeat

        //example
        // 3 + (x-1)(1/2 + (x-3/2)(1/3 + (x-0)(2)))

        // put rightmost f[,,,] value at the right
        // iteratively multiply
        // double[n-1] = everything >n multiplied by the x intercept.

        double[] coefficients = new double[unFormattedTable.get(0).length];
        for (int col = coefficients.length - 1; col >= 0; col--){
            coefficients[col] += unFormattedTable.get(col + 1)[0];
            for (int i = col; i < coefficients.length - 1; i++){
                coefficients[i] -= coefficients[i + 1] * unFormattedTable.get(0)[col];
            }
        }
        return coefficients;
    }

    private static double getAllPossibleCoefficients(double[] xIntercepts){



        return 0.0;
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
