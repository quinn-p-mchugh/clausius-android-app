package quinn_mchugh.clausius.Tables.Superheated_Region;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import quinn_mchugh.clausius.Tables.CSVFile;

/**
 * Represents the SuperTable
 */
public class SuperTable extends CSVFile {

    private Double[] pressures;     // The pressure values of the CSV file.
    private Double[] temperatures;  // The temperature values of the CSV file
    private Double[][] gridArr;     // The grid values of the CSV file.

    public SuperTable(InputStream inputStream) {
        super(inputStream);
        temperatures = new Double[700];
        pressures = new Double[700];
        gridArr = new Double[700][255];
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

    // TODO: Write a program to parse CSV files and make them into Java array format, such that the super and saturated tables can be hardcoded into the application, as opposed to read from a CSV file each time the application loads.
    /**
     * Reads data from the SatTable CSV file and stores it in appropriate data structures.
     */
    private void parseCSVFile() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            try {
                reader.readLine();  // Skip first line

                // Read pressure values from second line
                String line = reader.readLine();
                line = line.substring(line.indexOf(",")+1, line.length());  // Remove first element
                pressures = toDoubleArray(line.split(","));

                // Read temperature values from first line
                // and array of data bounded by temperature column and pressure row
                int row = 0;
                while ((line = reader.readLine()) != null) {
                    Double[] rowValues = toDoubleArray(line.split(","));

                    temperatures[row] = rowValues[0];
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
     * Returns the index of an element in an array who's value is the nearest
     * value that is greater than the user-specified value.
     *
     * @param value The specified value
     * @param array A one dimensional array sorted in ascending order to be
     *      searched through
     * @return The index of the element in the array that is the nearest value
     */
    private int findHigherValueIndex(double value, double[] array) {
        int i = 0;
        while (array[i] < value) {
            i++;
        }
        return i;
    }


    /**
     * Finds the temperature value in the temperatures array that is closest to
     * but not greater than the specified temperature.
     *
     * @param temperature The specified temperature
     */
    private double findLowerTemperature(double temperature) {
        //return temperatures[findLowerTemperatureIndex(temperature)];
        return 0;
    }


    /**
     * Finds the index of a temperature value in the temperatures array
     * that is the closest to but not great not greater than the specified
     * temperature.
     *
     * @param temperature
     * @param entropy
     * @return
     */
    private Integer findHigherEntropyIndex(double temperature, double entropy) {
        //int row = findNearestTemperatureIndex(temperature);
        int row = 0;
        int col = 1;
        while (gridArr[row][col] != null) {
            if (gridArr[row][col] < entropy && gridArr[row][col-1] > entropy) {
                return col;
            }
            col++;
        }
        return null;
    }

    private Integer findLowerEntropyIndex(double temperature, double entropy) {
        return findHigherEntropyIndex(temperature, entropy) - 1;
    }

    private double calculatePressure(double temperature, double entropy) {
        ///int row = findNearestTemperatureIndex(temperature);
        int row = 0;

        int lowerColumn = findLowerEntropyIndex(temperature, entropy);
        int higherColumn = findHigherEntropyIndex(temperature, entropy);

        double lowerIndexEntropy = gridArr[row][lowerColumn];
        double higherIndexEntropy = gridArr[row][higherColumn];
        double lowerIndexPressure = gridArr[row][lowerColumn];
        double higherIndexPressure = gridArr[row][higherColumn];

        return estimatePropertyValue(lowerIndexEntropy, higherIndexEntropy, entropy, lowerIndexPressure, higherIndexPressure);
    }
}
