/*
    Eric Chen
    CS3010 Assignment 4: Newton's Interpolation Algorithm

    Command line arguments java newtInterp.java [-rand (optional)] [(int) number of ordered pairs (only if -rand called)] [file name]
*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;
import java.util.Random;

public class newtInterp {
    
    static int n;

    public static void Coeff(double[] xs, double[] ys, double[] coeff){
        n = xs.length;

        for(int i = 0; i < n; i++){
            coeff[i] = ys[i];
        }

        for(int j = 1; j < n; j++){
            //System.out.println("j is "+ j);
            for(int i = n - 1; i > j - 1; i--){
                
                coeff[i] = ((coeff[i] - coeff[i-1]) / (xs[i] - xs[i - j]));
            }
        }
    }

    // z is the point evaluating at
    public static double EvalNewton(double[] xs, double[] ys, double[] coeff, double z, String input){
        long coeffStart = 0;
        long coeffEnd = 0;
        long evalStart = 0;
        long evalEnd = 0;

        long fillStart = 0;
        long fillEnd = 0;

        fillStart = System.nanoTime();
        fillArray(xs, ys, input);
        
        //Timing calculating coefficent array
        coeffStart = System.nanoTime();
        Coeff(xs, ys, coeff);
        coeffEnd = System.nanoTime();
        System.out.printf("The time to compute the coefficients array is: %.2f nanoseconds", (double)((double)coeffEnd - (double)coeffStart));

        double result = 0;
        result = coeff[xs.length - 1];

        evalStart = System.nanoTime();
        for(int i = xs.length - 2; i >= 0; i--){
            //System.out.println("result is: "+ result);
            result = (result * (z - xs[i])) + coeff[i];
        }
        evalEnd = System.nanoTime();
        System.out.printf("\nThe time to evaluate the data is: %.2f nanoseconds", (double)((double)evalEnd - (double)evalStart));
        return result;
    }

    //Fill x and y arrays

    public static void fillArray(double[] xs, double[] ys, String input){

        try{
            BufferedReader file = new BufferedReader(new FileReader(System.getProperty("user.dir").concat("/" + input)));
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

    public static void randFileGen(String fileName, int n){
        Random rand = new Random();
        double randX = 0;
        double randY = 0;
        double[] xValues = new double[n];
        double[] yValues = new double[n];

        //Fills X & Y arrays of size n with random floating point numbers. Checks X values for duplicates.
        for(int i = 0; i<n; i++){
            randX = -100 + rand.nextInt(200) + rand.nextDouble();
            xValues[i] = randX;
            while(i > 0 && (xValues[i - 1] == xValues[i])){
                randX = -100 + rand.nextInt(200) + rand.nextDouble();
                xValues[i] = randX;
            }

            randY = -100 + rand.nextInt(200) + rand.nextDouble();
            yValues[i] = randY;
            while(i > 0 && (yValues[i - 1] == yValues[i])){
                randY = -100 + rand.nextInt(200) + rand.nextDouble();
                yValues[i] = randY;
                System.out.println(yValues[i]);
            }
        }

        try{
            FileWriter writer = new FileWriter(System.getProperty("user.dir").concat("/" + fileName));
            for(int i = 0; i<n; i++){
                writer.write(xValues[i] + "\t");
            }
            writer.write("\n");
            for(int i = 0; i<n; i++){
                writer.write(yValues[i] + "\t");
            }
            writer.close();
        }

        catch(Exception e){
            e.getStackTrace();
            System.out.println("Random number generator could not produce file.");
        }
        
    }
    public static void main(String[] args) {
        int n = 0; //Number of ordered pairs
        String userIn = ""; //User input (To enable q to quit)
        double z = 0; // X value that is evaluated - First argument taken
        String inputFile;
        boolean fileGen = false;
        long timeStart = 0;
        long timeEnd = 0;

        // Command line input for input file
        if(args.length == 1){
            inputFile = args[0];  //If only 1 argument, the input file is the first entry
        }
        else{
            inputFile = args[args.length - 1]; //If multiple arguments, input file is the last entry
        }

        //Keep prompting user to enter in x values until they enter q to quit
        while(!userIn.equals('q') || !userIn.equals('Q')){
            //Take user input for z
            Scanner sc = new Scanner(System.in);
            System.out.print("Enter the x point to evaluate at (q to exit): ");
            userIn = sc.nextLine();
            //Make sure input is not empty
            while(userIn.isEmpty()){
                System.out.printf("\nBlank input. Please enter a number (q to exit): ");
                userIn = sc.nextLine();
            }
            if(userIn.equals("q") || userIn.equals("Q") ){
                return;
            }
            z = Double.parseDouble(userIn);

            //If the user uses flag -rand, example command line input: -rand 5 randGen.pnt
            if(args[0].equals("-rand")){
                n = Integer.parseInt(args[1]);
                //Check to make sure file is only randomized once.
                if(!fileGen){
                    randFileGen(args[args.length -1], n);
                    fileGen = true;
                }
                double[] xs = new double[n];
                double[] ys = new double [n];
                double[] coeff = new double[n];
                System.out.println("The answer is: "+EvalNewton(xs, ys, coeff, z, inputFile));
            } else{

            //Find n by reading number of inputs in each line of file
            try{
                BufferedReader file = new BufferedReader(new FileReader(System.getProperty("user.dir").concat("/" + inputFile)));
                String xVals = file.readLine();
                String yVals = file.readLine();
                //Split string of x and y values into array
                String [] xSplit = xVals.trim().split("\\s+");
                String [] ySplit = yVals.trim().split("\\s+");

                //Validate that each x and y value have a pair
                if(xSplit.length != ySplit.length){
                    System.out.println("Error. File does not have the same number of x and y values.");
                    return;
                }
                
                n = xSplit.length;
                file.close();
            }

            catch(Exception e){
                e.printStackTrace();
                System.out.println("Error accessing file.");
            }

            //Initialize arrays of size n
            double[] xs = new double[n];
            double[] ys = new double [n];
            double[] coeff = new double[n];

            timeStart = System.nanoTime();
            //If no -rand, run program with normal input file
            System.out.println("\nThe interpolated value is: "+EvalNewton(xs, ys, coeff, z, inputFile));
            timeEnd = System.nanoTime();
            //System.out.printf("The interpolation calculation took: %d nanoseconds\n", (double)(timeEnd - timeStart));
            System.out.println("Successfully interpolated the point. Enter another point to evaluate or exit with q.\n");
            }
        }   
    }
}
