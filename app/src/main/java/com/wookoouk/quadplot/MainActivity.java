package com.wookoouk.quadplot;

import com.o3dr.android.client.ControlTower;
import com.o3dr.android.client.Drone;
import com.o3dr.android.client.interfaces.DroneListener;
import com.o3dr.android.client.interfaces.TowerListener;
import com.o3dr.services.android.lib.drone.attribute.AttributeEvent;
import com.o3dr.services.android.lib.drone.connection.ConnectionResult;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity implements DroneListener, TowerListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int MinimumGPSAccuracy = 50; //lower is better
    private ArrayAdapter<String> adapter;
    private ListView mListView;
    private TextView gpsText;
    private Location currentLocation;
//    private FloatingActionButton fab;
    private ArrayList<String> listItems = new ArrayList<String>();
    private ArrayList<Location> locations = new ArrayList<Location>();
    private Drone drone;
    private ControlTower controlTower;
    private final Handler handler = new Handler();
    private static final int DEFAULT_USB_BAUD_RATE = 57600;



    private void viewConnect() {
        // Create a new Fragment to be placed in the activity layout
        ConnectFragment firstFragment = new ConnectFragment();

        // In case this activity was started with special instructions from an
        // Intent, pass the Intent's extras to the fragment as arguments
        firstFragment.setArguments(getIntent().getExtras());

        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, firstFragment)
                .commit();
    }

    private void updateConnectedButton(Boolean isConnected) {

//        if (viewingSetup) {
//            FloatingActionButton connectButton = (FloatingActionButton) findViewById(R.id.btnConnect);
//            if (isConnected) {
//                connectButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_network_connected_icon_36dp));
//                if (!viewingPlan) {
//                    viewPlan();
//                }
//            } else {
//                connectButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_network_disconnected_icon_36dp));
//                if (!viewingSetup) {
//                    viewSetup();
//                }
//            }
//        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);

        //Title and subtitle
        toolbar.setTitle("QuadPlot");
        toolbar.setSubtitle("Please don't crash...");
        toolbar.setNavigationIcon(R.drawable.quadplot);

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Go to this view first
            viewConnect();

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
//        if (this.controlTower == null) {
//            this.controlTower.connect(this);
//        }

    }

    @Override
    public void onStop() {
        super.onStop();
//        if (this.drone.isConnected()) {
//            this.drone.disconnect();
//            updateConnectedButton(false);
//        }
//
//        this.controlTower.unregisterDrone(this.drone);
//        this.controlTower.disconnect();
    }

    @Override
    public void onDroneConnectionFailed(ConnectionResult result) {
        alertUser("Connection Failed:" + result.getErrorMessage());
        updateConnectedButton(this.drone.isConnected());
    }

    @Override
    public void onDroneEvent(String event, Bundle extras) {

        switch (event) {
            case AttributeEvent.STATE_CONNECTED:
                alertUser("Drone Connected");
                updateConnectedButton(this.drone.isConnected());
                break;

            case AttributeEvent.STATE_DISCONNECTED:
                alertUser("Drone Disconnected");
                updateConnectedButton(this.drone.isConnected());
                break;

            default:
                Log.i("DRONE_EVENT", event); //Uncomment to see events from the drone
                break;
        }
    }

    @Override
    public void onDroneServiceInterrupted(String errorMsg) {
        updateConnectedButton(this.drone.isConnected());
    }

    @Override
    public void onTowerConnected() {
        alertUser("3DR Services Connected");
        this.controlTower.registerDrone(this.drone, this.handler);
        this.drone.registerDroneListener(this);
        updateConnectedButton(this.drone.isConnected());
    }

    @Override
    public void onTowerDisconnected() {
        updateConnectedButton(this.drone.isConnected());
        alertUser("3DR Service Interrupted");
    }

    private void alertUser(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        Log.d(TAG, message);
    }
}