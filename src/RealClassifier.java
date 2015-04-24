import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Andrew on 4/24/2015.
 */
public class RealClassifier extends Classifier {

    public double[] thetaValues;
    double theta;
    public String names_file;  // file name for the name file
    public HashMap<String, ArrayList<String>> fields;  // relates features to the allowed values
    public ArrayList<String> feature_order;  // order in which the features will appear in the .train file

    /**
     * Constructor: Initializes the classifier by reading in the .names file which lets the object
     * know what features of what types to expect
     */
    public RealClassifier(String namesFilepath) {
        this.names_file = namesFilepath;
        this.thetaValues = new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        this.theta = 0;
        this.fields = new HashMap<String, ArrayList<String>>();
        this.feature_order = new ArrayList<String>();
    }


    /**
     * Reads in the file at 'trainingDataFilepath' and trains this classifier on this training data
     */
    public void train(String trainingDataFilpath) {
    }

    /**
     * Makes predictions on new test data given at 'testDataFilepath'. Should print predictions to
     * standard output, one classification per line. Nothing else should be printed to standard output
     */
    public void makePredictions(String testDataFilepath) {
    }


    // reads the training file
    public ArrayList<int[]> read_training_file(String train_file) {
        int num_features = this.fields.keySet().size();
        ArrayList<int[]> training_data = new ArrayList<int[]>();
        BufferedReader Reader = null;
        try {
            Reader = new BufferedReader(new FileReader(train_file));
            String line = null;
            try {
                while ((line = Reader.readLine()) != null) {
                    if (line.isEmpty()) {
                        continue;  // ignore blank lines
                    } else {
                        int[] features = new int[num_features + 1];
                        String[] parts = line.split("\\s+");
                        int len = parts.length;
                        for (int i = 0; i < len; i++) {
                            if (i < num_features) {  // if its on of the features
                                String label = this.feature_order.get(i);
                                if (this.fields.get(label).size() == 1) { // if the field is numeric
                                    features[i] = Integer.parseInt(parts[i]);
                                } else {  // if the field is not numeric
                                    int index = this.fields.get(label).indexOf(parts[i]);
                                    features[i] = index;
                                }
                            } else {  // other wise it is the last element, the result
                                features[i] = parts[i].equals(">50K") ? 1 : 0;
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

    public double costFunction(ArrayList<int[]> dataList) {

        // Last element: if 1, greater than 50k, if 0, less than 50k
        if (dataList == null || dataList.size() == 0) {
            System.out.println("COULDN'T PARSE");
        }

        int m = dataList.size();
        double sum = 0;

        for (int i = 0; i < m; i++) {
            int[] row = dataList.get(i);
            int yVal = row[row.length - 1];  // FIX/VERIFY
            sum += Math.pow(hypothesisFunction(row) - yVal, 2);
        }
        return (1 / (2 * m)) * sum;
    }

    public double hypothesisFunction(int[] row) {
        return theta + thetaValues[0] * row[0] + thetaValues[1] * row[1] + thetaValues[2] * row[2] +
                thetaValues[3] * row[3] + thetaValues[4] * row[4] + thetaValues[5] * row[5] + thetaValues[6] * row[6] +
                thetaValues[7] * row[7] + thetaValues[8] * row[8] + thetaValues[9] * row[9] +
                thetaValues[10] * row[10] + thetaValues[11] * row[11] + thetaValues[12] * row[12];
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

}
