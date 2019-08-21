package com.example.parking.ui.vehicleexit;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;
import com.example.parking.AppConstants;
import com.example.parking.R;
import com.example.parking.ScanQRCodeActivity;
import com.example.parking.ui.vehicleentry.viewmodels.VehicleEntryViewModel;

public class VehicleExitActivity extends FragmentActivity {
    private VehicleExitViewModel mViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       String scannedQrCode= getIntent().getStringExtra("scan_qr");
        mViewModel = ViewModelProviders.of(this).get(VehicleExitViewModel.class);
        setContentView(R.layout.activity_exit);
        NavController navController= Navigation.findNavController(this, R.id.nav_host_fragment);
        NavGraph graph= navController.getNavInflater().inflate(R.navigation.navigation_exit);
        initToolBar();
        mViewModel.getEntryLiveData().observe(this,s->{
            navController.navigate(R.id.vehicleExitFragment);
        });
        mViewModel.getConfigDetails().observe(this, s->{
            Log.d("Parking Info","got config info");


        });
            if(scannedQrCode!=null) {
                graph.setStartDestination(R.id.vehicleExitFragment);
                mViewModel.setEntryFromQrCode(scannedQrCode);
            }
            else
            {
                graph.setStartDestination(R.id.parkedListFragment);

            }
            navController.setGraph(graph);

    }

    private void   initToolBar(){
        Toolbar toolbar =  findViewById(R.id.toolbar);
      this.setActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());
       toolbar.setTitle("Vehicle Exit");
    }


}
