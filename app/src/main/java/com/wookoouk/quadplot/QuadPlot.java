package com.wookoouk.quadplot;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.o3dr.android.client.ControlTower;
import com.o3dr.android.client.Drone;
import com.o3dr.android.client.interfaces.DroneListener;
import com.o3dr.android.client.interfaces.TowerListener;
import com.o3dr.services.android.lib.drone.attribute.AttributeEvent;
import com.o3dr.services.android.lib.drone.connection.ConnectionResult;
import com.o3dr.services.android.lib.util.Utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

interface USBConnectionChangedListener {
    void onUSBConnectionChanged();
}

interface DroneConnectionChangedListener {
    void onDroneConnectionChanged();
}

public class QuadPlot extends Application implements DroneListener, TowerListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    static ArrayList<Plot> plots = new ArrayList<>();
//    static ArrayList<String> listItems = new ArrayList<>();
//    static ArrayList<Location> locations = new ArrayList<>();

    private static Boolean USBConnected = false;
    private static Boolean DroneConnected = false;

    private static List<USBConnectionChangedListener> usbListeners = new ArrayList<USBConnectionChangedListener>();
    private static List<DroneConnectionChangedListener> droneListeners = new ArrayList<DroneConnectionChangedListener>();

    private static final String ACTION_DRONE_CONNECTION_FAILED = Utils.PACKAGE_NAME
            + ".ACTION_DRONE_CONNECTION_FAILED";
    private static final String EXTRA_CONNECTION_FAILED_ERROR_CODE = "extra_connection_failed_error_code";
    private static final String EXTRA_CONNECTION_FAILED_ERROR_MESSAGE = "extra_connection_failed_error_message";
    public static final long EVENTS_DISPATCHING_PERIOD = 200L; //MS
    public static final long DELAY_TO_DISCONNECTION = 1000L; // ms
    static final int DEFAULT_USB_BAUD_RATE = 57600;
//    private final List<ApiListener> apiListeners = new ArrayList<ApiListener>();

    private final Handler handler = new Handler();
    private final Map<String, Bundle> eventsBuffer = new LinkedHashMap<>(200);
    public static Drone drone;
    private static ControlTower controlTower;
    private LocalBroadcastManager lbm;

    static boolean GetIsUSBConnected() {
        return USBConnected;
    }

    static boolean GetIsDroneConnected() {
        return DroneConnected;
    }

    private static void SetUSBConnected(boolean value) {
        USBConnected = value;

        for (USBConnectionChangedListener l : usbListeners) {
            l.onUSBConnectionChanged();
        }
    }

    private static void SetDroneConnected(boolean value) {
        DroneConnected = value;

        for (DroneConnectionChangedListener l : droneListeners) {
            l.onDroneConnectionChanged();
        }
    }

    static void addUSBConnectionListener(USBConnectionChangedListener l) {
        usbListeners.add(l);
    }

    static void addDroneConnectionListener(DroneConnectionChangedListener l) {
        droneListeners.add(l);
    }


    @Override
    public void onCreate() {
        super.onCreate();

        final Context context = getApplicationContext();

        controlTower = new ControlTower(context);
        drone = new Drone(); // Later version will use context
        lbm = LocalBroadcastManager.getInstance(context);

//        final IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(ACTION_TOGGLE_DRONE_CONNECTION);
//        registerReceiver(broadcastReceiver, intentFilter);


        //TODO
        controlTower.connect(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        if (drone.isConnected()) {
            drone.disconnect();
//            updateConnectedButton(false);
        }

        controlTower.unregisterDrone(drone);
        controlTower.disconnect();
    }

    @Override
    public void onDroneConnectionFailed(ConnectionResult result) {
        String errorMsg = result.getErrorMessage();
        Toast.makeText(getApplicationContext(), "Connection failed: " + errorMsg,
                Toast.LENGTH_LONG).show();

        lbm.sendBroadcast(new Intent(ACTION_DRONE_CONNECTION_FAILED)
                .putExtra(EXTRA_CONNECTION_FAILED_ERROR_CODE, result.getErrorCode())
                .putExtra(EXTRA_CONNECTION_FAILED_ERROR_MESSAGE, result.getErrorMessage()));
    }

    @Override
    public void onDroneEvent(String event, Bundle extras) {
        switch (event) {
            case AttributeEvent.STATE_CONNECTED: {
                SetDroneConnected(true);

                Toast.makeText(getApplicationContext(), "Drone Connected",
                        Toast.LENGTH_LONG).show();

                break;
            }

            case AttributeEvent.STATE_DISCONNECTED: {
                SetDroneConnected(false);

                Toast.makeText(getApplicationContext(), "Drone Disconnected",
                        Toast.LENGTH_LONG).show();

                break;
            }

            default: {
                break;
            }
        }
    }


    @Override
    public void onDroneServiceInterrupted(String errorMsg) {
        controlTower.unregisterDrone(drone);

        if (!TextUtils.isEmpty(errorMsg))
            Log.e(TAG, errorMsg);
    }

    @Override
    public void onTowerConnected() {

        drone.unregisterDroneListener(this);

        controlTower.registerDrone(drone, handler);
        drone.registerDroneListener(this);


        Toast.makeText(getApplicationContext(), "Tower Connected",
                Toast.LENGTH_LONG).show();

        SetUSBConnected(true);
    }

    @Override
    public void onTowerDisconnected() {
        SetUSBConnected(false);
        Toast.makeText(getApplicationContext(), "Tower Disconnected",
                Toast.LENGTH_LONG).show();
    }
}