package quinn_mchugh.clausius;

import android.graphics.Rect;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import quinn_mchugh.clausius.Fragments.P_h_Fragment;
import quinn_mchugh.clausius.Fragments.P_v_Fragment;
import quinn_mchugh.clausius.Fragments.T_s_Fragment;
import quinn_mchugh.clausius.Tables.CSVFile;

import com.github.clans.fab.FloatingActionButton;

/**
 * Handles menu button actions, setting of table values, and switching between fragments.
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener {

    // Icons used to create cursor
    private ImageView cursorIcon;
    private ImageView cursorIconSmall;
    private ImageView cursorIconTransition;

    // Thermodynamic properties
    private TextView temperature;
    private TextView pressure;
    private TextView specificVolume;
    private TextView internalEnergy;
    private TextView enthalpy;
    private TextView entropy;
    private TextView quality;

    // CSV Files and Data
    private CSVFile satTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize all fragments. and start with the T-s fragment displayed.
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, new P_v_Fragment(), "P_v_fragment");
        fragmentTransaction.add(R.id.fragment_container, new P_h_Fragment(), "P_h_fragment");
        fragmentTransaction.add(R.id.fragment_container, new T_s_Fragment(), "t_s_fragment").commit();

        // Initialize floating action menu click listeners.
        FloatingActionButton t_s_menu_button = findViewById(R.id.t_s_menu_button);
        t_s_menu_button.setOnClickListener(this);
        FloatingActionButton p_h_menu_button = findViewById(R.id.p_h_menu_button);
        p_h_menu_button.setOnClickListener(this);
        FloatingActionButton p_v_menu_button = findViewById(R.id.p_v_menu_button);
        p_v_menu_button.setOnClickListener(this);

        // Initialize cursor icons.
        cursorIcon = findViewById(R.id.cursor_icon);
        cursorIconSmall = findViewById(R.id.cursor_icon_small);
        cursorIconTransition = findViewById(R.id.cursor_icon_transition);

        // Initialize property values to their corresponding TextViews.
        temperature = findViewById(R.id.temperature);
        pressure = findViewById(R.id.pressure);
        specificVolume = findViewById(R.id.specificVolume);
        internalEnergy = findViewById(R.id.internalEnergy);
        enthalpy = findViewById(R.id.enthalpy);
        entropy = findViewById(R.id.entropy);
        quality = findViewById(R.id.quality);
    }

    public CSVFile getSatTable() {
        return satTable;
    }

    public CSVFile setSatTable(CSVFile satTable) {
        return this.satTable = satTable;
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
                displayFragment("t_s_fragment");
                moveLayout(R.id.diagram_selection_menu, "left");
                moveLayout(R.id.property_table, "left");
                break;
            case R.id.p_h_menu_button:
                displayFragment("p_h_fragment");
                moveLayout(R.id.diagram_selection_menu, "left");
                moveLayout(R.id.property_table, "left");
                break;
            case R.id.p_v_menu_button:
                displayFragment("p_v_fragment");
                moveLayout(R.id.diagram_selection_menu, "right");
                moveLayout(R.id.property_table, "right");
                break;
        }
    }

    /**
     * Updates the cursor's location to where the user touches on the screen.
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
     * Sets the visibility of cursor icons when the user takes their finger off of the screen.
     */
    public void cursorActionUp() {
        cursorIcon.setVisibility(View.INVISIBLE);
        cursorIconTransition.setVisibility(View.VISIBLE);

        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                cursorIconTransition.setVisibility(View.INVISIBLE);
                cursorIconSmall.setVisibility(View.VISIBLE);
            }
        }, 10);
    }

    /**
     * Displays a fragment onto the screen that possesses a user-specified tag.
     *
     * @param fragmentTag The tag of the fragment that should be displayed.
     */
    public void displayFragment(String fragmentTag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentByTag(fragmentTag);

        if (fragment == null) {
            switch (fragmentTag) {
                case "t_s_fragment":
                    fragment = new T_s_Fragment();
                    break;
                case "p_h_fragment":
                    fragment = new P_h_Fragment();
                    break;
                case "p_v_fragment":
                    fragment = new P_v_Fragment();
                    break;
            }
            fragmentTransaction.add(R.id.fragment_container, fragment, fragmentTag).commit();
        } else {
            fragmentTransaction.replace(R.id.fragment_container, fragment, fragmentTag).commit();
        }
    }

    /**
     * Moves a layout with a user-specified Id to a certain side of the screen.
     *
     * @param layoutId The ID of the layout that should be moved
     * @param direction The side of the screen that the layout should be moved to ("left", "right")
     */
    public void moveLayout(int layoutId, String direction) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) findViewById(layoutId).getLayoutParams();

        switch (direction) {
            case "left":
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END, 0);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
                break;
            case "right":
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_START, 0);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
                break;
        }
        findViewById(layoutId).setLayoutParams(layoutParams);
    }
}

