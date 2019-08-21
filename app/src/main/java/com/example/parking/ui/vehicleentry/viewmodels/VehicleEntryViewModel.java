package com.example.parking.ui.vehicleentry.viewmodels;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.Log;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import com.example.parking.data.ParkingRespository;
import com.example.parking.data.local.entities.EntryTable;
import com.example.parking.model.ConfigDetails;
import com.example.parking.model.MonthlyCustomer;
import com.example.parking.model.Entry;
import com.example.parking.utils.QRCodeUtils;
import com.example.parking.utils.StringUtils;
import com.google.gson.Gson;

import java.util.List;

public class VehicleEntryViewModel extends AndroidViewModel {

    public MutableLiveData<String> slotNumber = new MutableLiveData<>();
    public MutableLiveData<Bitmap> qrCode = new MutableLiveData<>();
    public MutableLiveData<MonthlyCustomer> monthlyPlanMutableLiveData = new MutableLiveData<>();
   // public MutableLiveData<String> estimatedAmountLiveData = new MutableLiveData<>();
    public MutableLiveData<String> specialChargeLiveData = new MutableLiveData<>();

    public boolean isExit;
    public Entry entry;
    public boolean isMonthlyPlan;

    ParkingRespository repository;
    public boolean isPrintDone;
    LiveData<ConfigDetails> configDetails;

    public VehicleEntryViewModel(Application application) {
        super(application);
        repository = new ParkingRespository(application);
        isPrintDone = false;
        configDetails = repository.getConfig();

    }



    //todo move this to appropriate place
    public LiveData<Integer> getTransactions(){
        return  repository.fetchTransactions("2019-07-14 00:00:00","2019-07-14 23:59:59");
    }



    public LiveData<List<EntryTable>> sendUnSyncedEntries(){
        return repository.getUnSyncedEntries();
    }

    public LiveData<ConfigDetails> getConfigDetails() {
        return Transformations.map(configDetails, s -> {
            if(entry !=null && entry.receipt_number==0)
                entry.receipt_number=s.last_receipt_number+1;
            return s;
        });

    }

    public LiveData<String> saveEntry() {
        return Transformations.map(repository.sendEntryToBackend(entry), s -> {
            Log.d("Parking Info", "entry.transaction_id =" + s);
           // entry.transaction_id = s;
            generrateQR(entry);
            return s;
        });
    }







    /* creates a new entry*/
    public void createEntry() {
        entry = new Entry();
        entry.startEntry();
        System.out.println("Parking Info: Create entry 2");

    }

    public void createEntryForMonthlyVehicle(String json) {
        // if entry scanned qr code is for a monthly plan car
        MonthlyCustomer monthlyCustomer = new Gson().fromJson(json, MonthlyCustomer.class);
        // entry.monthlyCustomer=monthlyCustomer;
        monthlyPlanMutableLiveData.setValue(monthlyCustomer);
    }







    public void setParkingSlot(String slot) {
        entry.parkingSlot = slot;
        slotNumber.postValue(slot);
    }

    public void generrateQR(Entry entry) {
        Bitmap image = QRCodeUtils.generateQrCode(new Gson().toJson(entry,Entry.class));
        if (image != null)
            qrCode.postValue(image);

    }


}
