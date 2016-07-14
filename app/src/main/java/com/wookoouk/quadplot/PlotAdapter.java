package com.wookoouk.quadplot;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
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

    private void deletePlot(int position) {
        QuadPlot.plots.remove(position);
        this.notifyDataSetChanged();
    }


    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

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


        LinearLayout deleteLayout = (LinearLayout) row.findViewById(R.id.plot_delete_view);
        deleteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletePlot(position);
            }
        });

        Plot plot = data.get(position);

        holder.textView1.setText(plot.getName());
        holder.textView2.setText("Crop Height: " + plot.getHeight() + getContext().getString(R.string.distance_unit_short));

        return row;
    }

    private static class ViewHolder {
        TextView textView1;
        TextView textView2;
    }
}