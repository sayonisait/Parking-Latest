package com.example.parking.base;

import androidx.fragment.app.FragmentActivity;

public class BaseActivity extends FragmentActivity {
    @Override
    public void onBackPressed() {

        if(getSupportFragmentManager().getBackStackEntryCount()==1)
            finish();
        else
            super.onBackPressed();

    }
}
