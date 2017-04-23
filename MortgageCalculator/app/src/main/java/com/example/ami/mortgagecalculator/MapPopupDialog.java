package com.example.ami.mortgagecalculator;

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

/**
 * Created by ami on 3/18/17.
 */

public class MapPopupDialog extends Dialog {

    private Activity activity;
    private String perprotyString;
    private ListView mDrawerList;
    private MapPopupDialog mapPopupDialog = null;
    private MortgageHelper mortgageHelper;

    private static final String LOAN = MortgageSchema.MortgageEntry.LOAN;
    private static final String PROPERTY_TYPE = MortgageSchema.MortgageEntry.PROPERTY_TYPE;
    private static final String CITY = MortgageSchema.MortgageEntry.CITY;
    private static final String STREET = MortgageSchema.MortgageEntry.STREET;
    private static final String MONTHLY_PAY = MortgageSchema.MortgageEntry.MONTHLYPAY;
    private static final String APR = MortgageSchema.MortgageEntry.APR;
    private static final String[] projection = MortgageSchema.projection;


    public void setMapPopupDialog(MapPopupDialog m) {
        this.mapPopupDialog = m;
    }

    public MapPopupDialog(Activity activity, Bundle bundle) {
        super(activity);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.activity = activity;
        this.perprotyString = bundle.getString("PROPERTY_INFO");
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_popup);
        Window window = this.getWindow();
        this.setCancelable(true);
        Button btnEdit = (Button)this.findViewById(R.id.btnEdit);
        Button btnDelete = (Button)this.findViewById(R.id.btnDelete);
        Button btnCancel = (Button) this.findViewById(R.id.btnCancel);
        showText();

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();

                bundle.putString("PROPERTY_INFO", perprotyString);
                bundle.putBoolean("FROM_EDIT", true);

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
        TextView tvloan = (TextView)this.findViewById(R.id.contentLoan);
        TextView tvpt = (TextView)this.findViewById(R.id.contentPT); //property type
        TextView tvsa = (TextView)this.findViewById(R.id.contentSA); //street addr
        TextView tvcity = (TextView)this.findViewById(R.id.contentCity);
        TextView tvmp = (TextView)this.findViewById(R.id.contentMP); //monthly pay

        try {
            JSONObject perportyJSONObject = new JSONObject(perprotyString);
            tvloan.setText(perportyJSONObject.getString(LOAN));
            tvsa.setText(perportyJSONObject.getString(STREET));
            tvmp.setText(perportyJSONObject.getString(MONTHLY_PAY));
            tvpt.setText(perportyJSONObject.getString(PROPERTY_TYPE));
            tvcity.setText(perportyJSONObject.getString(CITY));
            tvapr.setText(perportyJSONObject.getString(APR));

        } catch (JSONException e) {
            e.printStackTrace();

        }

    }
}
