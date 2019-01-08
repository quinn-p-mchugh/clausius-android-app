package quinn_mchugh.clausius.Tables.Superheated_Region;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import quinn_mchugh.clausius.Tables.CSVFile;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Represents the SatTable
 */
public class SuperTable extends CSVFile {

    private Double[] pressures;
    private Double[] temperatures;
    private Double[][] gridArr;

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
     * Finds the temperature value in the list of temperature values that
     * is closest to the specified temperature.
     *
     * @param temperature The specified temperature
     * @return The index of the closest temperature that is greater than the specified temperature
     */
    private int findNearestTemperatureIndex(double temperature) {
        return (int) Math.round(temperature) - 1;
    }

    private Integer findHigherEntropyIndex(double temperature, double entropy) {
        int row = findNearestTemperatureIndex(temperature);
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
        int row = findNearestTemperatureIndex(temperature);

        int lowerColumn = findLowerEntropyIndex(temperature, entropy);
        int higherColumn = findHigherEntropyIndex(temperature, entropy);

        double lowerIndexEntropy = gridArr[row][lowerColumn];
        double higherIndexEntropy = gridArr[row][higherColumn];
        double lowerIndexPressure = gridArr[row][lowerColumn];
        double higherIndexPressure = gridArr[row][higherColumn];

        return estimatePropertyValue(lowerIndexEntropy, higherIndexEntropy, entropy, lowerIndexPressure, higherIndexPressure);
    }
}
