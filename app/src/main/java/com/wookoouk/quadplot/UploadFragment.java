package com.wookoouk.quadplot;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;


import com.o3dr.android.client.apis.MissionApi;
import com.o3dr.android.client.apis.VehicleApi;
import com.o3dr.services.android.lib.coordinate.LatLongAlt;
import com.o3dr.services.android.lib.drone.mission.Mission;
import com.o3dr.services.android.lib.drone.mission.item.command.ReturnToLaunch;
import com.o3dr.services.android.lib.drone.mission.item.command.Takeoff;
import com.o3dr.services.android.lib.drone.mission.item.spatial.Land;
import com.o3dr.services.android.lib.drone.mission.item.spatial.Waypoint;
import com.o3dr.services.android.lib.drone.property.VehicleMode;
import com.o3dr.services.android.lib.model.AbstractCommandListener;

class UploadFragment extends Fragment {
    private Mission currentMission;

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
                        createMission();
                        uploadMission();
//                        start();
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

    private void createMission() {

        currentMission = new Mission();
        currentMission.clear();

//        MissionItem takeOff = new MissionItem();

        //take off
        Takeoff takeOff = new Takeoff();
        currentMission.addMissionItem(takeOff);

        for (Plot p : QuadPlot.plots) {


            Waypoint waypoint = new Waypoint();
            double alt = p.getAlt();
            waypoint.setCoordinate(new LatLongAlt(p.getLocation().getLatitude(), p.getLocation().getLongitude(),
                    (float) alt));

            currentMission.addMissionItem(waypoint);

        }


        //return to launch (hover)
        ReturnToLaunch rtl = new ReturnToLaunch();
        currentMission.addMissionItem(rtl);
        //land
        Land land = new Land();
        currentMission.addMissionItem(land);
    }

    private void start() {
        VehicleApi.getApi(QuadPlot.drone).arm(true);
        VehicleApi.getApi(QuadPlot.drone).setVehicleMode(VehicleMode.COPTER_AUTO, new AbstractCommandListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(getContext(), "mode changed", Toast.LENGTH_LONG).show();
                MissionApi.getApi(QuadPlot.drone).startMission(true, false, new AbstractCommandListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getContext(), "mission started", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(int i) {
                        Toast.makeText(getContext(), "mission failed", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onTimeout() {
                    }
                });
//                missionFinished = false;
            }

            @Override
            public void onError(int i) {
                Toast.makeText(getContext(), "mode change error", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onTimeout() {
            }
        });


//        VehicleApi.getApi(QuadPlot.drone).arm(true);
//        MissionApi.getApi(QuadPlot.drone).startMission(true, true, new AbstractCommandListener() {
//            @Override
//            public void onSuccess() {
//                Toast.makeText(getContext(), "flight started", Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onError(int executionError) {
//                Toast.makeText(getContext(), "flight error", Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onTimeout() {
//                Toast.makeText(getContext(), "flight timeout", Toast.LENGTH_LONG).show();
//            }
//        });
    }


    private void uploadMission() {
//        ProgressDialog progressDialog = new ProgressDialog(getContext());
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progressDialog.setTitle("Uploading...");
//        progressDialog.setMessage("Uploading Mission, please wait...");
//        progressDialog.setCancelable(false);
//        progressDialog.show();
        MissionApi.getApi(QuadPlot.drone).setMission(currentMission, true);
        Toast.makeText(getContext(), "uploaded mission", Toast.LENGTH_SHORT).show();
//        progressDialog.dismiss();
    }


}
