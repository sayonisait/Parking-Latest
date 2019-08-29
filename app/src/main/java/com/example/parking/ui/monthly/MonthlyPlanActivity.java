package com.example.parking.ui.monthly;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import com.example.parking.R;
import com.example.parking.ScanQRCodeActivity;
import com.example.parking.base.BaseActivity;

public class MonthlyPlanActivity extends BaseActivity {

    private MonthlyViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.monthly_customer_activity);
        setActionBar(findViewById(R.id.tool_bar));
        mViewModel = ViewModelProviders.of(this).get(MonthlyViewModel.class);
        String scannedQrCode= getIntent().getStringExtra("scan_qr");
//        if(scannedQrCode!=null){
//            mViewModel.updateSubscription(scannedQrCode);
//        }
        mViewModel.createEntry(scannedQrCode).observe(this,s->{});

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
