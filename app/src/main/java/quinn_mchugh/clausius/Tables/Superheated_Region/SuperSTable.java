package quinn_mchugh.clausius.Tables.Superheated_Region;

import java.io.InputStream;

/**
 * Represents the super_s_table CSV file.
 */
public class SuperSTable extends SuperTable {

    private Double[][] entropyArr;  // The array of entropy values bounded by the temperature and pressure values in the CSV file

    /**
     * Required public constructor for SuperSTable class.
     *
     * @param inputStream The input stream used to read the super_s_table CSV file
     */
    public SuperSTable(InputStream inputStream) {
        super(inputStream, 58);
        entropyArr = super.getGridArr();
    }

    public Double[][] getEntropyArr() {
        return entropyArr;
    }

    public double calculatePressure (double temperature, double entropy) {
        int lowerTemperatureIndex = findLowerIndex(temperature, getTemperatures());
        int higherTemperatureIndex = findHigherIndex(temperature, getTemperatures());

        double lowerTemperature = getTemperatures()[lowerTemperatureIndex];
        double higherTemperature = getTemperatures()[higherTemperatureIndex];

        double percentage = (temperature - lowerTemperature) / (higherTemperature - lowerTemperature);
        Double[] lowerEntropyRow = entropyArr[lowerTemperatureIndex];
        Double[] higherEntropyRow = entropyArr[higherTemperatureIndex];

        Double[] estimatedEntropyRow = estimateRow(percentage, lowerEntropyRow, higherEntropyRow);

        int lowerEntropyIndex = findLowerIndex(entropy, estimatedEntropyRow);
        int higherEntropyIndex = findHigherIndex(entropy, estimatedEntropyRow);

        double lowerIndexEntropy = estimatedEntropyRow[lowerEntropyIndex];
        double higherIndexEntropy = estimatedEntropyRow[higherEntropyIndex];
        double lowerIndexPressure = getPressures()[lowerEntropyIndex];
        double higherIndexPressure = getPressures()[higherEntropyIndex];

        return estimatePropertyValue(lowerIndexEntropy, higherIndexEntropy, entropy, lowerIndexPressure, higherIndexPressure) / 1000; // MPa
    }

    /**
     * Returns an array in which all values in the array are between the lowerRow
     * and higherRow arrays.
     *
     * @param percentage
     * @param lowerRow
     * @param higherRow
     * @return
     */
    private Double[] estimateRow(double percentage, Double[] lowerRow, Double[] higherRow) {
        int maxArrLength = getMax(lowerRow.length, higherRow.length);
        Double[] estimatedRow = new Double[maxArrLength];

        for (int i = 0; i < maxArrLength; i++) {
            if (lowerRow[i] != null && higherRow[i] != null) {
                estimatedRow[i] = percentage * (higherRow[i] - lowerRow[i]) + lowerRow[i];
            }
            else if (lowerRow[i] == null && higherRow[i] != null) {
                estimatedRow[i] = higherRow[i];
            }
            else if (lowerRow[i] != null && higherRow[i] == null) {
                estimatedRow[i] = lowerRow[i];
            }
            else {  // Both values in each row are null
                estimatedRow[i] = null;
            }
        }
        return estimatedRow;
    }

    /**
     * Gets the maximum value of two specified integers
     *
     * @param a The first integer
     * @param b The second integer
     * @return The maximum of the two integers
     */
    private int getMax(int a, int b) {
        return a>b ? a:b;
    }



}
