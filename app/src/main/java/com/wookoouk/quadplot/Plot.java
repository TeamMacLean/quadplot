package com.wookoouk.quadplot;

import android.location.Location;

class Plot {

    private double height;
    private Location location;

    Plot(double height, Location location) {
        this.height = height;
        this.location = location;
    }

    public String getName() {
        return "Plot" + QuadPlot.plots.indexOf(this);
    }

    Location getLocation() {
        return location;
    }

    public double getHeight() {
        return height;
    }
}
