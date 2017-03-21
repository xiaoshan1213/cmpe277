package com.example.sam.mortgagecalculator;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by sam on 3/13/17.
 */

public class MortgageFragment extends Fragment implements View.OnClickListener{
    private View view;
    private int years = -1;
    private static final String[] projection = MortgageSchema.projection;
    private static final String LATITUDE = MortgageSchema.MortgageEntry.COLUMN_LATITUDE;
    private static final String LONGITUDE = MortgageSchema.MortgageEntry.COLUMN_LONGITUDE;
    private static final String LOAN = MortgageSchema.MortgageEntry.COLUMN_LOAN;
    private static final String PROPERTY_TYPE = MortgageSchema.MortgageEntry.COLUMN_PROPERTY_TYPE;
    private static final String CITY = MortgageSchema.MortgageEntry.COLUMN_CITY;
    private static final String STATE = MortgageSchema.MortgageEntry.COLUMN_STATE;
    private static final String STREET = MortgageSchema.MortgageEntry.COLUMN_STREET;
    private static final String MONTHLY_PAY = MortgageSchema.MortgageEntry.COLUMN_MONTHLYPAY;
    private static final String PROPERTY_PRICE = MortgageSchema.MortgageEntry.COLUMN_PROPERTY_PRICE;
    private static final String DOWN_PAYMENT = MortgageSchema.MortgageEntry.COLUMN_DOWN_PAYMENT;
    private static final String APR = MortgageSchema.MortgageEntry.COLUMN_APR;
    private static final String YEARS = MortgageSchema.MortgageEntry.COLUMN_YEARS;
    private static final String ZIPCODE = MortgageSchema.MortgageEntry.COLUMN_ZIPCODE;
    private static final String ID = MortgageSchema.MortgageEntry._ID;
    private static final int ZIP_CODE_MAX = MortgageSchema.ZIP_CODE_MAX;
    private static final int ZIP_CODE_MIN = MortgageSchema.ZIP_CODE_MIN;

    private JSONObject perportyInfoJSON = new JSONObject();

    private JSONObject savedObject = null;

    MortgageHelper mortgageHelper;

