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
 * Represents a fragment containing the pressure-enthalpy (P-h) diagram.
 */
public class P_h_Fragment extends Fragment implements View.OnTouchListener {

    private static final double MAX_ENTHALPY = 4505;    // [kJ/kg] The maximum enthalpy value the P-h diagram
    private static final double MIN_ENTHALPY = 0;       // [kJ/kg] The minimum enthalpy value on the P-h diagram
    private static final double MAX_PRESSURE = 1000;    // [MPa] The maximum pressure value on the P-h diagram
    private static final double MIN_PRESSURE = 0.01;    // [MPa] The minimum pressure value on the P-h diagram

    /**
     * Required public constructor.
     */
    public P_h_Fragment() {

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_p_h, container, false);

        ImageView p_h_diagram = view.findViewById(R.id.p_h_diagram);
        p_h_diagram.setOnTouchListener(this);

        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch(view.getId()) {
            /* If the P-h diagram is touched... */
            case R.id.p_h_diagram:
                setPropertyValues(view, motionEvent);
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
     * Sets the values of the thermodynamic property table displayed on-screen.
     *
     * @param view The view that is currently being displayed
     * @param motionEvent The motion event created when the user touches the screen
     */
    public void setPropertyValues(View view, MotionEvent motionEvent) {
        double enthalpy = calculateEnthalpy(view, motionEvent);
        double pressure = calculatePressure(view, motionEvent);

        ((MainActivity) getActivity()).displayEnthalpy(enthalpy);
        ((MainActivity) getActivity()).displayEnthalpy(pressure);
    }

    /**
     * Returns an estimated enthalpy value based on the location of the user's touch.
     *
     * @param view The view that is currently being displayed
     * @param motionEvent The motion event created when the user touches the screen
     * @return The estimated enthalpy value
     */
    private double calculateEnthalpy(View view, MotionEvent motionEvent) {
        return (motionEvent.getX() / view.getWidth()) * (MAX_ENTHALPY - MIN_ENTHALPY);
    }

    /**
     * Returns an estimated pressure value based on the location of the user's touch.
     *
     * @param view The view that is currently being displayed
     * @param motionEvent The motion event created when the user touches the screen
     * @return The estimated pressure value
     */
    private double calculatePressure(View view, MotionEvent motionEvent) {
        return MIN_PRESSURE * Math.pow((MAX_PRESSURE / MIN_PRESSURE), ((view.getHeight() - motionEvent.getY()) / view.getHeight()));
    }
}
