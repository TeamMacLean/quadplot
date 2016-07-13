package com.wookoouk.quadplot;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;


import com.google.android.gms.analytics.HitBuilders;
import com.o3dr.android.client.Drone;
import com.o3dr.android.client.apis.ControlApi;
import com.o3dr.android.client.apis.MissionApi;
import com.o3dr.android.client.apis.VehicleApi;
import com.o3dr.services.android.lib.coordinate.LatLongAlt;
import com.o3dr.services.android.lib.drone.connection.ConnectionParameter;
import com.o3dr.services.android.lib.drone.connection.ConnectionType;
import com.o3dr.services.android.lib.drone.connection.DroneSharePrefs;
import com.o3dr.services.android.lib.drone.mission.Mission;
import com.o3dr.services.android.lib.drone.mission.item.MissionItem;
import com.o3dr.services.android.lib.drone.mission.item.command.ReturnToLaunch;
import com.o3dr.services.android.lib.drone.mission.item.command.Takeoff;
import com.o3dr.services.android.lib.drone.mission.item.spatial.Land;
import com.o3dr.services.android.lib.drone.mission.item.spatial.Waypoint;
import com.o3dr.services.android.lib.drone.property.VehicleMode;
import com.o3dr.services.android.lib.model.AbstractCommandListener;

import java.util.List;

class UploadFragment extends Fragment {

    private boolean ok_connected;
    private boolean ok_plots;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.upload, container, false);

        CheckBox connectedCB = (CheckBox) view.findViewById(R.id.checkBox_connected);
        CheckBox plotsCB = (CheckBox) view.findViewById(R.id.checkBox_plots);

        ok_connected = QuadPlot.getIsDroneConnected();
        ok_plots = QuadPlot.plots.size() > 0;

        connectedCB.setChecked(ok_connected);
        plotsCB.setChecked(ok_plots);

        Button uploadButton = (Button) view.findViewById(R.id.upload_button);
        uploadButton.setEnabled(ok_connected && ok_plots);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ok_connected) {
                    if (ok_plots) {
                        sendMissionToAPM();
                    } else {
                        Toast.makeText(getContext(), "No plots defined", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getContext(), "No drone attached", Toast.LENGTH_LONG).show();
                }

            }
        });


        return view;
//        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private Mission createMission() {

        Mission currentMission = new Mission();
        currentMission.clear();

        //take off
        Takeoff takeOff = new Takeoff();
        takeOff.setTakeoffAltitude(10); //TODO default option
        currentMission.addMissionItem(takeOff);

        Toast.makeText(getContext(), "adding plots", Toast.LENGTH_SHORT).show();

        for (Plot p : QuadPlot.plots) {
            Waypoint waypoint = new Waypoint();
//            double alt = p.getAlt();
            double alt = p.getHeight();
            waypoint.setCoordinate(new LatLongAlt(p.getLocation().getLatitude(), p.getLocation().getLongitude(),
                    (float) alt));
            currentMission.addMissionItem(waypoint);
        }


        //return to launch
        ReturnToLaunch rtl = new ReturnToLaunch();
        currentMission.addMissionItem(rtl);

        return currentMission;
    }


    private void sendMissionToAPM() {
        Mission m = createMission();
        MissionApi.getApi(QuadPlot.drone).setMission(m, true); //test 2 (new)
    }


}
