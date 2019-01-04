package quinn_mchugh.clausius.Tables;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.Arrays;

import quinn_mchugh.clausius.States.SatState;

/**
 * Represents the the sat_table CSV file.
 */
public class SatTable extends CSVFile {

    private ArrayList<SatState> states;     // The thermodynamic states stored on each row of the CSV file

    /**
     * Required public constructor for SatState class.
     *
     * @param inputStream The input stream used to read the sat_table CSV file
     */
    public SatTable(InputStream inputStream) {
        super(inputStream);
        states = new ArrayList<>();
        parseCSVFile();
    }

    public ArrayList<SatState> getStates() {
        return states;
    }

    /**
     * Reads data from the SatTable CSV file, stores each row as a thermodynamic state, and stores each state in a list of states.
     */
    private void parseCSVFile() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            try {
                String line = reader.readLine();
                String[] columnHeaders = line.split(",");

                reader.readLine();   // Skip the line containing property units

                while ((line = reader.readLine()) != null) {
                    Double[] rowValues = toDoubleArray(line.split(","));

                    SatState state = new SatState();

                    double temperature = rowValues[Arrays.asList(columnHeaders).indexOf("T")];
                    state.setT(temperature);

                    double pressure = rowValues[Arrays.asList(columnHeaders).indexOf("P")];
                    state.setP(pressure);

                    double v_f = rowValues[Arrays.asList(columnHeaders).indexOf("v_f")];
                    state.setV_f(v_f);

                    double v_g = rowValues[Arrays.asList(columnHeaders).indexOf("v_g")];
                    state.setV_g(v_g);

                    double s_g = rowValues[Arrays.asList(columnHeaders).indexOf("s_g")];
                    state.setS_g(s_g);

                    double u_f = rowValues[Arrays.asList(columnHeaders).indexOf("u_f")];
                    state.setU_f(u_f);

                    double u_g = rowValues[Arrays.asList(columnHeaders).indexOf("u_g")];
                    state.setU_g(u_g);

                    double h_f = rowValues[Arrays.asList(columnHeaders).indexOf("h_f")];
                    state.setH_f(h_f);

                    double h_g = rowValues[Arrays.asList(columnHeaders).indexOf("h_g")];
                    state.setH_g(h_g);

                    double s_f = rowValues[Arrays.asList(columnHeaders).indexOf("s_f")];
                    state.setS_f(s_f);

                    states.add(state);
                }
            }
            catch (Exception e) {
                throw new RuntimeException("Error reading line in CSV file.", e);
            }
            finally {
                reader.close();
            }
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to read CSV file.", e);
        }
    }

    /**
     * Finds the index of a state in the states list whose temperature
     * value is the closest value greater than or equal to the specified temperature.
     *
     * @param temperature The specified temperature
     * @return The index of the state in the states list
     */
    private int findHigherStateIndex(Double temperature) {
        int i = 1;
        while (states.get(i).getT() < temperature) {
            i++;
        }
        return i;
    }

    /**
     * Finds the index of a state in the states list whose temperature
     * value is closest to but not above the specified temperature.
     *
     * @param temperature The specified temperature
     * @return The index of the state in the states list
     */
    private int findLowerStateIndex(Double temperature) {
        return findHigherStateIndex(temperature) - 1;
    }

    /**
     * Finds a state in the states list whose temperature
     * value is the first value above the specified temperature.
     *
     * @param temperature The specified temperature
     * @return The desired state in the states list
     */
    private SatState findHigherState(Double temperature) {
        return states.get(findHigherStateIndex(temperature));
    }

    /**
     * Finds the state in the states list whose temperature
     * value is closest to but not above the specified temperature.
     *
     * @param temperature The specified temperature
     * @return The desired state in the states list
     */
    private SatState findLowerState(Double temperature) {
        return states.get(findLowerStateIndex(temperature));
    }

    /**
     * Calculates pressure at the specified temperature in the saturated vapor dome.
     *_
     * @param T The temperature determined from the user's touch
     * @return The calculated pressure [MPa]
     */
    public double calculatePressure(double T) {
        SatState lowerState = findLowerState(T);
        SatState higherState = findHigherState(T);
        double pressure = estimatePropertyValue(lowerState.getT(), higherState.getT(), T,
                lowerState.getP(), higherState.getP());
        return pressure / 1000;
    }

    /**
     * Calculates internal energy at the specified temperature and quality in the saturated vapor dome.
     *_
     * @param T The temperature at the location of the user's touch
     * @param x The quality at the location of the user's touch
     * @return The calculated internal energy [kJ/kg]
     */
    public double calculateInternalEnergy(double T, double x) {
        SatState lowerState = findLowerState(T);
        SatState higherState = findHigherState(T);
        double u_g = estimatePropertyValue(lowerState.getT(), higherState.getT(), T,
                lowerState.getU_g(), higherState.getU_g());
        double u_f = estimatePropertyValue(lowerState.getT(), higherState.getT(), T,
                lowerState.getU_f(), higherState.getU_f());
        return u_f + x*(u_g - u_f);
    }

    /**
     * Calculates enthalpy at the specified temperature and quality in the saturated vapor dome.
     *_
     * @param T The temperature at the location of the user's touch
     * @param x The quality at the location of the user's touch
     * @return The calculated enthalpy [kJ/kg]
     */
    public double calculateEnthalpy(double T, double x) {
        SatState lowerState = findLowerState(T);
        SatState higherState = findHigherState(T);
        double h_g = estimatePropertyValue(lowerState.getT(), higherState.getT(), T,
                lowerState.getH_g(), higherState.getH_g());
        double h_f = estimatePropertyValue(lowerState.getT(), higherState.getT(), T,
                lowerState.getH_f(), higherState.getH_f());
        return h_f + x*(h_g - h_f);
    }

    /**
     * Calculates quality at the specified temperature and entropy in the saturated vapor dome.
     *_
     * @param T The temperature at the location of the user's touch
     * @param s The entropy at the location of the user's touch
     * @return The calculated quality [in decimal form, not percentage form]
     */
    public double calculateQuality(double T, double s) {
        SatState lowerState = findLowerState(T);
        SatState higherState = findHigherState(T);
        double s_g = estimatePropertyValue(lowerState.getT(), higherState.getT(), T,
                lowerState.getS_g(), higherState.getS_g());
        double s_f = estimatePropertyValue(lowerState.getT(), higherState.getT(), T,
                lowerState.getS_f(), higherState.getS_f());
        return (s - s_f) / (s_g - s_f);
    }

    /**
     * Calculates specific volume at the specified temperature and quality in the saturated vapor dome.
     *_
     * @param T The temperature at the location of the user's touch
     * @param x The quality at the location of the user's touch
     * @return The calculated specific volume [m^3/kg]
     */
    public double calculateSpecificVolume(double T, double x) {
        SatState lowerState = findLowerState(T);
        SatState higherState = findHigherState(T);
        double v_g = estimatePropertyValue(lowerState.getT(), higherState.getT(), T,
                lowerState.getV_g(), higherState.getV_g());
        double v_f = estimatePropertyValue(lowerState.getT(), higherState.getT(), T,
                lowerState.getV_f(), higherState.getV_f());
        return v_f + x*(v_g - v_f);
    }

    /**
     * Calculates entropy of a saturated vapor at the specified temperature in the saturated vapor dome.
     *_
     * @param T The temperature at the location of the user's touch
     * @return The calculated entropy [kJ/kg/K]
     */
    public double calculateS_g(double T) {
        SatState lowerState = findLowerState(T);
        SatState higherState = findHigherState(T);
        return estimatePropertyValue(lowerState.getT(), higherState.getT(), T,
                lowerState.getS_g(), higherState.getS_g());
    }

    /**
     * Calculates entropy of a saturated liquid at the specified temperature in the saturated vapor dome.
     *_
     * @param T The temperature at the location of the user's touch
     * @return The calculated entropy [kJ/kg/K]
     */
    public double calculateS_f(double T) {
        SatState lowerState = findLowerState(T);
        SatState higherState = findHigherState(T);
        return estimatePropertyValue(lowerState.getT(), higherState.getT(), T,
                lowerState.getS_f(), higherState.getS_f());
    }
}
