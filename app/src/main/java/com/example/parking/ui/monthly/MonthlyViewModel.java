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

import java.util.*;

public class MonthlyViewModel extends AndroidViewModel {

     enum MissedFieldsStatus{Name, VehicleNumber, VehicleModel, VehicleMake, Phone }

     MutableLiveData<Boolean> showProgress;


    public MutableLiveData<Bitmap> qrCode= new MutableLiveData<>();


    private MutableLiveData<MonthlyCustomer> monthlyCustomerMutableLiveData= new MutableLiveData<>();

    MonthlyCustomer monthlyCustomer;
    ParkingRespository repository;
    LiveData<ConfigDetails> configDetails;
    MutableLiveData<Integer> pageNumerShown;

    public MonthlyViewModel(Application application) {
        super(application);
        repository = new ParkingRespository(application);
        createEntry();
        configDetails = repository.getConfig();
        pageNumerShown=new MutableLiveData<>();
        pageNumerShown.setValue(1);
        showProgress= new MutableLiveData<>();


    }

    public LiveData<Integer> getPageNumberLiveData(){
        return pageNumerShown;
    }

    public LiveData<ConfigDetails> getConfigDetails() {
        return Transformations.map(configDetails, s -> {
            if(monthlyCustomer !=null ) {
                if(monthlyCustomer.receiptNumber==0)
                monthlyCustomer.receiptNumber = s.last_receipt_number + 1;
                monthlyCustomer.amount=s.monthly_package_amount;
                monthlyCustomer.amounFormatted = StringUtils.getAmountFormattedWithCurrency(s.monthly_package_amount);
                monthlyCustomer.packageID=s.monthly_package_id;

            }


            ;
            return s;
        });

    }


    public void createEntry(){
         monthlyCustomer = new MonthlyCustomer();
         monthlyCustomer.startDate= Calendar.getInstance().getTime();
         monthlyCustomer.endDate=getEndDate();
         monthlyCustomer.vehicle= new Vehicle();
         monthlyCustomerMutableLiveData.setValue(monthlyCustomer);
    }

    public LiveData<MonthlyCustomer> getMonthlyCustomer(){
        return monthlyCustomerMutableLiveData;
    }


    private Date getEndDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, monthlyCustomer.noOfMonths);
        return cal.getTime();
    }






    private LiveData<Boolean> saveSubscription() {
        showProgress.setValue(true);
        return Transformations.map(repository.saveSubscription(monthlyCustomer), s -> {
            if(s.equals("failure")){
                return false;
            }
            monthlyCustomer.rowID = s;
            Bitmap image = QRCodeUtils.generateQrCode(String.valueOf(monthlyCustomer.rowID));
            qrCode.setValue(image);
            return true;
        });
    }

    MutableLiveData<List<MissedFieldsStatus>> validationLiveData= new MutableLiveData<>();

     LiveData<Boolean> onSubmit(){
        if(pageNumerShown.getValue()!=null) {
            if (pageNumerShown.getValue() == 2) {
               return saveSubscription();

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

                }
            }
        }

        MutableLiveData<Boolean> liveData= new MutableLiveData<>();
        liveData.setValue(false);
        return  liveData;
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
