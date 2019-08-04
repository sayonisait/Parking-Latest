package com.example.parking.ui.vehicleentry.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.parking.data.ParkingRespository;

import java.util.List;

public class ParkingSlotViewModel extends AndroidViewModel {

    ParkingRespository repository;
    LiveData<String> maxCount;
    LiveData<List<String>> occupiedSlots;
    public ParkingSlotViewModel(@NonNull Application application) {
        super(application);
        repository = new ParkingRespository(application);
        maxCount=repository.getParkingCount();
        occupiedSlots=repository.getOccupiedSlots();
    }

    public LiveData<String> getParkingSlotsCount(){
        return maxCount;
    }

    public LiveData<List<String>> getOccupiedSlots(){
        return occupiedSlots;
    }




}
