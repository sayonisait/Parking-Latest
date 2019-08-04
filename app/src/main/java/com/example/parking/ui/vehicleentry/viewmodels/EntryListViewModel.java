package com.example.parking.ui.vehicleentry.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.parking.data.ParkingRespository;
import com.example.parking.model.Entry;

import java.util.List;

public class EntryListViewModel extends AndroidViewModel {
    ParkingRespository repository;
    LiveData<List<Entry>> parkedEntries;
    public EntryListViewModel(@NonNull Application application) {
        super(application);
        repository= new ParkingRespository(application);
        parkedEntries= repository.getParkedEntries("%%");
    }

    public LiveData<List<Entry>> getParkedEntries(){
        return parkedEntries;
    }

    public LiveData<List<Entry>> setQuery(String query){
        return repository.getParkedEntries("%"+query+"%");
    }

}
