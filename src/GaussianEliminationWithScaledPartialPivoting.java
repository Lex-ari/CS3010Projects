import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class GaussianEliminationWithScaledPartialPivoting {

    private static double[][] augmentedCoefficientMatrix;
    private static Scanner userInput = new Scanner(System.in);
    private ArrayList<Integer> indexVectors = new ArrayList();
    private static int[] scaleVectors;
    public static void main(String[] args){
        while (true){
            System.out.println("Enter \"File\" to input filename, enter \"Manual\" to input coefficients manually.");
            String initialString = userInput.nextLine();
            if (initialString.toLowerCase().equals("file")){
                augmentedCoefficientMatrix = getACMFromFile();
                break;
            } else if (initialString.toLowerCase().equals("manual")){
                augmentedCoefficientMatrix = getACMFromUser();
                break;
            } else {
                System.out.println("Input received does not match \"file\" or \"manual\", Please try again.");
            }
        }
        System.out.println("Solving: ");
        for (double[] row: augmentedCoefficientMatrix){
            System.out.println(Arrays.toString(row));
        }
        scaleVectors = getMaxOfAugmentedCoefficientMatrix(); // Setting scale vectors, the max coefficient of each equation
        ArrayList<Integer> initialIndexVectors = new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3));
        gaussianSolver(0, initialIndexVectors);
    }

    /***
     * Recursively solves for all values of equations. Modifies augmentedCoefficientMatrix.
     * @param indexVectors an ArrayList containing the index of equations that hasn't been solved yet.
     * @param step step containing number of iterations starting at 0.
     * @return double array containing the solved values of x1, x2, etc...
     */
    private static double[] gaussianSolver(int step, ArrayList<Integer> indexVectors){
        int workingIndex = getLargestIndexFromIndexVectors(step, indexVectors);
        for (int indexVector : indexVectors){
            double multiplier = augmentedCoefficientMatrix[indexVector][step] / augmentedCoefficientMatrix[workingIndex][step];
            for (int i = step; i < augmentedCoefficientMatrix[0].length; i++){
                if (indexVector != workingIndex){
                    augmentedCoefficientMatrix[indexVector][i] -= augmentedCoefficientMatrix[workingIndex][i] * multiplier;
                }
            }
        }

        return new double[0]; // STUB
    }

    /***
     * Gets index with the largest ratio of "current step" coefficient with scale vector.
     * @param step current coefficient working on, starts from 0.
     * @param indexVectors ArrayList of index of equations to work with (not ones that have been iterated through).
     * @return integer index containing largest ratio.
     */
    private static int getLargestIndexFromIndexVectors(int step, ArrayList<Integer> indexVectors){
        double max = 0f;
        int returnIndex = -1;
        for (int indexVector : indexVectors){
            double testingValue = Math.abs(augmentedCoefficientMatrix[indexVector][step]) / scaleVectors[indexVector];
            if (testingValue > max){
                max = testingValue;
                returnIndex = indexVector;
            }
        }
        return returnIndex; // STUB
    }

    /***
     * Gets maximum vector (regardless of sign) of augmentedCoefficientMatrix. Method is only called once where all values are integers.
     * @return int[] containing maximum vector of each row.
     */
    private static int[] getMaxOfAugmentedCoefficientMatrix(){
        int rows = augmentedCoefficientMatrix.length;
        int[] returnVectors = new int[rows]; //initialized with 0s
        for (int row = 0; row < rows; row++){
            double[] currentRow = augmentedCoefficientMatrix[row];
            for (int indexVector = 0; indexVector < currentRow.length - 1; indexVector++){
                int absoluteIndexVector = (int) Math.abs(currentRow[indexVector]);
                if (returnVectors[row] < absoluteIndexVector){
                    returnVectors[row] = absoluteIndexVector;
                }
            }
        }
        return returnVectors;
    }

    /***
     * Asks user to input number of equations and then input each row for the number of equations.
     * @return a matrix of the coefficients if user gives correct input of coefficients.
     */
    private static double[][] getACMFromUser(){
        int numEquations = 0;
        while (true){
            try {
                System.out.println("Please enter the number of equations used for the Gaussian elimination with Scaled Partial Pivoting");
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
