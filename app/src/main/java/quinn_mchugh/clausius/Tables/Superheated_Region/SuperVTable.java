package quinn_mchugh.clausius.Tables.Superheated_Region;

import java.io.InputStream;

/**
 * Created by Quinn McHugh on 10/28/2018.
 */
public class SuperVTable extends SuperTable {

    private Double[][] specificVolArr;

    public SuperVTable(InputStream inputStream) {
        super(inputStream);
        specificVolArr = super.getGridArr();
    }

    public Double[][] getSpecificVolArr() {
        return specificVolArr;
    }
}
