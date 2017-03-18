package com.example.sam.mortgagecalculator;

import android.Manifest;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sam on 3/17/17.
 */

public class MapFragment extends Fragment implements View.OnClickListener {

    private MortgageHelper mortgageHelper;
    private ArrayAdapter<String> aa;
    private SQLiteDatabase db;
    private ListView lv;
    private GoogleMap googleMap;
    private MapView mapView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.map, container, false);
        mortgageHelper = new MortgageHelper(getActivity());
        this.db = mortgageHelper.getReadableDatabase();
//        ArrayList<String> data_list=new ArrayList<String>();
//        this.lv=(ListView)rootView.findViewById(R.id.list_item);
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
                Log.d("mapview", "getmapasync");
                // For showing a move to my location button
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    Log.d("mapview", "permission denied");
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
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
                        Log.d("cursor_mapview", cursor.getString(1));
                        LatLng res = new LatLng(Double.parseDouble(cursor.getString(1)), Double.parseDouble(cursor.getString(2)));
                        Log.d("map_latlng", String.valueOf(res.latitude)+":"+res.longitude);
                        // For dropping a marker at a point on the Map
                        googleMap.addMarker(new MarkerOptions().position(res).title(cursor.getString(7)).snippet("Marker Description"));
                        // For zooming automatically to the location of the marker
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(res).zoom(12).build();
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//                data_list.add(cursor.getString(0));
//                data_list.add(cursor.getString(1));
//                data_list.add(cursor.getString(2));
                    } while (cursor.moveToNext());
                }

            }
        });

//        this.aa=new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, data_list);
//        lv.setAdapter(aa);
//        aa.notifyDataSetChanged();
//        Button btnDelete = (Button)rootView.findViewById(R.id.btnDelete);
//        btnDelete.setOnClickListener(this);

        return rootView;

    }

    @Override
    public void onClick(View v) {
        // Define 'where' part of query.
        String selection = MortgageSchema.MortgageEntry.COLUMN_PROPERTY_TYPE + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { "House" };
        // Issue SQL statement.
        db.delete(MortgageSchema.MortgageEntry.TABLE_NAME, selection, selectionArgs);
        ((BaseAdapter)lv.getAdapter()).notifyDataSetChanged();
    }
}
