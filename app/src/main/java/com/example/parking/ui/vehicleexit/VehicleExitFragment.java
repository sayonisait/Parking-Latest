package com.example.parking.ui.vehicleexit;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import butterknife.ButterKnife;
import com.example.parking.R;
import com.example.parking.databinding.FragmentExitBinding;
import com.example.parking.ui.vehicleentry.fragments.VehicleEntryFragment;

public class VehicleExitFragment extends Fragment {

    private Context mContext;
    private VehicleExitViewModel exitViewModel;
    private FragmentExitBinding binding;
    NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_exit, container, false);
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_exit, container, false);
        exitViewModel= ViewModelProviders.of((FragmentActivity) mContext).get(VehicleExitViewModel.class);
        exitViewModel.getEntryLiveData().observe(this,s->{
            binding.setParkedVehicle(s);
        });

        binding.buttonSubmit.setOnClickListener(s->{
            exitViewModel.saveEntry().observe(this,saved->{
               if(saved)
                getActivity().finish();
               else
                   Log.d("Parking Info", "Saving failed");
            });

        });


        setHasOptionsMenu(true);
        navController = NavHostFragment.findNavController(VehicleExitFragment.this);
        binding.setLifecycleOwner(this);
//        if (navController.getCurrentDestination().getId() == R.id.vehicleExitFragment) {
//            navController.navigate(R.id.action_to_parked_list);
//        }
        return binding.getRoot();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;

    }




}
