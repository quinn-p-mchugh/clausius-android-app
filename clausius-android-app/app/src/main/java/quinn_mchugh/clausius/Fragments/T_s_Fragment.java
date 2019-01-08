package quinn_mchugh.clausius.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;

import java.io.InputStream;

import quinn_mchugh.clausius.MainActivity;
import quinn_mchugh.clausius.R;
import quinn_mchugh.clausius.Tables.SatTable;
import quinn_mchugh.clausius.Tables.Superheated_Region.SuperHTable;
import quinn_mchugh.clausius.Tables.Superheated_Region.SuperSTable;
import quinn_mchugh.clausius.Tables.Superheated_Region.SuperVTable;

/**
 * Represents a fragment containing the temperature-entropy (T-s) diagram.
 */
public class T_s_Fragment extends Fragment implements View.OnTouchListener {

    private static final double MAX_ENTROPY = 9.35;         // [kJ/kg/K] The maximum entropy value on the T-s diagram
    private static final double MIN_ENTROPY = 0;            // [kJ/kg/K] The minimum entropy value on the T-s diagram
    private static final double MAX_TEMPERATURE = 702.5;    // [°C] The maximum temperature value on the T-s diagram
    private static final double MIN_TEMPERATURE = 0;        // [°C] The minimum temperature value on the T-s diagram

    private SatTable satTable;
    private SuperSTable superSTable;
    private SuperHTable superHTable;
    private SuperVTable superVTable;

    /**
     * Required public constructor for T_s_Fragment class.
     */
    public T_s_Fragment() {

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_t_s, container, false);

        ImageView t_s_diagram = view.findViewById(R.id.t_s_diagram);
        t_s_diagram.setOnTouchListener(this);

        InputStream is;
        is = getActivity().getApplicationContext().getResources().openRawResource(R.raw.sat_table);
        satTable = new SatTable(is);
        is = getActivity().getApplicationContext().getResources().openRawResource(R.raw.super_s_table);
        superSTable = new SuperSTable(is);
        is = getActivity().getApplicationContext().getResources().openRawResource(R.raw.super_h_table);
        superHTable = new SuperHTable(is);
        is = getActivity().getApplicationContext().getResources().openRawResource(R.raw.super_v_table);
        superVTable = new SuperVTable(is);

        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch(view.getId()) {
            /* If the T-s diagram is touched... */
            case R.id.t_s_diagram:
                setDiagramValues(view, motionEvent);
                ((MainActivity) getActivity()).updateCursorLocation(motionEvent);

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ((MainActivity) getActivity()).cursorActionDown();
                        break;
                    case MotionEvent.ACTION_UP:
                        ((MainActivity) getActivity()).cursorActionUp();
                        break;
                }

                break;
        }
        return true;
    }

    /**
     * Sets the values of the thermodynamic property table displayed on-screen.
     *
     * @param view The view that is currently being displayed
     * @param motionEvent The motion event created when the user touches the screen
     */
    private void setDiagramValues(View view, MotionEvent motionEvent) {
        double entropy = calculateEntropy(view, motionEvent);
        double temperature = calculateTemperature(view, motionEvent);
        if (temperature >= 0) {
            ((MainActivity) getActivity()).displayTemperature(temperature);
        }
        if (entropy >= 0) {
            ((MainActivity) getActivity()).displayEntropy(entropy);
        }

        double quality = 0;
        double pressure = 0;
        double specificVolume = 0;
        double internalEnergy = 0;
        double enthalpy = 0;
        // TODO: Change "temperature >= 0.13" to "temperature >= 0"
        // TODO: and add Sat Stable values for when temperature = 0
        if (temperature >= 0.13 && temperature < 374) {
            /* User's touch is within the saturated vapor dome... */
            if (inSaturatedRegion(temperature, entropy)) {
                quality = satTable.calculateQuality(temperature, entropy);
            }
            /* Users touch is within compressed liquid region... */
            if (inCompressedLiquidRegion(temperature, entropy)) {
                quality = 0;
            }
            pressure = satTable.calculatePressure(temperature);
            specificVolume = satTable.calculateSpecificVolume(temperature, quality);
            internalEnergy = satTable.calculateInternalEnergy(temperature, quality);
            enthalpy = satTable.calculateEnthalpy(temperature, quality);
        }
        else {
            pressure = superSTable.calculatePressure(temperature, entropy);
        }

        ((MainActivity) getActivity()).displayQuality(quality);
        ((MainActivity) getActivity()).displayPressure(pressure);
        ((MainActivity) getActivity()).displaySpecificVolume(specificVolume);
        ((MainActivity) getActivity()).displayInternalEnergy(internalEnergy);
        ((MainActivity) getActivity()).displayEnthalpy(enthalpy);
    }

    /**
     * Determines whether or not the user's touch is within the saturated vapor dome.
     *
     * @param temperature The temperature value corresponding to the location of the user's touch
     * @param entropy The entropy value corresponding to the location of the user's touch
     * @return Whether or not the user's touch is within the saturated vapor dome
     */
    private boolean inSaturatedRegion(double temperature, double entropy) {
        double s_g = satTable.calculateS_g(temperature);
        double s_f = satTable.calculateS_f(temperature);
        return entropy < s_g && entropy > s_f;
    }

    /**
     * Determines whether or not the user's touch is within the compressed liquid region.
     *
     * @param temperature The temperature value corresponding to the location of the user's touch.
     * @param entropy The entropy value corresponding to the location of the user's touch
     * @return Whether or not the user's touch is within the compressed liquid region
     */
    private boolean inCompressedLiquidRegion(double temperature, double entropy) {
        double s_f = satTable.calculateS_f(temperature);
        return entropy <= s_f;
    }

    /**
     * Returns an estimated entropy value based on the location of the user's touch.
     *
     * @param view The view that is currently being displayed
     * @param motionEvent The motion event created when the user touches the screen
     * @return The estimated entropy value
     */
    private double calculateEntropy(View view, MotionEvent motionEvent) {
        return (motionEvent.getX() / view.getWidth()) * (MAX_ENTROPY - MIN_ENTROPY);
    }

    /**
     * Returns an estimated temperature value based on the location of the user's touch.
     *
     * @param view The view that is currently being displayed
     * @param motionEvent The motion event created when the user touches the screen
     * @return The estimated temperature value
     */
    private double calculateTemperature(View view, MotionEvent motionEvent) {
        return ((view.getHeight() - motionEvent.getY()) / view.getHeight()) * (MAX_TEMPERATURE - MIN_TEMPERATURE);
    }
}
