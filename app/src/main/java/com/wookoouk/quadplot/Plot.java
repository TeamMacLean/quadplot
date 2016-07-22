package com.wookoouk.quadplot;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;

class Plot {

    private double plotHeight;
    private Location location;

    Plot(double plotHeight, Location location) {
        this.plotHeight = plotHeight;
        this.location = location;
    }

    public String getName() {
        return "Plot" + QuadPlot.plots.indexOf(this);
    }

    Location getLocation() {
        return location;
    }

    double getTotalHeight(Context context) {
        
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        double baseHeight = Integer.parseInt(sp.getString("base_height", "5")) * 1.0;

        return baseHeight + plotHeight;
    }

    double getPlotHeight() {
        return plotHeight;
    }
}
