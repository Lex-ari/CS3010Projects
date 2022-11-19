import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
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

    private static double[] bisectionOnce(double a, double b, function function, double stoppingError){
        return new double[0];
    }

    private static double[] newtonRaphsonOnce(double xn, function function, double stoppingError){
        return new double[0];
    }

    private static double[] falsePositionOnce(double a, double b, function function, double stoppingError){
        return new double[0];
    }

    private static double[] secantOnce(double xnminus1, double x, function function, double stoppingError){
        return new double[0];
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

    interface function {
        public double getValue(double x);
        public double getFirstDerivative(double x);
    }

    class functionOne implements function {
        //f(x)=2x^3-11.7x^2+17.7x-5
        public double getValue(double x){
            return x;
        }

        public double getFirstDerivative(double x){
            return x;
        }
    }

    class functionTwo implements function {
        //f(x)=x+10-xcosh(50/x)
        public double getValue(double x){
            return x;
        }
        public double getFirstDerivative(double x){
            return x;
        }
    }
}
