package quinn_mchugh.clausius.Tables.Superheated_Region;

import java.io.InputStream;

/**
 * Created by Quinn McHugh on 10/28/2018.
 */
public class SuperSTable extends SuperTable {

    private Double[][] entropyArr;

    public SuperSTable(InputStream inputStream) {
        super(inputStream);
        entropyArr = super.getGridArr();
    }

    public Double[][] getEntropyArr() {
        return entropyArr;
    }

    private double calculatePressure(double temperature, double entropy) {
        //int row = findNearestTemperatureIndex(temperature);
        int row = 0;
        int lowerColumn = 0;
        int higherColumn = 0;
        //int lowerColumn = findLowerEntropyIndex(temperature, entropy);
        //int higherColumn = findHigherEntropyIndex(temperature, entropy);

        double lowerIndexEntropy = entropyArr[row][lowerColumn];
        double higherIndexEntropy = entropyArr[row][higherColumn];
        double lowerIndexPressure = entropyArr[row][lowerColumn];
        double higherIndexPressure = entropyArr[row][higherColumn];

        return estimatePropertyValue(lowerIndexEntropy, higherIndexEntropy, entropy, lowerIndexPressure, higherIndexPressure);
    }
}
