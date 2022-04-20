/*
    * Eric Chen
    * CS3010 Assignment 4
*/

import java.io.BufferedReader;
import java.io.FileReader;

public class newtInterp {
    
    public static void Coeff(double[] xs, double[] ys, double[] coeff){
        int n = xs.length;

        for(int i = 0; i < n; i++){
            coeff[i] = ys[i];
        }

        for(int j = 1; j < n; j++){
            System.out.println("j is "+ j);
            for(int i = n - 1; i > j - 1; i--){
                
                coeff[i] = ((coeff[i] - coeff[i-1]) / (xs[i] - xs[i - j]));
            }
        }
    }

    // z is the point evaluating at
    public static double EvalNewton(double[] xs, double[] ys, double[] coeff, double z){

        fillArray(xs, ys);
        Coeff(xs, ys, coeff);
        for(int i = 0; i< xs.length; i++){
            System.out.printf("Point %d is: (%f,%f)",i+1,xs[i],ys[i]);
        }
        for(int i = 0; i< xs.length; i++){
            System.out.printf("\nCoeff %d is: (%f)",i+1,coeff[i]);
        }

        double result = 0;
        result = coeff[xs.length - 1];

        for(int i = xs.length - 2; i >= 0; i--){
            //System.out.println("result is: "+ result);
            result = (result * (z - xs[i])) + coeff[i];
        }

        return result;
    }

    //Fill x and y arrays

    public static void fillArray(double[] xs, double[] ys){

        try{
            BufferedReader file = new BufferedReader(new FileReader(System.getProperty("user.dir").concat("/example.pnt")));
            String xVals = file.readLine();
            String yVals = file.readLine();
            //Split string of x and y values into array
            String [] xSplit = xVals.trim().split("\\s+");
            String [] ySplit = yVals.trim().split("\\s+");

            //Fill xs array with x values
            for (int i = 0; i < xSplit.length; i++) {
                xs[i] = Double.parseDouble(xSplit[i]);
            }

            //Fill ys array with y values
            for (int i = 0; i < ySplit.length; i++) {
                ys[i] = Double.parseDouble(ySplit[i]);
            }

            file.close();
        }

        catch(Exception e){
            e.printStackTrace();
            System.out.println("Error accessing file.");
        }

    }
    public static void main(String[] args) {

        double[] xs = new double[5];
        double[] ys = new double [5];
        double[] coeff = new double[5];
        System.out.println("\nThe answer is: "+EvalNewton(xs, ys, coeff, 2));
    }
}
