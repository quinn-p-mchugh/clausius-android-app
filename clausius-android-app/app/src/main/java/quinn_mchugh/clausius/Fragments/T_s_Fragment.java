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
import java.text.DecimalFormat;

import quinn_mchugh.clausius.MainActivity;
import quinn_mchugh.clausius.R;
import quinn_mchugh.clausius.Tables.SatTable;
import quinn_mchugh.clausius.Tables.Superheated_Region.SuperHTable;
import quinn_mchugh.clausius.Tables.Superheated_Region.SuperSTable;
import quinn_mchugh.clausius.Tables.Superheated_Region.SuperVTable;

import static java.lang.Math.abs;

/**
 * Represents a fragment containing the T-s diagram.
 */
public class T_s_Fragment extends Fragment implements View.OnTouchListener {

    private static final double MAX_ENTROPY = 9.35;
    private static final double MAX_TEMPERATURE = 702.5;

    private InputStream is;
    private SatTable satTable;
    private SuperSTable superSTable;
    private SuperHTable superHTable;
    private SuperVTable superVTable;

    public T_s_Fragment() {

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_t_s, container, false);

        ImageView t_s_diagram = view.findViewById(R.id.t_s_diagram);
        t_s_diagram.setOnTouchListener(this);

        is = ((MainActivity) getActivity()).getApplicationContext().getResources().openRawResource(R.raw.sat_table);
        satTable = new SatTable(is);

        is = ((MainActivity) getActivity()).getApplicationContext().getResources().openRawResource(R.raw.super_s_table);
        superSTable = new SuperSTable(is);

        is = ((MainActivity) getActivity()).getApplicationContext().getResources().openRawResource(R.raw.super_h_table);
        superHTable = new SuperHTable(is);

        is = ((MainActivity) getActivity()).getApplicationContext().getResources().openRawResource(R.raw.super_v_table);
        superVTable = new SuperVTable(is);

        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch(view.getId()) {
            case R.id.t_s_diagram:  // If the TList-s diagram is touched...
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
     * Sets the values of the thermodynamic property table.
     *
     * @param view The view that is currently being displayed.
     * @param motionEvent The motionEvent created when the user touches the screen.
     */
    public void setDiagramValues(View view, MotionEvent motionEvent) {
        double entropy = calculateEntropy(view, motionEvent);
        double temperature = calculateTemperature(view, motionEvent);

        if (temperature >= 0) {
            displayTemperature(temperature);
        }
        if (entropy >= 0) {
            displayEntropy(entropy);
        }

        // TODO: Change "temperature >= 0.13" to "temperature >= 0"
        // TODO: and add Sat Stable values for when T = 0
        if (temperature >= 0.13 && temperature < 374) {
            double quality = 0;
            if (inSaturatedRegion(temperature, entropy)) {
                if (inCompressedLiquidRegion(temperature, entropy)) {
                    quality = 0;
                    displayQuality(quality);
                }
                else {
                    quality = satTable.calculateQuality(temperature, entropy);
                    displayQuality(quality);
                }
                double pressure = satTable.calculatePressure(temperature);
                displayPressure(pressure);

                double specificVolume = satTable.calculateSpecificVolume(temperature, quality);
                displaySpecificVolume(specificVolume);

                double internalEnergy = satTable.calculateInternalEnergy(temperature, quality);
                displayInternalEnergy(internalEnergy);

                double enthalpy = satTable.calculateEnthalpy(temperature, quality);
                displayEnthalpy(enthalpy);
            }
        }
    }

    /**
     * Determines if the use-specified thermodynamic state is within the mixture region (i.e. vapor dome) or not.
     *
     * @return Whether or not the user-specified thermodynamic state is within the mixture region (i.e. vapor dome).
     */
    public boolean inSaturatedRegion(double T, double entropy) {
        double s_g = satTable.calculateS_g(T);
        double s_f = satTable.calculateS_f(T);
        if (entropy < s_g && entropy > s_f) {
            return true;
        }
        return false;
    }

    public boolean inCompressedLiquidRegion(double T, double entropy) {
        double s_f = satTable.calculateS_f(T);
        if (entropy <= s_f) {
            return true;
        }
        return false;
    }

    public void displayPressure(double pressure) {
        DecimalFormat df = new DecimalFormat();
        if (pressure < 0.01) {
            df.applyPattern("#0.00000");
        }
        else if (pressure < 0.1) {
            df.applyPattern("#0.0000");
        }
        else if (pressure < 1) {
            df.applyPattern("#0.000");
        }
        else {
            df.applyPattern("#0.00");
        }
        ((MainActivity) getActivity()).getPressure().setText(String.valueOf(df.format(pressure)));
    }

    public void displayQuality(double quality) {
        ((MainActivity) getActivity()).getQuality().
                setText(String.valueOf((new DecimalFormat("#0.00")).format(quality * 100)));
    }

    public void displaySpecificVolume(double specificVolume) {
        DecimalFormat df = new DecimalFormat();
        if (specificVolume < 0.01) {
            df.applyPattern("#0.00000");
        }
        else if (specificVolume < 0.1) {
            df.applyPattern("#0.0000");
        }
        else if (specificVolume < 1) {
            df.applyPattern("#0.000");
        }
        else {
            df.applyPattern("#0.00");
        }
        ((MainActivity) getActivity()).getSpecificVolume().setText(String.valueOf(df.format(specificVolume)));
    }

    public void displayInternalEnergy(double internalEnergy) {
        ((MainActivity) getActivity()).getInternalEnergy().
                setText(String.valueOf((new DecimalFormat("#0.00")).format(internalEnergy)));
    }

    public void displayEnthalpy(double enthalpy) {
        ((MainActivity) getActivity()).getEnthalpy().
                setText(String.valueOf((new DecimalFormat("#0.00")).format(enthalpy)));
    }

    public void displayTemperature(double temperature) {
        ((MainActivity) getActivity()).getTemperature()
                .setText(String.valueOf((new DecimalFormat("#0.00")).format(temperature)));
    }

    public void displayEntropy(double entropy) {
        ((MainActivity) getActivity()).getEntropy().
                setText(String.valueOf((new DecimalFormat("#0.00")).format(entropy)));
    }

    public double calculateEntropy(View view, MotionEvent motionEvent) {
        return (motionEvent.getX() / view.getWidth()) * MAX_ENTROPY;
    }

    public double calculateTemperature(View view, MotionEvent motionEvent) {
        return ((view.getHeight() - motionEvent.getY()) / view.getHeight()) * MAX_TEMPERATURE;
    }
}
