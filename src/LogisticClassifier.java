import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Andrew on 4/25/2015.
 * Implements Logisitic classification
 */

public class LogisticClassifier extends Classifier {

    public static void main(String [] args) {
        String file = "/Users/muntaserahmed/Desktop/4thYear/8thSemester/CS4710/hw/Classifier/Training_Data/census.names";
        String train_file = "/Users/muntaserahmed/Desktop/4thYear/8thSemester/CS4710/hw/Classifier/Training_Data/train1.train";
        String test_file = "/Users/muntaserahmed/Desktop/4thYear/8thSemester/CS4710/hw/Classifier/Training_Data/test1.test";
        LogisticClassifier MyClassifier = new LogisticClassifier(file);
        MyClassifier.train(train_file);
        MyClassifier.makePredictions(test_file);
    }

    public ArrayList<double[]> thetaValues;
    double theta;
    public String names_file;  // file name for the name file
    public HashMap<String, ArrayList<String>> fields;  // relates features to the allowed values
    public ArrayList<String> feature_order;  // order in which the features will appear in the .train file

    /**
     * Constructor: Initializes the classifier by reading in the .names file which lets the object
     * know what features of what types to expect
     */
    public LogisticClassifier(String namesFilepath) {
        this.names_file = namesFilepath;
        this.theta = 0;
        this.fields = new HashMap<String, ArrayList<String>>();
        this.feature_order = new ArrayList<String>();
    }


    /**
     * Reads in the file at 'trainingDataFilepath' and trains this classifier on this training data
     */
    public void train(String trainingDataFilepath) {
        this.read_name_file();
        this.initiailize_thetas();
        ArrayList<ArrayList<int[]>> data = this.read_data_file(trainingDataFilepath, 1);
        // loop until satisfied
            // get predictions --> get results from cost fn
            // run gradient decent --> fix thetas

        for (int i = 0; i < 1000; i++) {
            gradient(data);
            int y = 1 + 1;
        }
    }

    /**
     * Makes predictions on new test data given at 'testDataFilepath'. Should print predictions to
     * standard output, one classification per line. Nothing else should be printed to standard output
     */
    public void makePredictions(String testDataFilepath) {
        ArrayList<ArrayList<int[]>> data = this.read_data_file(testDataFilepath, 0);
        // for all rows, run hypothesis fn
            // print result
        for (int i = 0; i < data.size(); i++) {
            double pred = hypothesisFunction(data.get(i));
            if (pred > .5) {
                System.out.println(">50K");
            }
            else {
                System.out.println("<=50K");
            }
        }
    }

    // initialize the theta values with random #'s between 1 and 0
    public void initiailize_thetas() {
        Random rand = new Random();
        this.thetaValues = new ArrayList<double[]>();
        this.thetaValues.add(new double[]{rand.nextDouble()});  // theta 0
        for (String label : this.feature_order){
            int num_values = this.fields.get(label).size();
            double[] thetas = new double[num_values];
            for (int i = 0; i < num_values; i++) {
                thetas[i] = rand.nextDouble();
            }
            this.thetaValues.add(thetas);
        }
        System.out.println("Finished init thetas");
    }

