package com.wookoouk.quadplot;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import timber.log.Timber;


public class QuadPlot extends Application implements DroneListener, TowerListener {

    private static final String TAG = MainActivity.class.getSimpleName();


    private static final String ACTION_DRONE_CONNECTION_FAILED = Utils.PACKAGE_NAME
            + ".ACTION_DRONE_CONNECTION_FAILED";
    private static final String EXTRA_CONNECTION_FAILED_ERROR_CODE = "extra_connection_failed_error_code";
    private static final String EXTRA_CONNECTION_FAILED_ERROR_MESSAGE = "extra_connection_failed_error_message";
    private static final long EVENTS_DISPATCHING_PERIOD = 200L; //MS
    private static final long DELAY_TO_DISCONNECTION = 1000L; // ms
    private static final int DEFAULT_USB_BAUD_RATE = 57600;
//    private final List<ApiListener> apiListeners = new ArrayList<ApiListener>();

    private final Handler handler = new Handler();
    private final Map<String, Bundle> eventsBuffer = new LinkedHashMap<>(200);
    private Drone drone;
    private ControlTower controlTower;
    private LocalBroadcastManager lbm;


//    private final Runnable eventsDispatcher = new Runnable() {
//        @Override
//        public void run() {
//            handler.removeCallbacks(this);
//
//            //Go through the events buffer and empty it
//            for (Map.Entry<String, Bundle> entry : eventsBuffer.entrySet()) {
//                String event = entry.getKey();
//                Bundle extras = entry.getValue();
//
//                final Intent droneIntent = new Intent(event);
//                if (extras != null)
//                    droneIntent.putExtras(extras);
//                lbm.sendBroadcast(droneIntent);
//            }
//
//            eventsBuffer.clear();
//
//            handler.postDelayed(this, EVENTS_DISPATCHING_PERIOD);
//        }
//    };

//    private final Runnable disconnectionTask = new Runnable() {
//        @Override
//        public void run() {
//            Timber.d("Starting control tower disconnect process...");
//            controlTower.unregisterDrone(drone);
//            controlTower.disconnect();
//
//            handler.removeCallbacks(this);
//        }
//    };

//    private void shouldWeTerminate() {
//        if (apiListeners.isEmpty() && !drone.isConnected()) {
//            // Wait 30s, then disconnect the service binding.
//            handler.postDelayed(disconnectionTask, DELAY_TO_DISCONNECTION);
//        }
//    }

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
//                handler.removeCallbacks(disconnectionTask);
//                startService(new Intent(getApplicationContext(), QuadService.class));
//
//                final boolean isReturnToMeOn = dpPrefs.isReturnToMeEnabled();
//                VehicleApi.getApi(drone).enableReturnToMe(isReturnToMeOn, new AbstractCommandListener() {
//                    @Override
//                    public void onSuccess() {
//                        Timber.i("Return to me %s successfully.", isReturnToMeOn ? "started" : "stopped");
//                    }
//
//                    @Override
//                    public void onError(int i) {
//                        Timber.e("%s return to me failed: %d", isReturnToMeOn ? "Starting" : "Stopping", i);
//                    }
//
//                    @Override
//                    public void onTimeout() {
//                        Timber.w("%s return to me timed out.", isReturnToMeOn ? "Starting" : "Stopping");
//                    }
//                });
//
//                final Intent droneIntent = new Intent(event);
//                if (extras != null)
//                    droneIntent.putExtras(extras);
//                lbm.sendBroadcast(droneIntent);
//
//                handler.postDelayed(eventsDispatcher, EVENTS_DISPATCHING_PERIOD);
                break;
            }

            case AttributeEvent.STATE_DISCONNECTED: {
//                handler.removeCallbacks(eventsDispatcher);
//
//                shouldWeTerminate();
//
//                final Intent droneIntent = new Intent(event);
//                if (extras != null)
//                    droneIntent.putExtras(extras);
//                lbm.sendBroadcast(droneIntent);
                break;
            }

            default: {
                //Buffer the remaining events, and only fire them at 30hz
                //TODO: remove this once the buffer is placed on the 3DR Services side
//                eventsBuffer.put(event, extras);
                break;
            }
        }
    }

//    private void notifyApiConnected() {
//        if (apiListeners.isEmpty())
//            return;
//
//        for (ApiListener listener : apiListeners)
//            listener.onApiConnected();
//    }
//
//    private void notifyApiDisconnected() {
//        if (apiListeners.isEmpty())
//            return;
//
//        for (ApiListener listener : apiListeners)
//            listener.onApiDisconnected();
//    }

    @Override
    public void onDroneServiceInterrupted(String errorMsg) {
        Timber.d("Drone service interrupted: %s", errorMsg);
        controlTower.unregisterDrone(drone);

        if (!TextUtils.isEmpty(errorMsg))
            Log.e(TAG, errorMsg);
    }

    @Override
    public void onTowerConnected() {
        Timber.d("Connecting to the control tower.");

        drone.unregisterDroneListener(this);

        controlTower.registerDrone(drone, handler);
        drone.registerDroneListener(this);

//        notifyApiConnected();
    }

    @Override
    public void onTowerDisconnected() {
        Timber.d("Disconnection from the control tower.");
//        notifyApiDisconnected();
    }
}