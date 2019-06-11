package com.example.parking;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.parking.ui.monthly.MonthlyPlanActivity;
import com.example.parking.ui.vehicleentry.VehicleEntryActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends Activity  {

    int totalParkingCount=100, occupiedCount=60;

    @BindView(R.id.textViewVacCount)
    TextView textViewVacantCount;
    @BindView(R.id.textViewOccupCount)
    TextView textViewOccupCount;
    @OnClick(R.id.box1)
     void startNewVehicleEntryActivity(){
        Intent intent= new Intent(this, VehicleEntryActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.box3)
    void startMonthlyActivity(){
        Intent intent= new Intent(this, MonthlyPlanActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.box2)
     void scanQrCode(){
        try {

            Intent intent= new Intent(this, ScanQRCodeActivity.class);
            startActivityForResult(intent,1);

        } catch (Exception e) {

            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

        }
    }


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        initViews();
    }

   private void initViews(){
       ButterKnife.bind(this);
       textViewOccupCount.setText(String.valueOf(occupiedCount));
       textViewVacantCount.setText(String.valueOf(totalParkingCount-occupiedCount));
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

    void startExitActivity(String contents){
        Intent intent= new Intent(this, VehicleEntryActivity.class);
        intent.putExtra("content",contents);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK){
            startExitActivity(data.getStringExtra(AppConstants.KEY_QR_CODE));
        }
    }





}
