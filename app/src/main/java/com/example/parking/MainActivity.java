package com.example.parking;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;

import android.widget.LinearLayout;
import android.widget.Toolbar;


import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class MainActivity extends Activity  {

    int totalParkingCount=100, occupiedCount=60;

    @BindView(R.id.chart)
    PieChartView pieChartView;
    @BindView(R.id.chart_vacant)
    PieChartView pieChartViewVacant;
    @OnClick(R.id.new_entry)
     void startNewVehicleEntryActivity(){
        Intent intent= new Intent(this, VehicleEntryActivity.class);
        startActivity(intent);
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


       List<SliceValue> pieData = new ArrayList<>();
       pieData.add(new SliceValue(occupiedCount,getResources().getColor( android.R.color.holo_red_light) ));
       pieData.add(new SliceValue(totalParkingCount-occupiedCount, getResources().getColor( android.R.color.holo_green_light)));

       List<SliceValue> pieData2 = new ArrayList<>();
       pieData2.add(new SliceValue(totalParkingCount-occupiedCount,getResources().getColor( android.R.color.holo_green_light) ));
       pieData2.add(new SliceValue(occupiedCount, getResources().getColor( android.R.color.holo_red_light) ));

       PieChartData pieChartData = new PieChartData(pieData);
       pieChartData.setHasCenterCircle(true).setCenterText1(String.valueOf(occupiedCount)).setCenterText1FontSize(20).setCenterCircleScale(.8f);



     pieChartView.setPieChartData(pieChartData);


       PieChartData pieChartDataVacant = new PieChartData(pieData2);
       pieChartDataVacant.setHasCenterCircle(true).setCenterText1(String.valueOf(totalParkingCount-occupiedCount)).setCenterText1FontSize(20).setCenterCircleScale(.8f);
       pieChartViewVacant.setPieChartData(pieChartDataVacant);


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





}
