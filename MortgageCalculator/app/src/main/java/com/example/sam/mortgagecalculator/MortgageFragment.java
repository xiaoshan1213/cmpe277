package com.example.sam.mortgagecalculator;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * Created by sam on 3/13/17.
 */

public class MortgageFragment extends Fragment implements View.OnClickListener{
    public static final String ARG_PLANET_NUMBER = "planet_number";
    private View view;
    private int years;
    private double loan;
    private double apr;
    private double monthlypay;

    public MortgageFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mortgage, container, false);
        int i=0;
        if(getArguments()!=null)
            i = getArguments().getInt(ARG_PLANET_NUMBER);
        String title = getResources().getStringArray(R.array.title_arr)[i];
        getActivity().setTitle(title);
        Button btnCal = (Button)view.findViewById(R.id.btnCalculate);
        btnCal.setOnClickListener(this);
        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.rg);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                if(checkedId == R.id.terms_15)
                    years = 15;
                else
                    years = 30;
            }
        });
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCalculate:
                handleCalculate(view);
                Fragment fragment_output = new OutputFragment();
                Bundle bundle = new Bundle();
                FragmentManager fragmentManager = getFragmentManager();
                bundle.putDouble("loan", this.loan);
                bundle.putDouble("apr",this.apr);
                bundle.putDouble("monthlypay", this.monthlypay);
                Toast.makeText(getActivity(), this.monthlypay + "", Toast.LENGTH_SHORT).show();
                fragment_output.setArguments(bundle);
                Log.d("tag", apr+"");
                Log.d("mortgageFrag", String.valueOf(this.loan));
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment_output).commit();
                break;
        }

    }

    public void handleCalculate(View view) {
        float propertyPrice = Float.parseFloat(((EditText) view.findViewById(R.id.propertyPrice)).getText().toString());
        float downPayment = Float.parseFloat(((EditText) view.findViewById(R.id.downPayment)).getText().toString());
        float apr = Float.parseFloat(((EditText) view.findViewById(R.id.apr)).getText().toString()) / 1200;
        int months = this.years * 12;
        float loan = propertyPrice - downPayment;
        this.loan = loan;
        this.apr = apr;
        double pow = Math.pow(1 + apr, months);
        this.monthlypay = (double) apr * pow * (double) loan / (pow - 1);
    }

}
