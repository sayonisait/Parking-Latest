package com.example.parking.ui.monthly;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import com.example.parking.R;
import com.example.parking.databinding.FragmentMonthlyNewBinding;
import com.kaopiz.kprogresshud.KProgressHUD;

public class MonthlyPlanFragment extends Fragment {

    private MonthlyViewModel mViewModel;


    public static MonthlyPlanFragment newInstance() {
        return new MonthlyPlanFragment();
    }

    private Context mContext;
    FragmentMonthlyNewBinding binding;
    private AnimatorSet mSetRightOut;
    private AnimatorSet mSetLeftIn;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = ViewModelProviders.of((FragmentActivity) mContext).get(MonthlyViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_monthly_new, container, false);
        binding.setViewmodel(mViewModel);
        binding.setLifecycleOwner(this);
//        loadAnimations();
//        changeCameraDistance();
        return binding.getRoot();

    }

    //    private void changeCameraDistance() {
//        int distance = 8000;
//        float scale = getResources().getDisplayMetrics().density * distance;
//        binding.frame1.setCameraDistance(scale);
//        binding.frame2.setCameraDistance(scale);
//    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        binding.editTextModel.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.models)));
        mViewModel.validationLiveData.observe(this, s -> {
            Toast.makeText(mContext, "Please fill the mandatory fields", Toast.LENGTH_SHORT).show();
            for (MonthlyViewModel.MissedFieldsStatus status : s) {
                switch (status) {
                    case Name:
                        binding.editTextName.setError("Field required");
                        break;
                    case Phone:
                        binding.editTextPhone.setError("Field required");
                        break;
                    case VehicleMake:
                        binding.editTextVehicleMake.setError("Field required");
                        break;
                    case VehicleModel:
                        binding.editTextModel.setError("Field required");
                        break;
                    case VehicleNumber:
                        binding.editTextNumber.setError("Field required");

                        break;
                }
            }
        });


        binding.buttonSubmit.setOnClickListener(view -> {
            showProgress();
            mViewModel.onSubmit().observe(this, s -> {
                hideProgress();
                if (s)// if saving successful
                {
                    NavHostFragment.findNavController(MonthlyPlanFragment.this).navigate(R.id.action_monthly_print);
                }else
                {
                    Toast.makeText(mContext,"Save failed. Connection or Server failure", Toast.LENGTH_SHORT).show();
                }
            });
        });

        mViewModel.getConfigDetails().observe(this, s -> {
            Log.d("Parking Info", "got config info");
            binding.editAmount.setText(mViewModel.monthlyCustomer.amounFormatted);
        });
    }
    KProgressHUD kProgressHUD;
    private void showProgress() {

            kProgressHUD = KProgressHUD.create(mContext)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Loading");

            kProgressHUD.setCancellable(true)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f)
                    .show();

    }

    private void hideProgress() {
        if (kProgressHUD != null)
            kProgressHUD.dismiss();

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
