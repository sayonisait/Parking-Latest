package com.example.parking.ui.vehicleentry;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.example.parking.AppConstants;
import com.example.parking.R;
import com.example.parking.ScanQRCodeActivity;
import com.example.parking.ui.vehicleentry.viewmodels.VehicleEntryViewModel;

public class VehicleEntryActivity extends FragmentActivity  {

    private VehicleEntryViewModel mViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(VehicleEntryViewModel.class);
        mViewModel.createEntry();// if entry, create an entry record

        setContentView(R.layout.vehicle_entry_activity);
        //setActionBar(findViewById(R.id.tool_bar));

        NavController navController= Navigation.findNavController(this, R.id.nav_host_fragment);

        // if slot is selected, then navigate back
        mViewModel.slotNumber.observe(this, s -> {
             navController.popBackStack ();
        });


        System.out.println("Parking Info: finished activitys on create");

    }




    @Override
    public void onBackPressed() {
        if(mViewModel.isPrintDone|getSupportFragmentManager().getBackStackEntryCount()==1)
            finish();
        else
            super.onBackPressed();
    }






}
