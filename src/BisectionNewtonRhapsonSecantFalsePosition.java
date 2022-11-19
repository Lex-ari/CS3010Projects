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
 * Programming Project 3
 */

public class BisectionNewtonRhapsonSecantFalsePosition {

    private static int[][] augmentedCoefficientMatrix;
    private static Scanner userInput = new Scanner(System.in);
    private static int[] startingSolutions;
    private static double stoppingError;
    public static void main(String[] args){

        //Hardcoded
        //f(x)=2x^3-11.7x^2+17.7x-5
        //f(x)=x+10-xcosh(50/x)
    }

    /**
     * Copies each value of a double[] array to a new double[] array
     * @param array double[] to copy from
     * @return double[] to copy to.
     */
    private static double[] copyArray(double[] array){
        double[] returnArray = new double[array.length];
        for (int i = 0; i < array.length; i++){
            returnArray[i] = array[i];
        }
        return returnArray;
    }

    /**
     * Method to convert integer to double array.
     * @param intArray int[] array
     * @return double[] array
     */
    private static double[] intArrayToDoubleArray(int[] intArray){
        double[] returnDoubleArray = new double[intArray.length];
        for (int i = 0; i < intArray.length; i++){
            returnDoubleArray[i] = intArray[i];
        }
        return returnDoubleArray;
    }
}
