package quinn_mchugh.clausius.Tables.Superheated_Region;

import java.io.InputStream;

/**
 * Represents the super_v_table CSV file.
 */
public class SuperVTable extends SuperTable {

    private Double[][] specificVolArr;  // The array of entropy values bounded by the temperature and pressure values in the CSV file

    /**
     * Required public constructor for SuperVTable class.
     *
     * @param inputStream The input stream used to read the super_v_table CSV file
     */
    public SuperVTable(InputStream inputStream) {
        super(inputStream, 32);
        specificVolArr = super.getGridArr();
    }

    public Double[][] getSpecificVolArr() {
        return specificVolArr;
    }
}
