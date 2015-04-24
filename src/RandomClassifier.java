/**
 * Created by Andrew on 4/24/2015.
 */


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketPermission;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.FileReader;

public class RandomClassifier extends Classifier {
    public String names_file;
    public HashMap<String, String> fields;
    public double[] thetaValues;
    double theta;

    /**
     * Constructor: Initializes the classifier by reading in the .names file which lets the object
     * know what features of what types to expect
     */
    public RandomClassifier(String namesFilepath){
        this.names_file = namesFilepath;
        this.thetaValues = new double[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        this.theta = 0;
    }

    public void read_name_file() {
        BufferedReader Reader = null;
        try {
            Reader = new BufferedReader( new FileReader(this.names_file ));
            String line = null;
            try {
                while ((line = Reader.readLine()) != null) {
                    if (line.isEmpty()) {
                        continue;
                    }  // ignore blank lines
                    else {
                        String[] parts = line.split(" ");
                        int len = parts.length;
                        for (String str : parts) {
                            System.out.print(str);
                            System.out.print(",");
                        }
                        System.out.println("");

                    }
                }
            }
            catch (IOException e) {}

        }
        catch (FileNotFoundException e) {}
        finally {
            if (Reader != null) {
                try {
                    Reader.close();
                }
                catch (IOException e) {}
            }
        }
    }

    /**
     * Reads in the file at 'trainingDataFilepath' and trains this classifier on this training data
     */
    public void train(String trainingDataFilpath){}

    /**
     * Makes predictions on new test data given at 'testDataFilepath'. Should print predictions to
     * standard output, one classification per line. Nothing else should be printed to standard output
     */
    public void makePredictions(String testDataFilepath){}

    public double costFunction(ArrayList<int[]> dataList) {

        // Last element: if 1, greater than 50k, if 0, less than 50k
        if (dataList == null || dataList.size() == 0) {
            System.out.println("COULDN'T PARSE");
        }

        int m = dataList.size();
        double sum = 0;

        for (int i = 0; i < m; i++) {
            int[] row = dataList.get(i);
            int[] yVal = row[row.length]; // FIX/VERIFY
            sum += Math.pow(hypothesisFunction(row - yVal), 2);
        }
        return (1 / (2 * m)) * sum;
    }

    public double hypothesisFunction(int[] row) {
        return theta + thetaValues[0] * row[0] + thetaValues[1] * row[1] + thetaValues[2] * row[2] +
                thetaValues[3] * row[3] + thetaValues[4] * row[4] + thetaValues[5] * row[5] + thetaValues[6] * row[6] +
                thetaValues[7] * row[7] + thetaValues[8] * row[8] + thetaValues[9] * row[9] +
                thetaValues[10] * row[10] + thetaValues[11] * row[11] + thetaValues[12] * row[12];
    }

    public static void main(String [] args) {
        String file = "C:\\Users\\student\\Classifier\\Training_Data\\census.names";
        RandomClassifier MyClassifier = new RandomClassifier(file);
        MyClassifier.read_name_file();

    }

}
