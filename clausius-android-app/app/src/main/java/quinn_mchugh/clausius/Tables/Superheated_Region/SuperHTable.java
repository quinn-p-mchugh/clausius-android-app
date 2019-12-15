package quinn_mchugh.clausius.Tables.Superheated_Region;

import java.io.InputStream;

/**
 * Represents the super_h_table CSV file.
 */
public class SuperHTable extends SuperTable {

    private Double[][] enthalpyArr;     // The array of enthalpy values bounded by the temperature and pressure values in the CSV file

    /**
     * Required public constructor for SuperHTable class.
     *
     * @param inputStream The input stream used to read the super_h_table CSV file
     */
    public SuperHTable(InputStream inputStream) {
        super(inputStream, 451);
        enthalpyArr = super.getGridArr();
    }

    public Double[][] getEnthalpyArr() {
        return enthalpyArr;
    }

    /**
     * Calculates the enthalpy value using the super_h_table CSV file, given temperature and entropy.
     * @param temperature
     * @param pressure [MPa]
     * @return The enthalpy in
     */
    public Double calculateEnthalpy (double temperature, double pressure) {
        return calculateGridValue(temperature, pressure, getEnthalpyArr());
    }
}
