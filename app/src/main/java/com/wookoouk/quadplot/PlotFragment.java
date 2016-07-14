package com.wookoouk.quadplot;

import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;


class PlotFragment extends Fragment {
    private ListView mListView;
    private PlotAdapter adapter;

    private TextView gpsStatus = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }


    private void addPlot(View v) {

        SeekBar seek = (SeekBar) v.findViewById(R.id.crop_seekBar);

        int height = seek.getProgress() / 10;

        QuadPlot.plots.add(new Plot(height, QuadPlot.getCurrentLocation()));


        try {
            Util.storePlots(getContext());
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
//            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//            //nothing
//                }
//            });
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


        QuadPlot.addLoadionListener(new LocationChangedListener() {
            @Override
            public void onLocationChanged() {
                if (gpsStatus != null) {
                    gpsStatus.setText("GPS: 68% accurate to " + QuadPlot.getCurrentLocation().getAccuracy() + getContext().getString(R.string.distance_unit_short));
                }
            }
        });


        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View clickView) {

                Location currentLocation = QuadPlot.getCurrentLocation();

                if (currentLocation != null) {
                    if (currentLocation.getAccuracy() < QuadPlot.MINIMUM_GPS_ACCURACY) {

                        final View alertView = inflater.inflate(R.layout.crop_height, new LinearLayout(getContext()), false);

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                        final TextView inM = (TextView) alertView.findViewById(R.id.height);

                        SeekBar sb = (SeekBar) alertView.findViewById(R.id.crop_seekBar);
                        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                                double converted = i / 10;

                                inM.setText("" + converted + R.string.distance_unit_short);
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
                                });
//                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                    }
//                                });

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    } else {
                        Snackbar.make(view, "Accuracy " + currentLocation.getAccuracy() + " of minimum " + QuadPlot.MINIMUM_GPS_ACCURACY, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                } else {
                    Snackbar.make(view, R.string.gps_connecting, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }
        });

        adapter = new PlotAdapter(view.getContext(), R.layout.plot_row, QuadPlot.plots);

        mListView.setAdapter(adapter);

        return view;
    }


}

