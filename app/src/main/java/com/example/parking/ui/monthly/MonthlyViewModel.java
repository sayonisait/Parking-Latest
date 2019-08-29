package com.example.parking.ui.monthly;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.Log;
import androidx.lifecycle.*;
import com.example.parking.data.ParkingRespository;
import com.example.parking.model.ConfigDetails;
import com.example.parking.model.MonthlyCustomer;
import com.example.parking.model.Vehicle;
import com.example.parking.utils.QRCodeUtils;
import com.example.parking.utils.StringUtils;
import com.google.gson.Gson;

import java.util.*;

public class MonthlyViewModel extends AndroidViewModel {

     enum MissedFieldsStatus{Name, VehicleNumber, VehicleModel, VehicleMake, Phone }
    enum SaveStatus{Saved, SaveFailed, Validated }

     MutableLiveData<Boolean> showProgress;
    MutableLiveData<Boolean> scanQrCode;

    public MutableLiveData<Bitmap> qrCode= new MutableLiveData<>();

    private MutableLiveData<MonthlyCustomer> monthlyCustomerMutableLiveData= new MutableLiveData<>();

    MonthlyCustomer monthlyCustomer;
    ParkingRespository repository;
    LiveData<ConfigDetails> configDetails;
    MutableLiveData<Integer> pageNumerShown;

    public MonthlyViewModel(Application application) {
        super(application);
        repository = new ParkingRespository(application);
        createEntry(null);
        configDetails = repository.getConfig();
        pageNumerShown=new MutableLiveData<>();
        pageNumerShown.setValue(1);
        showProgress= new MutableLiveData<>();
        scanQrCode= new MutableLiveData<>();

    }

    public LiveData<Integer> getPageNumberLiveData(){
        return pageNumerShown;
    }

    public LiveData<ConfigDetails> getConfigDetails() {
        return Transformations.map(configDetails, s -> {
            if(monthlyCustomer !=null ) {
                monthlyCustomer.setConfigDetails(s);
                monthlyCustomerMutableLiveData.setValue(monthlyCustomer);
            }
            return s;
        });

    }

    public LiveData<MonthlyCustomer> createEntry(String qrCode){
        monthlyCustomer = new MonthlyCustomer();
        monthlyCustomerMutableLiveData.setValue(monthlyCustomer);
        //if qrcode is not null ,fetch from database
        if(qrCode!=null){
           return   Transformations.switchMap(repository.getSubscription(qrCode),s->{
              monthlyCustomer.setDetails(s);
              return monthlyCustomerMutableLiveData;
          });
        }
        //else create a new one with default valuses like startdate and enddate
        else{
            monthlyCustomer.createNewEntry();
        }


        return monthlyCustomerMutableLiveData;
    }

    public LiveData<MonthlyCustomer> getMonthlyCustomer(){
        return monthlyCustomerMutableLiveData;
    }

    private LiveData<SaveStatus> saveSubscription() {
        showProgress.setValue(true);
        return Transformations.map(repository.saveSubscription(monthlyCustomer), s -> {
            if(s.equals("failure")){
                return SaveStatus.SaveFailed;
            }
            monthlyCustomer.rowID = s;
            Bitmap image = QRCodeUtils.generateQrCode(String.valueOf(monthlyCustomer.serverID));
            qrCode.setValue(image);
            return SaveStatus.Saved;
        });
    }

    MutableLiveData<List<MissedFieldsStatus>> validationLiveData= new MutableLiveData<>();

    LiveData<SaveStatus> onSubmit(){
        LiveData<SaveStatus> liveData= new MutableLiveData<>();
        if(pageNumerShown.getValue()!=null) {
            if (pageNumerShown.getValue() == 2) {
               liveData= saveSubscription();

            } else {
                List<MissedFieldsStatus> statusList= new ArrayList<>();
                if(StringUtils.isEmpty(monthlyCustomer.name))
                {
                    statusList.add(MissedFieldsStatus.Name);
                }
                if(StringUtils.isEmpty(monthlyCustomer.phone))
                {
                    statusList.add(MissedFieldsStatus.Phone);

                }
                if(StringUtils.isEmpty(monthlyCustomer.vehicle.vehicleNumber))
                {
                    statusList.add(MissedFieldsStatus.VehicleNumber);
                }
                if(StringUtils.isEmpty(monthlyCustomer.vehicle.vehicleMake))
                {
                   statusList.add(MissedFieldsStatus.VehicleMake);
                }
                if(StringUtils.isEmpty(monthlyCustomer.vehicle.vehicleModel))
                {
                   statusList.add(MissedFieldsStatus.VehicleModel);
                }
                if(statusList.size()>0) {
                    validationLiveData.setValue(statusList);
                }else {
                    pageNumerShown.setValue(pageNumerShown.getValue() + 1);
                    ((MutableLiveData<SaveStatus>) liveData).setValue(SaveStatus.Validated);
                }
            }
        }



        return  liveData;
     }

     public void scanQrCode(){
         scanQrCode.setValue(true);
     }

    public LiveData<Integer> onBack(){

        if(pageNumerShown.getValue()!=null) {
            if (pageNumerShown.getValue() >1) {
                pageNumerShown.setValue(pageNumerShown.getValue() -1);

            }
        }
        return pageNumerShown;

    }
}
