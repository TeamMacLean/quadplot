package com.wookoouk.quadplot;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.o3dr.services.android.lib.drone.connection.ConnectionParameter;
import com.o3dr.services.android.lib.drone.connection.ConnectionType;

public class ConnectFragment extends Fragment {


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

        final ImageView iv = (ImageView) view.findViewById(R.id.tx_image);

        if (QuadPlot.GetIsUSBConnected()) {
            iv.setVisibility(View.INVISIBLE);
        } else {
            iv.setVisibility(View.VISIBLE);
        }

        QuadPlot.addUSBConnectionListener(new USBConnectionChangedListener() {
            @Override
            public void onUSBConnectionChanged() {

                if (QuadPlot.GetIsUSBConnected()) {
                    iv.setVisibility(View.INVISIBLE);
                } else {
                    iv.setVisibility(View.VISIBLE);
                }

            }
        });

        QuadPlot.addDroneConnectionListener(new DroneConnectionChangedListener() {
            @Override
            public void onDroneConnectionChanged() {
                if (QuadPlot.GetIsDroneConnected()) {
                    connectButton.setImageResource(R.drawable.connect);
                } else {
                    connectButton.setImageResource(R.drawable.disconnect);
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
            extraParams.putInt(ConnectionType.EXTRA_USB_BAUD_RATE, QuadPlot.DEFAULT_USB_BAUD_RATE); // Set default baud rate to 57600

            ConnectionParameter connectionParams = new ConnectionParameter(0, extraParams, null);
            QuadPlot.drone.connect(connectionParams);
        }
    }
}
