package quinn_mchugh.clausius.Tables;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a CSVFile that parses CSV files into
 */
public class CSVFile {

    protected InputStream inputStream;
    private HashMap<String, List<Double>> data;

    public CSVFile(InputStream inputStream) {
        this.inputStream = inputStream;
        //data = readDataWithHeaders();
    }

    /**
     * Reads data from a CSV file. Assumes column headers are the first line of the file.
     *
     * @return A HashMap with the CSV file column headers as the keys and the column data stored as ArrayLists as the values.
     */
    /*public HashMap<String, List<Double>> readData() {
        HashMap<String, List<Double>> data;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            data = new HashMap<String, List<Double>>();
            try {
                // Store headers in CSV file as keys in HashMap
                String line = reader.readLine();
                String[] headers = line.split(",");
                for (String header : headers) {
                    data.put(header, new ArrayList<Double>());
                }

                // Store all subsequent data into ArrayLists
                while ((line = reader.readLine()) != null) {
                    String[] tokens = line.split(",");
                    Double[] dblTokens = toDoubleArray(tokens);
                    for (int i = 0; i < tokens.length; i++) {
                        data.get(headers[i]).add(dblTokens[i]);
                    }
                }
            }
            catch (Exception e) {
                throw new RuntimeException("Error reading line in CSV file.", e);
            }
            finally {
                reader.close();
            }
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to read CSV file.", e);

        }
        return data;
    }*/

    public Double[] toDoubleArray(String[] strArr) {
        Double[] dblArr = new Double[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            try {
                dblArr[i] = Double.parseDouble(strArr[i]);
            }
            catch (NumberFormatException e) {
                // String is not parsable
            }
        }
        return dblArr;
    }

    public Integer getIndexOfHeader(String header, String[] headers) {
        int i = 0;
        for (String str : headers) {
            if (str.equals(header)) {
                return i;
            }
            i++;
        }
        return null;
    }

    /**
     * Uses proportions to estimate the value of a thermodynamic property at a desired thermodynamic
     * state, using the value of another property of that state and two sets of property values
     * at two other states that are immediately lower and higher than the desired state.
     *
     * @param prop1LowerVal The value of property 1 (e.g. temperature) at the lower state.
     * @param prop1HigherVal The value of property 1 (e.g. temperature) at the higher state.
     * @param prop1Val  The value of property 1 (e.g. temperature) at the state where property 2 (e.g. entropy) will be estimated.
     * @param prop2LowerVal The value of property 2 (e.g. entropy) at the lower state.
     * @param prop2HigherVal The value of property 2 (e.g. entropy) at the higher state.
     * @return The estimated value of property 2 (e.g. entropy) at the desired state.
     */
    public double estimatePropertyValue(double prop1LowerVal, double prop1HigherVal, double prop1Val, double prop2LowerVal, double prop2HigherVal) {
        double percentage = Math.abs((prop1Val - prop1LowerVal) / (prop1HigherVal - prop1LowerVal));
        return percentage * (prop2HigherVal - prop2LowerVal) + prop2LowerVal;
    }
}
