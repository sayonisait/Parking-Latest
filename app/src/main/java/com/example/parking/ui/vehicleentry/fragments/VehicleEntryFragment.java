package com.example.parking.ui.vehicleentry.fragments;

import android.content.Context;
import android.view.*;
import android.widget.Button;
import android.widget.EditText;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.parking.R;
import com.example.parking.entities.MonthlyPlan;
import com.example.parking.ui.vehicleentry.viewmodels.VehicleEntryViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class VehicleEntryFragment extends Fragment {

    private VehicleEntryViewModel mViewModel;

    @BindView(R.id.textInputStartDate)
    TextInputLayout textInputStartDate;
    @BindView(R.id.textInputEndDate)
    TextInputLayout textInputEndDate;
    @BindView(R.id.editTextStartDate)
    EditText editTextStartDate;
    @BindView(R.id.editTextEndDate)
    EditText editEndDate;


    @BindView(R.id.textInputModel)
    TextInputLayout textInputModel;
    @BindView(R.id.textInputNumber)
    TextInputLayout textInputNumber;
    @BindView(R.id.editTextModel)
    EditText editTextModel;
    @BindView(R.id.editTextNumber)
    EditText editTextNumber;
    @BindView(R.id.editTextEntryTime)
    EditText editTextTime;
    @BindView(R.id.textInputExitime)
    TextInputLayout textInputExitTime;
    @BindView(R.id.editExitTime)
    EditText editExitTime;
    @BindView(R.id.editTextSlot)
    EditText editTextSlot;
    @BindView(R.id.buttonSubmit)
    Button buttonSubmit;

    public static VehicleEntryFragment newInstance() {
        return new VehicleEntryFragment();
    }
    private Context mContext;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_entry_exit, container, false);
        ButterKnife.bind(this,view);
        return view;

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of((FragmentActivity) mContext).get(VehicleEntryViewModel.class);
        editTextTime.setText(mViewModel.entry.entryTime);
        editTextSlot.setText(mViewModel.entry.parkingSlot);
        MonthlyPlan monthlyPlan=mViewModel.monthlyPlanMutableLiveData.getValue();
        if(monthlyPlan!=null) {
            editTextNumber.setText(monthlyPlan.vehicle.vehicleNumber);
            editTextModel.setText(monthlyPlan.vehicle.vehicleModel);
            textInputEndDate.setVisibility(View.VISIBLE);
            textInputStartDate.setVisibility(View.VISIBLE);
            editTextStartDate.setText(monthlyPlan.startDate);
            editEndDate.setText(monthlyPlan.endDate);
        }
        else{
            textInputEndDate.setVisibility(View.GONE);
            textInputStartDate.setVisibility(View.GONE);

            if(mViewModel.isExit){

                // commenting temporarily
//                textInputCharge.setVisibility(View.VISIBLE);
//                textInputHours.setVisibility(View.VISIBLE);
//                editHours.setText(mViewModel.entry.hours);
//                editCharge.setText(mViewModel.entry.charge);


                editExitTime.setFocusable(true);
                editExitTime .setText(mViewModel.entry.exitTime);
                editTextNumber.setText(mViewModel.entry.vehicle.vehicleNumber);
                editTextModel.setText(mViewModel.entry.vehicle.vehicleModel);


            }else{
//                //commenting temporarily
//                textInputCharge.setVisibility(View.GONE);
//                textInputHours.setVisibility(View.GONE);
             editExitTime.setFocusable(false);
            }
        }
        editTextSlot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                System.out.println(String.format(" x is %f , full width is %d, image width is %d",event.getRawX(),editTextSlot.getRight(), 10));
                if(event.getRawX() > (editTextSlot.getRight() - 10)) {
                    NavController navController= NavHostFragment.findNavController(VehicleEntryFragment.this);
                    if (navController.getCurrentDestination().getId()== R.id.vehicleentry) {
                        navController.navigate(R.id.action_to_parkingSlot);
                        return true;

                    }
                }
                return false;
            }
        });





        View.OnFocusChangeListener focusChangeListener= new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String text=((TextInputEditText)v).getText().toString();
                if(hasFocus && !text.equals(""))
                    return;
                switch(v.getId()){
                    case R.id.editTextNumber:
                        mViewModel.entry.vehicle.vehicleNumber=text;
                        break;
                    case R.id.editTextModel:
                        mViewModel.entry.vehicle.vehicleModel=text;
                        break;
                    case R.id.editTextSlot:
                        mViewModel.entry.parkingSlot=text;
                        break;

                }

            }
        };

        editTextNumber.setOnFocusChangeListener(focusChangeListener);
        editTextSlot.setOnFocusChangeListener(focusChangeListener);
        editTextModel.setOnFocusChangeListener(focusChangeListener);

        mViewModel.slotNumber.observe(this, s -> {
            editTextSlot.setText (mViewModel.slotNumber.getValue());
        });

        buttonSubmit.setOnClickListener(view -> {
            boolean isValidated=true;
            ((FragmentActivity) mContext).getCurrentFocus().clearFocus();

            if(editTextModel.getText().toString().trim().equals("")){
                textInputModel.setError("Field can not be empty");
                isValidated=false;
            }
            if(editTextNumber.getText().toString().trim().equals("")){
                textInputNumber.setError("Field can not be empty");
                isValidated=false;
            }
            if(isValidated) {
                if (monthlyPlan == null )
                    mViewModel.createQRCode(editTextModel.getText().toString());
                else
                    if(mViewModel.isExit){

                        NavHostFragment.findNavController(VehicleEntryFragment.this).navigate(R.id.action_entry_print);
                    }else
                    //todo make API call to submit the entry or save in room
                    ((FragmentActivity) mContext).finish();
            }
        });
        ((FragmentActivity)  mContext). getActionBar().setTitle("Vehicle Entry");

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