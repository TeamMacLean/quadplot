package com.wookoouk.quadplot;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.o3dr.services.android.lib.drone.connection.ConnectionParameter;
import com.o3dr.services.android.lib.drone.connection.ConnectionType;

class ConnectFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.connect, container, false);
        final FloatingActionButton connectButton = (FloatingActionButton) view.findViewById(R.id.connectButton);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBtnConnectTap();
            }
        });

        QuadPlot.addDroneConnectionListener(new DroneConnectionChangedListener() {
            @Override
            public void onDroneConnectionChanged() {
                if (QuadPlot.getIsDroneConnected()) {
                    connectButton.setImageResource(R.drawable.connected);
                } else {
                    connectButton.setImageResource(R.drawable.disconnected);
                }

            }
        });

        return view;
    }

    private void onBtnConnectTap() {
        if (QuadPlot.drone.isConnected()) {
            QuadPlot.drone.disconnect();
        } else {
            Bundle extraParams = new Bundle();
            extraParams.putInt(ConnectionType.EXTRA_USB_BAUD_RATE, QuadPlot.DEFAULT_USB_BAUD_RATE);
            final ConnectionParameter connParams = new ConnectionParameter(ConnectionType.TYPE_USB, extraParams, null);
            QuadPlot.drone.connect(connParams);
        }
    }
}
