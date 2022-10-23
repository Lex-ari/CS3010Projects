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
 * Programming Project 2
 */

public class JacobiIIterativeAndGauss_SeidelMethods {

    private static double[][] augmentedCoefficientMatrix;
    private static Scanner userInput = new Scanner(System.in);
    private static int[] startingSolutions;
    private static double stoppingError;
    public static void main(String[] args){

        //Handling Input
        while (true){
            System.out.println("Enter \"File\" to input filename, enter \"Manual\" to input coefficients manually.");
            String initialString = userInput.nextLine();
            if (initialString.equalsIgnoreCase("file")){
                augmentedCoefficientMatrix = getACMFromFile();
                break;
            } else if (initialString.equalsIgnoreCase("manual")){
                augmentedCoefficientMatrix = getACMFromUser();
                break;
            } else {
                System.out.println("Input received does not match \"file\" or \"manual\", Please try again.");
            }
        }
        while (true){
            System.out.println("Enter Desired Stopping Error");;
            if (userInput.hasNextDouble()){
                stoppingError = userInput.nextDouble();
                break;
            } else {
                System.out.println("Input not a double.");
            }
        }
        startingSolutions = getStartingSolutions();
        System.out.println("Solving: ");

        // Jacobi Method
        double[] nSolutions = intArrayToDoubleArray(startingSolutions);
        double[] nMinus1Solutions;
        int i = 0;
        do {
            nMinus1Solutions = copyArray(nSolutions);
            nSolutions = jacobiIterateOnce(nSolutions);
            System.out.println(Arrays.toString(nSolutions));
            i++;
        } while (i < 50 && calculateError(nSolutions, nMinus1Solutions) > stoppingError);

        System.out.println();

        // Gauss-Seidel Method
        nSolutions = intArrayToDoubleArray(startingSolutions);
        i = 0;
        do {
            nMinus1Solutions = copyArray(nSolutions);
            nSolutions = gaussSeidelIterateOnce(nSolutions);
            System.out.println(Arrays.toString(nSolutions));
            i++;
        } while (i < 50 && calculateError(nSolutions, nMinus1Solutions) > stoppingError);
    }

    private static double[] copyArray(double[] array){
        double[] returnArray = new double[array.length];
        for (int i = 0; i < array.length; i++){
            returnArray[i] = array[i];
        }
        return returnArray;
    }

    private static double calculateError(double[] nSolutions, double[] nMinus1Solutions){
        double squaredSum = 0;
        for (int i = 0; i < nSolutions.length; i++){
            squaredSum += Math.pow((nSolutions[i] - nMinus1Solutions[i]), 2);
        }
        double returnError = Math.sqrt(squaredSum) / calculateL2(nSolutions);
        //System.out.println("Error of current iteration: " + returnError);
        return returnError;
    }

    private static double[] intArrayToDoubleArray(int[] intArray){
        double[] returnDoubleArray = new double[intArray.length];
        for (int i = 0; i < intArray.length; i++){
            returnDoubleArray[i] = intArray[i];
        }
        return returnDoubleArray;
    }

    private static double calculateL2(double[] solutions){
        double squaredSum = 0;
        for (int i = 0; i < solutions.length; i++){
            squaredSum += solutions[i] * solutions[i];
        }
        double returnSquareRoot = Math.sqrt(squaredSum);
        //System.out.println("L2 of current iteration: " + returnSquareRoot);
        return returnSquareRoot;
    }

    private static double[] jacobiIterateOnce(double[] solutions){
        // Let Ax1 + Bx2 + Cx3 = D  (times 3 different rows)
        // Then x1 = 1/A * (D - Bx2 - Cx3)  (This equation is used for commenting)
        //other ex:
        // x2 = 1/B (D - Ax2 - Cx3)
        // x3 = 1/C (D - Ax2 - Bx2)
        double[] newSolutions = new double[augmentedCoefficientMatrix.length];
        for (int row = 0; row < augmentedCoefficientMatrix.length; row++){
            double sum = augmentedCoefficientMatrix[row][augmentedCoefficientMatrix[row].length - 1];   // D
            for (int col = 0; col < augmentedCoefficientMatrix.length; col++){
                if (col != row){
                    sum -= augmentedCoefficientMatrix[row][col] * solutions[col]; // (D - Bx2 - Cx3)
                }
            }
            newSolutions[row] = sum / augmentedCoefficientMatrix[row][row];   // 1/A *
        }
        return newSolutions;
    }

