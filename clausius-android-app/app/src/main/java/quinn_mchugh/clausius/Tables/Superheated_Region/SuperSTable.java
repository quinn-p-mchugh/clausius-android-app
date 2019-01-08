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
}
