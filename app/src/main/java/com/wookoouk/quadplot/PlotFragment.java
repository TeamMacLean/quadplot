package com.wookoouk.quadplot;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class PlotFragment extends Fragment {
    private ListView mListView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> listItems = new ArrayList<String>();
    private ArrayList<Location> locations = new ArrayList<Location>();

//    private void setListAdapter(ListAdapter adapter) {
//        getView().getListView().setAdapter(adapter);
//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.plot, container, false);

        if (mListView == null) {
            mListView = (ListView) view.findViewById(R.id.plot_list);
        }

        listItems.add("Take Off");
        listItems.add("Return to land");

        adapter = new ArrayAdapter<>(view.getContext(),
                android.R.layout.simple_list_item_1,
                listItems);

        mListView.setAdapter(adapter);

        return view;
    }
}
