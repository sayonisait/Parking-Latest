package com.example.parking;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.parking.databinding.NewHomeBinding;
import com.example.parking.ui.login.LoginActivity;
import com.example.parking.ui.monthly.MonthlyPlanActivity;
import com.example.parking.ui.vehicleentry.VehicleEntryActivity;
import com.example.parking.ui.vehicleexit.VehicleExitActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class MainActivity extends Activity  {




    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.navigation_dashboard:
                   // mTextMessage.setText(R.string.dashboard);
                    return true;
                case R.id.navigation_reports:
                    //mTextMessage.setText(R.string.reports);
                    return true;
            }
            return false;
        }
    };

    NewHomeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NewHomeBinding binding = DataBindingUtil.setContentView(this, R.layout.new_home);
        binding.imageRegEntry.setOnClickListener(s ->
        {
            Intent intent = new Intent(this, VehicleEntryActivity.class);
            startActivity(intent);
        });

        binding.exitButton.setOnClickListener(s ->
        {
            View view = getLayoutInflater().inflate(R.layout.fragment_exit_menu_dialog, null);
          Button btnQr=  view.findViewById(R.id.button_qr);
          Button btnManual=view.findViewById(R.id.button_manual);
            BottomSheetDialog dialog = new BottomSheetDialog(this);
           btnQr.setOnClickListener(onClick->{
                scanQrCode(true);
                dialog.dismiss();
            });
            btnManual.setOnClickListener(onClick->{
                startExitActivity(null);
                dialog.dismiss();

            });
            dialog.setContentView(view);
            dialog.show();
           // startExitActivity();
        });
        binding.boxSubscription.setOnClickListener(s->{
            Intent intent= new Intent(this, MonthlyPlanActivity.class);
           startActivity(intent);
        });
        binding.boxRenewal.setOnClickListener(s->{
            scanQrCode(false);
        });
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        initViews();
    }



   private void initViews(){
       ButterKnife.bind(this);
//       textViewOccupCount.setText(String.valueOf(occupiedCount));
//       textViewVacantCount.setText(String.valueOf(totalParkingCount-occupiedCount));
       initToolBar();
       initListeners();

    }

    private void   initToolBar(){
        Toolbar mToolbar =  findViewById(R.id.tool_bar);
        this.setActionBar(mToolbar);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(true);
        getActionBar().setTitle("Home");
    }

    private void initListeners(){

    }

    void startExitActivity(String qrCoderesult){
        Intent intent= new Intent(this, VehicleExitActivity.class);
        intent.putExtra("scan_qr",qrCoderesult);
        startActivity(intent);
    }

    void startSubscriptionActivity(String qrCodeResult){
        Intent intent= new Intent(this, MonthlyPlanActivity.class);
        intent.putExtra("scan_qr",qrCodeResult);
        startActivity(intent);
    }

    public void scanQrCode(boolean isExit) {
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
                        new String[]{Manifest.permission.CAMERA}, isExit?1:2
                );
            }
        } else
            startQRCodeActivity(isExit?1:2);
    }

    private void startQRCodeActivity(int requestCode){
        try {

            Intent intent= new Intent(this, ScanQRCodeActivity.class);
            startActivityForResult(intent,requestCode);

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
                    startQRCodeActivity(requestCode);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK) {
            if (requestCode == 1 ) {
                startExitActivity(data.getStringExtra(AppConstants.KEY_QR_CODE));
            }
            if(requestCode==2){
                startSubscriptionActivity(data.getStringExtra(AppConstants.KEY_QR_CODE));
            }
        }
    }

    void startLoginActivity(){

        Intent intent= new Intent(this, LoginActivity.class);
        startActivity(intent);
    }






}
