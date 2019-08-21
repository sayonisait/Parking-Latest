package com.example.parking;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;

import android.view.View;
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

    int totalParkingCount=100, occupiedCount=60;

//    @BindView(R.id.textViewVacCount)
//    TextView textViewVacantCount;
//    @BindView(R.id.textViewOccupCount)
//    TextView textViewOccupCount;
//    @OnClick(R.id.box1)
//     void startNewVehicleEntryActivity(){
//        Intent intent= new Intent(this, VehicleEntryActivity.class);
//        startActivity(intent);
//    }
//    @OnClick(R.id.boxReprint)
//    void startMonthlyActivity(){
//        Intent intent= new Intent(this, MonthlyPlanActivity.class);
//        startActivity(intent);
//    }
//    @OnClick(R.id.box2)
//     void exit(){
//       startExitActivity();
//    }


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

            BottomSheetDialog dialog = new BottomSheetDialog(this);
           view.findViewById(R.id.button_qr).setOnClickListener(onClick->{
               scanQrCode();
                dialog.dismiss();
            });
            view.findViewById(R.id.button_manual).setOnClickListener(onClick->{
                startExitActivity(null);
                dialog.dismiss();

            });
            dialog.setContentView(view);
            dialog.show();
           // startExitActivity();
        });
        binding.imageMonthly.setOnClickListener(s->{
            Intent intent= new Intent(this, MonthlyPlanActivity.class);
           startActivity(intent);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK){
            startExitActivity(data.getStringExtra(AppConstants.KEY_QR_CODE));
        }
    }

    void startLoginActivity(){

        Intent intent= new Intent(this, LoginActivity.class);
        startActivity(intent);
    }






}
