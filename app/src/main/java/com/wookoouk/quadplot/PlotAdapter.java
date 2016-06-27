package com.wookoouk.quadplot;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

class PlotAdapter extends ArrayAdapter<Plot> {
    private final Context context;
    private final ArrayList<Plot> data;
    private final int layoutResourceId;

    PlotAdapter(Context context, int layoutResourceId, ArrayList<Plot> data) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.data = data;
        this.layoutResourceId = layoutResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ViewHolder();
            holder.textView1 = (TextView) row.findViewById(R.id.plot_title);
            holder.textView2 = (TextView) row.findViewById(R.id.plot_height);

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        Plot plot = data.get(position);

        holder.textView1.setText(plot.getName());
        holder.textView2.setText("Crop Height: " + plot.getHeight() + " feet");

        return row;
    }

    private static class ViewHolder {
        TextView textView1;
        TextView textView2;
    }
}