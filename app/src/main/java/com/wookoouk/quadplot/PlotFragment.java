package com.wookoouk.quadplot;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


public class PlotFragment extends Fragment {
    private ListView mListView;
    private PlotAdapter adapter;
    private Location currentLocation;
    private static final int MinimumGPSAccuracy = 150; //lower is better
    private TextView gpsStatus = null;

    //    }
    private void initGPS() {

        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {

            public void onLocationChanged(Location location) {
//                gpsText.setText(getText(R.string.gps_label) + " " + location.getAccuracy());
                currentLocation = location;


                if (gpsStatus != null) {
                    gpsStatus.setText("GPS: 68% accurate to " + Util.MtoF(location.getAccuracy()) + " Feet");
                }

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("ERROR", "COULD NOT USE GPS");
            if (gpsStatus != null) {
                gpsStatus.setText("Could not connect to GPS!");
            }
            return;
        }
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        } catch (SecurityException se) {
            se.printStackTrace();
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        initGPS();
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }


    private void addPlot(View v) {

        //TODO try and get crop height from View v

        SeekBar seek = (SeekBar) v.findViewById(R.id.crop_seekBar);

        int height = seek.getProgress();


        QuadPlot.plots.add(new Plot(height, currentLocation));

//        QuadPlot.listItems.add(QuadPlot.listItems.size(), "Plot " + (QuadPlot.locations.size()));
//                MainActivity.listItems.add(MainActivity.listItems.size() - 1, "Plot " + (MainActivity.locations.size() + 1));
//        QuadPlot.locations.add(currentLocation);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        menu.add("Remove All");
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (QuadPlot.plots.size() > 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Remove all plots");
            builder.setMessage("Are you sure?");

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    QuadPlot.plots.clear();
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //nothing
                }
            });
            builder.create().show();
        } else {
            Toast.makeText(getContext(), "No plots to remove", Toast.LENGTH_LONG).show();
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.plot, container, false);

        if (mListView == null) {
            mListView = (ListView) view.findViewById(R.id.plot_list);
        }

        gpsStatus = (TextView) view.findViewById(R.id.gps_status);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View clickView) {

                if (currentLocation != null) {
                    if (Util.MtoF(currentLocation.getAccuracy()) < MinimumGPSAccuracy) {

                        final View alertView = inflater.inflate(R.layout.crop_height, null);

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                        final TextView inM = (TextView) alertView.findViewById(R.id.height);

                        SeekBar sb = (SeekBar) alertView.findViewById(R.id.crop_seekBar);
                        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                                inM.setText(i + " feet");
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {

                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {

                            }
                        });

                        builder.setView(alertView)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        addPlot(alertView);
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //nothing
                                    }
                                });

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    } else {
                        Snackbar.make(view, currentLocation.getAccuracy() + " of minimum " + MinimumGPSAccuracy, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                } else {
                    Snackbar.make(view, "GPS Connecting...", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }
        });

        adapter = new PlotAdapter(view.getContext(), R.layout.plot_row, QuadPlot.plots);

        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });


        return view;
    }


}

