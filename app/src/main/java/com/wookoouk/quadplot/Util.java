package com.wookoouk.quadplot;

import android.content.SharedPreferences;
import android.location.Location;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

class Util {

    static int MtoF(int m) {
        return (int) (m * 3.28084);
    }

    static int MtoF(float m) {
        return (int) (Math.round(m) * 3.28084);
    }

//    static void storePlots(SharedPreferences mPrefs, ArrayList<Plot> locations) throws JSONException {
//        JSONArray ja = new JSONArray();
//
//        for (Plot p : locations) {
//            JSONObject jo = new JSONObject();
//            jo.put("latitude", p.getLocation().getLatitude());
//            jo.put("longitude", p.getLocation().getLongitude());
//            jo.put("altitude", p.getLocation().getAltitude());
//            ja.put(jo);
//        }
//
//
//        SharedPreferences.Editor prefsEditor = mPrefs.edit();
//        Gson gson = new Gson();
//        String json = gson.toJson(ja);
//        Log.d("QUAD", "STORING PLOTS!!!!");
//        prefsEditor.putString("savedPlots", json);
//        prefsEditor.apply();
//    }
//
//    static ArrayList<Plot> loadPlots(SharedPreferences mPrefs) throws JSONException {
//        Gson gson = new Gson();
//        String json = mPrefs.getString("savedPlots", "");
//
//        Log.d("QUAD", "LOADING PLOTS");
//
//        JSONArray ja = gson.fromJson(json, JSONArray.class);
//        if (ja == null) {
//            return null;
//        }
//
//        ArrayList<Plot> plots = new ArrayList<>();
//
//        for (int i = 0; i < ja.length(); i++) {
//            int latitude = ja.getJSONObject(i).getInt("latitude");
//            int longitude = ja.getJSONObject(i).getInt("longitude");
//            int altitude = ja.getJSONObject(i).getInt("altitude");
//
//            Log.d("QUAD", latitude + "");
//
//            Location targetLocation = new Location("");//provider name is unecessary
//            targetLocation.setLatitude(latitude);//your coords of course
//            targetLocation.setLongitude(longitude);
//
//            plots.add(new Plot(altitude, targetLocation));
//
//        }
//        return plots;
//    }


}
