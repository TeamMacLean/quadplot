package com.wookoouk.quadplot;

import android.content.Context;
import android.location.Location;
import android.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

class Util {

    static void storePlots(Context context) throws JSONException {

        JSONArray jsonArray = new JSONArray();

        if (QuadPlot.plots.size() > 0) {

            for (Plot plot : QuadPlot.plots) {

                JSONObject jsonObject = new JSONObject();

                Location l = plot.getLocation();
                double height = plot.getHeight();

                jsonObject.put("height", height);
                jsonObject.put("latitude", l.getLatitude());
                jsonObject.put("longitude", l.getLongitude());

                jsonArray.put(jsonObject);
            }


            String jsonStr = jsonArray.toString();

            PreferenceManager.getDefaultSharedPreferences(context).edit().putString("plots", jsonStr).apply();
        }


    }

    static void loadPlots(Context context) throws JSONException {
//        String jsonStr = PreferenceManager.getDefaultSharedPreferences(context).getString("plots", "[]");
//
//        ArrayList<Plot> tmpPlots = new ArrayList<>();
//
//        JSONArray jsonArray = new JSONArray(jsonStr);
//
//        if (jsonArray.length() > 0) {
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject jsonObject = jsonArray.getJSONObject(i);
//
//                double height = (double) jsonObject.get("height");
//                double lat = (double) jsonObject.get("latitude");
//                double lon = (double) jsonObject.get("longitude");
//
//                Location loc = new Location("gps");
//                loc.setLatitude(lat);
//                loc.setLongitude(lon);
//
//                Plot p = new Plot(height, loc);
//                tmpPlots.add(p);
//
//            }
//            QuadPlot.plots = tmpPlots;
//        }
    }

}
