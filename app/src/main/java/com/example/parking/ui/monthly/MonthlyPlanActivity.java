package com.example.parking.ui.monthly;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import com.example.parking.R;
import com.example.parking.ScanQRCodeActivity;

public class MonthlyPlanActivity extends FragmentActivity  {

    private MonthlyViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vehicle_entry_activity);
        setActionBar(findViewById(R.id.tool_bar));

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, MonthlyPlanFragment.newInstance()).addToBackStack("slot")
                    .commit();
        }
        mViewModel = ViewModelProviders.of(this).get(MonthlyViewModel.class);
        mViewModel.createEntry();
//        mViewModel.slotNumber.observe(this, s -> {
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.container,VehicleEntryFragment.newInstance() ).addToBackStack("monthlyPlan")
//                    .commit();
//        });
        mViewModel.qrCode.observe(this, s->{
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MonthlyPrintFragment.newInstance() ).addToBackStack("preview")
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
}
