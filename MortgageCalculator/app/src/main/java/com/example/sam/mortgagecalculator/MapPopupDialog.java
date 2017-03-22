package com.example.sam.mortgagecalculator;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by sam on 3/18/17.
 */

public class MapPopupDialog extends Dialog {

    private Activity activity;
    private String perprotyString;
    private ListView mDrawerList;
    private MapPopupDialog mapPopupDialog = null;
    private MortgageHelper mortgageHelper;

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
    private static final String[] projection = MortgageSchema.projection;

    public MapPopupDialog(Activity activity, Bundle bundle) {
        super(activity);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.activity = activity;
        this.perprotyString = bundle.getString("PROPERTY_INFO");
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
    }

    public void setMapPopupDialog(MapPopupDialog m) {
        this.mapPopupDialog = m;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_popup);
        Window window = this.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            wlp.gravity = Gravity.LEFT | Gravity.BOTTOM;
        } else {
            wlp.gravity = Gravity.BOTTOM;
        }

        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        this.setCancelable(true);
        Button btnEdit = (Button)this.findViewById(R.id.btnEdit);
        Button btnDelete = (Button)this.findViewById(R.id.btnDelete);
        Button btnCancel = (Button) this.findViewById(R.id.btnCancel);


        showText();


        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                //add bundle user info
                bundle.putString("PROPERTY_INFO", perprotyString);
                bundle.putBoolean("FROM_EDIT", true);

                //jump back to mortgage fragment to edit
                MortgageFragment mortgageFragment = new MortgageFragment();
                mortgageFragment.setArguments(bundle);
                FragmentManager fragmentManager = activity.getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, mortgageFragment).commit();

                mapPopupDialogdismiss();

            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mortgageHelper = new MortgageHelper(activity);
                SQLiteDatabase db = mortgageHelper.getWritableDatabase();

                try {
                    JSONObject perportyJSONObject = new JSONObject(perprotyString);
                    db.delete(MortgageSchema.MortgageEntry.TABLE_NAME, "_id = \"" + perportyJSONObject.getString(projection[0]) + "\"", null);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                MapFragment mapFragment = new MapFragment();
                FragmentManager fragmentManager = activity.getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, mapFragment).commit();

                mapPopupDialogdismiss();

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mapPopupDialogdismiss();

            }

        }) ;

    }

    private void mapPopupDialogdismiss() {
        if (null != mapPopupDialog) {
            mapPopupDialog.dismiss();
            mapPopupDialog = null;
        }

    }

    private void showText() {

        TextView tvapr = (TextView)this.findViewById(R.id.contentAPR);
        TextView tvcity = (TextView)this.findViewById(R.id.contentCity);
        TextView tvloan = (TextView)this.findViewById(R.id.contentLoan);
        TextView tvmp = (TextView)this.findViewById(R.id.contentMP); //monthly pay
        TextView tvpt = (TextView)this.findViewById(R.id.contentPT); //property type
        TextView tvsa = (TextView)this.findViewById(R.id.contentSA); //street addr
        try {
            JSONObject perportyJSONObject = new JSONObject(perprotyString);
            tvloan.setText(perportyJSONObject.getString(LOAN));
            tvpt.setText(perportyJSONObject.getString(PROPERTY_TYPE));
            tvcity.setText(perportyJSONObject.getString(CITY));
            tvsa.setText(perportyJSONObject.getString(STREET));
            tvmp.setText(perportyJSONObject.getString(MONTHLY_PAY));
            tvapr.setText(perportyJSONObject.getString(APR));

        } catch (JSONException e) {
            e.printStackTrace();

        }

    }
}
