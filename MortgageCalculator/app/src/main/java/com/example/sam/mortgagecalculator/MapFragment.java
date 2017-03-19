package com.example.sam.mortgagecalculator;

import android.Manifest;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sam on 3/17/17.
 */

public class MapFragment extends Fragment {

    private MortgageHelper mortgageHelper;
    private SQLiteDatabase db;
    private GoogleMap googleMap;
    private MapView mapView;
    private HashMap<Marker, List<String>> markerMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.map, container, false);
        mortgageHelper = new MortgageHelper(getActivity());
        markerMap = new HashMap<>();
        this.db = mortgageHelper.getReadableDatabase();
        mapView = (MapView) rootView.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume(); // needed to get the map to display immediately
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
                        List<String> selectedMaker = markerMap.get(marker);
                        MapPopupDialog mapPopupDialog = new MapPopupDialog(getActivity(), selectedMaker, bundle);
                        mapPopupDialog.show();
                    }
                });
                Log.d("mapview", "getmapasync");
                // For showing a move to my location button
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    Log.d("mapview", "permission denied");
                    return;
                }
                googleMap.setMyLocationEnabled(true);
                //read from db
                String[] projection = {
                        MortgageSchema.MortgageEntry._ID,
                        MortgageSchema.MortgageEntry.COLUMN_LATITUDE,
                        MortgageSchema.MortgageEntry.COLUMN_LONGITUDE,
                        MortgageSchema.MortgageEntry.COLUMN_LOAN,
                        MortgageSchema.MortgageEntry.COLUMN_PROPERTY_TYPE,
                        MortgageSchema.MortgageEntry.COLUMN_CITY,
                        MortgageSchema.MortgageEntry.COLUMN_STATE,
                        MortgageSchema.MortgageEntry.COLUMN_STREET,
                        MortgageSchema.MortgageEntry.COLUMN_MONTHLYPAY

                };

                // Filter results WHERE "title" = 'My Title'
                String selection = MortgageSchema.MortgageEntry.COLUMN_PROPERTY_TYPE + " = ?";
                String[] selectionArgs = { "*" };

                // How you want the results sorted in the resulting Cursor
                String sortOrder =
                        MortgageSchema.MortgageEntry.COLUMN_LOAN + " DESC";

                Cursor cursor = db.query(
                        MortgageSchema.MortgageEntry.TABLE_NAME,                     // The table to query
                        projection,                               // The columns to return
                        null,                                // The columns for the WHERE clause
                        null,                            // The values for the WHERE clause
                        null,                                     // don't group the rows
                        null,                                     // don't filter by row groups
                        sortOrder                                 // The sort order
                );
                Log.d("mapview", "getcursor");
                cursor.moveToFirst();
                Log.d("cursor_mapview", cursor.getString(4));
                if (cursor.moveToFirst())
                {
                    do {
                        List<String> listitem = new ArrayList<String>();
                        Log.d("cursor_mapview", cursor.getString(1));
                        LatLng res = new LatLng(Double.parseDouble(cursor.getString(1)), Double.parseDouble(cursor.getString(2)));
                        Log.d("map_latlng", String.valueOf(res.latitude)+":"+res.longitude);
                        // For dropping a marker at a point on the Map
                        for(int i=1;i<9;i++){
                            listitem.add(cursor.getString(i));
                        }
                        Marker m = googleMap.addMarker(new MarkerOptions().position(res).title(cursor.getString(4)).snippet(cursor.getString(7)+","+cursor.getString(5)));
                        markerMap.put(m, listitem);
                        // For zooming automatically to the location of the marker
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(res).zoom(12).build();
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    } while (cursor.moveToNext());
                }

            }
        });


        return rootView;

    }

}
