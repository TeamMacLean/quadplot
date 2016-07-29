package com.wookoouk.quadplot;

import android.content.Context;
import android.location.Location;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

class Util {

    static void storePlots(Context context) throws JSONException {
        try {
            JSONArray jsonArray = new JSONArray();

            if (QuadPlot.plots.size() > 0) {

                for (Plot plot : QuadPlot.plots) {

                    JSONObject jsonObject = new JSONObject();

                    Location l = plot.getLocation();
                    double plotHeight = plot.getPlotHeight();

                    jsonObject.put("height", plotHeight);
                    jsonObject.put("latitude", l.getLatitude());
                    jsonObject.put("longitude", l.getLongitude());

                    jsonArray.put(jsonObject);
                }


                String jsonStr = jsonArray.toString();

                PreferenceManager.getDefaultSharedPreferences(context).edit().putString("plots", jsonStr).apply();
            }
        } catch (Throwable e) {
            Toast.makeText(context, "Could not save plots", Toast.LENGTH_SHORT).show();
            Log.d("ERROR", "Could save plots");
        }
    }

    static void loadPlots(Context context) throws JSONException {
        try {
            String jsonStr = PreferenceManager.getDefaultSharedPreferences(context).getString("plots", "[]");

            ArrayList<Plot> tmpPlots = new ArrayList<>();

            JSONArray jsonArray = new JSONArray(jsonStr);

            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {


                    JSONObject jsonObject = jsonArray.getJSONObject(i);


                    double height = jsonObject.getDouble("height");
                    double lat = jsonObject.getDouble("latitude");
                    double lon = jsonObject.getDouble("longitude");

                    Location loc = new Location("gps");
                    loc.setLatitude(lat);
                    loc.setLongitude(lon);

                    Plot p = new Plot(height, loc);
                    tmpPlots.add(p);


                }
                QuadPlot.plots = tmpPlots;
            }
        } catch (Throwable e) {
            Toast.makeText(context, "Could not load previous plots", Toast.LENGTH_SHORT).show();
            Log.d("ERROR", "Could not load previous plots," + e.getMessage());
        }
    }

    public static boolean hasSeenIntro(Context context) {
        Boolean seenIntro = false;
        try {
            seenIntro = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("seenIntro", false);
        } catch (Throwable e) {
            Log.d("ERROR", "Could not load preference");
        }
        return seenIntro;
    }

    static void hasSeenIntro(Context context, Boolean has) {

        try {
            PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("seenIntro", has).apply();
        } catch (Throwable e) {
            Log.d("ERROR", "Could not save preference");
        }

    }

}
