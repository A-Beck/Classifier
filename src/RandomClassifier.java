/**
 * Created by Andrew on 4/24/2015.
 */


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketPermission;
import java.util.HashMap;
import java.io.FileReader;

public class RandomClassifier extends Classifier {
    public String names_file;
    public HashMap<String, String> fields;

    /**
     * Constructor: Initializes the classifier by reading in the .names file which lets the object
     * know what features of what types to expect
     */
    public RandomClassifier(String namesFilepath){
        this.names_file = namesFilepath;
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

    public static void main(String [] args) {
        String file = "C:\\Users\\student\\Classifier\\Training_Data\\census.names";
        RandomClassifier MyClassifier = new RandomClassifier(file);
        MyClassifier.read_name_file();

    }

}
