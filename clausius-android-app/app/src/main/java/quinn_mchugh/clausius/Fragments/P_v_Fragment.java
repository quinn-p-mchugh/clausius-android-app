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
 * Represents a fragment containing the P-v diagram.
 */
public class P_v_Fragment extends Fragment implements View.OnTouchListener {

    private static final double MAX_SPECIFIC_VOLUME = 37.5;
    private static final double MIN_SPECIFIC_VOLUME = 0.001;
    private static final double MAX_PRESSURE = 51.70;
    private static final double MIN_PRESSURE = 0.01;

    public P_v_Fragment() {
        // Required empty public constructor
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
            case R.id.p_v_diagram:  // If the T-s diagram is touched...
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
    public void setDiagramValues(View view, MotionEvent motionEvent) {

        double xTouch = motionEvent.getX();
        double yTouch = motionEvent.getY();

        double viewWidth = view.getWidth();
        double viewHeight = view.getHeight();

        double specificVolume = MIN_SPECIFIC_VOLUME *Math.pow((MAX_SPECIFIC_VOLUME /0.001), (xTouch / viewWidth));
        double pressure = MIN_PRESSURE *Math.pow((MAX_PRESSURE / MIN_PRESSURE), ((viewHeight - yTouch) / viewHeight));

        DecimalFormat numFormatLarge = new DecimalFormat("#0.000");
        DecimalFormat numFormatSmall = new DecimalFormat("0.0000");

        ((MainActivity) getActivity()).getSpecificVolume().setText(String.valueOf(numFormatSmall.format(specificVolume)));
        ((MainActivity) getActivity()).getPressure().setText(String.valueOf(numFormatLarge.format(pressure)));
    }
}
