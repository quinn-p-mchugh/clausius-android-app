package quinn_mchugh.clausius.Tables;

import java.io.InputStream;

/**
 * Represents a typical CSV file.
 */
public class CSVFile {

    protected InputStream inputStream;

    /**
     * Required public constructor for CSVFile class.
     *
     * @param inputStream The input stream used to read the CSV file
     */
    public CSVFile(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    /**
     * Converts a array of strings to an array of doubles by attempting to parsing each element to
     * a double.
     *
     * @param strArr An array of strings to be converted
     * @return An array of Doubles
     */
    public Double[] toDoubleArray(String[] strArr) {
        Double[] dblArr = new Double[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            try {
                dblArr[i] = Double.parseDouble(strArr[i]);
            }
            catch (NumberFormatException e) {
                /* String is not parsable */
            }
        }
        return dblArr;
    }

    /**
     * Estimates the value of a thermodynamic property at an undefined state using linear interpolation.
     * https://en.wikipedia.org/wiki/Linear_interpolation
     *
     * Property values of states directly above and below the undefined state are used to estimate
     * the desired property value.
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
