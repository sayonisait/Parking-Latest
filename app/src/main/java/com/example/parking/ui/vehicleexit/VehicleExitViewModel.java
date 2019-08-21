package com.example.parking.ui.vehicleexit;

import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import com.example.parking.data.ParkingRespository;
import com.example.parking.model.ConfigDetails;
import com.example.parking.model.Entry;
import com.example.parking.utils.StringUtils;
import com.google.gson.Gson;

public class VehicleExitViewModel extends AndroidViewModel {

    ParkingRespository repository;
    LiveData<ConfigDetails> configDetails;

    private MutableLiveData<Entry> mEntryLiveData;
   private MutableLiveData<Boolean> needsToScanQrCode;
    public VehicleExitViewModel(@NonNull Application application) {
        super(application);
        repository = new ParkingRespository(application);
        mEntryLiveData= new MutableLiveData<Entry>();
        needsToScanQrCode= new MutableLiveData<>();
        needsToScanQrCode.setValue(false);
        configDetails = repository.getConfig();

    }
    public LiveData<Boolean> getNeedToScanQrCodeLiveData(){
        return needsToScanQrCode;
    }

    public LiveData<Entry> getEntryLiveData() {
        return mEntryLiveData;
    }
    public void setSelectedEntry(Entry entry){
       mEntryLiveData.setValue(entry);
        entry.makeExit();
    }

    public void scanQrCode(){
        needsToScanQrCode.setValue(true);
    }

    public void setEntryFromQrCode(String qrCodeData){
       Entry mEntry=new Gson().fromJson(qrCodeData, Entry.class);
        mEntry.makeExit();
        this.mEntryLiveData.setValue(mEntry);
    }

    public LiveData<Boolean> saveEntry() {
        return Transformations.map(repository.sendEntryToBackend(mEntryLiveData.getValue()), s -> {
            Log.d("Parking Info", "entry.transaction_id =" + s);
            return (s!=null&& !s.equals(""));
        });
    }

    public LiveData<ConfigDetails> getConfigDetails() {
       return configDetails;

    }


}
