package com.wookoouk.quadplot;

import android.location.Location;

public class Plot {

    private int height;
    private Location location;
//    private String name;

    Plot(int height, Location location) {
//        this.name = name;
        this.height = height;
        this.location = location;
    }

    public String getName() {

        return "Plot" + QuadPlot.plots.indexOf(this);

//        return name;
    }

    public Location getLocation() {
        return location;
    }

    public int getHeight() {
        return height;
    }
}
