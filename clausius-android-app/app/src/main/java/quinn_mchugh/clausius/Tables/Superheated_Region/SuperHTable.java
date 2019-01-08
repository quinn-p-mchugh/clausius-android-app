package quinn_mchugh.clausius.Tables.Superheated_Region;

import java.io.InputStream;

/**
 * Created by Quinn McHugh on 10/28/2018.
 */
public class SuperHTable extends SuperTable {

    private Double[][] enthalpyArr;

    public SuperHTable(InputStream inputStream) {
        super(inputStream);
        enthalpyArr = super.getGridArr();
    }

    public Double[][] getEnthalpyArr() {
        return enthalpyArr;
    }
}
