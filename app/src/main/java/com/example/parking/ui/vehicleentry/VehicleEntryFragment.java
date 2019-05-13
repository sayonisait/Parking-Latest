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
import com.google.android.material.textfield.TextInputLayout;

public class VehicleEntryFragment extends Fragment {

    private VehicleEntryViewModel mViewModel;

    @BindView(R.id.textInputModel)
    TextInputLayout textInputModel;
    @BindView(R.id.textInputNumber)
    TextInputLayout textInputNumber;
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
        editTextTime.setText(mViewModel.getEntryTime());
        editTextSlot.setText(mViewModel.slotNumber.getValue());
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
            if(isValidated)
            mViewModel.createQRCode(editTextNumber.getText().toString(), editTextModel.getText().toString());
        });
        ((FragmentActivity)  mContext). getActionBar().setTitle("Vehicle Entry");

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem scan=menu.add("scan");
        scan.setIcon(R.drawable.ic_qr_code);
        scan.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

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
