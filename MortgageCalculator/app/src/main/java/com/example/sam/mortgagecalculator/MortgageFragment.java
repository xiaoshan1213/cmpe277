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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * Created by sam on 3/13/17.
 */

public class MortgageFragment extends Fragment implements View.OnClickListener{
    public static final String ARG_PLANET_NUMBER = "planet_number";
    private View view;
    private int years;
    private float loan;
    private float apr;
    private double monthlypay;

    public MortgageFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mortgage, container, false);
        int i = getArguments().getInt(ARG_PLANET_NUMBER);
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
                bundle.putFloat("apr",this.apr);
                bundle.putDouble("monthlypay", this.monthlypay);
                Toast.makeText(getActivity(), this.monthlypay + "", Toast.LENGTH_SHORT).show();
                fragment_output.setArguments(bundle);
                Log.d("tag", apr+"");
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment_output).commit();
                break;
        }

    }

    public void handleCalculate(View view){
        float propertyPrice = Float.parseFloat(((EditText) view.findViewById(R.id.propertyPrice)).getText().toString());
        float downPayment = Float.parseFloat(((EditText) view.findViewById(R.id.downPayment)).getText().toString());
        float apr = Float.parseFloat(((EditText) view.findViewById(R.id.apr)).getText().toString()) / 1200 ;
        int months = this.years * 12;
        float loan = propertyPrice - downPayment;
        this.loan = loan;
        this.apr = apr;
        double pow = Math.pow(1+apr, months);
        this.monthlypay = (double)apr * pow * (double)loan / (pow - 1);
//        Toast.makeText(getActivity(), this.monthlypay+"", Toast.LENGTH_SHORT).show();
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
    }

//    public void onRadioButtonClicked(View view) {
//        // Is the button now checked?
//        boolean checked = ((RadioButton) view).isChecked();
//
//        // Check which radio button was clicked
//        switch(view.getId()) {
//            case R.id.terms_15:
//                if (checked) {
//                    // Pirates are the best
//                    years = 15;
//                    break;
//                }
//            case R.id.terms_30:
//                if (checked) {
//                    // Ninjas rule
//                    years = 30;
//                    break;
//                }
//        }
//        return ;
//    }
}
