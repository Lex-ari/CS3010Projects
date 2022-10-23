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
        userInput.nextLine();
        System.out.println("Solving: ");
        double[] jacobiSolutions = intArrayToDoubleArray(startingSolutions);

    }

    private static double[] intArrayToDoubleArray(int[] intArray){
        double[] returnDoubleArray = new double[intArray.length];
        for (int i = 0; i < intArray.length; i++){
            returnDoubleArray[i] = intArray[i];
        }
        return returnDoubleArray;
    }

    private static double[] jacobiIterateOnce(){
        // Let Ax1 + Bx2 + Cx3 = D  (times 3 different rows)
        // Then x1 = 1/A * (D - Bx2 - Cx3)  (This equation is used for commenting)
        //other ex:
        // x2 = 1/B (D - Ax2 - Cx3)
        // x3 = 1/C (D - Ax2 - Bx2)
        double[] newSolutions = new double[augmentedCoefficientMatrix.length];
        for (int row = 0; row < augmentedCoefficientMatrix.length; row++){
            double sum = augmentedCoefficientMatrix[row][augmentedCoefficientMatrix[row].length];   // D
            for (int col = 0; col < augmentedCoefficientMatrix[row].length - 1 && col != row; row++){
                sum -= col * startingSolutions[col];    // (D - Bx2 - Cx3)
            }
            newSolutions[row] = sum / startingSolutions[row];   // 1/A *
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
