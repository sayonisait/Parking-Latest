package com.example.parking.ui.vehicleentry;

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
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.parking.R;
import com.example.parking.entities.MonthlyPlan;
import com.google.android.material.textfield.TextInputLayout;

public class VehicleEntryFragment extends Fragment {

    private VehicleEntryViewModel mViewModel;
    @BindView(R.id.textInputCharge)
    TextInputLayout textInputCharge;
    @BindView(R.id.textInputHours)
    TextInputLayout textInputHours;
    @BindView(R.id.textInputExitTime)
    TextInputLayout textInputExitTime;
    @BindView(R.id.textInputStartDate)
    TextInputLayout textInputStartDate;
    @BindView(R.id.textInputEndDate)
    TextInputLayout textInputEndDate;
    @BindView(R.id.textInputModel)
    TextInputLayout textInputModel;
    @BindView(R.id.textInputNumber)
    TextInputLayout textInputNumber;


    @BindView(R.id.editExitTime)
    EditText editExitTime;
    @BindView(R.id.editHours)
    EditText editHours;
    @BindView(R.id.editCharge)
    EditText editCharge;
    @BindView(R.id.editTextStartDate)
    EditText editTextStartDate;
    @BindView(R.id.editEndDate)
    EditText editEndDate;
    @BindView(R.id.editTextModel)
    EditText editTextModel;
    @BindView(R.id.editTextNumber)
    EditText editTextNumber;
    @BindView(R.id.editTextTime)
    EditText editTextTime;
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
        View view= inflater.inflate(R.layout.vehicle_entry_fragment, container, false);
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
                textInputCharge.setVisibility(View.VISIBLE);
                textInputHours.setVisibility(View.VISIBLE);
                textInputExitTime.setVisibility(View.VISIBLE);
                editExitTime.setText(mViewModel.entry.exitTime);
                editTextNumber.setText(mViewModel.entry.vehicle.vehicleNumber);
                editTextModel.setText(mViewModel.entry.vehicle.vehicleModel);
                editHours.setText(mViewModel.entry.hours);
                editCharge.setText(mViewModel.entry.charge);

            }else{
                textInputCharge.setVisibility(View.GONE);
                textInputHours.setVisibility(View.GONE);
                textInputExitTime.setVisibility(View.GONE);
            }
        }

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
                if (monthlyPlan == null )
                    mViewModel.createQRCode(editTextNumber.getText().toString(), editTextModel.getText().toString());
                else
                    if(mViewModel.isExit){
                        getFragmentManager().beginTransaction()
                                .replace(R.id.container, TicketPrintFragment.newInstance() ).addToBackStack("preview")
                                .commit();
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
