package com.wookoouk.quadplot;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private MapView mapView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.map, container, false);

        mapView = (MapView) v.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this); //this is important
        mapView.setPadding(5, 5, 5, 5);

//        mapView.setMapType(mapView.MAP_TYPE_SATELLITE);

        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.getUiSettings().setZoomControlsEnabled(true);


//        MainActivity.locations.forEach();

        Log.d("INFO", "THERE ARE " + QuadPlot.plots.size() + " locations");


        if (QuadPlot.plots.size() > 0) {

            LatLngBounds.Builder builder = new LatLngBounds.Builder();

            for (Plot p : QuadPlot.plots) {

                String name = p.getName();

//                String name = QuadPlot.plot.get(QuadPlot.locations.indexOf(l));

                // do some work here on intValue
                LatLng loc = new LatLng(p.getLocation().getLatitude(), p.getLocation().getLongitude());


//                MarkerOptions marker = new MarkerOptions()
//                        .position(loc)
//                        .title("Melbourne")
//                        .snippet("Population: 4,137,400")
//                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.delete));

//                Marker myLocMarker = new MarkerOptions()
//                        .position(loc)
//                        .icon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(R.drawable.bluebox, "your text goes here")));

                MarkerOptions markerOptions = new MarkerOptions()
                        .position(loc)
                        .title(name);


//                        .snippet("Population: 4,137,400");
                builder.include(loc);
                Marker marker = googleMap.addMarker(markerOptions);
                marker.showInfoWindow();
            }

            LatLngBounds bounds = builder.build();

            int padding = 0; // offset from edges of the map in pixels
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
//        googleMap.moveCamera(cu);
            googleMap.animateCamera(cu);
        }


//        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(43.1, -87.9), 10);
//        map.animateCamera(cameraUpdate);

//        map.addMarker(new MarkerOptions().position(/*some location*/));
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(/*some location*/, 10));
    }

    private Bitmap writeTextOnDrawable(int drawableId, String text) {

        Bitmap bm = BitmapFactory.decodeResource(getResources(), drawableId)
                .copy(Bitmap.Config.ARGB_8888, true);

        Typeface tf = Typeface.create("Helvetica", Typeface.BOLD);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTypeface(tf);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(convertToPixels(getContext(), 11));

        Rect textRect = new Rect();
        paint.getTextBounds(text, 0, text.length(), textRect);

        Canvas canvas = new Canvas(bm);

        //If the text is bigger than the canvas , reduce the font size
        if (textRect.width() >= (canvas.getWidth() - 4))     //the padding on either sides is considered as 4, so as to appropriately fit in the text
            paint.setTextSize(convertToPixels(getContext(), 7));        //Scaling needs to be used for different dpi's

        //Calculate the positions
        int xPos = (canvas.getWidth() / 2) - 2;     //-2 is for regulating the x position offset

        //"- ((paint.descent() + paint.ascent()) / 2)" is the distance from the baseline to the center.
        int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));

        canvas.drawText(text, xPos, yPos, paint);

        return bm;
    }


    private static int convertToPixels(Context context, int nDP) {
        final float conversionScale = context.getResources().getDisplayMetrics().density;

        return (int) ((nDP * conversionScale) + 0.5f);

    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    void save() {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(getContext());
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(1);
        jsonArray.put(2);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("key", jsonArray.toString());
        System.out.println(jsonArray.toString());
        editor.apply();
//        editor.commit();
    }

    void load() {
        try {
            SharedPreferences prefs = PreferenceManager
                    .getDefaultSharedPreferences(getContext());
            JSONArray jsonArray2 = new JSONArray(prefs.getString("key", "[]"));
            for (int i = 0; i < jsonArray2.length(); i++) {
                Log.d("your JSON Array", jsonArray2.getInt(i) + "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}