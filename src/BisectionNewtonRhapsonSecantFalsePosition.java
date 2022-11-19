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
    private static final double DEFAULT_STOPPING_ERROR = 0.01;
    public static void main(String[] args){

        //Hardcoded
        //f(x)=2x^3-11.7x^2+17.7x-5
        //f(x)=x+10-xcosh(50/x)
        function firstEquation = new functionOne();
        function secondEquation = new functionTwo();
        function[] equations = new function[]{firstEquation, secondEquation};
        double[] values;
        double currentError;
        int iteration;
        for (function currentFunction : equations){
            values = new double[]{0, 4, currentFunction.getValue(0), currentFunction.getValue(1)};
            iteration = 0;
            currentError = (values[1] - values[0])/2;
            while (currentError > DEFAULT_STOPPING_ERROR && iteration <= 100 ){
                System.out.println(iteration + " " + Arrays.toString(values) + " " + currentError);
                currentError = (values[1] - values[0])/Math.pow(2, iteration + 2);
                values = bisectionOnce(values, currentFunction);
                iteration++;
            }
        }

    }

    // double values = {a, b, fa, fb, function, stoppingError}
    private static double[] bisectionOnce(double[] values, function function){
        double a = values[0];
        double b = values[1];
        double fa = values[2];
        double fb = values[3];
        double c = (b + a)/2.0;
        double fc = function.getValue(c);
        if (fa * fc >= 0){ // if f(a) and f(c) have same sign
            values = new double[]{c, b, fc, fb};
        } else {
            values = new double[]{fa, c, fc, fc};
        }
        return values;
    }

    private static double newtonRaphsonOnce(double xn, function function, double stoppingError){
        double fn = function.getValue(xn);
        double fprimen = function.getFirstDerivative(xn);
        return xn - fn / fprimen;
    }

    // double values = {a, b, fa, fb}
    private static double[] falsePositionOnce(double[] values, function function, double stoppingError){
        double a = values[0];
        double b = values[1];
        double fa = values[2];
        double fb = values[3];
        double c = (a*fb - b*fa)/(fb - fa);
        double fc = function.getValue(c);
        if (fa * fc >= 0){ // if f(a) and f(c) have same sign
            return new double[]{c, b, fc, fb};
        } else {
            return new double[]{a, c, fa, fc};
        }
    }

    // double values = {xn-1, xn, f(xn-1), f(xn)}
    private static double[] secantOnce(double[] values, function function, double stoppingError){
        double xnminus1 = values[0];
        double xn = values[1];
        double fxnminus1 = values[2];
        double fxn = values[3];
        double xnplus1 = xn - ((xn - xnminus1) / (fxn - fxnminus1)) * fxn;
        return new double[]{xn, xnplus1, fxn, function.getValue(xnplus1)};
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

    static class functionOne implements function {
        //f(x) = 2x^3 - 11.7x^2 + 17.7x - 5
        public double getValue(double x){
            return 2.0*x*x*x - 11.7*x*x + 17.7*x - 5.0;
        }

        //f'(x) = 6x^2 - 23.4x + 17.7
        public double getFirstDerivative(double x){
            return 6.0*x*x - 23.4*x + 17.5;
        }
    }

    static class functionTwo implements function {
        //f(x) = x + 10 - xcosh(50/x)
        public double getValue(double x) {
            return x + 10.0 - x * Math.cosh(50.0 / x);
        }
            //f'(x) = 1 - ((50 * sinh(50/x)) / x) + cosh(50/x)
        public double getFirstDerivative(double x){
            return 1.0 - (50.0*Math.sinh(50.0/x))/x + Math.cosh(50.0/x);
        }
    }
}
