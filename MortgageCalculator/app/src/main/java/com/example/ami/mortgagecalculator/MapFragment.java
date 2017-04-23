package com.example.ami.mortgagecalculator;

import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ami on 3/17/17.
 */

public class MapFragment extends Fragment {

    private MortgageHelper mortgageHelper;
    private SQLiteDatabase db;
    private GoogleMap googleMap;
    private MapView mapView;
    private HashMap<Marker, String> markerMap;
    private static final String[] PROJECTION = MortgageSchema.projection;
    MapPopupDialog mapPopupDialog;

    public static final int PROPERTY_TYPE_INDEX = MortgageSchema.COLUMN_PROPERTY_TYPE_INDEX;
    public static final int STREET_INDEX = MortgageSchema.COLUMN_STREET_INDEX;
    public static final int CITY_INDEX = MortgageSchema.COLUMN_CITY_INDEX;
    public static final int LATITUDE_INDEX = MortgageSchema.COLUMN_LATITUDE_INDEX;
    public static final int LONGITUDE_INDEX = MortgageSchema.COLUMN_LONGITUDE_INDEX;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.map, container, false);
        mortgageHelper = new MortgageHelper(getActivity());
        markerMap = new HashMap<>();
        this.db = mortgageHelper.getReadableDatabase();
        mapView = (MapView) rootView.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        Log.d("mapview", "oncreateview");
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap gMap) {
                googleMap = gMap;
                googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        Log.d("mapview", "infoclick");
                        Bundle bundle = new Bundle();
                        String selectedMakerJSONString = markerMap.get(marker);
                        bundle.putString("PROPERTY_INFO", selectedMakerJSONString);
                        mapPopupDialog = new MapPopupDialog(getActivity(), bundle);
                        mapPopupDialog.setMapPopupDialog(mapPopupDialog);
                        mapPopupDialog.show();
                    }
                });
                Log.d("mapview", "getmapasync");

                Cursor cursor = db.query(
                        MortgageSchema.MortgageEntry.TABLE_NAME,                     // The table to query
                        PROJECTION,                               // The columns to return
                        null,                                // The columns for the WHERE clause
                        null,                            // The values for the WHERE clause
                        null,                                     // don't group the rows
                        null,                                     // don't filter by row groups
                        null                                // The sort order
                );
                Log.d("mapview", "getcursor");
                if (cursor.moveToFirst())
                {
                    do {
                        Log.d("cursor_mapview", cursor.getString(1));
                        LatLng res = new LatLng(Double.parseDouble(cursor.getString(LATITUDE_INDEX)),
                                                Double.parseDouble(cursor.getString(LONGITUDE_INDEX)));
                        Log.d("map_latlng", String.valueOf(res.latitude)+":"+res.longitude);
                        JSONObject json = getJSONObject(cursor);


                        Marker m = googleMap.addMarker(
                                new MarkerOptions().position(res).title(cursor.getString(PROPERTY_TYPE_INDEX)).snippet(cursor.getString(STREET_INDEX) +
                                        "," + cursor.getString(CITY_INDEX)));
                        markerMap.put(m, json.toString());
                        googleMap.getUiSettings().setZoomControlsEnabled(true);
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(res).zoom(12).build();
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    } while (cursor.moveToNext());
                }

            }
        });

        return rootView;

    }

    private JSONObject getJSONObject(Cursor c) {

        try {
            JSONObject retVal = new JSONObject();
            for(int i=0; i<c.getColumnCount(); i++) { retVal.put(c.getColumnName(i), c.getString(i)); }
            return retVal;

        }catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

}
