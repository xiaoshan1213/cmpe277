package com.example.sam.mortgagecalculator;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by sam on 3/13/17.
 */

public class OutputFragment extends Fragment implements View.OnClickListener{
    private float loan;
    private float apr;
    private double monthlypay;
    private View view;

    public OutputFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            this.monthlypay = bundle.getDouble("monthlypay", 0.0);
            this.apr = bundle.getFloat("apr", 0);
            this.loan = bundle.getFloat("loan", 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_output, container, false);
        getActivity().setTitle("Output");
        ((TextView)rootView.findViewById(R.id.paymentholder)).setText(""+this.monthlypay);
        Button btnSaveCal = (Button)rootView.findViewById(R.id.btnSaveCalculation);
        btnSaveCal.setOnClickListener(this);
        return rootView;
    }


    @Override
    public void onClick(View v) {
        Fragment fragmentProperty = new PropertyFragment();
        Bundle bundle = new Bundle();
        FragmentManager fragmentManager = getFragmentManager();
        bundle.putDouble("loan", this.loan);
        bundle.putFloat("apr",this.apr);
        bundle.putDouble("monthlypay", this.monthlypay);
        fragmentProperty.setArguments(bundle);
        Log.d("tag", apr+"");
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragmentProperty).commit();
    }
}
