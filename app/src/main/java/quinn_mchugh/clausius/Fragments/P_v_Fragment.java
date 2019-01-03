package quinn_mchugh.clausius.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;

import quinn_mchugh.clausius.MainActivity;
import quinn_mchugh.clausius.R;

/**
 * Represents a fragment containing the pressure-specific volume (P-v) diagram.
 */
public class P_v_Fragment extends Fragment implements View.OnTouchListener {

    private static final double MAX_SPECIFIC_VOLUME = 37.5;     // [m^3/kg] The maximum specific volume value on the P-v diagram
    private static final double MIN_SPECIFIC_VOLUME = 0.001;    // [m^3/kg] The minimum specific volume value on the P-v diagram
    private static final double MAX_PRESSURE = 51.70;           // [MPa] The maximum pressure value on the P-v diagram
    private static final double MIN_PRESSURE = 0.01;            // [MPa] The maximum pressure value on the P-v diagram

    /**
     * Required public constructor.
     */
    public P_v_Fragment() {

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_p_v, container, false);

        ImageView p_v_diagram = view.findViewById(R.id.p_v_diagram);
        p_v_diagram.setOnTouchListener(this);

        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch(view.getId()) {
            /* If the P-v diagram is touched... */
            case R.id.p_v_diagram:
                setDiagramValues(view, motionEvent);
                ((MainActivity) getActivity()).updateCursorLocation(motionEvent);

                switch(motionEvent.getAction()) {
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
    private void setDiagramValues(View view, MotionEvent motionEvent) {
        double specificVolume = calculateSpecificVolume(view, motionEvent);
        double pressure = calculatePressure(view, motionEvent);

        ((MainActivity) getActivity()).displaySpecificVolume(specificVolume);
        ((MainActivity) getActivity()).displayPressure(pressure);
    }

    /**
     * Returns an estimated specific volume value based on the location of the user's touch.
     *
     * @param view The view that is currently being displayed
     * @param motionEvent The motion event created when the user touches the screen
     * @return The estimated specific volume value
     */
    private double calculateSpecificVolume(View view, MotionEvent motionEvent) {
        return MIN_SPECIFIC_VOLUME * Math.pow((MAX_SPECIFIC_VOLUME / MIN_SPECIFIC_VOLUME), (motionEvent.getX() / view.getWidth()));
    }

    /**
     * Returns an estimated pressure value based on the location of the user's touch.
     *
     * @param view The view that is currently being displayed
     * @param motionEvent The motion event created when the user touches the screen
     * @return The estimated pressure value
     */
    private double calculatePressure(View view, MotionEvent motionEvent) {
        return MIN_PRESSURE *Math.pow((MAX_PRESSURE / MIN_PRESSURE), ((view.getHeight() - motionEvent.getY()) / view.getHeight()));
    }
}
