package quinn_mchugh.clausius;

import android.graphics.Rect;

import android.os.Bundle;
import android.os.Handler;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.text.Layout;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import java.text.DecimalFormat;

import quinn_mchugh.clausius.Fragments.P_h_Fragment;
import quinn_mchugh.clausius.Fragments.P_v_Fragment;
import quinn_mchugh.clausius.Fragments.T_s_Fragment;

/**
 * Handles menu button actions, setting of table values, and switching between fragments.
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener {

    /* Icons used to create diagram cursor */
    private ImageView cursorIcon;
    private ImageView cursorIconSmall;
    private ImageView cursorIconTransition;

    /* Thermodynamic properties */
    private TextView temperature;
    private TextView pressure;
    private TextView specificVolume;
    private TextView internalEnergy;
    private TextView enthalpy;
    private TextView entropy;
    private TextView quality;

    private enum LayoutPosition {
         LEFT, RIGHT
    }

    private enum FragmentTag {
        T_S_FRAGMENT, P_H_FRAGMENT, P_V_FRAGMENT
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Initialize all fragments and start with the T-s fragment displayed. */
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, new P_v_Fragment(), "P_v_fragment");
        fragmentTransaction.add(R.id.fragment_container, new P_h_Fragment(), "P_h_fragment");
        fragmentTransaction.add(R.id.fragment_container, new T_s_Fragment(), "t_s_fragment").commit();

        /* Initialize click listeners for floating action button menu. */
        FloatingActionButton t_s_menu_button = findViewById(R.id.t_s_menu_button);
        t_s_menu_button.setOnClickListener(this);
        FloatingActionButton p_h_menu_button = findViewById(R.id.p_h_menu_button);
        p_h_menu_button.setOnClickListener(this);
        FloatingActionButton p_v_menu_button = findViewById(R.id.p_v_menu_button);
        p_v_menu_button.setOnClickListener(this);

        /* Initialize cursor icons. */
        cursorIcon = findViewById(R.id.cursor_icon);
        cursorIconSmall = findViewById(R.id.cursor_icon_small);
        cursorIconTransition = findViewById(R.id.cursor_icon_transition);

        /* Initialize property value views */
        temperature = findViewById(R.id.temperature);
        pressure = findViewById(R.id.pressure);
        specificVolume = findViewById(R.id.specificVolume);
        internalEnergy = findViewById(R.id.internalEnergy);
        enthalpy = findViewById(R.id.enthalpy);
        entropy = findViewById(R.id.entropy);
        quality = findViewById(R.id.quality);
    }

    public TextView getTemperature() {
        return temperature;
    }

    public void setTemperature(TextView temperature) {
        this.temperature = temperature;
    }

    public TextView getPressure() {
        return pressure;
    }

    public void setPressure(TextView pressure) {
        this.pressure = pressure;
    }

    public TextView getSpecificVolume() {
        return specificVolume;
    }

    public void setSpecificVolume(TextView specificVolume) {
        this.specificVolume = specificVolume;
    }

    public TextView getInternalEnergy() {
        return internalEnergy;
    }

    public void setInternalEnergy(TextView internalEnergy) {
        this.internalEnergy = internalEnergy;
    }

    public TextView getEnthalpy() {
        return enthalpy;
    }

    public void setEnthalpy(TextView enthalpy) {
        this.enthalpy = enthalpy;
    }

    public TextView getEntropy() {
        return entropy;
    }

    public void setEntropy(TextView entropy) {
        this.entropy = entropy;
    }

    public TextView getQuality() {
        return quality;
    }

    public void setQuality(TextView quality) {
        this.quality = quality;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.t_s_menu_button:
                displayFragment(FragmentTag.P_H_FRAGMENT);
                moveLayout(R.id.diagram_selection_menu, LayoutPosition.LEFT);
                moveLayout(R.id.property_table, LayoutPosition.LEFT);
                break;
            case R.id.p_h_menu_button:
                displayFragment(FragmentTag.P_H_FRAGMENT);
                moveLayout(R.id.diagram_selection_menu, LayoutPosition.LEFT);
                moveLayout(R.id.property_table, LayoutPosition.LEFT);
                break;
            case R.id.p_v_menu_button:
                displayFragment(FragmentTag.P_V_FRAGMENT);
                moveLayout(R.id.diagram_selection_menu, LayoutPosition.RIGHT);
                moveLayout(R.id.property_table, LayoutPosition.RIGHT);
                break;
        }
    }

    /**
     * Updates the cursor's location to wherever the user touches on the screen.
     *
     * @param motionEvent The motionEvent created when the user touches the screen.
     */
    public void updateCursorLocation(MotionEvent motionEvent) {
        double xTouch = motionEvent.getRawX();
        double yTouch = motionEvent.getRawY();

        Rect rectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        float statusBarHeight = rectangle.top;

        /* Update each cursor image's X & Y position. */
        ImageView[] imageViews = {cursorIcon, cursorIconSmall, cursorIconTransition};
        for (ImageView imageView : imageViews) {
            if (imageView == cursorIconTransition) {
                imageView.setX((float) (xTouch - 0.6 * imageView.getWidth()));
            }
            else {
                imageView.setX((float) (xTouch - 0.5 * imageView.getWidth()));
            }
            imageView.setY((float) (yTouch - statusBarHeight - 0.5 * imageView.getHeight()));
        }
    }

    /**
     * Sets the visibility of cursor icons when the user presses their finger down onto the screen.
     */
    public void cursorActionDown() {
        cursorIcon.setVisibility(View.VISIBLE);
        cursorIconSmall.setVisibility(View.INVISIBLE);
    }

    /**
     * Sets the visibility of cursor icons when the user takes their finger off the screen.
     */
    public void cursorActionUp() {
        cursorIcon.setVisibility(View.INVISIBLE);
        cursorIconTransition.setVisibility(View.VISIBLE);

        /* Delay is added to create transition effect */
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                cursorIconTransition.setVisibility(View.INVISIBLE);
                cursorIconSmall.setVisibility(View.VISIBLE);
            }
        }, 10);
    }

    /**
     * Displays a fragment on the screen that possesses a user-specified tag.
     *
     * @param fragmentTag The tag of the fragment that should be displayed.
     */
    public void displayFragment(FragmentTag fragmentTag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentByTag(fragmentTag.toString());

        if (fragment == null) {
            switch (fragmentTag) {
                case T_S_FRAGMENT:
                    fragment = new T_s_Fragment();
                    break;
                case P_H_FRAGMENT:
                    fragment = new P_h_Fragment();
                    break;
                case P_V_FRAGMENT:
                    fragment = new P_v_Fragment();
                    break;
            }
            fragmentTransaction.add(R.id.fragment_container, fragment, fragmentTag.toString()).commit();
        }
        else {
            fragmentTransaction.replace(R.id.fragment_container, fragment, fragmentTag.toString()).commit();
        }
    }

    /**
     * Moves a layout with a user-specified ID to a certain side of the screen.
     *
     * @param layoutId The ID of the layout that should be moved
     * @param layoutPosition The side of the screen that the layout should be moved to
     */
    public void moveLayout(int layoutId, LayoutPosition layoutPosition) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) findViewById(layoutId).getLayoutParams();

        switch (layoutPosition) {
            case LEFT:
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END, 0);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
                break;
            case RIGHT:
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_START, 0);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
                break;
        }
        findViewById(layoutId).setLayoutParams(layoutParams);
    }

    /**
     * Displays a pressure value on the thermodynamic property table on-screen.
     *
     * @param pressure The pressure value to be displayed on-screen
     */
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
        getPressure().setText(String.valueOf(df.format(pressure)));
    }

    /**
     * Displays a quality value on the thermodynamic property table on-screen..
     *
     * @param quality The quality value to be displayed on-screen
     */
    public void displayQuality(double quality) {
        getQuality().setText(String.valueOf((new DecimalFormat("#0.00")).format(quality * 100)));
    }

    /**
     * Displays a quality value on the thermodynamic property table on-screen.
     *
     * @param specificVolume The specific volume value to be displayed on-screen
     */
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
        getSpecificVolume().setText(String.valueOf(df.format(specificVolume)));
    }

    /**
     * Displays an internal energy value on the thermodynamic property table on-screen.
     *
     * @param internalEnergy The internal energy value to be displayed on-screen
     */
    public void displayInternalEnergy(double internalEnergy) {
        getInternalEnergy().setText(String.valueOf((new DecimalFormat("#0.00")).format(internalEnergy)));
    }

    /**
     * Displays a quality value on the thermodynamic property table on-screen.
     *
     * @param enthalpy The enthalpy value to be displayed on-screen
     */
    public void displayEnthalpy(double enthalpy) {
        getEnthalpy().setText(String.valueOf((new DecimalFormat("#0.00")).format(enthalpy)));
    }

    /**
     * Displays a temperature value on the thermodynamic property table on-screen.
     *
     * @param temperature The temperature value to be displayed on-screen
     */
    public void displayTemperature(double temperature) {
        getTemperature().setText(String.valueOf((new DecimalFormat("#0.00")).format(temperature)));
    }

    /**
     * Displays an entropy value on the thermodynamic property table on-screen.
     *
     * @param entropy The entropy value to be displayed on-screen
     */
    public void displayEntropy(double entropy) {
        getEntropy().setText(String.valueOf((new DecimalFormat("#0.00")).format(entropy)));
    }
}

