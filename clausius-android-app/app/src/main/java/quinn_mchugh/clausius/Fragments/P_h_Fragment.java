package quinn_mchugh.clausius.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.text.DecimalFormat;

import quinn_mchugh.clausius.MainActivity;
import quinn_mchugh.clausius.R;

/**
 * Represents a fragment containing the P-h diagram.
 */
public class P_h_Fragment extends Fragment implements View.OnTouchListener {

    private static final double MAX_ENTHALPY = 4505;
    private static final double MAX_PRESSURE = 1000;
    private static final double MIN_PRESSURE = 0.01;

    public P_h_Fragment() {
        // Required empty public constructor
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
     * Sets the values of the thermodynamic property table.
     *
     * @param view The view that is currently being displayed.
     * @param motionEvent The motionEvent created when the user touches the screen.
     */
    public void setPropertyValues(View view, MotionEvent motionEvent) {
        double xTouch = motionEvent.getX();
        double yTouch = motionEvent.getY();

        double viewWidth = view.getWidth();
        double viewHeight = view.getHeight();

        double enthalpy = (xTouch / viewWidth) * MAX_ENTHALPY;
        double pressure = MIN_PRESSURE *Math.pow((MAX_PRESSURE / MIN_PRESSURE), ((viewHeight - yTouch) / viewHeight));

        DecimalFormat numFormatLarge = new DecimalFormat("#0.0");
        DecimalFormat numFormatSmall = new DecimalFormat("#0.000");

        ((MainActivity) getActivity()).getEnthalpy().setText(String.valueOf(numFormatLarge.format(enthalpy)));
        ((MainActivity) getActivity()).getPressure().setText(String.valueOf(numFormatSmall.format(pressure)));
    }
}
