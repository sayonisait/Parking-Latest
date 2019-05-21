package com.example.parking.ui.vehicleentry;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import com.example.parking.AppConstants;
import com.example.parking.R;
import com.example.parking.ScanQRCodeActivity;
import com.example.parking.entities.MonthlyPlan;

public class VehicleEntryActivity extends FragmentActivity  {

    private VehicleEntryViewModel mViewModel;
    boolean isExit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vehicle_entry_activity);
        setActionBar(findViewById(R.id.tool_bar));

        if (savedInstanceState == null) {
            getActionBar().setTitle("Select slot");
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container,VehicleEntryFragment.newInstance() ).addToBackStack("entry")
                    .commit();
        }

        isExit=getIntent().getBooleanExtra("exit",false);

        mViewModel = ViewModelProviders.of(this).get(VehicleEntryViewModel.class);
        mViewModel.createEntry(isExit);
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
        mViewModel.monthlyPlanMutableLiveData.observe(this, s-> {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container,VehicleEntryFragment.newInstance() ).addToBackStack("entry")
                    .commit();

        });
        mViewModel.entryMutableLiveData.observe(this, s-> {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container,VehicleEntryFragment.newInstance() ).addToBackStack("entry")
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

            Intent intent= new Intent(this, ScanQRCodeActivity.class);
            startActivityForResult(intent,1);

        } catch (Exception e) {

            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK){
            String contents = data.getStringExtra(AppConstants.KEY_QR_CODE);
            mViewModel.updateEntry(contents);
        }
    }
}
