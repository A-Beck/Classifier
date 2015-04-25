/**
 * Created by Andrew on 4/24/2015.
 * This class just guesses the classification: 750 <=50K, 749 >50K
 */


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketPermission;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.FileReader;
import java.util.Random;

public class RandomClassifier extends Classifier {
    public double[] thetaValues;
    double theta;
    public String names_file;  // file name for the name file
    public HashMap<String, ArrayList<String>> fields;  // relates features to the allowed values
    public ArrayList<String> feature_order;  // order in which the features will appear in the .train file

    /**
     * Constructor: Initializes the classifier by reading in the .names file which lets the object
     * know what features of what types to expect
     */
    public RandomClassifier(String namesFilepath){
        this.names_file = namesFilepath;
        this.fields = new HashMap<String, ArrayList<String>>();
        this.feature_order = new ArrayList<String>();
    }


    /**
     * Reads in the file at 'trainingDataFilepath' and trains this classifier on this training data
     */
    public void train(String trainingDataFilpath){
        this.read_name_file();
        ArrayList<int []> training_data = read_data_file(trainingDataFilpath, 1);
    }

    /**
     * Makes predictions on new test data given at 'testDataFilepath'. Should print predictions to
     * standard output, one classification per line. Nothing else should be printed to standard output
     */
    public void makePredictions(String testDataFilepath){
        ArrayList<int []> test_data = read_data_file(testDataFilepath, 0);
        Random rand = new Random();
        for (int i = 0; i < test_data.size(); i++){
            //int[] arr = test_data.get(i);
            Double val = rand.nextDouble();
            if (val < 0.5) System.out.println(">50k");
            else System.out.println("<=50K");
        }
    }

    // reads the data file
    // if flag == 0, it is a test file
    // if flag ==1, it is a training file
    public ArrayList<int []> read_data_file(String train_file, int flag) {
        int num_features = this.fields.keySet().size();
        ArrayList<int []> training_data = new ArrayList<int []>();
        BufferedReader Reader = null;
        try {
            Reader = new BufferedReader( new FileReader(train_file));
            String line = null;
            try {
                while ((line = Reader.readLine()) != null) {
                    if (line.isEmpty()) {
                        continue;  // ignore blank lines
                    }
                    else {
                        int[] features = new int[num_features + flag];
                        String[] parts = line.trim().split("\\s+");
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
                            } else {  // otherwise it is the last element, the result
                                // should only enter this section if it is train data file, should not enter if
                                // test data file
                                features[i] = parts[i].equals(">50K") ? 1 : 0;
                            }
                        }
                        training_data.add(features);
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

        return training_data;
    }

    public void read_name_file() {
        BufferedReader Reader = null;
        try {
            Reader = new BufferedReader( new FileReader(this.names_file ));
            String line = null;
            try {
                while ((line = Reader.readLine()) != null) {
                    if (line.isEmpty()) {
                        continue;  // ignore blank lines
                    }
                    else {
                        String[] parts = line.trim().split("\\s+");
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

    public static void main(String [] args) {
        String file = "C:\\Users\\student\\Classifier\\Training_Data\\census.names";
        String train_file = "C:\\Users\\student\\Classifier\\Training_Data\\train1.train";
        String test_file = "C:\\Users\\student\\Classifier\\Training_Data\\test1.test";
        RandomClassifier MyClassifier = new RandomClassifier(file);
        MyClassifier.train(train_file);
        MyClassifier.makePredictions(test_file);

    }

}