    // reads the training file
    // flag == 1 --> read training data
    // flag == 0 --> read test data
    public  ArrayList<ArrayList<int[]>> read_data_file(String train_file, int flag) {
        int num_features = this.fields.keySet().size();
        ArrayList<ArrayList<int[]>> training_data = new  ArrayList<ArrayList<int[]>>();
        BufferedReader Reader = null;
        try {
            Reader = new BufferedReader(new FileReader(train_file));
            String line = null;
            try {
                while ((line = Reader.readLine()) != null) {
                    if (line.isEmpty()) {
                        continue;  // ignore blank lines
                    } else {
                        ArrayList<int[]> features = new ArrayList<int[]>();
                        String[] parts = line.split("\\s+");
                        int len = parts.length;
                        for (int i = 0; i < len; i++) {
                            if (i < num_features) {  // if its on of the features
                                String label = this.feature_order.get(i);
                                int num_values = this.fields.get(label).size();
                                if (num_values == 1) { // if the field is numeric
                                    int [] feat = new int[1];
                                    feat[0] = Integer.parseInt(parts[i]);
                                    features.add(feat);
                                } else {  // if the field is not numeric
                                    int [] feat = new int[num_values];
                                    int index = this.fields.get(label).indexOf(parts[i]);
                                    feat[index] = 1;
                                    features.add(feat);
                                }
                            }  else if (flag == 1) {  // otherwise it is the last element, the result
                                // only process if reading the training file
                                int val = parts[i].equals(">50K") ? 1 : 0;
                                int [] feat = new int[1];
                                feat[0] = val;
                                features.add(feat);
                            }
                        }
                        training_data.add(features);
                    }
                }

            } catch (IOException e) {
            }

        } catch (FileNotFoundException e) {
        } finally {
            if (Reader != null) {
                try {
                    Reader.close();
                } catch (IOException e) {
                }
            }

        }

        return training_data;
    }

    public void read_name_file() {
        BufferedReader Reader = null;
        try {
            Reader = new BufferedReader(new FileReader(this.names_file));
            String line = null;
            try {
                while ((line = Reader.readLine()) != null) {
                    if (line.isEmpty()) {
                        continue;  // ignore blank lines
                    } else {
                        String[] parts = line.split("\\s+");
                        int len = parts.length;
                        if (len > 0) {
                            String label = parts[0];
                            if (label.equals(">50K")) {
                                continue; // not a field, just the available options
                            }
                            this.feature_order.add(label);
                            this.fields.put(label, new ArrayList<String>());
                            for (int i = 1; i < len; i++) {
                                this.fields.get(label).add(parts[i]);
                            }
                        }
                    }
                }
            } catch (IOException e) {
            }
        } catch (FileNotFoundException e) {
        } finally {
            if (Reader != null) {
                try {
                    Reader.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public double sigmoid(double x) {
        double den = 1 + Math.pow(Math.exp(1.0), -1 * x);
        double d = 1 / den;
        return d;
    }

    public double hypothesisFunction(ArrayList<int[]> row) {
        double sum = this.thetaValues.get(0)[0]; // sum = theta_0
        for (int i = 1; i < thetaValues.size(); i++) {
            for (int j = 0; j < thetaValues.get(i).length; j++) {
                sum += thetaValues.get(i)[j] * row.get(i-1)[j];
            }
        }
        return sigmoid(sum);
    }

    public double costFunction(ArrayList<double[]> thetaVector, ArrayList<ArrayList<int[]>> rows) {
        double m = rows.size();
        double cost = 0;
        for (int i = 0; i < m; i++) {
            int realValue = rows.get(i).get(rows.get(i).size() - 1)[0];
            cost += ( (-1 * realValue) * Math.log(hypothesisFunction(rows.get(i)))
                    - (1 - realValue) * Math.log(1 - hypothesisFunction(rows.get(i))));
        }

        return cost / m;
    }

    public void gradient(ArrayList<ArrayList<int[]>> rows) {
        double learning_rate = 0.6;  // TODO find reasonable value
        double m = rows.size();
        for (int j = 1; j < thetaValues.size(); j++) {
            for (int k = 0; k < thetaValues.get(j).length; k++) {
                double gradient = 0;
                for (int i = 0; i < rows.size(); i++) {
                    int realValue = rows.get(i).get(rows.get(i).size() - 1)[0];
                    gradient += (hypothesisFunction(rows.get(i)) - realValue) * rows.get(i).get(j-1)[k];
                }
                thetaValues.get(j)[k] = thetaValues.get(j)[k] - learning_rate * (gradient / m );
            }
        }
    }



}
