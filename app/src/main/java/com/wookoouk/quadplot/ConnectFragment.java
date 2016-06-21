package com.wookoouk.quadplot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ConnectFragment extends android.support.v4.app.Fragment {

    private boolean connected = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.connect_fragment, container, false);
    }
}
