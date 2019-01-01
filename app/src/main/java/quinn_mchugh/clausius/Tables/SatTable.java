package quinn_mchugh.clausius.Tables;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import quinn_mchugh.clausius.States.SatState;

/**
 * Represents the SatTable
 */
public class SatTable extends CSVFile {

    private ArrayList<SatState> states;
    private String[] headers;

    public SatTable(InputStream inputStream) {
        super(inputStream);
        states = new ArrayList<>();
        parseCSVFile();
    }

    public ArrayList<SatState> getStates() {
        return states;
    }

    public String[] getHeaders() {
        return headers;
    }

    /**
     * Reads data from the SatTable CSV file and stores it in appropriate data structures.
     */
    public void parseCSVFile() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            try {
                String line = reader.readLine();
                headers = line.split(",");

                line = reader.readLine();   // Skip the line containing units

                while ((line = reader.readLine()) != null) {
                    Double[] rowValues = toDoubleArray(line.split(","));

                    SatState state = new SatState();
                    state.setT(rowValues[getIndexOfHeader("T", headers)]);
                    state.setP(rowValues[getIndexOfHeader("P", headers)]);
                    state.setV_f(rowValues[getIndexOfHeader("v_f", headers)]);
                    state.setV_g(rowValues[getIndexOfHeader("v_g", headers)]);
                    state.setS_g(rowValues[getIndexOfHeader("s_g", headers)]);
                    state.setU_f(rowValues[getIndexOfHeader("u_f", headers)]);
                    state.setU_g(rowValues[getIndexOfHeader("u_g", headers)]);
                    state.setH_f(rowValues[getIndexOfHeader("h_f", headers)]);
                    state.setH_g(rowValues[getIndexOfHeader("h_g", headers)]);
                    state.setS_f(rowValues[getIndexOfHeader("s_f", headers)]);

                    states.add(state);
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
     * Iterates through a list and returns the index of an element in a list who's value is the next value below the user-defined value.
     *
     * @param temperature The user-specified temperature used for comparison.
     * @return The index of an element in the list who's value is the next value below the user-defined value.
     */
    public SatState findLowerState(Double temperature) {
        return states.get(findHigherStateIndex(temperature) - 1);
    }

    public SatState findHigherState(Double temperature) {
        return states.get(findHigherStateIndex(temperature));
    }

    private int findHigherStateIndex(Double temperature) {
        int i = 1;
        while (states.get(i).getT() < temperature) {
            i++;
        }
        return i;
    }

    /**for (int i = 1; i < states.size(); i++) {
        if (states.get(i).getT() < temperature) {
            return i;
        }
        return null;
    }*/


    /**
     * Calculates the pressure at the same state as a user-specified temperature using values from a CSV file
     *_
     * @param T The temperature determined from the user's touch
     * @return The pressure in [kPa].
     */
    public double calculatePressure(double T) {
        SatState lowerState = findLowerState(T);
        SatState higherState = findHigherState(T);
        double pressure = estimatePropertyValue(lowerState.getT(), higherState.getT(), T, lowerState.getP(), higherState.getP());
        return pressure / 1000; // [MPa]
    }

    public double calculateInternalEnergy(double T, double x) {
        SatState lowerState = findLowerState(T);
        SatState higherState = findHigherState(T);
        double u_g = estimatePropertyValue(lowerState.getT(), higherState.getT(), T, lowerState.getU_g(), higherState.getU_g());
        double u_f = estimatePropertyValue(lowerState.getT(), higherState.getT(), T, lowerState.getU_f(), higherState.getU_f());
        return u_f + x*(u_g - u_f);
    }

    public double calculateEnthalpy(double T, double x) {
        SatState lowerState = findLowerState(T);
        SatState higherState = findHigherState(T);
        double h_g = estimatePropertyValue(lowerState.getT(), higherState.getT(), T, lowerState.getH_g(), higherState.getH_g());
        double h_f = estimatePropertyValue(lowerState.getT(), higherState.getT(), T, lowerState.getH_f(), higherState.getH_f());
        return h_f + x*(h_g - h_f);
    }

    public double calculateQuality(double T, double s) {
        SatState lowerState = findLowerState(T);
        SatState higherState = findHigherState(T);
        double s_g = estimatePropertyValue(lowerState.getT(), higherState.getT(), T, lowerState.getS_g(), higherState.getS_g());
        double s_f = estimatePropertyValue(lowerState.getT(), higherState.getT(), T, lowerState.getS_f(), higherState.getS_f());
        return (s - s_f) / (s_g - s_f);
    }

    public double calculateSpecificVolume(double T, double x) {
        SatState lowerState = findLowerState(T);
        SatState higherState = findHigherState(T);
        double v_g = estimatePropertyValue(lowerState.getT(), higherState.getT(), T, lowerState.getV_g(), higherState.getV_g());
        double v_f = estimatePropertyValue(lowerState.getT(), higherState.getT(), T, lowerState.getV_f(), higherState.getV_f());
        return v_f + x*(v_g - v_f);
    }

    public double calculateS_g(double T) {
        SatState lowerState = findLowerState(T);
        SatState higherState = findHigherState(T);
        return estimatePropertyValue(lowerState.getT(), higherState.getT(), T,
                lowerState.getS_g(), higherState.getS_g());
    }

    public double calculateS_f(double T) {
        SatState lowerState = findLowerState(T);
        SatState higherState = findHigherState(T);
        return estimatePropertyValue(lowerState.getT(), higherState.getT(), T,
                lowerState.getS_f(), higherState.getS_f());
    }
}
