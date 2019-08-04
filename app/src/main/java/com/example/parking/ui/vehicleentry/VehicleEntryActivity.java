package com.example.parking.ui.vehicleentry;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.example.parking.AppConstants;
import com.example.parking.R;
import com.example.parking.ScanQRCodeActivity;
import com.example.parking.model.Entry;
import com.example.parking.ui.vehicleentry.viewmodels.VehicleEntryViewModel;
import com.google.gson.Gson;

public class VehicleEntryActivity extends FragmentActivity  {

    private VehicleEntryViewModel mViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        mViewModel = ViewModelProviders.of(this).get(VehicleEntryViewModel.class);

        mViewModel.isExit=getIntent().getBooleanExtra("exit",false);

        if(mViewModel.isExit)
            scanQrCode();// for scanning qr code to get the details of the entry
        else
            mViewModel.createEntry();// if entry, create an entry record

        setContentView(R.layout.vehicle_entry_activity);
        setActionBar(findViewById(R.id.tool_bar));

        NavController navController= Navigation.findNavController(this, R.id.nav_host_fragment);

        // if slot is selected, then navigate back
        mViewModel.slotNumber.observe(this, s -> {
             navController.popBackStack ();
        });
        mViewModel.getSelectedEntryFromList().observe(this,s->{
            navController.popBackStack ();
        });

        System.out.println("Parking Info: finished activitys on create");

    }

    private void startQRCodeActivity(){
        try {

            Intent intent= new Intent(this, ScanQRCodeActivity.class);
            startActivityForResult(intent,1);

        } catch (Exception e) {

            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    startQRCodeActivity();
                } else {
                    // permission denied,
                    Toast.makeText(this,"Permission denied . Can not proceed scanning the QR code", Toast.LENGTH_SHORT).show();
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
    @Override
    public void onBackPressed() {
        if(mViewModel.isPrintDone|getSupportFragmentManager().getBackStackEntryCount()==1)
            finish();
        else
            super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK){

           mViewModel.setEntryFromQrCode(data.getStringExtra(AppConstants.KEY_QR_CODE));
        }
    }

    public void scanQrCode() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
                Toast.makeText(this, "Permissions required to access camera to scan qr code. Grand the permissions in settings.", Toast.LENGTH_SHORT).show();
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA}, 1
                );
            }
        } else
            startQRCodeActivity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }
}
