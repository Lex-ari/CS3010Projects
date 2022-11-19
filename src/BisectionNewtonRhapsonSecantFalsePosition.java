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
    private static double error = 0;
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
            double a;
            double b;
            double xn;
            double xnminus1;

            //bisection
            while (true){
                System.out.println("Enter integers a and b for Bisection Method");
                a = getIntegerFromUser();
                b = getIntegerFromUser();
                if (uvCheck(a, b, currentFunction)){
                    break;
                }
            }
            values = new double[]{a, b, currentFunction.getValue(a), currentFunction.getValue(b)};
            iteration = 0;
            error = (values[1] - values[0]);
            System.out.printf("%3s %8s %8s %8s %8s %8s %8s %8s%n","i", "a", "b", "f(a)", "f(b)", "c", "f(c)", "error");
            do{
                error /= 2;
                System.out.printf("%3d ", iteration);
                values = bisectionOnce(values, currentFunction);
                System.out.printf("%8.4f%n", error);
                iteration++;
            } while (error > DEFAULT_STOPPING_ERROR && iteration <= 100 );
            System.out.println();

            //newton-raphson
            while (true){
                System.out.println("Enter integer xn for Newton-Raphson Method");
                xn = getIntegerFromUser();
                if (newtonCheck(xn, currentFunction)){
                    break;
                }
            }
            xnminus1 = xn;
            error = Math.abs(currentFunction.getValue(xn));
            System.out.printf("%3s %8s %8s %8s %8s %8s%n", "i", "xn", "f(xn)", "f'(xn)", "f(xn+1)", "error");
            iteration = 0;
            do {
                System.out.printf("%3d ", iteration);
                xn = newtonRaphsonOnce(xn, currentFunction);
                iteration++;

                if (!newtonRunawayCheck(xnminus1, xn)){
                    break;
                }
                xnminus1 = xn;
            } while (error > DEFAULT_STOPPING_ERROR && iteration <= 100 );
            System.out.println();

            //false position
            while (true){
                System.out.println("Enter integers a and b for False-Position Method");
                a = getIntegerFromUser();
                b = getIntegerFromUser();
                if (uvCheck(a, b, currentFunction)){
                    break;
                }
            }
            values = new double[]{a, b, currentFunction.getValue(a), currentFunction.getValue(b)};
            iteration = 0;
            error = (values[1] - values[0]);
            System.out.printf("%3s %8s %8s %8s %8s %8s %8s %8s%n","i", "a", "b", "f(a)", "f(b)", "c", "f(c)", "error");
            do{
                error /= 2;
                System.out.printf("%3d ", iteration);
                values = falsePositionOnce(values, currentFunction);
                System.out.printf("%8.4f%n", error);
                iteration++;
            } while (error > DEFAULT_STOPPING_ERROR && iteration <= 100 );
            System.out.println();

            //secant
            while (true){
                System.out.println("Enter integers xnminus1 and xn for Secant Method");
                xnminus1 = getIntegerFromUser();
                xn = getIntegerFromUser();
                if (div0Check(xnminus1, xn, currentFunction)){
                    break;
                }
            }
            values = new double[]{xnminus1, xn, currentFunction.getValue(xnminus1), currentFunction.getValue(xn)};
            iteration = 0;
            System.out.printf("%3s %8s %8s %8s %8s %8s %8s%n","i", "xn-1", "xn", "f(xn-1)", "f(xn)", "xn+1", "error");
            do{
                error = Math.abs(values[1]-values[0]);
                System.out.printf("%3d ", iteration);
                values = secantOnce(values, currentFunction);
                System.out.printf("%8.4f%n", error);
                iteration++;
            } while (error > DEFAULT_STOPPING_ERROR && iteration <= 100 );
        }
    }

    private static boolean uvCheck(double a, double b, function function){
        boolean test = function.getValue(a) * function.getValue(b) < 0;
        test = test && a != b;
        if (!test){
            System.out.println("Variables do not satisfy uv < 0! and a != b");
            return false;
        }
        return true;
    }

    private static boolean newtonCheck(double xn, function function){
        if (Math.abs(function.getFirstDerivative(xn)) < 1E-9){
            System.out.println("Flat Spot Error - Poor X0");
            return false;
        }
        return true;
    }
    private static boolean newtonRunawayCheck(double xnminus1, double xn){
        if (Math.abs(xn - xnminus1) > 1E3){
            System.out.println("Possible Runaway Detected - Poor X0");
            return false;
        }
        return true;
    }

    private static boolean div0Check(double a, double b, function function){
        if (Math.abs(function.getFirstDerivative(a) - function.getFirstDerivative(b)) < 1E-9){
            System.out.println("Div 0 - Bad Points");
            return false;
        }
        return true;
    }

    // double values = {a, b, fa, fb}
    private static double[] bisectionOnce(double[] values, function function){
        double a = values[0];
        double b = values[1];
        double fa = values[2];
        double fb = values[3];
        double c = (b + a)/2.0;
        double fc = function.getValue(c);
        printValues(values);
        System.out.printf("%8.4f %8.4f ", c, fc);
        if (fa * fc >= 0){ // if f(a) and f(c) have same sign
            values = new double[]{c, b, fc, fb};
        } else {
            values = new double[]{a, c, fc, fc};
        }
        return values;
    }
    // double values = {xn}
    private static double newtonRaphsonOnce(double xn, function function){
        double fn = function.getValue(xn);
        double fprimen = function.getFirstDerivative(xn);
        double xnplus1 = xn - fn / fprimen;
        error = Math.abs(function.getValue(xnplus1));
        System.out.printf("%8.4f %8.4f %8.4f %8.4f %8.4f%n", xn, fn, fprimen, xnplus1, error);
        return xnplus1;
    }

    // double values = {a, b, fa, fb}
    private static double[] falsePositionOnce(double[] values, function function){
        double a = values[0];
        double b = values[1];
        double fa = values[2];
        double fb = values[3];
        double c = (a*fb - b*fa)/(fb - fa);
        double fc = function.getValue(c);
        printValues(values);
        System.out.printf("%8.4f %8.4f ", c, fc);
        if (fa * fc >= 0){ // if f(a) and f(c) have same sign
            return new double[]{c, b, fc, fb};
        } else {
            return new double[]{a, c, fa, fc};
        }
    }

    // double values = {xn-1, xn, f(xn-1), f(xn)}
    private static double[] secantOnce(double[] values, function function){
        double xnminus1 = values[0];
        double xn = values[1];
        double fxnminus1 = values[2];
        double fxn = values[3];
        double xnplus1 = xn - ((xn - xnminus1) / (fxn - fxnminus1)) * fxn;
        printValues(values);
        System.out.printf("%8.4f, ", xnplus1);
        return new double[]{xn, xnplus1, fxn, function.getValue(xnplus1)};
    }

    private static void printVariables(int iteration, double[] values, double stoppingError){
        System.out.print(iteration + " ");
        for (double value : values){
            System.out.printf("%8.4f ", value);
        }
        System.out.printf("%8.4f%n", stoppingError);
    }

    private static void printValues(double[] values){
        for (double value : values){
            System.out.printf("%8.4f ", value);
        }
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


    /**
     * Method to get integer from the user
     * @return integer
     */
    private static double getIntegerFromUser(){
        //userInput.nextLine();   // shift to prevent counting error from previous questions.
        while (true) {
            try {
                System.out.println("Enter an integer");
                return userInput.nextDouble();
            } catch (Exception e) {
                System.out.println("Error: Unreadable Input. Please try again (input MUST be an integer!)");
            }
        }
    }
}
