package quinn_mchugh.clausius.Tables.Superheated_Region;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import quinn_mchugh.clausius.Tables.CSVFile;

/**
 * Represents a CSV file containing data related to the superheated region of the T-s diagram.
 */
public class SuperTable extends CSVFile {

    private Double[] pressures;     // The pressure values in the CSV file.
    private Double[] temperatures;  // The temperature values in the CSV file
    private Double[][] gridArr;     // The array of values bounded by the temperature and pressure values in CSV file.

    /**
     * Required public constructor for SuperTable class.
     *
     * @param inputStream The input stream used to read the CSV file
     * @param numColumns The number of pressure values in the CSV file
     */
    public SuperTable(InputStream inputStream, int numColumns) {
        super(inputStream);
        temperatures = new Double[700];
        pressures = new Double[numColumns];
        gridArr = new Double[700][numColumns];
        parseCSVFile();
    }

    public Double[] getPressures() {
        return pressures;
    }

    public Double[] getTemperatures() {
        return temperatures;
    }

    public Double[][] getGridArr() {
        return gridArr;
    }

    /* TODO: Write a program to parse CSV files and make them into Java array format, such that the super and saturated tables can be hardcoded into the application, as opposed to read from a CSV file each time the application loads. */
    /**
     * Reads data from the SatTable CSV file and stores it in appropriate data structures.
     */
    private void parseCSVFile() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            try {
                reader.readLine();  // Skip the first line in the CSV file

                /* Read pressure values from second line and store in an array of doubles */
                String line = reader.readLine();
                line = line.substring(line.indexOf(",")+1, line.length());  // Remove first element
                pressures = toDoubleArray(line.split(","));

                /* Process remaining data in CSV file */
                int row = 0;
                while ((line = reader.readLine()) != null) {
                    Double[] rowValues = toDoubleArray(line.split(","));

                    temperatures[row] = rowValues[0];   // Store first column value in temperatures array
                    /* Store all values not in the first column in the gridArr */
                    for (int i = 1; i < rowValues.length; i++) {
                        gridArr[row][i-1] = rowValues[i];
                    }
                    row++;
                }
            }
            catch (Exception e) {
                throw new RuntimeException("Error reading line in CSV file.", e);
            }
            finally {
                reader.close();
            }
            return;
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to read CSV file.", e);
        }
    }

    /**
     * Returns the index of an element in an array who's value is the
     * closest value greater than or equal to the specified value.
     *
     * @param value The specified value
     * @param array A one dimensional array sorted in ascending or descending order to be
     *      searched through
     * @return The index of the element in the array that is the closest value
     *      greater than or equal to the specified value.
     */
    protected Integer findHigherIndex(double value, Double[] array) {
        for (int i = 1; i < array.length; i++) {
            if ((array[i] >= value && array[i-1] < value) ||
                    (array[i] < value && array[i-1] >= value)) {
                return i;
            }
        }
        return null;
    }

    /**
     * Returns the index of an element in an array who's value is the
     * closest value less than the specified value.
     *
     * @param value The specified value
     * @param array A one dimensional array sorted in ascending order to be
     *      searched through
     * @return The index of the element in the array that is the closest value
     *      greater than or equal to the specified value.
     */
    protected int findLowerIndex(double value, Double[] array) {
        return findHigherIndex(value, array) - 1;
    }
}
