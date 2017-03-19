package com.example.sam.mortgagecalculator;

import android.app.Fragment;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by sam on 3/13/17.
 */

public class PropertyFragment extends Fragment implements View.OnClickListener{

    double monthlypay;
    double loan;
    float apr;
    String property_type, street, city, state, zipcode;
    MortgageHelper mortgageHelper;
    View view;

    public PropertyFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.property_info, container, false);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            this.monthlypay = bundle.getDouble("monthlypay", 0.0);
            this.loan = bundle.getDouble("loan", 0.0);
            this.apr = bundle.getFloat("apr", 0);
            Log.d("propertyfrag", String.valueOf(this.loan));
        }
        getActivity().setTitle("PropertyInfo");
        Button btnsaveproperty = (Button)view.findViewById(R.id.btnSaveProperty);
        btnsaveproperty.setOnClickListener(this);
        return view;
    }

    public void savePropertyAndOutput(){
        String[] latitude = {""};
        String[] longitude = {""};
        this.mortgageHelper = new MortgageHelper(getActivity());
        SQLiteDatabase db = mortgageHelper.getWritableDatabase();
        //check validation of data
        this.property_type = ((Spinner)view.findViewById(R.id.propertySpinner)).getSelectedItem().toString();
        this.street = ((EditText)view.findViewById(R.id.streetAdds)).getText().toString();
        this.city = ((EditText)view.findViewById(R.id.city)).getText().toString();
        this.state = ((Spinner)view.findViewById(R.id.stateSpinner)).getSelectedItem().toString();
        this.zipcode = ((EditText)view.findViewById(R.id.zipcode)).getText().toString();
        this.street = this.street.replaceAll(" ", "%20");
        this.city = this.city.replaceAll(" ", "%20");
        String addr = this.street + "+" + this.city + "+" + this.state ;
        Log.d("addr",addr);
        LatLngTask latLngTask = (LatLngTask) new LatLngTask(getActivity()).execute(addr, getString(R.string.google_api));
        try {
            JSONObject res = (JSONObject) latLngTask.get();
            latLngTask.get(1000, TimeUnit.MILLISECONDS);
            latitude[0] = res.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").get("lat").toString();
            longitude[0] = res.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").get("lng").toString();
            Log.d("latlng", latitude[0] +":"+ longitude[0]);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        Toast.makeText(getActivity(), latitude + ":" + longitude, Toast.LENGTH_SHORT).show();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(MortgageSchema.MortgageEntry.COLUMN_PROPERTY_TYPE, this.property_type);
        values.put(MortgageSchema.MortgageEntry.COLUMN_STREET, this.street);
        values.put(MortgageSchema.MortgageEntry.COLUMN_CITY, this.city);
        values.put(MortgageSchema.MortgageEntry.COLUMN_STATE, this.state);
        values.put(MortgageSchema.MortgageEntry.COLUMN_ZIPCODE, this.zipcode);
        values.put(MortgageSchema.MortgageEntry.COLUMN_APR, this.apr);
        values.put(MortgageSchema.MortgageEntry.COLUMN_MONTHLYPAY, this.monthlypay);
        values.put(MortgageSchema.MortgageEntry.COLUMN_LATITUDE, latitude[0]);
        values.put(MortgageSchema.MortgageEntry.COLUMN_LONGITUDE, longitude[0]);
        values.put(MortgageSchema.MortgageEntry.COLUMN_LOAN, this.loan);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(MortgageSchema.MortgageEntry.TABLE_NAME, null, values);
//        String[] projection = {
//                MortgageSchema.MortgageEntry._ID,
//                MortgageSchema.MortgageEntry.COLUMN_PROPERTY_TYPE,
//                MortgageSchema.MortgageEntry.COLUMN_LOAN
//        };
//        String selection = MortgageSchema.MortgageEntry.COLUMN_PROPERTY_TYPE + " = ?";
//        String[] selectionArgs = { "House" };
//        String sortOrder =
//                MortgageSchema.MortgageEntry.COLUMN_LOAN + " DESC";
//
//        Cursor cursor = db.query(
//                MortgageSchema.MortgageEntry.TABLE_NAME,                     // The table to query
//                projection,                               // The columns to return
//                selection,                                // The columns for the WHERE clause
//                selectionArgs,                            // The values for the WHERE clause
//                null,                                     // don't group the rows
//                null,                                     // don't filter by row groups
//                sortOrder                                 // The sort order
//        );
//        List itemIds = new ArrayList<>();
//        while(cursor.moveToNext()) {
//            long itemId = cursor.getLong(
//                    cursor.getColumnIndexOrThrow(MortgageSchema.MortgageEntry._ID));
//            itemIds.add(itemId);
//        }
//        cursor.close();

        Toast.makeText(getActivity(), "save", Toast.LENGTH_SHORT).show();

        //jump to map view
        MapFragment mapFragment = new MapFragment();
        getFragmentManager().beginTransaction().replace(R.id.content_frame, mapFragment).commit();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSaveProperty:
                savePropertyAndOutput();
                break;

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        mortgageHelper.close();
    }
}
