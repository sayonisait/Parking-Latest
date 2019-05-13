package com.example.parking;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import com.example.parking.ui.vehicleentry.*;

public class VehicleEntryActivity extends FragmentActivity  {

    private VehicleEntryViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vehicle_entry_activity);
        setActionBar(findViewById(R.id.tool_bar));

        if (savedInstanceState == null) {
            getActionBar().setTitle("Select slot");
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, ParkingSlotFragment.newInstance()).addToBackStack("slot")
                    .commit();
        }
        mViewModel = ViewModelProviders.of(this).get(VehicleEntryViewModel.class);
        mViewModel.createEntry();
        mViewModel.slotNumber.observe(this, s -> {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container,VehicleEntryFragment.newInstance() ).addToBackStack("entry")
                    .commit();
        });
        mViewModel.qrCode.observe(this, s->{
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, TicketPrintFragment.newInstance() ).addToBackStack("preview")
                    .commit();

        });

    }

    @Override
    public void onBackPressed() {

        if(getSupportFragmentManager().getBackStackEntryCount()==1)
            finish();
        else
            super.onBackPressed();



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {

            Intent intent= new Intent(this,ScanQRCodeActivity.class);
            startActivityForResult(intent,1);

        } catch (Exception e) {

            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

        }
        return true;
    }
}
