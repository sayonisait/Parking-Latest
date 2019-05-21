package com.example.parking.ui.monthly;

import android.content.Context;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.parking.R;
import com.google.android.material.textfield.TextInputLayout;

public class MonthlyPlanFragment extends Fragment {

    private MonthlyViewModel mViewModel;

    @BindView(R.id.textInputModel)
    TextInputLayout textInputModel;
    @BindView(R.id.textInputNumber)
    TextInputLayout textInputNumber;
    @BindView(R.id.editTextModel)
    EditText editTextModel;
    @BindView(R.id.editTextNumber)
    EditText editTextNumber;
    @BindView(R.id.editTextStartDate)
    EditText editStartDate;
    @BindView(R.id.editEndDate)
    EditText editEndDate;
    @BindView(R.id.buttonSubmit)
    Button buttonSubmit;

    public static MonthlyPlanFragment newInstance() {
        return new MonthlyPlanFragment();
    }
    private Context mContext;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.monthly_fragment, container, false);
        ButterKnife.bind(this,view);
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of((FragmentActivity) mContext).get(MonthlyViewModel.class);
        editStartDate.setText(mViewModel.getStartDate());
        editEndDate.setText(mViewModel.monthlyPlan.endDate);


        buttonSubmit.setOnClickListener(view -> {
            boolean isValidated=true;
            if(editTextModel.getText().toString().trim().equals("")){
                textInputModel.setError("Field can not be empty");
                isValidated=false;
            }
            if(editTextNumber.getText().toString().trim().equals("")){
                textInputNumber.setError("Field can not be empty");
                isValidated=false;
            }
            if(isValidated) {

                mViewModel.createQRCode(editTextNumber.getText().toString(), editTextModel.getText().toString());
            }
        });
        ((FragmentActivity)  mContext). getActionBar().setTitle("Monthly Plan Activation");

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }



}
