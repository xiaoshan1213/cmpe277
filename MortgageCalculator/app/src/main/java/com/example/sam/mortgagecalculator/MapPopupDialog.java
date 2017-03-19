package com.example.sam.mortgagecalculator;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sam on 3/18/17.
 */

public class MapPopupDialog extends Dialog {

    Activity activity;
    List<String> list;
    Bundle bundle;

    public MapPopupDialog(Activity activity, List<String> list, Bundle bundle) {
        super(activity);
        this.activity = activity;
        this.list = list;
        this.bundle = bundle;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_popup);
        Window window = this.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        this.setCancelable(true);
        Button btnEdit = (Button)this.findViewById(R.id.btnEdit);
        Button btnDelete = (Button)this.findViewById(R.id.btnDelete);
        TextView tvapr = (TextView)this.findViewById(R.id.contentAPR);
        TextView tvcity = (TextView)this.findViewById(R.id.contentCity);
        TextView tvloan = (TextView)this.findViewById(R.id.contentLoan);
        TextView tvmp = (TextView)this.findViewById(R.id.contentMP); //monthly pay
        TextView tvpt = (TextView)this.findViewById(R.id.contentPT); //property type
        TextView tvsa = (TextView)this.findViewById(R.id.contentSA); //street addr
        tvloan.setText(list.get(2));
        tvpt.setText(list.get(3));
        tvcity.setText(list.get(4));
        tvsa.setText(list.get(6));
        tvmp.setText(list.get(7));
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add bundle user info
                bundle.putString("id", "id");
                //jump back to mortgage fragment to edit
                MortgageFragment mortgageFragment = new MortgageFragment();
                FragmentManager fragmentManager = activity.getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, mortgageFragment).commit();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
