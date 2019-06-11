package com.example.parking.ui.vehicleentry;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;
import com.example.parking.AppConstants;
import com.example.parking.R;
import com.example.parking.ScanQRCodeActivity;
import com.example.parking.ui.vehicleentry.adapters.ViewPagerAdapter;
import com.example.parking.ui.vehicleentry.fragments.ParkingSlotFragment;
import com.example.parking.ui.vehicleentry.fragments.TicketPrintFragment;
import com.example.parking.ui.vehicleentry.fragments.VehicleEntryFragment;
import com.example.parking.ui.vehicleentry.viewmodels.VehicleEntryViewModel;

public class VehicleEntryActivity extends FragmentActivity  {

    private VehicleEntryViewModel mViewModel;
    String exitContents;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vehicle_entry_activity);
        setActionBar(findViewById(R.id.tool_bar));

        NavController navController= Navigation.findNavController(this, R.id.nav_host_fragment);

        exitContents=getIntent().getStringExtra("content");

        mViewModel = ViewModelProviders.of(this).get(VehicleEntryViewModel.class);

        if(exitContents!=null)
            mViewModel.createExit(exitContents);
        else
            mViewModel.createEntry();

        mViewModel.slotNumber.observe(this, s -> {
             navController.popBackStack ();
        });
        mViewModel.qrCode.observe(this, s->{
           navController.navigate(R.id.ticketprint);

        });
        mViewModel.monthlyPlanMutableLiveData.observe(this, s-> {
          //  viewPager.setCurrentItem(1);


        });
        mViewModel.entryMutableLiveData.observe(this, s-> {
           // viewPager.setCurrentItem(1);


        });


        //navController.navigate(R.id.action_entry_print);

    }

    @Override
    public void onBackPressed() {

        if(getSupportFragmentManager().getBackStackEntryCount()==1)
            finish();
        else
            super.onBackPressed();



    }




}
