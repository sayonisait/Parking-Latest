package com.example.parking.ui.monthly;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.parking.R;
import com.example.parking.databinding.MonthlyFragmentBinding;
import com.example.parking.model.Vehicle;
import com.google.android.material.textfield.TextInputLayout;

public class MonthlyPlanFragment extends Fragment {

    private MonthlyViewModel mViewModel;

//    @BindView(R.id.textInputModel)
//    TextInputLayout textInputModel;
//    @BindView(R.id.textInputNumber)
//    TextInputLayout textInputNumber;
//    @BindView(R.id.editTextModel)
//    AutoCompleteTextView editTextModel;
//    @BindView(R.id.editTextNumber)
//    EditText editTextNumber;
//    @BindView(R.id.editTextStartDate)
//    EditText editStartDate;
//    @BindView(R.id.editEndDate)
//    EditText editEndDate;
//    @BindView(R.id.editTextName)
//    EditText editTextName;
//    @BindView(R.id.editTextPhone)
//    EditText editTextPhone;
//    @BindView(R.id.editTextVehicleMake)
//    EditText editTextVehicleMake;
//
//    @BindView(R.id.buttonSubmit)
//    Button buttonSubmit;

    public static MonthlyPlanFragment newInstance() {
        return new MonthlyPlanFragment();
    }
    private Context mContext;
    MonthlyFragmentBinding binding ;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = ViewModelProviders.of((FragmentActivity) mContext).get(MonthlyViewModel.class);
        binding= DataBindingUtil.inflate(inflater, R.layout.monthly_fragment, container, false);
        binding.setSubscription(mViewModel.monthlyCustomer);
        binding.setVehicle(mViewModel.monthlyCustomer.vehicle);
        return binding.getRoot();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        binding.editTextModel.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.models)));


        binding.buttonSubmit.setOnClickListener(view -> {
            boolean isValidated = true;
            if (mViewModel.monthlyCustomer.vehicle.vehicleModel.trim().equals("")) {
                binding.textInputModel.setError("Field can not be empty");
                isValidated = false;
            }
            if (mViewModel.monthlyCustomer.vehicle.vehicleNumber.trim().equals("")) {
                binding.textInputNumber.setError("Field can not be empty");
                isValidated = false;
            }
            if (mViewModel.monthlyCustomer.name.trim().equals("")) {
                binding.textInputCustomerName.setError("Field can not be empty");
                isValidated = false;
            }
            //todo more mandatory fields
            if (isValidated) {
                // once all validations are done , go to print screen to print all details
                //todo uncomment while implementting monthly plan ......  if (monthlyPlan == null) {
                ProgressDialog dialog = showProgress();
                //saves and generates qr code
                mViewModel.saveSubscription().observe(this, s -> {
                    NavHostFragment.findNavController(MonthlyPlanFragment.this).navigate(R.id.action_monthly_print);
                    dialog.dismiss();


                });
            }
        });
        ((FragmentActivity) mContext).getActionBar().setTitle("Monthly Plan Activation");// todo change title settiing from here
        mViewModel.getConfigDetails().observe(this, s -> {
            Log.d("Parking Info", "got config info");
            binding.editAmount.setText(mViewModel.monthlyCustomer.amounFormatted);
        });
    }

    private ProgressDialog showProgress(){
        // Initialize a new instance of progress dialog
        final ProgressDialog pd = new ProgressDialog(mContext, R.style.MyGravity);
        // Set progress dialog style horizontal

        // Set the progress dialog background color transparent
        pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        pd.setCancelable(false);
        // Finally, show the progress dialog
        pd.show();
        return pd;

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