    public MortgageFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mortgage, container, false);

        ListView mDrawerList = (ListView) getActivity().findViewById(R.id.left_drawer);
        mDrawerList.setItemChecked(0, true);
        String title = getResources().getStringArray(R.array.title_arr)[0];
        getActivity().setTitle(title);

        Button btnCal = (Button)view.findViewById(R.id.btnCalculate);
        btnCal.setOnClickListener(this);
        Button btnSave = (Button)view.findViewById(R.id.btnSaveProperty);
        btnSave.setOnClickListener(this);
        Button btnNew = (Button) view.findViewById(R.id.btnNew);
        btnNew.setOnClickListener(this);
        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.rg);
        showPropertyInfo();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId){
                // checkedId is the RadioButton selected
                if(checkedId == R.id.terms_15)
                    years = 15;
                else
                    years = 30;
            }
        });
        return view;
    }

    private void showPropertyInfo() {
        Bundle bundle = getArguments();
        String savedPropertyInfo = null;
        boolean fromEdit = false;

        String[] aPropertyType = getResources().getStringArray(R.array.property_type);
        String[] aStateType = getResources().getStringArray(R.array.states);
        int position = 0;
        String aprStr = null;


        if (null == bundle) { return; }

        savedPropertyInfo = bundle.getString("PROPERTY_INFO");
        if ((null == savedPropertyInfo) || (0 == savedPropertyInfo.length())) { return; }

        fromEdit = bundle.getBoolean("FROM_EDIT", false);


        try {
            savedObject = new JSONObject(savedPropertyInfo);
            position = Arrays.asList(aPropertyType).indexOf(savedObject.getString(PROPERTY_TYPE));
            ((Spinner)view.findViewById(R.id.propertySpinner)).setSelection(position);

            ((EditText)view.findViewById(R.id.city)).setText(savedObject.getString(CITY));

            position = Arrays.asList(aStateType).indexOf(savedObject.getString(STATE));
            ((Spinner)view.findViewById(R.id.stateSpinner)).setSelection(position);

            ((EditText)view.findViewById(R.id.streetAdds)).setText(savedObject.getString(STREET));
            ((EditText)view.findViewById(R.id.propertyPrice)).setText(savedObject.getString(PROPERTY_PRICE));
            ((EditText)view.findViewById(R.id.downPayment)).setText(savedObject.getString(DOWN_PAYMENT));
            ((EditText)view.findViewById(R.id.zipcode)).setText(savedObject.getString(ZIPCODE));

            aprStr = savedObject.getString(APR);
            aprStr = String.valueOf(Float.parseFloat(aprStr));
            ((EditText)view.findViewById(R.id.apr)).setText(aprStr);

            if (15 == Integer.valueOf(savedObject.getString(YEARS))) {
                ((RadioButton)view.findViewById(R.id.terms_15)).setChecked(true);
                years = 15;
            } else {
                ((RadioButton)view.findViewById(R.id.terms_30)).setChecked(true);
                years = 30;
            }

            if (-1 != Double.valueOf(savedObject.getString(MONTHLY_PAY))) {
                ((TextView) view.findViewById(R.id.paymentholder)).setText("        " + savedObject.getString(MONTHLY_PAY));
            }

            if (false == fromEdit) {
                savedObject = null;
            }

        } catch (JSONException e) {

        }


    }

    private void refreshProCalInfo() throws JSONException {

        perportyInfoJSON.put(PROPERTY_TYPE, ((Spinner)view.findViewById(R.id.propertySpinner)).getSelectedItem().toString());
        perportyInfoJSON.put(CITY, ((EditText)view.findViewById(R.id.city)).getText().toString());
        perportyInfoJSON.put(STATE, ((Spinner)view.findViewById(R.id.stateSpinner)).getSelectedItem().toString());
        perportyInfoJSON.put(STREET, ((EditText)view.findViewById(R.id.streetAdds)).getText().toString());
        perportyInfoJSON.put(ZIPCODE, ((EditText)view.findViewById(R.id.zipcode)).getText().toString());
        perportyInfoJSON.put(MONTHLY_PAY, String.valueOf(-1));
        perportyInfoJSON.put(PROPERTY_PRICE, ((EditText) view.findViewById(R.id.propertyPrice)).getText().toString());
        perportyInfoJSON.put(DOWN_PAYMENT, ((EditText) view.findViewById(R.id.downPayment)).getText().toString());
        perportyInfoJSON.put(LATITUDE, String.valueOf(0));
        perportyInfoJSON.put(LONGITUDE, String.valueOf(0));
        perportyInfoJSON.put(APR, String.valueOf(Float.parseFloat(((EditText) view.findViewById(R.id.apr)).getText().toString())));
        perportyInfoJSON.put(LOAN, String.valueOf(-1));
        perportyInfoJSON.put(YEARS, String.valueOf(years));

    }
    
    private void refreshCalInfoOnly() throws JSONException {
        
        perportyInfoJSON.put(MONTHLY_PAY, String.valueOf(-1));
        perportyInfoJSON.put(PROPERTY_PRICE, ((EditText) view.findViewById(R.id.propertyPrice)).getText().toString());
        perportyInfoJSON.put(DOWN_PAYMENT, ((EditText) view.findViewById(R.id.downPayment)).getText().toString());
        perportyInfoJSON.put(APR, String.valueOf(Float.parseFloat(((EditText) view.findViewById(R.id.apr)).getText().toString())));
        perportyInfoJSON.put(LOAN, String.valueOf(-1));
        perportyInfoJSON.put(YEARS, String.valueOf(years));
    }

    private boolean checkProCalInfo() {
        int zipCode = 0;

        if (false == checkCalInfo()) { return false; }

        if (0 == ((Spinner) view.findViewById(R.id.propertySpinner)).getSelectedItem().toString().length()) { return false; }

        if (0 == ((EditText) view.findViewById(R.id.city)).getText().toString().length()) {
            alertDialog("Please input a city.");
            return false;
        }
        if (0 == ((Spinner) view.findViewById(R.id.stateSpinner)).getSelectedItem().toString().length()) { return false; }
        if (0 == ((EditText) view.findViewById(R.id.streetAdds)).getText().toString().length()) {
            alertDialog("Please input a street.");
            return false;
        }

        if (0 == ((EditText) view.findViewById(R.id.zipcode)).getText().toString().length()) {
            alertDialog("Please input a zip code.");
            return false;
        }


        zipCode = Integer.valueOf(((EditText) view.findViewById(R.id.zipcode)).getText().toString());
        if ((zipCode > ZIP_CODE_MAX) || (zipCode < ZIP_CODE_MIN)) {
            alertDialog("Please input a valid zip code.");
            return false;
        }

        return true;



    }
    
    private boolean checkCalInfo() {
        float propertyPrice = 0;
        float downPayment = 0;
        float apr = 0;

        if (0 == ((EditText) view.findViewById(R.id.propertyPrice)).getText().toString().length()) {
            alertDialog("Please input property price.");
            return false;
        }

        if (0 == ((EditText) view.findViewById(R.id.downPayment)).getText().toString().length()) {
            alertDialog("Please input down payment.");
            return false;
        }

        if (0 == ((EditText) view.findViewById(R.id.apr)).getText().toString().length()) {
            alertDialog("Please input APR.");
            return false;
        }

        propertyPrice = Float.parseFloat(((EditText) view.findViewById(R.id.propertyPrice)).getText().toString());
        downPayment = Float.parseFloat(((EditText) view.findViewById(R.id.downPayment)).getText().toString());
        apr = Float.parseFloat(((EditText) view.findViewById(R.id.apr)).getText().toString());

        if (apr > 100) {
            alertDialog("APR cannot exceed 100%.");
            return false;
        }

        if (downPayment > propertyPrice) {
            alertDialog("Down payment cannot be larger than property price.");
            return false;
        }

        if (-1 == years) {
            alertDialog("Please select a term.");
            return false;
        }

        return true;

    }


    @Override
    public void onClick(View v) {
        double monthlypay = 0;
        try{

            switch (v.getId()) {
                case R.id.btnCalculate:

                    if (false == checkCalInfo()) { return; }
                    refreshCalInfoOnly();

                    monthlypay = handleCalculate(view);
                    System.out.println(monthlypay);
                    ((TextView) (getActivity().findViewById(R.id.paymentholder))).setText("       " + monthlypay);


                    break;

                case R.id.btnSaveProperty:
                    if (false == checkProCalInfo()) { return; }
                    refreshProCalInfo();
                    handleCalculate(view);
                    saveProperty(view);

                    break;
                case R.id.btnNew:
                    newDialog();
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }catch (ExecutionException e) {
            e.printStackTrace();
        }

    }


    private double handleCalculate(View view) throws JSONException {
        float propertyPrice = Float.parseFloat(perportyInfoJSON.getString(PROPERTY_PRICE));
        float downPayment = Float.parseFloat(perportyInfoJSON.getString(DOWN_PAYMENT));
        float apr = Float.parseFloat(perportyInfoJSON.getString(APR)) / 1200;
        int months = Integer.parseInt(perportyInfoJSON.getString(YEARS)) * 12;
        float loan = propertyPrice - downPayment;
        double pow = Math.pow(1+apr, months);
        System.out.println("pow");
        System.out.println(pow);
        double month_pay = ((double)apr * pow * (double)loan / (pow - 1));
        month_pay = (double)(Math.round(month_pay * 100) / 100.0);
        perportyInfoJSON.put(MONTHLY_PAY, String.valueOf(month_pay));
        perportyInfoJSON.put(LOAN, String.valueOf(loan));

        return month_pay;
    }
    private void updateLocationInfo(JSONObject res) throws JSONException {
        String latitude = res.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").get("lat").toString();
        String longitude = res.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").get("lng").toString();
        String _id = res.getJSONArray("results").getJSONObject(0).getString("place_id");
        Log.d("latlng", latitude +":"+ longitude);
        perportyInfoJSON.put(LATITUDE, latitude);
        perportyInfoJSON.put(LONGITUDE, longitude);
        perportyInfoJSON.put(ID, _id);

    }

    public void saveProperty(View view) throws JSONException, ExecutionException, TimeoutException{

        this.mortgageHelper = new MortgageHelper(getActivity());
        SQLiteDatabase db = mortgageHelper.getWritableDatabase();
        //check validation of data

        String addr = getAddr();

        Log.d("addr",addr);

        LatLngTask latLngTask = (LatLngTask) new LatLngTask(getActivity()).execute(addr, getString(R.string.google_api));
        try {
            JSONObject res = (JSONObject) latLngTask.get();
            latLngTask.get(1000, TimeUnit.MILLISECONDS);
            if (false == checkResults(res)) {
                alertDialog("Address information is invalid!");
                return;
            }
            updateLocationInfo(res);

        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }


        // Create a new map of values, where column names are the keys


        if (needOldPropertyInfo()) {
            System.out.println("delete");
            db.delete(MortgageSchema.MortgageEntry.TABLE_NAME, "_id = \"" + savedObject.getString(projection[0]) + "\"", null);
            savedObject = null;
        }

        ContentValues values = getContentValues();

        // Insert the new row, returning the primary key value of the new row
        db.insertWithOnConflict(MortgageSchema.MortgageEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);

        Toast.makeText(getActivity(), "save", Toast.LENGTH_SHORT).show();
    }

    private boolean needOldPropertyInfo() throws JSONException{

        if (null != savedObject) {
            if (false ==
                    savedObject.getString(projection[0]).equals(perportyInfoJSON.getString(projection[0]))) {
                return true;
            }
        }

        return false;

    }



    private boolean checkResults(JSONObject res) throws JSONException{
        JSONArray addressComponents = null;
        int i = 0;
        String ZipCode = perportyInfoJSON.getString(ZIPCODE);
        System.out.println(res.getString("status"));

        if (!res.getString("status").equals("OK")) {return false;}

        addressComponents = res.getJSONArray("results").getJSONObject(0).getJSONArray("address_components");
        for (i = 0; i < addressComponents.length(); i++) {
            System.out.println(addressComponents.getJSONObject(i).get("types"));
            String types = (String) addressComponents.getJSONObject(i).getJSONArray("types").get(0);

            if (types.equals("postal_code")) {
                System.out.println(addressComponents.getJSONObject(i).getString("long_name"));
                if (addressComponents.getJSONObject(i).getString("long_name").equals(ZipCode)) {
                    return true;
                } else {
                    return false;
                }
            }
        }

        return false;


    }

    private String getAddr() throws JSONException{
        String streetWithoutSpace = (perportyInfoJSON.getString(STREET)).replaceAll(" ", "%20");
        String cityWithoutSpace = (perportyInfoJSON.getString(CITY)).replaceAll(" ", "%20");
        String state = perportyInfoJSON.getString(STATE);
        String zipcode = perportyInfoJSON.getString(ZIPCODE);
        String addr = streetWithoutSpace + "+" + cityWithoutSpace + "+" + state + "+" + zipcode;

        return addr;

    }

    private ContentValues getContentValues() throws JSONException{

        ContentValues values = new ContentValues();
        int i = 0;

        for (i = 0; i < projection.length; i++) { values.put(projection[i], perportyInfoJSON.getString(projection[i])); }

        return values;
    }

    private void clearPerportInfo() {


        ((Spinner)view.findViewById(R.id.propertySpinner)).setSelection(0);

        ((EditText)view.findViewById(R.id.city)).setText("");

        ((Spinner)view.findViewById(R.id.stateSpinner)).setSelection(0);

        ((EditText)view.findViewById(R.id.streetAdds)).setText("");
        ((EditText)view.findViewById(R.id.propertyPrice)).setText("");
        ((EditText)view.findViewById(R.id.downPayment)).setText("");
        ((EditText)view.findViewById(R.id.zipcode)).setText("");

        ((EditText)view.findViewById(R.id.apr)).setText("");

        ((RadioButton)view.findViewById(R.id.terms_15)).setChecked(false);
        ((RadioButton)view.findViewById(R.id.terms_30)).setChecked(false);



        ((TextView) view.findViewById(R.id.paymentholder)).setText("");
        savedObject = null;
        years = -1;


    }

    private void newDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Unsaved changes will be discarded");
        builder.setTitle("Alert");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                clearPerportInfo();
            }
        });

        setNegativeButton(builder);
        builder.create().show();
    }

    private void setNegativeButton(AlertDialog.Builder builder) {

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

    }

    private void alertDialog(String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message);
        builder.setTitle("Alert");

        setNegativeButton(builder);
        builder.create().show();
    }


}