    private static double[] gaussSeidelIterateOnce(double[] solutions){
        // Let Ax1 + Bx2 + Cx3 = D  (times 3 different rows)
        // Then x1 = 1/A * (D - Bx2 - Cx3)  (This equation is used for commenting)

        double[] newSolutions = solutions;
        for (int row = 0; row < augmentedCoefficientMatrix.length; row++){
            double sum = augmentedCoefficientMatrix[row][augmentedCoefficientMatrix[row].length - 1];   // D
            for (int col = 0; col < augmentedCoefficientMatrix.length; col++){
                if (col != row){
                    sum -= augmentedCoefficientMatrix[row][col] * solutions[col]; // (D - Bx2 - Cx3)
                }
            }
            newSolutions[row] = sum / augmentedCoefficientMatrix[row][row];   // 1/A *
        }
        return newSolutions;
    }

    /***
     * Asks user to input number of equations and then input each row for the number of equations.
     * @return a matrix of the coefficients if user gives correct input of coefficients.
     */
    private static double[][] getACMFromUser(){
        int numEquations = 0;
        while (true){
            try {
                System.out.println("Please enter the number of equations used for Jacobi and Gauss-Seidel method");
                numEquations = userInput.nextInt();
                break;
            } catch (Exception e){
                System.out.println("Error: Unreadable Input. Please try again (input MUST be an integer!)");
            }
        }
        int numCoefficients = numEquations + 1;
        ArrayList<ArrayList<Integer>> rowLists = new ArrayList<ArrayList<Integer>>();
        userInput.nextLine();   // shift to prevent counting error from previous quesitons.
        for (int i = 0; i < numEquations; i++){
            while (true) {
                try {
                    System.out.println("Enter each coefficient in row " + i + ". Ex: 2x+3y+0z=8 = \"2 3 0 8\".");
                    String currentLine = userInput.nextLine();
                    Scanner coefficientScanner = new Scanner(currentLine);
                    ArrayList coefficientList = new ArrayList();
                    while (coefficientScanner.hasNextInt()){
                        coefficientList.add(coefficientScanner.nextInt());
                    }
                    if (coefficientList.size() > numCoefficients){
                        System.out.println("Error: Incorrect number of coefficients. Expected: " + numCoefficients + " got: " + coefficientList.size());
                        continue;
                    }
                    rowLists.add(coefficientList);
                    break;
                } catch (Exception e) {
                    System.out.println("Error: Unreadable Input. Please try again (inputs MUST be integers!)");
                }
            }
        }
        int cols = rowLists.get(0).size();
        double[][] returnMatrix = new double[numEquations][cols];
        for (int y = 0; y < numEquations; y++){
            for (int x = 0; x < cols; x++){
                returnMatrix[y][x] = rowLists.get(y).get(x);
            }
        }
        return returnMatrix;
    }

    private static int[] getStartingSolutions(){
        userInput.nextLine();   // shift to prevent counting error from previous quesitons.
        int numSolutions = augmentedCoefficientMatrix.length;
        int[] returnStartingSolutions = new int[augmentedCoefficientMatrix.length];
        while (true) {
            try {
                System.out.println("Enter the starting solutions in the format, \"0 0 0\"");
                String currentLine = userInput.nextLine();
                Scanner coefficientScanner = new Scanner(currentLine);
                for (int i = 0; i < returnStartingSolutions.length; i++){
                    if (coefficientScanner.hasNextInt()){
                        returnStartingSolutions[i] = coefficientScanner.nextInt();
                    }
                }
                break;
            } catch (Exception e) {
                System.out.println("Error: Unreadable Input. Please try again (inputs MUST be integers!)");
            }
        }
        return returnStartingSolutions;
    }

    /***
     * Asks users for the name of a file
     * @return a matrix of the coefficients if the file is found.
     */
    private static double[][] getACMFromFile(){
        ArrayList<ArrayList<Integer>> rowLists = new ArrayList<ArrayList<Integer>>();
        while (true){
            System.out.println("Enter the name of the file");
            String fileName = userInput.nextLine();
            try {
                FileReader fileReader = new FileReader(fileName);
                Scanner fileScanner = new Scanner(fileReader);
                while (fileScanner.hasNextLine()){
                    String currentLine = fileScanner.nextLine();
                    ArrayList coefficientList = new ArrayList();
                    Scanner coefficientScanner = new Scanner(currentLine);
                    while (coefficientScanner.hasNextInt()){
                        coefficientList.add(coefficientScanner.nextInt());
                    }
                    rowLists.add(coefficientList);
                }
                fileReader.close();
                break;
            } catch (IOException e){
                System.out.println("Error: Unable to get file: " + fileName + ", please try again.");
            }
        }
        int rows = rowLists.size();
        int cols = rowLists.get(0).size();
        double[][] returnMatrix = new double[rows][cols];
        for (int y = 0; y < rows; y++){
            for (int x = 0; x < cols; x++){
                returnMatrix[y][x] = rowLists.get(y).get(x);
            }
        }
        return returnMatrix;
    }


}
