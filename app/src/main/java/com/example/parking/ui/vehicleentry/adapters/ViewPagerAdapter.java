package com.example.parking.ui.vehicleentry.adapters;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.example.parking.ui.vehicleentry.fragments.ParkingSlotFragment;
import com.example.parking.ui.vehicleentry.fragments.TicketPrintFragment;
import com.example.parking.ui.vehicleentry.fragments.VehicleEntryFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {


    public ViewPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new VehicleEntryFragment();
            case 1: return new ParkingSlotFragment();
            case 2: return new TicketPrintFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0: return "Vehicle EntryTable";
            case 1: return "Select Slot";
            case 2: return "Print";
        }
        return null;
    }
}
